package dataAccess.access;

import dataAccess.dao.AuthDAO;
import dataAccess.dao.UserDAO;

public interface DataAccess {
    AuthDAO getAuthDAO();
}