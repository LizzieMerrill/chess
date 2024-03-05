package dataAccessTests;

import dataAccess.access.DataAccessException;
import dataAccess.dao.*;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.CreateResponse;
import requests.RegisterResponse;
import service.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class SQLGameDAOTests {
    AuthDAO authDAO = new SQLAuthDAO();
    UserDAO userDAO = new SQLUserDAO();
    GameDAO gameDAO = new SQLGameDAO();
    final DataService dataService = new DataService(authDAO, userDAO, gameDAO);
    final UserService userService = new UserService(authDAO, userDAO);
    final GameService gameService = new GameService(authDAO, userDAO, gameDAO);

    public SQLGameDAOTests() throws DataAccessException, SQLException {
    }

    @BeforeEach
    void clear() throws Exception {
        dataService.clear();
    }

    @Test
    void clearChessDataTest() throws Exception{//only needs one because its a clear function
        UserData testUser1 = new UserData("user1", "pass1", "eail@email.com");
        RegisterResponse registration = userService.register(testUser1);
        userService.login(testUser1);
        assertTrue(gameDAO.clearChessData());
    }
    @Test
    void getGameTestPositive() throws Exception{
        UserData testUser6 = new UserData("user6", "pass6", "eail6@email.com");
        userService.register(testUser6);
        userService.login(testUser6);

        CreateResponse expected = new CreateResponse(gameDAO.createGame("awesome game"), null);
        assertNotNull(gameDAO.getGame(expected.gameID()));
    }
    @Test
    void getGameTestNegative() throws Exception{
        assertNull(gameDAO.getGame(100));
    }
    @Test
    void createGameTestPositive() throws Exception{

    }
    @Test
    void createGameTestNegative() throws Exception{}
    @Test
    void updateGameTestPositive() throws Exception{}
    @Test
    void updateGameTestNegative() throws Exception{}
    @Test
    void getAllGameDataTestPositive() throws Exception{}
    @Test
    void getAllGameDataTestNegative() throws Exception{}
    @Test
    void getGameListTestPositive() throws Exception{}
    @Test
    void getGameListTestNegative() throws Exception{}
}
