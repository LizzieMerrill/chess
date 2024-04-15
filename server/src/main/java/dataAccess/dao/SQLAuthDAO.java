package dataAccess.dao;

import dataAccess.access.DataAccessException;
import model.AuthData;
import dataAccess.DatabaseManager;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.sql.DriverManager.getConnection;

public class SQLAuthDAO implements AuthDAO {
    private static final DatabaseManager manager = new DatabaseManager();
    private final String authenticateUserQuery = "SELECT * FROM user_table WHERE username = ? AND password = ?";
    private final String addAuthTokenQuery = "INSERT INTO auth_table(username, auth_token) VALUES (?, ?)";
    private final String getAuthTokenQuery = "SELECT * FROM auth_table WHERE auth_token = ?";
    private final String getByAuthTokenQuery = "SELECT * FROM auth_table WHERE auth_token = ?";
    private final String getByUsernameQuery = "SELECT * FROM auth_table WHERE username = ?";
    private final String addAuthDataQuery = "INSERT INTO auth_table(username, auth_token) VALUES (?, ?)";
    private final String removeAuthDataQuery = "DELETE FROM auth_table WHERE auth_token = ?";
    private final String clearAuthDataQuery = "DELETE FROM auth_table";
    private HashMap<String, Integer> joinedGames = new HashMap<String, Integer>();

    public SQLAuthDAO() {
        //dbCreationCheck();
        try {
            manager.createDatabase();
        } catch (DataAccessException e) {
            //e.printStackTrace();
        }
    }
    @Override
    public Map<String, AuthData> getAuthList() throws DataAccessException {
        dbCreationCheck();
        Map<String, AuthData> authMap = new HashMap<>();

        try (Connection connection = manager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getByAuthTokenQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String authToken = resultSet.getString("auth_token");
                String username = resultSet.getString("username");
                authMap.put(authToken, new AuthData(authToken, username));
            }

        } catch (SQLException e) {
            //handleSQLException(e);
        }

        return authMap;
    }

    public HashMap<String, Integer> getJoinedGames(){
        return joinedGames;
    }


    @Override
    public boolean clearAuthData() throws DataAccessException {
        dbCreationCheck();
        try (Connection connection = manager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(clearAuthDataQuery)) {

            preparedStatement.executeUpdate();
            joinedGames.clear();
            return true;

        } catch (SQLException e) {
            //e.printStackTrace();
        }//boop
        return false;
    }


    @Override
    public boolean addAuthToken(AuthData authData) throws DataAccessException {
        dbCreationCheck();
        try (Connection connection = manager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(addAuthTokenQuery)) {

            preparedStatement.setString(1, authData.getUsername());
            preparedStatement.setString(2, authData.getAuthToken());
            preparedStatement.executeUpdate();
            joinedGames.put(authData.getAuthToken(), -1);
            return true;
        } catch (SQLException e) {
        }
        return false;
    }

    @Override
    public AuthData getAuthToken(String authToken) throws DataAccessException {
        dbCreationCheck();
        return fetchDataByQuery(getAuthTokenQuery, authToken);
    }

    @Override
    public boolean removeAuthData(String authToken) throws DataAccessException {
        dbCreationCheck();
        try (Connection connection = manager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(removeAuthDataQuery)) {

            preparedStatement.setString(1, authToken);
            preparedStatement.executeUpdate();
            joinedGames.remove(authToken);
            return true;
        } catch (SQLException e) {
        }
        return false;
    }

    private AuthData fetchDataByQuery(String query, String parameter) throws DataAccessException {
        dbCreationCheck();
        try (Connection connection = manager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, parameter);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new AuthData(resultSet.getString("auth_token"), resultSet.getString("username"));
                }
            }

        } catch (SQLException e) {
        }
        return null;
    }
    @Override
    public boolean isValidAuthToken(String authToken) throws DataAccessException {
        dbCreationCheck();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = manager.getConnection();

            statement = connection.createStatement();

            String query = "SELECT * FROM auth_table WHERE auth_token = '" + authToken + "'";
            resultSet = statement.executeQuery(query);

            return resultSet.next();

        } catch (SQLException e) {

            return false;
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                //e.printStackTrace();
            }
        }
    }


    public static void dbCreationCheck() throws DataAccessException {
        boolean dbExists;
        try {
            Connection connection = manager.getConnection();
            connection.close();
            dbExists = true;
        } catch (SQLException e) {
            dbExists = false;
        }

        if(dbExists){
            Connection connection = manager.getConnection();
            createTableIfNotExists("game_table");
            createTableIfNotExists("auth_table");
            createTableIfNotExists("user_table");
        }
        else{
            Connection connection = manager.getConnection();
            manager.createDatabase();

            createTableIfNotExists("game_table");
            createTableIfNotExists("auth_table");
            createTableIfNotExists("user_table");
        }
    }
    private static void createTableIfNotExists(String tableName) throws DataAccessException {
        try (Connection connection = manager.getConnection();
             Statement statement = connection.createStatement()) {

            if (!tableExists(statement, tableName)) {

                String createTableQuery = "CREATE TABLE " + tableName + " ("
                        + "id INT PRIMARY KEY AUTO_INCREMENT,"
                        + "column1 VARCHAR(255),"
                        + "column2 INT)";
                // If the table doesn't exist, create it
                if(tableName.equals("game_table")){
                    createTableQuery = "CREATE TABLE " + tableName + " ("
                            + "white_username VARCHAR(255),"
                            + "black_username VARCHAR(255),"
                            + "game_name VARCHAR(255),"
                            + "game_id INT PRIMARY KEY AUTO_INCREMENT,"
                    + "game BLOB)";
                }
                if(tableName.equals("user_table")){
                    createTableQuery = "CREATE TABLE " + tableName + " ("
                            + "username VARCHAR(255),"
                            + "password VARCHAR(255),"
                            + "email VARCHAR(255))";
                }
                if(tableName.equals("auth_table")){
                    createTableQuery = "CREATE TABLE " + tableName + " ("
                            + "username VARCHAR(255),"
                            + "auth_token VARCHAR(255))";
                }
                statement.executeUpdate(createTableQuery);
            }

        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }

    private static boolean tableExists(Statement statement, String tableName) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SHOW TABLES LIKE '" + tableName + "'");
        return resultSet.next();
    }

}
