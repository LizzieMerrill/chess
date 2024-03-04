package serviceTests;

import dataAccess.access.DataAccessException;
import dataAccess.dao.*;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.RegisterResponse;
import service.DataService;
import service.GameService;
import service.UserService;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {

    AuthDAO authDAO = new SQLAuthDAO();
    UserDAO userDAO = new SQLUserDAO();
    GameDAO gameDAO = new SQLGameDAO();
    final DataService dataService = new DataService(authDAO, userDAO, gameDAO);
    final UserService userService = new UserService(authDAO, userDAO);

    public UserServiceTests() throws DataAccessException, SQLException {
    }

    @BeforeEach
    void clear() throws Exception {
        dataService.clear();
    }

        @Test
        void registerTestPositive() throws Exception {
            UserData testUser3 = new UserData("user3", "pass3", "eail3@email.com");
            userService.register(testUser3);
            assertTrue(userDAO.getUserList().contains(testUser3));
        }
    @Test
    void registerTestNegative() throws Exception {
        new UserData("user9", "pass9", "eail9@email.com");
        RegisterResponse registration = userService.register(null);
        assertNotNull(registration.message());
    }

    @Test
    void loginTestPositive() throws Exception {
        UserData testUser4 = new UserData("user4", "pass4", "eail4@email.com");
        userService.register(testUser4);
        assertEquals(testUser4.getUsername(), userService.login(testUser4).username());
    }
    @Test
    void loginTestNegative() throws Exception {
        UserData testUser10 = new UserData(null, null, null);
        RegisterResponse response = userService.register(testUser10);
        assertNotNull(response.message());
        assertNotNull(userService.login(testUser10).message());
    }

        @Test
        void logoutTestPositive() throws Exception {
            UserData testUser5 = new UserData("user5", "pass5", "eail5@email.com");
            userService.register(testUser5);
            RegisterResponse registration = userService.login(testUser5);

            userService.logout(registration.authToken());

        assertFalse(authDAO.isValidAuthToken(registration.authToken()));
        }
    @Test
    void logoutTestNegative() throws Exception {
        UserData testUser11 = new UserData("user11", "pass11", "eail11@email.com");
        userService.register(testUser11);
        userService.login(testUser11);

        assertNotNull(userService.logout(null).message());
    }

    }
