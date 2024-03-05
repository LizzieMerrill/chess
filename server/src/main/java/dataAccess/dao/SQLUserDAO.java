package dataAccess.dao;

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
    private final String jdbcUrl = "jdbc:mysql://localhost:3306/chess";
    private final String username = "root";
    private final String password = "JavaRulez2!";
    private final String addUserQuery = "INSERT INTO user_table(username, password, email) VALUES (?, ?, ?)";
    private final String authenticateUserQuery = "SELECT * FROM user_table WHERE username = ? AND password = ?";
    private final String clearUserDataQuery = "DELETE FROM user_table";
    private final String getByUsernameQuery = "SELECT * FROM user_table WHERE username = ?";


    public SQLUserDAO() throws DataAccessException {

        dbCreationCheck(jdbcUrl, username, password);
    }

    @Override
    public boolean addUser(UserData userData) throws DataAccessException {
        dbCreationCheck("jdbc:mysql://localhost:3306/chess", "root", "JavaRulez2!");
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(addUserQuery)) {
            preparedStatement.setString(1, userData.getUsername());
            preparedStatement.setString(2, userData.getPassword());
            preparedStatement.setString(3, userData.getEmail());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        dbCreationCheck("jdbc:mysql://localhost:3306/chess", "root", "JavaRulez2!");
        try (Connection connection = DriverManager.getConnection(jdbcUrl, this.username, this.password)) {
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
        dbCreationCheck("jdbc:mysql://localhost:3306/chess", "root", "JavaRulez2!");
        try (Connection connection = getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(clearUserDataQuery)) {

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            //e.printStackTrace();
        }
        return false;
    }
    public Collection<UserData> getUserList() throws DataAccessException {
        Collection<UserData> userMap = new HashSet<>();

        try (Connection connection = getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(getByUsernameQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String username = resultSet.getString("username");
                userMap.add(getUser(username));
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }

        return userMap;
    }

    private void handleSQLException(SQLException e) throws DataAccessException {
        //e.printStackTrace();
        throw new DataAccessException("SQL Exception: " + e.getMessage(), e);
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
