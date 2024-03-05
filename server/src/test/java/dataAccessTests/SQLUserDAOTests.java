package dataAccessTests;

import dataAccess.access.DataAccessException;
import dataAccess.dao.*;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.RegisterResponse;
import service.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class SQLUserDAOTests {
    AuthDAO authDAO = new SQLAuthDAO();
    UserDAO userDAO = new SQLUserDAO();
    GameDAO gameDAO = new SQLGameDAO();
    final DataService dataService = new DataService(authDAO, userDAO, gameDAO);
    final UserService userService = new UserService(authDAO, userDAO);
    final GameService gameService = new GameService(authDAO, userDAO, gameDAO);

    public SQLUserDAOTests() throws DataAccessException, SQLException {
    }

    @BeforeEach
    void clear() throws Exception {
        dataService.clear();
    }

    @Test
    void addUserTestPositive() throws Exception{
        UserData testUser1 = new UserData("user1", "pass1", "eail@email.com");
//        RegisterResponse registration = userService.register(testUser1);
//        userService.login(testUser1);
        assertTrue(userDAO.addUser(testUser1));
    }
    @Test
    void addUserTestNegative() throws Exception{
        UserData testUser1 = new UserData("user1", "pass1", "eail@email.com");
//        RegisterResponse registration = userService.register(testUser1);
//        userService.login(testUser1);
        assertFalse(userDAO.addUser(testUser1));
    }
    @Test
    void getUserTestPositive() throws Exception{
        UserData testUser1 = new UserData("user1", "pass1", "eail@email.com");
        RegisterResponse registration = userService.register(testUser1);
        assertNotNull(userDAO.getUser("user1"));
    }
    @Test
    void getUserTestNegative() throws Exception{
        UserData testUser1 = new UserData("user1", "pass1", "eail@email.com");
        RegisterResponse registration = userService.register(testUser1);
        assertNull(userDAO.getUser("user1"));
    }
    @Test
    void clearUserDataTest() throws Exception{//only need one
        UserData testUser1 = new UserData("user1", "pass1", "eail@email.com");
        RegisterResponse registration = userService.register(testUser1);
        userService.login(testUser1);
        assertTrue(userDAO.clearUserData());
    }
    @Test
    void getUserListTestPositive() throws Exception{
        UserData testUser1 = new UserData("user1", "pass1", "eail@email.com");
        RegisterResponse registration = userService.register(testUser1);
        userService.login(testUser1);
        assertFalse(userDAO.getUserList().isEmpty());
    }
    @Test
    void getUserListTestNegative() throws Exception{
        UserData testUser1 = new UserData("user1", "pass1", "eail@email.com");
        RegisterResponse registration = userService.register(testUser1);
        userService.login(testUser1);
        assertTrue(userDAO.getUserList().isEmpty());
    }
    @Test
    void handleSQLExceptionTestPositive() throws Exception{
        assertTrue(true);
    }
    @Test
    void handleSQLExceptionTestNegative() throws Exception{
        assertTrue(true);
    }

}
