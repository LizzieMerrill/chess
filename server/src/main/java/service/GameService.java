package service;

import chess.ChessGame;
import dataAccess.dao.*;
import model.AuthData;
import model.GameData;
import requests.CreateResponse;
import requests.JoinResponse;

public class GameService {

    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private UserDAO userDAO;

    public GameService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }


    public JoinResponse join(String authToken, int gameId, ChessGame.TeamColor teamColor) {
        try {
            if (authToken == null){
                return new JoinResponse("Error: unauthorized");
            }

            AuthData authorization = authDAO.getAuthToken(authToken);
            if (authorization == null) {
                return new JoinResponse("Error: unauthorized");
            }

            GameData gameData = gameDAO.getGame(gameId);
            if(gameData == null){
                return new JoinResponse("Error: bad request");
            }


            if ((teamColor == ChessGame.TeamColor.WHITE && gameData.getWhiteUsername() != null) ||
                    (teamColor == ChessGame.TeamColor.BLACK && gameData.getBlackUsername() != null)) {
                return new JoinResponse("Error: already taken");
            }

            if(teamColor == ChessGame.TeamColor.WHITE){
                gameData.setWhiteUsername(authorization.getUsername());
                gameDAO.updateGame(gameData);
            }
            else if(teamColor == ChessGame.TeamColor.BLACK){
                gameData.setBlackUsername(authorization.getUsername());
                gameDAO.updateGame(gameData);
            }
            else{
                gameDAO.updateGame(gameData);
            }
            return new JoinResponse(null); // Success

            } catch (Exception e) {
            return new JoinResponse("Error: " + e.getMessage());
        }
    }




    public CreateResponse create(String authToken, String gameName) {
        try {
            // Check if the request contains a valid Authorization header

            if (authToken == null || !authDAO.isValidAuthToken(authToken)) {
                return new CreateResponse(null, "Error: unauthorized");
            }
            else if(gameName == null){
                return new CreateResponse(null, "Error: bad request");
            }
            else{
                return new CreateResponse(gameDAO.createGame(gameName), null);
            }

        } catch (Exception e) {
            return new CreateResponse(null, "Error: " + e.getMessage());
        }
    }
}
