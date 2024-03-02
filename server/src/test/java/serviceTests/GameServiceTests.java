package serviceTests;

import chess.ChessGame;
import dataAccess.dao.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.CreateResponse;
import requests.JoinResponse;
import requests.RegisterResponse;
import service.DataService;
import service.GameService;
import service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameServiceTests {


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
        void joinGameTest() throws Exception {
            UserData testUser5 = new UserData("user5", "pass5", "eail5@email.com");
            RegisterResponse registration = userService.register(testUser5);
            userService.login(testUser5);
            int gameID = gameService.create(registration.authToken(), "cool game").gameID();

            assertEquals(new JoinResponse(null), gameService.join(registration.authToken(), gameID, ChessGame.TeamColor.WHITE));
        }
    @Test
    void createGameTest() throws Exception {
        UserData testUser6 = new UserData("user6", "pass6", "eail6@email.com");
        RegisterResponse registration = userService.register(testUser6);
        userService.login(testUser6);

        assertEquals(new CreateResponse(gameDAO.createGame("awesome game"), null), gameService.create(registration.authToken(), "awesomegame"));
    }
}
