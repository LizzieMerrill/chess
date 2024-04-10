package WebSocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;


    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    Notification notification = new Gson().fromJson(message, Notification.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
    public void joinPlayer(int gameId, ChessGame.TeamColor color, String authToken) throws ResponseException{
        try {
            var joinPlayer = new JoinPlayer(gameId, color, authToken);
            this.session.getBasicRemote().sendText(new Gson().toJson(joinPlayer));
            this.session.close();
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
    public void joinObserver(int gameId, ChessGame.TeamColor color, String authToken) throws ResponseException{
        try {
            var joinObserver = new JoinObserver(gameId, authToken);
            this.session.getBasicRemote().sendText(new Gson().toJson(joinObserver));
            this.session.close();
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
    public void leave(int gameId, ChessGame.TeamColor color, String authToken) throws ResponseException{
        try {
            var leave = new Leave(gameId, authToken);
            this.session.getBasicRemote().sendText(new Gson().toJson(leave));
            this.session.close();
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void resign(int gameId, ChessGame.TeamColor color, String authToken) throws ResponseException{
        try {
            var resign = new Resign(gameId, authToken);
            this.session.getBasicRemote().sendText(new Gson().toJson(resign));
            this.session.close();
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
    public void makeMove(int gameId, ChessMove move, String authToken) throws ResponseException{
        try {
            var makeMove = new MakeMove(gameId, move, authToken);
            this.session.getBasicRemote().sendText(new Gson().toJson(makeMove));
            this.session.close();
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

}