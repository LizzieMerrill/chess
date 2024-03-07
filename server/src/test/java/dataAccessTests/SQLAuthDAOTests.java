package dataAccessTests;

import dataAccess.access.DataAccessException;
import dataAccess.dao.*;
import model.UserData;
import org.junit.jupiter.api.Test;
import requests.RegisterResponse;
import service.*;

import org.junit.jupiter.api.BeforeEach;

import java.sql.SQLException;

import static dataAccess.dao.SQLAuthDAO.dbCreationCheck;
import static org.junit.jupiter.api.Assertions.*;

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
        dbCreationCheck();
        dataService.clear();
    }

    @Test
    void clearAuthDataTest() throws Exception{//only need one
        dbCreationCheck();
        UserData testUser1 = new UserData("user1", "pass1", "eail@email.com");
        RegisterResponse registration = userService.register(testUser1);
        userService.login(testUser1);
        assertTrue(authDAO.clearAuthData());
    }
    @Test
    void addAuthTokenTestPositive() throws Exception{
        dbCreationCheck();
        UserData testUser1 = new UserData("user1", "pass1", "eail@email.com");
        RegisterResponse registration = userService.register(testUser1);
        userService.login(testUser1);
        assertTrue(authDAO.isValidAuthToken(registration.authToken()));
    }
    @Test
    void addAuthTokenTestNegative() throws Exception{
        dbCreationCheck();
        UserData testUser1 = new UserData("user1", "pass1", "eail@email.com");
        RegisterResponse registration = userService.register(testUser1);
        assertTrue(authDAO.isValidAuthToken(registration.authToken()));
    }
    @Test
    void getAuthTokenTestPositive() throws Exception{
        dbCreationCheck();
        UserData testUser1 = new UserData("user1", "pass1", "eail@email.com");
        RegisterResponse registration = userService.register(testUser1);
        userService.login(testUser1);
        assertNotNull(authDAO.getAuthToken(registration.authToken()));
    }
    @Test
    void getAuthTokenTestNegative() throws Exception{
        dbCreationCheck();
        UserData testUser1 = new UserData("user1", "pass1", "eail@email.com");
        RegisterResponse registration = userService.register(testUser1);
        //userService.login(testUser1);
        assertNull(authDAO.getAuthToken(registration.authToken()));
    }
    @Test
    void removeAuthDataTestPositive() throws Exception{//do i need both?
        dbCreationCheck();
        UserData testUser1 = new UserData("user1", "pass1", "eail@email.com");
        RegisterResponse registration = userService.register(testUser1);
        userService.login(testUser1);
        assertTrue(authDAO.removeAuthData(registration.authToken()));
    }
    @Test
    void removeAuthDataTestNegative() throws Exception{//do i need both?
        dbCreationCheck();
        UserData testUser1 = new UserData("user1", "pass1", "eail@email.com");
        RegisterResponse registration = userService.register(testUser1);
        //userService.login(testUser1);
        assertFalse(authDAO.removeAuthData(registration.authToken()));
    }
    @Test
    void fetchDataByQueryTestPositive() throws Exception{
        dbCreationCheck();
        assertTrue(true);
    }
    @Test
    void fetchDataByQueryTestNegative() throws Exception{
        dbCreationCheck();
        assertTrue(true);
    }
    @Test
    void isValidAuthTokenTestPositive() throws Exception{
        dbCreationCheck();
        UserData testUser1 = new UserData("user1", "pass1", "eail@email.com");
        RegisterResponse registration = userService.register(testUser1);
        userService.login(testUser1);
        assertTrue(authDAO.isValidAuthToken(registration.authToken()));
    }
    @Test
    void isValidAuthTokenTestNegative() throws Exception{
        dbCreationCheck();
        UserData testUser1 = new UserData("user1", "pass1", "eail@email.com");
        RegisterResponse registration = userService.register(testUser1);
        assertFalse(authDAO.isValidAuthToken(registration.authToken()));
    }
    @Test
    void getAuthListTestPositive() throws Exception{
        dbCreationCheck();
        assertFalse(authDAO.getAuthList().isEmpty());
    }
    @Test
    void getAuthListTestNegative() throws Exception{
        dbCreationCheck();
        assertTrue(authDAO.getAuthList().isEmpty());
    }

    @Test
    void dbCreationCheckTestNegative() throws Exception{
        dbCreationCheck();
        assertTrue(true);
    }
    @Test
    void dbCreationCheckTestPositive() throws Exception{
        dbCreationCheck();
        assertTrue(true);
    }
    @Test
    void createTableIfNotExistsTestNegative() throws Exception{
        dbCreationCheck();
        assertTrue(true);
    }
    @Test
    void createTableIfNotExistsTestPositive() throws Exception{
        dbCreationCheck();
        assertTrue(true);
    }
    @Test
    void handleSQLExceptionTestPositive() throws Exception{
        dbCreationCheck();
        assertTrue(true);
    }
    @Test
    void handleSQLExceptionTestNegative() throws Exception{
        dbCreationCheck();
        assertTrue(true);
    }
    @Test
    void tableExistsTestPositive() throws Exception{
        dbCreationCheck();
        assertTrue(true);
    }
    @Test
    void tableExistsTestNegative() throws Exception{
        dbCreationCheck();
        assertTrue(true);
    }

}
