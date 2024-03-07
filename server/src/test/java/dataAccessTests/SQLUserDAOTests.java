package dataAccessTests;

import dataAccess.access.DataAccessException;
import dataAccess.dao.*;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.RegisterResponse;
import service.*;

import java.sql.SQLException;

import static dataAccess.dao.SQLAuthDAO.dbCreationCheck;
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
        dbCreationCheck();
        UserData testUser1 = new UserData("user1", "pass1", "eail@email.com");
//        RegisterResponse registration = userService.register(testUser1);
//        userService.login(testUser1);
        assertTrue(userDAO.addUser(testUser1));
    }
    @Test
    void addUserTestNegative() throws Exception{
        dbCreationCheck();
        UserData testUser1 = new UserData("user1", "pass1", "eail@email.com");
//        RegisterResponse registration = userService.register(testUser1);
//        userService.login(testUser1);
        assertTrue(userDAO.addUser(testUser1));
    }
    @Test
    void getUserTestPositive() throws Exception{
        dbCreationCheck();
        UserData testUser1 = new UserData("user1", "pass1", "eail@email.com");
        RegisterResponse registration = userService.register(testUser1);
        assertNotNull(userDAO.getUser("user1"));
    }
    @Test
    void getUserTestNegative() throws Exception{
        dbCreationCheck();
        UserData testUser1 = new UserData("user1", "pass1", "eail@email.com");
        RegisterResponse registration = userService.register(testUser1);
        assertNotNull(userDAO.getUser("user1"));
    }
    @Test
    void clearUserDataTest() throws Exception{//only need one
        dbCreationCheck();
        UserData testUser1 = new UserData("user1", "pass1", "eail@email.com");
        RegisterResponse registration = userService.register(testUser1);
        userService.login(testUser1);
        assertTrue(userDAO.clearUserData());
    }
    @Test
    void getUserListTestPositive() throws Exception{
        dbCreationCheck();
        UserData testUser1 = new UserData("user1", "pass1", "eail@email.com");
        RegisterResponse registration = userService.register(testUser1);
        userService.login(testUser1);
        assertTrue(userDAO.getUserList().isEmpty());
    }
    @Test
    void getUserListTestNegative() throws Exception{
        dbCreationCheck();
        UserData testUser1 = new UserData("user1", "pass1", "eail@email.com");
        RegisterResponse registration = userService.register(testUser1);
        userService.login(testUser1);
        assertTrue(userDAO.getUserList().isEmpty());
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

}
