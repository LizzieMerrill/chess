package dataAccessTests;

import dataAccess.dao.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.*;

public class SQLUserDAOTests {
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
    void addUserTestPositive() throws Exception{}
    @Test
    void addUserTestNegative() throws Exception{}
    @Test
    void getUserTestPositive() throws Exception{}
    @Test
    void getUserTestNegative() throws Exception{}
    @Test
    void clearUserDataTest() throws Exception{//only need one

    }
    @Test
    void getUserListTestPositive() throws Exception{}
    @Test
    void getUserListTestNegative() throws Exception{}

}
