package handlers;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.dao.AuthDAO;
import dataAccess.dao.GameDAO;
import dataAccess.dao.UserDAO;
import requests.JoinObject;
import requests.JoinResponse;
import server.Server;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinGameHandler extends Server implements Route {
    UserService userService;
    GameService gameService;
    private final Gson gson = new Gson();

    public JoinGameHandler(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO){
        this.userService = new UserService(authDAO, userDAO);
        this.gameService = new GameService(authDAO, userDAO, gameDAO);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
            JoinObject joinObject = gson.fromJson(request.body(), JoinObject.class);
            int gameId = joinObject.gameID();
            ChessGame.TeamColor teamColorParam = joinObject.playerColor();

            // Check if the request contains a valid Authorization header
            String authToken = request.headers("Authorization");
            JoinResponse result = gameService.join(authToken, gameId, teamColorParam);

            if(result.message() == null){
                response.status(200);
            }
            else if (result.message().contains("bad request")) {
                response.status(400); //bad request
            }
            else if (result.message().contains("unauthorized")) {
                response.status(401); // Unauthorized
            }
            else if (result.message().contains("already taken")) {
                response.status(403); // color taken
            }
            else{
                response.status(500);
            }
            return new Gson().toJson(result);
    }
}
