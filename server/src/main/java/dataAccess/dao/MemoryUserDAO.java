package dataAccess.dao;

import model.UserData;

import java.util.Collection;
import java.util.HashSet;


public class MemoryUserDAO implements UserDAO {

    public final Collection<UserData> users = new HashSet<>();
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
    public Collection<UserData> getUserList(){
        return users;
    }

    @Override
    public void clearUserData() {
        users.clear();
    }
}