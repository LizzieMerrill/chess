package serviceTests;
//clear only needs positive

import dataAccess.dao.*;
import model.GameData;
import model.UserData;
import requests.RegisterResponse;
import service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
public class DataServiceTests {

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
    void clearTest() throws Exception {//only needs positive, so just this one
            UserData testUser1 = new UserData("user1", "pass1", "eail@email.com");
        RegisterResponse registration = userService.register(testUser1);
        gameService.create(registration.authToken(), "best game ever");

        dataService.clear();

        assertEquals(true, gameDAO.getGameList().isEmpty());
        assertEquals(true, authDAO.getAuthList().isEmpty());
        assertEquals(true, userDAO.getUserList().isEmpty());
    }

        @Test
        void listGamesTestPositive() throws Exception {
            Collection<GameData> expected = new HashSet<>();

            UserData testUser2 = new UserData("user2", "pass2", "eail2@email.com");
            RegisterResponse registration = userService.register(testUser2);

            GameData midGame = gameDAO.getGame(gameService.create(registration.authToken(), "most mid game ever").gameID());
            GameData averageGame = gameDAO.getGame(gameService.create(registration.authToken(), "average game").gameID());;
            GameData decentGame = gameDAO.getGame(gameService.create(registration.authToken(), "decent game").gameID());;

            expected.add(decentGame);
            expected.add(averageGame);
            expected.add(midGame);


            Collection<GameData> actual = dataService.listGames(registration.authToken()).games();
            assertTrue(expected.containsAll(actual));
        }

    @Test
    void listGamesTestNegative() throws Exception {
        Collection<GameData> expected = new HashSet<>();

        UserData testUser8 = new UserData("user8", "pass8", "eail8@email.com");
        RegisterResponse registration = userService.register(testUser8);

        GameData midGame = gameDAO.getGame(gameService.create(registration.authToken(), "most mid game ever").gameID());
        GameData averageGame = gameDAO.getGame(gameService.create(registration.authToken(), "average game").gameID());;
        GameData decentGame = gameDAO.getGame(gameService.create(registration.authToken(), "decent game").gameID());;

        expected.add(decentGame);
        expected.add(averageGame);
        expected.add(midGame);


        Collection<GameData> actual = dataService.listGames(null).games();
        assertNull(actual);
    }
}
