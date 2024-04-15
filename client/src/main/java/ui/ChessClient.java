package ui;

import com.google.gson.*;
import exception.ResponseException;
import model.GameData;
import requests.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;
import com.google.gson.Gson;
import WebSocket.NotificationHandler;
import server.ServerFacade;
import WebSocket.WebSocketFacade;
public class ChessClient {
    private boolean loggedIn;
    String authToken;
    private boolean inGame;
    //private Session session;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;
    Scanner scanner = new Scanner(System.in);
    private WebSocketFacade ws;
    private ServerFacade server = null;
    private NotificationHandler notificationHandler = null;

    public ChessClient(String serverUrl, NotificationHandler notificationHandler) {
        this.serverUrl = serverUrl;
        server = new ServerFacade(serverUrl);
        this.loggedIn = false;
        this.notificationHandler = notificationHandler;
    }


    public String help() {
        while(true){
            if (state == State.SIGNEDOUT) {
                return """
                    - Login
                    - Register
                    - Help
                    - Quit
                    """;
            }
            if (inGame) {
                return """
                    - Make Move
                    - Leave
                    - Resign
                    - Redraw Chess Board
                    - Highlight Legal Moves
                    - Help
                    """;
            } else {
                return """
                    - Logout
                    - Create Game
                    - List Games
                    - Join Player
                    - Join Observer
                    - Help
                    """;
            }
        }
    }

    public String eval(String input) throws IOException {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register();
                case "join player" -> joinPlayer(params);
                case "list games" -> listGames();
                case "logout" -> logout(params);
                case "join observer" -> joinObserver(params);
                case "make move" -> makeMoveHandler(params);
                case "highlight legal moves" -> legalMoves(params);
                case "redraw board" -> redrawBoard(params);
                case "leave" -> leaveGameHandler(params);
                case "resign" -> resignHandler(params);
                case "create game" -> createGame(params);
                case "quit" -> "quit";
                case "" -> help();
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String login(String... params) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter username: ");
            String username = reader.readLine().trim();
            System.out.print("Enter password: ");
            String password = reader.readLine().trim();
            System.out.print("Enter email: ");
            String email = reader.readLine().trim();

            URL url = new URL("http://localhost:8080/session");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            model.UserData userData = new model.UserData(username, password, email);
            userData.setUsername(username);
            userData.setPassword(password);
            userData.setEmail(email);

            Gson gson = new Gson();
            String requestBody = gson.toJson(userData);

            connection.getOutputStream().write(requestBody.getBytes());

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String jsonResponse = in.readLine();
                RegisterResponse loginResponse = gson.fromJson(jsonResponse, RegisterResponse.class);
                if (loginResponse.message() == null) {
                    authToken = loginResponse.authToken();
                    loggedIn = true;
                    //System.out.println("Successfully logged in!");
                    System.out.println("\u001B[36mSuccessfully logged in!\u001B[0m");
                    return "\u001B[36mSuccessfully logged in!\u001B[0m";
                }
            } else {
                //System.out.println("Login failed. Please check your credentials.");
                System.out.println("\u001B[31mLogin failed. Please check your credentials.\u001B[0m");
                return "\u001B[31mLogin failed. Please check your credentials.\u001B[0m";
            }

            connection.disconnect();
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return null;
    }

    private String register() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter email: ");
            String email = reader.readLine().trim();
            System.out.print("Enter username: ");
            String username = reader.readLine().trim();
            System.out.print("Enter password: ");
            String password = reader.readLine().trim();

            URL url = new URL("http://localhost:8080/user");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            model.UserData userData = new model.UserData(username, password, email);
            userData.setEmail(email);
            userData.setUsername(username);
            userData.setPassword(password);

            Gson gson = new Gson();
            String requestBody = gson.toJson(userData);

            connection.getOutputStream().write(requestBody.getBytes());

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String jsonResponse = in.readLine();
                RegisterResponse registerResponse = gson.fromJson(jsonResponse, RegisterResponse.class);
                if (registerResponse.message() == null) {
                    authToken = registerResponse.authToken();
                    loggedIn = true;
                    System.out.println("\u001B[36mSuccessfully registered and logged in!\u001B[0m");
                    return "\u001B[36mSuccessfully registered and logged in!\u001B[0m";
                }
            } else {
                System.out.println("\u001B[31mRegistration failed. Please try again later.\u001B[0m");
                return "\u001B[31mRegistration failed. Please try again later.\u001B[0m";
            }

            connection.disconnect();
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return null;
    }

    private String logout(String... params) {
        String response = null;
        try {
            URL url = new URL("http://localhost:8080/session");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Authorization", authToken);
            connection.setDoOutput(true);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                loggedIn = false;
                authToken = null;
                System.out.println("\u001B[36mLogged out successfully.\u001B[0m");
                response = "\u001B[36mLogged out successfully.\u001B[0m";
            } else {
                System.out.println("\u001B[31mLogout failed.\u001B[0m");
                response = "\u001B[31mLogout failed.\u001B[0m";
            }

            connection.disconnect();
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return response;
    }

    //NEW
    public String createGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 1) {
            try {
                var id = Integer.parseInt(params[0]);
                var gameName = getGame(id).getGameName();
                var game = getGame(id);
                if (game == null) {
                    server.createGame();
                    return String.format("Name: %s, ID: %d", gameName, id);
                }
            } catch (NumberFormatException ignored) {
            }
        }
        throw new ResponseException(400, "Expected: " + Integer.parseInt(params[0]));//game id
    }
    private GameData getGame(int id) throws ResponseException {
        for (var game : server.listGames()) {
            if (game.getGameID() == id) {
                return game;
            }
        }
        return null;
    }


    //NEW
    private String listGames() throws ResponseException {
        assertSignedIn();
        var games = server.listGames();
        var result = new StringBuilder();
        var gson = new Gson();
        for (var game : games) {
            result.append(gson.toJson(game)).append('\n');
        }
        return result.toString();
    }

    private String redrawBoard(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 1) {
            try {
                var id = Integer.parseInt(params[0]);
                var game = getGame(id);
                if (game != null) {
                    //new DrawBoardHandler(params).draw(game.getBoard());
                    return "Board redrawn.";
                }
            } catch (NumberFormatException ignored) {
            }
        }
        throw new ResponseException(400, "Game does not exist");//throw new error object?
    }

    private String legalMoves(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 1) {
            try {
                var id = Integer.parseInt(params[0]);
                var game = getGame(id);
                if (game != null) {
                    //new LegalMovesHandler(params);
                    return "Legal moves";
                }
            } catch (NumberFormatException ignored) {
            }
        }
        throw new ResponseException(400, "Game does not exist");//throw new error object?
    }

    private String resignHandler(String... params) throws IOException {
        //session.getRemote().sendString("RESIGN");
        System.out.print(Arrays.toString(params));
        return "resigned";
    }

    //private void leaveGameHandler(Session session, String message) throws DataAccessException, IOException {
    private String leaveGameHandler(String... params) throws IOException {
        return "left game";
    }

    private String makeMoveHandler(String... params) throws IOException {
        return "Move completed";
    }

    private String joinPlayer(String... params) throws IOException {
        //session.getRemote().sendString("JOIN_PLAYER");
        return "Joined as a player";
    }

    private String joinObserver(String... params) throws IOException {
        //session.getRemote().sendString("JOIN_OBSERVER");
        return "Joined as an observer";
    }

    private static class UserData {
        private String username;
        private String password;
        private String email;

        public UserData(String username, String password, String email) {
            this.username = username;
            this.password = password;
            this.email = email;
        }
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }
}