package dataAccessTests;

import dataAccess.dao.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.*;

public class SQLGameDAOTests {
    AuthDAO authDAO = new SQLAuthDAO();
    UserDAO userDAO = new SQLUserDAO();
    GameDAO gameDAO = new SQLGameDAO();
    final DataService dataService = new DataService(authDAO, userDAO, gameDAO);
    final UserService userService = new UserService(authDAO, userDAO);
    final GameService gameService = new GameService(authDAO, userDAO, gameDAO);
    @BeforeEach
    void clear() throws Exception {
        dataService.clear();
    }

    @Test
    void clearChessDataTest() throws Exception{//only needs one because its a clear function

    }
    @Test
    void getGameTestPositive() throws Exception{}
    @Test
    void getGameTestNegative() throws Exception{}
    @Test
    void createGameTestPositive() throws Exception{}
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
