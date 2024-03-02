package dataAccess.dao;

import dataAccess.DatabaseManager;
import dataAccess.access.DataAccessException;
import model.UserData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import static dataAccess.dao.SQLAuthDAO.checkDatabaseExists;

public class SQLUserDAO implements UserDAO {
    private final String jdbcUrl = "jdbc:mysql://localhost:3306/chess";
    private final String username = "root";
    private final String password = "JavaRulez2!";
    private final DatabaseManager manager = new DatabaseManager();
    boolean databaseExists = checkDatabaseExists(jdbcUrl, username, password);
    private final String addUserQuery = "INSERT INTO user_table(username, password, email) VALUES (?, ?, ?)";
    private final String authenticateUserQuery = "SELECT * FROM user_table WHERE username = ? AND password = ?";

    public SQLUserDAO() {
        //Class.forName("com.mysql.cj.jdbc.Driver");
    }

    @Override
    public void addUser(UserData userData) throws DataAccessException {
        if (!databaseExists) {
            manager.createDatabase();
        }
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(addUserQuery)) {
            preparedStatement.setString(1, userData.getUsername());
            preparedStatement.setString(2, userData.getPassword());
            preparedStatement.setString(3, userData.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        if (!databaseExists) {
            manager.createDatabase();
        }
        try (Connection connection = DriverManager.getConnection(jdbcUrl, this.username, this.password)) {
            String query = "SELECT * FROM user_table WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Assuming the structure of your 'user_table'
                        String retrievedUsername = resultSet.getString("username");
                        String retrievedPassword = resultSet.getString("password");
                        String retrievedEmail = resultSet.getString("email");

                        // Create a UserData object
                        return new UserData(retrievedUsername, retrievedPassword, retrievedEmail);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error while retrieving user from database", e);
        }

        return null; // Return null if the user is not found
    }

    @Override
    public void clearUserData() throws DataAccessException {
        if (!databaseExists) {
            manager.createDatabase();
        }

    }
    public Collection<UserData> getUserList() throws DataAccessException {
        if (!databaseExists) {
            manager.createDatabase();
        }
        return new HashSet<UserData>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SQLUserDAO that = (SQLUserDAO) o;
        return Objects.equals(jdbcUrl, that.jdbcUrl) && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(addUserQuery, that.addUserQuery) && Objects.equals(authenticateUserQuery, that.authenticateUserQuery);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jdbcUrl, username, password, addUserQuery, authenticateUserQuery);
    }
}
