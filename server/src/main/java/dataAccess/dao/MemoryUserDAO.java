package dataAccess.dao;

import dataAccess.data.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {

    private final Map<String, UserData> users;

    public MemoryUserDAO() {
        this.users = new HashMap<>();
    }

    @Override
    public void addUser(UserData userData) {
        users.put(userData.getUsername(), userData);
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }

//    @Override
//    public boolean authenticateUser(String username, String password) {
//        UserData storedUser = users.get(username);
//        return storedUser != null && storedUser.getPassword().equals(password);
//    }

    // Add other user-related methods as needed

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryUserDAO that = (MemoryUserDAO) o;
        return Objects.equals(users, that.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(users);
    }
}