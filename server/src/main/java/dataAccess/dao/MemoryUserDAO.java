package dataAccess.dao;

import dataAccess.DataAccessException;
import dataAccess.data.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {

    private final Map<String, UserData> users;

    public MemoryUserDAO() {
        this.users = new HashMap<>();
    }

    @Override
    public void addUser(UserData userData) {

        users.put(userData.getUsername(), userData);
    }

//    @Override
//    public boolean authenticateUser(String username, String password) {
//        UserData storedUser = users.get(username);
//        return storedUser != null && storedUser.getPassword().equals(password);
//    }

    // Add other user-related methods as needed

}