package dataAccess.access;

import dataAccess.dao.AuthDAO;
import dataAccess.dao.GameDAO;
import dataAccess.dao.UserDAO;

public interface DataAccess {
    UserDAO getUserDAO();
    GameDAO getGameDAO();
    AuthDAO getAuthDAO();
}