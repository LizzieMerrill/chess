package dataAccess.dao;

import dataAccess.data.UserData;

import java.util.Collection;
import java.util.HashSet;


public class MemoryUserDAO implements UserDAO {

    //private static final Map<String, UserData> users = new HashMap<>();
    private static final Collection<UserData> users = new HashSet<>();


    @Override
    public void addUser(UserData userData) {
        users.add(userData);
    }

    @Override
    public UserData getUser(String username) {
        for(UserData userData : users){
            if(userData.getUsername().equals(username)){
                return userData;
            }
        }
        return null;
    }

    @Override
    public void clearUserData() {
        users.clear();
    }


//    @Override
//    public boolean authenticateUser(String username, String password) {
//        UserData storedUser = users.get(username);
//        return storedUser != null && storedUser.getPassword().equals(password);
//    }

    // Add other user-related methods as needed


}