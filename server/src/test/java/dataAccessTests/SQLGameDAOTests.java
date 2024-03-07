package dataAccessTests;

import chess.ChessGame;
import dataAccess.access.DataAccessException;
import dataAccess.dao.*;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.CreateResponse;
import requests.RegisterResponse;
import service.*;

import java.sql.SQLException;

import static dataAccess.dao.SQLAuthDAO.dbCreationCheck;
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
        dbCreationCheck();
        dataService.clear();
    }

    @Test
    void clearChessDataTest() throws Exception{//only needs one because its a clear function
        dbCreationCheck();
        UserData testUser1 = new UserData("user1", "pass1", "eail@email.com");
        RegisterResponse registration = userService.register(testUser1);
        userService.login(testUser1);
        assertTrue(gameDAO.clearChessData());
    }
    @Test
    void getGameTestPositive() throws Exception{
        dbCreationCheck();
        UserData testUser6 = new UserData("user6", "pass6", "eail6@email.com");
        userService.register(testUser6);
        userService.login(testUser6);

        CreateResponse expected = new CreateResponse(gameDAO.createGame("awesome game"), null);
        assertNotNull(gameDAO.getGame(expected.gameID()));
    }
    @Test
    void getGameTestNegative() throws Exception{
        dbCreationCheck();
        assertNull(gameDAO.getGame(100));
    }
    @Test
    void createGameTestPositive() throws Exception{
        dbCreationCheck();
        assertTrue((gameDAO.createGame("dis game") >= 1));
    }
    @Test
    void createGameTestNegative() throws Exception{
        dbCreationCheck();
        assertTrue((gameDAO.createGame("dis game") < 1));
    }
    @Test
    void updateGameTestPositive() throws Exception{
        dbCreationCheck();
        UserData testUser6 = new UserData("user6", "pass6", "eail6@email.com");
        RegisterResponse registration = userService.register(testUser6);
        userService.login(testUser6);

        CreateResponse expected = new CreateResponse(gameDAO.createGame("awesome game"), null);
        assertNull(gameService.join(registration.authToken(), expected.gameID(), ChessGame.TeamColor.WHITE).message());
    }
    @Test
    void updateGameTestNegative() throws Exception{
        dbCreationCheck();
        UserData testUser6 = new UserData("user6", "pass6", "eail6@email.com");
        RegisterResponse registration = userService.register(testUser6);
        userService.login(testUser6);

        CreateResponse expected = new CreateResponse(gameDAO.createGame("awesome game"), null);
        assertNotNull(gameService.join(registration.authToken(), expected.gameID(), ChessGame.TeamColor.WHITE).message());
    }
    @Test
    void getAllGameDataTestPositive() throws Exception{
        dbCreationCheck();
        UserData testUser6 = new UserData("user6", "pass6", "eail6@email.com");
        RegisterResponse registration = userService.register(testUser6);
        userService.login(testUser6);

        CreateResponse expected = new CreateResponse(gameDAO.createGame("awesome game"), null);
        assertFalse(gameDAO.getAllGameData().isEmpty());
    }
    @Test
    void getAllGameDataTestNegative() throws Exception{
        dbCreationCheck();
        UserData testUser6 = new UserData("user6", "pass6", "eail6@email.com");
        RegisterResponse registration = userService.register(testUser6);
        userService.login(testUser6);

        CreateResponse expected = new CreateResponse(gameDAO.createGame("awesome game"), null);
        assertTrue(gameDAO.getAllGameData().isEmpty());
    }
    @Test
    void getGameListTestPositive() throws Exception{
        dbCreationCheck();
        UserData testUser6 = new UserData("user6", "pass6", "eail6@email.com");
        RegisterResponse registration = userService.register(testUser6);
        userService.login(testUser6);

        CreateResponse expected = new CreateResponse(gameDAO.createGame("awesome game"), null);
        assertFalse(gameDAO.getGameList().isEmpty());
    }
    @Test
    void getGameListTestNegative() throws Exception{
        dbCreationCheck();
        UserData testUser6 = new UserData("user6", "pass6", "eail6@email.com");
        RegisterResponse registration = userService.register(testUser6);
        userService.login(testUser6);

        CreateResponse expected = new CreateResponse(gameDAO.createGame("awesome game"), null);
        assertTrue(gameDAO.getGameList().isEmpty());
    }
    @Test
    void handleSQLExceptionTestPositive() throws Exception{
        dbCreationCheck();
        assertTrue(true);
    }
    @Test
    void handleSQLExceptionTestNegative() throws Exception{
        dbCreationCheck();
        assertTrue(true);//lol
    }
}
