package dataAccess.access;

import dataAccess.dao.*;

public class SQLDataAccess implements DataAccess {
    private final UserDAO userDAO = new SQLUserDAO();
    private final GameDAO gameDAO = new SQLGameDAO();
    private final AuthDAO authDAO = new SQLAuthDAO();

    @Override
    public UserDAO getUserDAO() {
        return userDAO;
    }

    @Override
    public GameDAO getGameDAO() {
        return gameDAO;
    }

    @Override
    public AuthDAO getAuthDAO() {
        return authDAO;
    }
}