package service;

import dataAccess.dao.AuthDAO;
import dataAccess.dao.GameDAO;
import dataAccess.dao.UserDAO;
import model.GameData;
import requests.ErrorObject;
import requests.ListResponse;
import java.util.Collection;

public class DataService {
    AuthDAO authDAO;
    UserDAO userDAO;
    GameDAO gameDAO;
    public DataService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    public ErrorObject clear() {

        try {
            gameDAO.clearChessData();
            userDAO.clearUserData();
            authDAO.clearAuthData();
            return new ErrorObject("");

        } catch (Exception e) {
            return new ErrorObject("Error: " + e.getMessage());
        }
    }

    public ListResponse listGames(String authToken) {
        try{
        Collection<GameData> gamesList = null;
        if(authDAO.isValidAuthToken(authToken)) {
            gamesList = gameDAO.getAllGameData();
            return new ListResponse(gamesList, null);
        }
        else{
            return new ListResponse(null, "Error: unauthorized");
        }
        } catch (Exception e) {
            return new ListResponse(null,"Error: " + e.getMessage());
        }
    }
}
