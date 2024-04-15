package server.WebSocket;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataAccess.access.DataAccessException;
import dataAccess.dao.*;
import deserializers.DeserializerUserGameCommand;
import exception.ResponseException;
import handlers.DrawBoardHandler;
import handlers.LegalMovesHandler;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.UserGameCommand;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;


@WebSocket
public class WSocketHandler {

    GameDAO gameDAO = new SQLGameDAO();
    AuthDAO authDAO = new SQLAuthDAO();
    UserDAO userDAO = new SQLUserDAO();
    public WSocketHandler(){

    }
    private final ConnectionManager connections = new ConnectionManager();

@OnWebSocketError
public void onError(Session session, Throwable throwable) {
    throwable.printStackTrace();
}
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException, ResponseException {
        Gson gson = new GsonBuilder().registerTypeAdapter(UserGameCommand.class, new DeserializerUserGameCommand()).create();
        var userGameCommand = gson.fromJson(message, UserGameCommand.class);//EXCEPTION
        switch (userGameCommand.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(gson.fromJson(message, JoinPlayer.class), session);
            case JOIN_OBSERVER -> joinObserver(gson.fromJson(message, JoinObserver.class), session);
            case LEAVE -> leave(gson.fromJson(message, Leave.class), session);
            case MAKE_MOVE -> makeMove(gson.fromJson(message, MakeMove.class), session);
            case RESIGN -> resign(gson.fromJson(message, Resign.class), session);
            case REDRAW_CHESS_BOARD -> redrawBoard(gson.fromJson(message, RedrawBoard.class), session);
            case HIGHLIGHT_LEGAL_MOVES -> legalMoves(gson.fromJson(message, HighlightMoves.class), session);
        }
    }


    private void joinPlayer(JoinPlayer joinPlayerGameCommand, Session session) throws IOException, DataAccessException {
    AuthData authData = authDAO.getAuthToken(joinPlayerGameCommand.getAuthString());
    if(authData == null){
            Error authError = new Error("Invalid auth token");
        session.getRemote().sendString(new Gson().toJson(authError));
        }
        else {
            //fix, add game id
            connections.add(joinPlayerGameCommand.getAuthString(), session); // store the websocket object
            LoadGame loadGameMessage;
            try {
                if((joinPlayerGameCommand.getPlayerColor() == ChessGame.TeamColor.BLACK || joinPlayerGameCommand.getPlayerColor() == ChessGame.TeamColor.WHITE)) {
                    if (joinPlayerGameCommand.getPlayerColor() == ChessGame.TeamColor.BLACK && Objects.equals(gameDAO.getGame(joinPlayerGameCommand.getGameID()).getBlackUsername(), authData.getUsername())) {
                        loadGameMessage = new LoadGame(gameDAO.getGame(joinPlayerGameCommand.getGameID()).getGame());
                        session.getRemote().sendString(new Gson().toJson(loadGameMessage));
                        Notification notification = new Notification(authDAO.getAuthToken(joinPlayerGameCommand.getAuthString()).getUsername() + " joined the game as the black team!");
                        connections.broadcast(joinPlayerGameCommand.getAuthString(), notification);
                    } else if (joinPlayerGameCommand.getPlayerColor() == ChessGame.TeamColor.WHITE && Objects.equals(gameDAO.getGame(joinPlayerGameCommand.getGameID()).getWhiteUsername(), authData.getUsername())) {
                        loadGameMessage = new LoadGame(gameDAO.getGame(joinPlayerGameCommand.getGameID()).getGame());
                        session.getRemote().sendString(new Gson().toJson(loadGameMessage));
                        Notification notification = new Notification(authDAO.getAuthToken(joinPlayerGameCommand.getAuthString()).getUsername() + " joined the game as the white team!");
                        connections.broadcast(joinPlayerGameCommand.getAuthString(), notification);
                    } else {
                        Error errorGameMessage = new Error("That color is already taken!");
                        session.getRemote().sendString(new Gson().toJson(errorGameMessage));
                    }
                }
                else{
                    Error nullColorError = new Error("You must pick a team color!");
                    session.getRemote().sendString(new Gson().toJson(nullColorError));
                }
            } catch (Exception e) {
                Error errorGameMessage = new Error(e.getMessage());
                session.getRemote().sendString(new Gson().toJson(errorGameMessage));
            }
        }
//        System.out.println("test6");
    }

    private void joinObserver(JoinObserver joinObserverGameCommand, Session session) throws IOException, DataAccessException {
        if(!authDAO.isValidAuthToken(joinObserverGameCommand.getAuthString())){
            Error authError = new Error("Bad auth token");
            session.getRemote().sendString(new Gson().toJson(authError));
        }
        else {
            if(gameDAO.getGame(joinObserverGameCommand.getGameID()) == null){
                Error idError = new Error("Game does not exist");
                session.getRemote().sendString(new Gson().toJson(idError));
            }
            else {
                connections.add(joinObserverGameCommand.getAuthString(), session);
                LoadGame loadGameMessage;
                try {
                    loadGameMessage = new LoadGame(gameDAO.getGame(joinObserverGameCommand.getGameID()).getGame());
                } catch (Exception e) {
                    loadGameMessage = new LoadGame(e.getMessage());
                }
                session.getRemote().sendString(new Gson().toJson(loadGameMessage));
                Notification notification = new Notification(authDAO.getAuthToken(joinObserverGameCommand.getAuthString()).getUsername() + " joined the game as an observer!");
                connections.broadcast(joinObserverGameCommand.getAuthString(), notification);
            }
        }
    }

