package dataAccess.dao;

import dataAccess.DatabaseManager;
import dataAccess.access.DataAccessException;
import model.UserData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static dataAccess.dao.SQLAuthDAO.dbCreationCheck;
import static java.sql.DriverManager.getConnection;

public class SQLUserDAO implements UserDAO {
//    private final String jdbcUrl = "jdbc:mysql://localhost:3306/chess";
//    private final String username = "root";
//    private final String password = "JavaRulez2!";
private static final DatabaseManager manager = new DatabaseManager();
    private final String addUserQuery = "INSERT INTO user_table(username, password, email) VALUES (?, ?, ?)";
    private final String authenticateUserQuery = "SELECT * FROM user_table WHERE username = ? AND password = ?";
    private final String clearUserDataQuery = "DELETE FROM user_table";
    private final String getByUsernameQuery = "SELECT * FROM user_table WHERE username = ?";


    public SQLUserDAO(){
        try {
            manager.createDatabase();
        } catch (DataAccessException e) {
            //e.printStackTrace();
        }//bla
    }

    @Override
    public boolean addUser(UserData userData) throws DataAccessException {
        dbCreationCheck();
        try (Connection connection = manager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(addUserQuery)) {
            preparedStatement.setString(1, userData.getUsername());
            preparedStatement.setString(2, userData.getPassword());
            preparedStatement.setString(3, userData.getEmail());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {

        }
        return false;
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        dbCreationCheck();
        try (Connection connection = manager.getConnection()) {
            String query = "SELECT * FROM user_table WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String retrievedUsername = resultSet.getString("username");
                        String retrievedPassword = resultSet.getString("password");
                        String retrievedEmail = resultSet.getString("email");

                        //create a UserData object
                        return new UserData(retrievedUsername, retrievedPassword, retrievedEmail);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error while retrieving user from database", e);
        }

        return null;
    }

    @Override
    public boolean clearUserData() throws DataAccessException {
        dbCreationCheck();
        try (Connection connection = manager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(clearUserDataQuery)) {

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            //e.printStackTrace();
        }
        return false;
    }
    public Collection<UserData> getUserList() throws DataAccessException {
        dbCreationCheck();
        Collection<UserData> userMap = new HashSet<>();

        try (Connection connection = manager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getByUsernameQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String username = resultSet.getString("username");
                userMap.add(getUser(username));
            }

        } catch (SQLException e) {
            //handleSQLException(e);
        }

        return userMap;
    }

//    private void handleSQLException(SQLException e) throws DataAccessException {
//        dbCreationCheck();
//        //e.printStackTrace();
//        throw new DataAccessException("SQL Exception: " + e.getMessage(), e);
//    }
}
