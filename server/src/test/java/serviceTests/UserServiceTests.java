package serviceTests;

import dataAccess.dao.*;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.RegisterResponse;
import service.DataService;
import service.GameService;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {

    AuthDAO authDAO = new MemoryAuthDAO();
    UserDAO userDAO = new MemoryUserDAO();
    GameDAO gameDAO = new MemoryGameDAO();
    final DataService dataService = new DataService(authDAO, userDAO, gameDAO);
    final UserService userService = new UserService(authDAO, userDAO);
    final GameService gameService = new GameService(authDAO, userDAO, gameDAO);

    @BeforeEach
    void clear() throws Exception {
        dataService.clear();
    }

        @Test
        void registerTest() throws Exception {
            UserData testUser3 = new UserData("user3", "pass3", "eail3@email.com");
            RegisterResponse registration = userService.register(testUser3);
            assertTrue(userDAO.getUserList().contains(testUser3));
        }

    @Test
    void loginTest() throws Exception {
        UserData testUser4 = new UserData("user4", "pass4", "eail4@email.com");
        userService.register(testUser4);
        assertEquals(testUser4.getUsername(), userService.login(testUser4).username());
    }

        @Test
        void logoutTest() throws Exception {
            UserData testUser5 = new UserData("user5", "pass5", "eail5@email.com");
            RegisterResponse registration = userService.register(testUser5);
            userService.login(testUser5);

        assertFalse(authDAO.isValidAuthToken(registration.authToken()));
        }

    }
