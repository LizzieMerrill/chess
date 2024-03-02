package dataAccess.access;

import dataAccess.dao.*;

import java.util.Objects;

public class SQLDataAccess implements DataAccess {
    private final UserDAO userDAO = new SQLUserDAO();
    private final GameDAO gameDAO = new SQLGameDAO();
    private final AuthDAO authDAO = new SQLAuthDAO();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SQLDataAccess that = (SQLDataAccess) o;
        return Objects.equals(userDAO, that.userDAO) && Objects.equals(gameDAO, that.gameDAO) && Objects.equals(authDAO, that.authDAO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userDAO, gameDAO, authDAO);
    }
}