    private void leave(Leave leaveGameCommand, Session session) throws IOException, DataAccessException {
        connections.add(leaveGameCommand.getAuthString(), session);
        session.getRemote().sendString(new Gson().toJson(leaveGameCommand));
        Notification notificationGameMessage = new Notification(authDAO.getAuthToken(leaveGameCommand.getAuthString()).getUsername() + " left the game");
        connections.broadcast(null, notificationGameMessage);
        session.getRemote().sendString(new Gson().toJson(notificationGameMessage));
    }

    private void makeMove(MakeMove makeMoveCommand, Session session) throws DataAccessException, IOException {
        String username = authDAO.getAuthToken(makeMoveCommand.getAuthString()).getUsername();
        GameData game = gameDAO.getGame(makeMoveCommand.getGameID());
        ChessBoard board = game.getGame().getBoard();
        ChessPosition start = makeMoveCommand.getMove().getStartPosition();
        ChessPosition end = makeMoveCommand.getMove().getEndPosition();
        ChessPiece piece = board.getPiece(start);
        String whiteUsername = game.getWhiteUsername();
        String blackUsername = game.getBlackUsername();
        if (gameDAO.getGame(makeMoveCommand.getGameID()).getGame().getIsGameOver()) {
            Error empty = new Error("Game is over");
            session.getRemote().sendString(new Gson().toJson(empty));
        } else if (gameDAO.getGame(makeMoveCommand.getGameID()).getCurrentTurn() !=
                gameDAO.getGame(makeMoveCommand.getGameID()).getBoard().getPiece(makeMoveCommand.getMove().getStartPosition()).getTeamColor()) {
            Error turnTaker = new Error("You cannot move for the opponent");
            session.getRemote().sendString(new Gson().toJson(turnTaker));
        } else if(!piece.pieceMoves(board, start).contains(end)){
            //invalid move
            Error invalid = new Error("Invalid move");
            session.getRemote().sendString(new Gson().toJson(invalid));
        }
        else {
            session.getRemote().sendString(new Gson().toJson(new LoadGame(gameDAO.getGame(makeMoveCommand.getGameID()).getGame())));
        }
    }




    private void resign(Resign resignGameCommand, Session session) throws DataAccessException, IOException {
        boolean player = false;
        int resignId = -1;
        Collection<GameData> games = gameDAO.getAllGameData();
        UserGameCommand command = new UserGameCommand(resignGameCommand.getAuthString());
        for (GameData game : games) {
            if(authDAO.getAuthToken(command.getAuthString()).getUsername() == game.getBlackUsername()){
                player = true;
                Notification notificationGameMessage = new Notification(
                        authDAO.getAuthToken(resignGameCommand.getAuthString()).getUsername()
                                + " resigned, game over!");
                session.getRemote().sendString(new Gson().toJson(notificationGameMessage));
                resignId = game.getGameID();
            }
            if(authDAO.getAuthToken(command.getAuthString()).getUsername() == game.getWhiteUsername()){
                player = true;
                Notification notificationGameMessage = new Notification(
                        authDAO.getAuthToken(resignGameCommand.getAuthString()).getUsername()
                                + " resigned, game over!");
                session.getRemote().sendString(new Gson().toJson(notificationGameMessage));
                resignId = game.getGameID();
            }
        }
        if(player){
            //resign logic and break
            //command = new Resign(resignId, resignGameCommand.getAuthString());
            Notification notificationGameMessage = new Notification(
                    authDAO.getAuthToken(resignGameCommand.getAuthString()).getUsername()
                            + " resigned, game over!");
            //connections.add(resignGameCommand.getAuthString(), session);
            session.getRemote().sendString(new Gson().toJson(notificationGameMessage));
            gameDAO.getGame(resignGameCommand.getGameID()).setIsGameOver(true);
            //connections.broadcast(null, notificationGameMessage);
        }
        else{
            Error resignError = new Error("You cannot resign a game you are not playing in.");
            session.getRemote().sendString(new Gson().toJson(resignError));
        }
    }
    private void legalMoves(HighlightMoves legalMovesCommand, Session session) throws DataAccessException, IOException, ResponseException {
        if(!authDAO.isValidAuthToken(authDAO.getAuthToken(legalMovesCommand.getAuthString()).getAuthToken())){
            Error authError = new Error("Unauthorized");
            session.getRemote().sendString(new Gson().toJson(authError));
        }
        else{
            try {
                ChessGame game = legalMovesCommand.getGame();
                if (game != null) {
                    LegalMovesHandler legalHandler = new LegalMovesHandler(game);
                }
            } catch (NumberFormatException ignored) {
            }
        }
        throw new ResponseException(400, "Game does not exist");//throw new error object?
    }
    private void redrawBoard(RedrawBoard redrawBoardCommand, Session session) throws DataAccessException, ResponseException, IOException {
        //assertSignedIn();
        if(!authDAO.isValidAuthToken(authDAO.getAuthToken(redrawBoardCommand.getAuthString()).getAuthToken())){
            Error authError = new Error("Unauthorized");
            session.getRemote().sendString(new Gson().toJson(authError));
        }
        else{
            try {
                var game = redrawBoardCommand.getGame();
                if (game != null) {
                    //new DrawBoardHandler(params).draw(game.getBoard());
                    DrawBoardHandler draw = new DrawBoardHandler(game);
                }
            } catch (NumberFormatException ignored) {
            }
        }

            throw new ResponseException(400, "Game does not exist");//throw new error object?
    }
}