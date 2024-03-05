package dataAccess.dao;

import dataAccess.access.DataAccessException;
import model.UserData;

import java.util.Collection;

public interface UserDAO {
    boolean addUser(UserData userData) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    boolean clearUserData() throws DataAccessException;
    Collection<UserData> getUserList() throws DataAccessException;
}
