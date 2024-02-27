package dataAccess.access;

import dataAccess.dao.*;

import java.util.Objects;

public class MemoryDataAccess implements DataAccess {
    private final UserDAO userDAO = new MemoryUserDAO();
    private final GameDAO gameDAO = new MemoryGameDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryDataAccess that = (MemoryDataAccess) o;
        return Objects.equals(userDAO, that.userDAO) && Objects.equals(gameDAO, that.gameDAO) && Objects.equals(authDAO, that.authDAO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userDAO, gameDAO, authDAO);
    }
}