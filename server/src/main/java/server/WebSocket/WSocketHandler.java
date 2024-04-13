package server.WebSocket;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataAccess.access.DataAccessException;
import dataAccess.dao.*;
import deserializers.DeserializerUserGameCommand;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.Spark;
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
//        public static void main(String[] args) {
//            Spark.port(8080);
//            Spark.webSocket("/connect", WSocketHandler.class);
//            Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
//        }

//        @OnWebSocketMessage
//        public void onMessage(Session session, String message) throws Exception {
//            session.getRemote().sendString("WebSocket response: " + message);
//        }




    private final ConnectionManager connections = new ConnectionManager();

//    @OnWebSocketError
//    public void onError(Session session, String message) throws IOException, DataAccessException {
//        Error error = new Error("Error");
//    }
@OnWebSocketError
public void onError(Session session, Throwable throwable) {
    throwable.printStackTrace();
}
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        Gson gson = new GsonBuilder().registerTypeAdapter(UserGameCommand.class, new DeserializerUserGameCommand()).create();
        var userGameCommand = gson.fromJson(message, UserGameCommand.class);//EXCEPTION
        switch (userGameCommand.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(gson.fromJson(message, JoinPlayer.class), session);
            case JOIN_OBSERVER -> joinObserver(gson.fromJson(message, JoinObserver.class), session);
            case LEAVE -> leave(gson.fromJson(message, Leave.class), session);
            case MAKE_MOVE -> makeMove(gson.fromJson(message, MakeMove.class), session);
            case RESIGN -> resign(gson.fromJson(message, Resign.class), session);
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
                        loadGameMessage = new LoadGame(joinPlayerGameCommand.getGameID());
                        session.getRemote().sendString(new Gson().toJson(loadGameMessage));
                        Notification notification = new Notification(authDAO.getAuthToken(joinPlayerGameCommand.getAuthString()).getUsername() + " joined the game as the black team!");
                        connections.broadcast(joinPlayerGameCommand.getAuthString(), notification);
                    } else if (joinPlayerGameCommand.getPlayerColor() == ChessGame.TeamColor.WHITE && Objects.equals(gameDAO.getGame(joinPlayerGameCommand.getGameID()).getWhiteUsername(), authData.getUsername())) {
                        loadGameMessage = new LoadGame(joinPlayerGameCommand.getGameID());
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
                    loadGameMessage = new LoadGame(joinObserverGameCommand.getGameID());
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
//        if(){//if the user is an active player in the game, set isGameOver to true
//            gameDAO.getGame(resignGameCommand.getGameID()).setIsGameOver(true);
//        }
//        else{//if observer, just let them leave
//
//        }

//        int leaveId = -1;
//        UserGameCommand command = new UserGameCommand(authToken);
////        Collection<GameData> games = gameDAO.getAllGameData();
////        for (GameData game : games) {
////            if(authDAO.getAuthToken(command.getAuthString()).getUsername() == game.getBlackUsername()
////                    || authDAO.getAuthToken(command.getAuthString()).getUsername() == game.getWhiteUsername()){
////                leaveId = game.getGameID();
////            }
////        }
//        //JUST ASK THEM WHAT GAME THEY WANT TO LEAVE OR ACCESS WHICH GAME THEY ARE IN
//        if(leaveId != -1){
//            //leave logic and break
//            command = new Leave(leaveId, authToken);
//        }
//        else{
//            java.lang.Error leaveError = new java.lang.Error("You cannot leave a game that you are not playing or observing.");
//        }
        connections.add(leaveGameCommand.getAuthString(), session);
        Notification notificationGameMessage = new Notification(authDAO.getAuthToken(leaveGameCommand.getAuthString()).getUsername() + " left the game");
        connections.broadcast(null, notificationGameMessage);
    }

    private void makeMove(MakeMove makeMoveCommand, Session session) throws DataAccessException, IOException {
    if(!authDAO.isValidAuthToken(authDAO.getAuthToken(makeMoveCommand.getAuthString()).getAuthToken())){
        Error unauth = new Error("Game does not exist");
        session.getRemote().sendString(new Gson().toJson(unauth));
    }
    else{
        if(gameDAO.getGame(makeMoveCommand.getGameID()) == null){
            Error empty = new Error("Game does not exist");
            session.getRemote().sendString(new Gson().toJson(empty));
        }
        else{
            //checks if user is trying to make move for the opponent
            if(gameDAO.getGame(makeMoveCommand.getGameID()).getCurrentTurn() !=
                    gameDAO.getGame(makeMoveCommand.getGameID()).getBoard().getPiece(makeMoveCommand.getMove().getStartPosition()).getTeamColor()){
                Error turnTaker = new Error("You cannot move for the opponent");
                session.getRemote().sendString(new Gson().toJson(turnTaker));
            }
            else{
                //checks if user is moving out of turn OR is an observer
                if(gameDAO.getGame(makeMoveCommand.getGameID()).getCurrentTurn() == ChessGame.TeamColor.WHITE){
                    if (!Objects.equals(gameDAO.getGame(makeMoveCommand.getGameID()).getWhiteUsername(), authDAO.getAuthToken(makeMoveCommand.getAuthString()).getUsername())){
                        Error badMove = new Error("You are not allowed to move right now");
                        session.getRemote().sendString(new Gson().toJson(badMove));
                    }
                    else{
                        //normal move, make sure its valid and check that the game is not over and nobody has resigned
                        makeMoveNormalHelper(makeMoveCommand, session, authDAO.getAuthToken(makeMoveCommand.getAuthString()).getAuthToken());
                    }
                }
                else{
                    if (!Objects.equals(gameDAO.getGame(makeMoveCommand.getGameID()).getBlackUsername(), authDAO.getAuthToken(makeMoveCommand.getAuthString()).getUsername())){
                        Error badMove = new Error("You are not allowed to move right now");
                    }
                    else{
                        //normal move, make sure its valid and check that the game is not over and nobody has resigned
                        makeMoveNormalHelper(makeMoveCommand, session, authDAO.getAuthToken(makeMoveCommand.getAuthString()).getAuthToken());
                    }
                }
            }
        }
    }
//        boolean player = false;
//        int playersGameId = -1;
//        Collection<GameData> games = gameDAO.getAllGameData();
//        UserGameCommand command = new UserGameCommand(authToken);
//        for (GameData game : games) {
//            if(authDAO.getAuthToken(command.getAuthString()).getUsername() == game.getBlackUsername()
//                    || authDAO.getAuthToken(command.getAuthString()).getUsername() == game.getWhiteUsername()){
//                player = true;
//                playersGameId = game.getGameID();
//            }
//        }
//        if(player){
//            //resign logic and break
//            command = new Resign(playersGameId, authToken);
//        }
//        else{
//            java.lang.Error resignError = new java.lang.Error("You cannot resign a game you are not playing in.");
//        }
    }

    //normal move, make sure its valid and check that the game is not over and nobody has resigned
    private void makeMoveNormalHelper(MakeMove makeMoveCommand, Session session, String authToken) throws DataAccessException, IOException {
        //        boolean player = false;
//        int playersGameId = -1;
//        Collection<GameData> games = gameDAO.getAllGameData();
//        UserGameCommand command = new UserGameCommand(authToken);
//        for (GameData game : games) {
//            if(authDAO.getAuthToken(command.getAuthString()).getUsername() == game.getBlackUsername()
//                    || authDAO.getAuthToken(command.getAuthString()).getUsername() == game.getWhiteUsername()){
//                player = true;
//                playersGameId = game.getGameID();
//            }
//        }
//        if(player){
//            //resign logic and break
//            command = new Resign(playersGameId, authToken);
//        }
//        else{
//            Error resignError = new Error("You cannot resign a game you are not playing in.");
//        }
    if(gameDAO.getGame(makeMoveCommand.getGameID()).getIsGameOver() == true){
            Error gameOverMove = new Error("You cannot move after the game has ended");
            //session.getRemote().sendString(new Gson().toJson(gameOverMove));
        }
        else{
            if(!gameDAO.getGame(makeMoveCommand.getGameID()).isValidMove(makeMoveCommand.getMove())){
                Error invalidMove = new Error("Invalid move");
                //session.getRemote().sendString(new Gson().toJson(invalidMove));
            }
            else{
                //FINALLY SUCCESS
                connections.add(makeMoveCommand.getAuthString(), session);
                makeMoveCommand = new MakeMove(makeMoveCommand.getGameID(), makeMoveCommand.getMove(), authToken);
                Notification notificationGameMessage = new Notification(authDAO.getAuthToken(makeMoveCommand.getAuthString()).getUsername() +
                        " moved their " + gameDAO.getGame(makeMoveCommand.getGameID()).getGame().getBoard().getPiece(makeMoveCommand.getMove().getEndPosition()) +
                        " from " + makeMoveCommand.getMove().getStartPosition() + " to " + makeMoveCommand.getMove().getEndPosition());
                connections.broadcast(null, notificationGameMessage);
                session.getRemote().sendString(new Gson().toJson(makeMoveCommand));
            }
        }
    }




    private void resign(Resign resignGameCommand, Session session) throws DataAccessException, IOException {
        String player = "no";
        int resignId = -1;
        Collection<GameData> games = gameDAO.getAllGameData();
        UserGameCommand command = new UserGameCommand(resignGameCommand.getAuthString());
        for (GameData game : games) {
            if(authDAO.getAuthToken(command.getAuthString()).getUsername() == game.getBlackUsername()){
                player = "black";
                resignId = game.getGameID();
            }
            if(authDAO.getAuthToken(command.getAuthString()).getUsername() == game.getWhiteUsername()){
                player = "white";
                resignId = game.getGameID();
            }
        }
        if(!Objects.equals(player, "no")){
            //resign logic and break
            //command = new Resign(resignId, resignGameCommand.getAuthString());
            connections.add(resignGameCommand.getAuthString(), session);
            gameDAO.getGame(resignGameCommand.getGameID()).setIsGameOver(true);
            Notification notificationGameMessage = new Notification(
                    authDAO.getAuthToken(resignGameCommand.getAuthString()).getUsername()
                            + " resigned, game over!");
            connections.broadcast(null, notificationGameMessage);
        }
        else{
            Error resignError = new Error("You cannot resign a game you are not playing in.");
            session.getRemote().sendString(new Gson().toJson(resignError));
        }
    }
}