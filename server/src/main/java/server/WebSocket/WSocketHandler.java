package server.WebSocket;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataAccess.access.DataAccessException;
import dataAccess.dao.*;
import deserializers.DeserializerUserGameCommand;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.UserGameCommand;
import webSocketMessages.userCommands.*;

import java.io.IOException;


@WebSocket
public class WSocketHandler {

    GameDAO gameDAO = new SQLGameDAO();
    AuthDAO authDAO = new SQLAuthDAO();
    public WSocketHandler(){

    }


    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(UserGameCommand.class, new DeserializerUserGameCommand()).create();
        var userGameCommand = gson.fromJson(message, UserGameCommand.class);
        switch (userGameCommand.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(gson.fromJson(message, JoinPlayer.class), session);
            case JOIN_OBSERVER -> joinObserver(gson.fromJson(message, JoinObserver.class), session);
            case LEAVE -> leave(gson.fromJson(message, Leave.class), session);
            case MAKE_MOVE -> makeMove(gson.fromJson(message, MakeMove.class), session);
            case RESIGN -> resign(gson.fromJson(message, Resign.class), session);
        }
    }

    private void joinPlayer(JoinPlayer joinPlayerGameCommand, Session session) throws IOException {
        connections.add(joinPlayerGameCommand.getAuthString(), session); // store the websocket object
        LoadGame loadGameMessage;
        try {
            if(joinPlayerGameCommand.getPlayerColor() == ChessGame.TeamColor.BLACK && gameDAO.getGame(joinPlayerGameCommand.getGameID()).getBlackUsername() == null){
                loadGameMessage = new LoadGame(joinPlayerGameCommand.getGameID());
                session.getRemote().sendString(new Gson().toJson(loadGameMessage));
                Notification notification = new Notification(authDAO.getAuthToken(joinPlayerGameCommand.getAuthString()).getUsername() + " joined the game as the black team!");
                connections.broadcast(joinPlayerGameCommand.getAuthString(), notification);
            }
            else if(joinPlayerGameCommand.getPlayerColor() == ChessGame.TeamColor.WHITE && gameDAO.getGame(joinPlayerGameCommand.getGameID()).getWhiteUsername() == null){
                loadGameMessage = new LoadGame(joinPlayerGameCommand.getGameID());
                session.getRemote().sendString(new Gson().toJson(loadGameMessage));
                Notification notification = new Notification(authDAO.getAuthToken(joinPlayerGameCommand.getAuthString()).getUsername() + " joined the game as the white team!");
                connections.broadcast(joinPlayerGameCommand.getAuthString(), notification);
            }
            else{
                Error errorGameMessage = new Error("That color is already taken!");
                session.getRemote().sendString(new Gson().toJson(errorGameMessage));
            }
        } catch (Exception e) {
            Error errorGameMessage = new Error(e.getMessage());
            session.getRemote().sendString(new Gson().toJson(errorGameMessage));
        }
//        System.out.println("test6");
    }

    private void joinObserver(JoinObserver joinObserverGameCommand, Session session) throws IOException, DataAccessException {
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

    private void leave(Leave leaveGameCommand, Session session) throws IOException, DataAccessException {
        connections.remove(leaveGameCommand.getAuthString());
        Notification notificationGameMessage = new Notification(authDAO.getAuthToken(leaveGameCommand.getAuthString()).getUsername() + " left the game");
        connections.broadcast(null, notificationGameMessage);
        //TODO
    }

    private void makeMove(MakeMove makeMoveCommand, Session session) throws DataAccessException, IOException {
        //check move validity??
//        if(gameDAOauthDAO.getAuthToken(makeMoveCommand.getMove()).{
//
//        }
//        else{
//
//        }
        Notification notificationGameMessage = new Notification(authDAO.getAuthToken(makeMoveCommand.getAuthString()).getUsername() +
                " moved their " + gameDAO.getGame(makeMoveCommand.getGameID()).getGame().getBoard().getPiece(makeMoveCommand.getMove().getEndPosition()) +
                " from " + makeMoveCommand.getMove().getStartPosition() + " to " + makeMoveCommand.getMove().getEndPosition());
        connections.broadcast(null, notificationGameMessage);
        //TODO
    }

    private void resign(Resign resignGameCommand, Session session) throws DataAccessException, IOException {
        connections.remove(resignGameCommand.getAuthString());
        Notification notificationGameMessage = new Notification(
                authDAO.getAuthToken(resignGameCommand.getAuthString()).getUsername()
                        + " resigned, game over!");
        connections.broadcast(null, notificationGameMessage);
        //TODO
    }

//    private void exit(String visitorName) throws IOException {
//        connections.remove(visitorName);
//        var message = String.format("%s left the shop", visitorName);
//        var notification = new Notification(Notification.Type.DEPARTURE, message);
//        connections.broadcast(visitorName, notification);
//    }
//
//    public void makeNoise(String petName, String sound) throws ResponseException {
//        try {
//            var message = String.format("%s says %s", petName, sound);
//            var notification = new Notification(Notification.Type.NOISE, message);
//            connections.broadcast("", notification);
//        } catch (Exception ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }
}