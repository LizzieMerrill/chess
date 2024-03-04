package dataAccessTests;

import dataAccess.access.DataAccessException;
import dataAccess.dao.*;
import org.junit.jupiter.api.Test;
import service.*;

import org.junit.jupiter.api.BeforeEach;

import java.sql.SQLException;

public class SQLAuthDAOTests {
    AuthDAO authDAO = new SQLAuthDAO();
    UserDAO userDAO = new SQLUserDAO();
    GameDAO gameDAO = new SQLGameDAO();
    final DataService dataService = new DataService(authDAO, userDAO, gameDAO);
    final UserService userService = new UserService(authDAO, userDAO);
    final GameService gameService = new GameService(authDAO, userDAO, gameDAO);

    public SQLAuthDAOTests() throws DataAccessException, SQLException {
    }

    @BeforeEach
    void clear() throws Exception {
        dataService.clear();
    }

    @Test
    void clearAuthDataTest() throws Exception{//only need one

    }
    @Test
    void addAuthTokenTestPositive() throws Exception{

    }
    @Test
    void addAuthTokenTestNegative() throws Exception{}
    @Test
    void getAuthTokenTestPositive() throws Exception{}
    @Test
    void getAuthTokenTestNegative() throws Exception{}
    @Test
    void removeAuthDataTestPositive() throws Exception{//do i need both?

    }
    @Test
    void removeAuthDataTestNegative() throws Exception{//do i need both?

    }
    @Test
    void fetchDataByQueryTestPositive() throws Exception{}
    @Test
    void fetchDataByQueryTestNegative() throws Exception{}
    @Test
    void isValidAuthTokenTestPositive() throws Exception{}
    @Test
    void isValidAuthTokenTestNegative() throws Exception{}
    @Test
    void getAuthListTestPositive() throws Exception{}
    @Test
    void getAuthListTestNegative() throws Exception{}

}
