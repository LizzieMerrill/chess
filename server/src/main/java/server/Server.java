package server;

import com.google.gson.Gson;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Objects;
import java.util.logging.Logger;

import dataAccess.access.DataAccessException;
import handlers.*;
import spark.Spark;
import dataAccess.dao.*;
import javax.sql.DataSource;

import static java.sql.DriverManager.getConnection;

public class Server {

    final Gson gson = new Gson();
    private final String jdbcUrl = "jdbc:mysql://localhost:3306/chess";
    private final String username = "root";
    private final String password = "JavaRulez2!";
//    private DataSource dataSource = new DataSource() {
//        @Override
//        public Connection getConnection() throws SQLException {
//            // Implement logic to obtain and return a database connection
//            try (Connection connection = getConnection(jdbcUrl, username, password);{
//            return connection;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        }
//
//        @Override
//        public Connection getConnection(String username, String password) throws SQLException {
//            // Implement logic to obtain and return a database connection with the provided username and password
//            return YourDatabaseConnectionProvider.getConnection(username, password);
//        }
//
//        @Override
//        public PrintWriter getLogWriter() throws SQLException {
//            // Implement logic to return the log writer
//            return null; // You can customize this as needed
//        }
//
//        @Override
//        public void setLogWriter(PrintWriter out) throws SQLException {
//            // Implement logic to set the log writer
//        }
//
//        @Override
//        public void setLoginTimeout(int seconds) throws SQLException {
//            // Implement logic to set the login timeout
//        }
//
//        @Override
//        public int getLoginTimeout() throws SQLException {
//            // Implement logic to get the login timeout
//            return 0; // You can customize this as needed
//        }
//
//        @Override
//        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
//            // Implement logic to get the parent logger
//            return null; // You can customize this as needed
//        }
//    };
    final UserDAO userDAO;

    {
        try {
            userDAO = new SQLUserDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    final GameDAO gameDAO;

    {
        try {
            gameDAO = new SQLGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    final AuthDAO authDAO;

    {
        try {
            authDAO = new SQLAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Server() {
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");
        registerEndpoints();
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void registerEndpoints() {
        //clear
        Spark.delete("/db", new ClearHandler(authDAO, userDAO, gameDAO));

        //logout
        Spark.delete("/session", new LogoutHandler(authDAO));

        //register
        Spark.post("/user", new RegisterHandler(gson, userDAO, authDAO));

        //login
        Spark.post("/session", new LoginHandler(userDAO, authDAO));

        //create game
        Spark.post("/game", new CreateGameHandler(authDAO, gameDAO, userDAO));

        //join game
        Spark.put("/game", new JoinGameHandler(authDAO, gameDAO, userDAO));

        //list game
        Spark.get("/game", new ListGamesHandler(authDAO, gameDAO, userDAO));

    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Server server = (Server) o;
        return Objects.equals(gson, server.gson) && Objects.equals(userDAO, server.userDAO) && Objects.equals(gameDAO, server.gameDAO) && Objects.equals(authDAO, server.authDAO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gson, userDAO, gameDAO, authDAO);
    }

    @Override
    public String toString() {
        return "Server{" +
                "gson=" + gson +
                ", userDAO=" + userDAO +
                ", gameDAO=" + gameDAO +
                ", authDAO=" + authDAO +
                '}';
    }
}
