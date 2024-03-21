package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DatabaseManager;
import dataAccess.dao.*;
import model.UserData;
import requests.JoinObject;
import requests.JoinResponse;
import requests.RegisterResponse;
import server.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ChessClient {
    private boolean loggedIn;
    final Gson gson = new Gson();
    final UserDAO userDAO = new SQLUserDAO();
    final GameDAO gameDAO = new SQLGameDAO();
    final AuthDAO authDAO = new SQLAuthDAO();
    Server server = new Server();
    DatabaseManager manager = new DatabaseManager();

    public ChessClient() {
        this.loggedIn = false;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Chess Pregame!");

        while (true) {
            if (!loggedIn) {
                displayPreloginOptions();
                String input = scanner.nextLine().trim().toLowerCase();
                switch (input) {
                    case "help":
                        displayPreloginHelp();
                        break;
                    case "quit":
                        System.out.println("Goodbye!");
                        return;
                    case "login":
                        login();
                        break;
                    case "register":
                        register();
                        break;
                    default:
                        System.out.println("Invalid command. Please try again.");
                }
            } else {
                displayPostloginOptions();
                String input = scanner.nextLine().trim().toLowerCase();
                switch (input) {
                    case "help":
                        displayPostloginHelp();
                        break;
                    case "logout":
                        logout();
                        break;
                    case "create game":
                        createGame();
                        break;
                    case "list games":
                        listGames();
                        break;
                    case "join game":
                        joinGame();
                        break;
                    case "join observer":
                        joinObserver();
                        break;
                    default:
                        System.out.println("Invalid command. Please try again.");
                }
            }
        }
    }

    private void displayPreloginOptions() {
        System.out.println("Prelogin commands: Help, Quit, Login, Register");
    }

    private void displayPreloginHelp() {
        System.out.println("Help - Displays available commands\n" +
                "Quit - Exits the program\n" +
                "Login - Login with existing credentials\n" +
                "Register - Register a new account");
    }

    private void login() {
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

            // Prepare the user data for login
            UserData userData = new UserData(username, password, email);
            userData.setUsername(username);
            userData.setPassword(password);
            userData.setEmail(email);

            Gson gson = new Gson();
            String requestBody = gson.toJson(userData);

            connection.getOutputStream().write(requestBody.getBytes());

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Login successful, read the response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String jsonResponse = in.readLine();
                RegisterResponse loginResponse = gson.fromJson(jsonResponse, RegisterResponse.class);
                // Process the login response further if needed
                loggedIn = true;
                System.out.println("Successfully logged in!");
            } else {
                // Login failed, handle accordingly
                System.out.println("Login failed. Please check your credentials.");
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void register() {
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

            // Prepare the user data for registration
            UserData userData = new UserData(username, password, email);
            userData.setEmail(email);
            userData.setUsername(username);
            userData.setPassword(password);

            Gson gson = new Gson();
            String requestBody = gson.toJson(userData);

            connection.getOutputStream().write(requestBody.getBytes());

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Registration successful, read the response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String jsonResponse = in.readLine();
                RegisterResponse registerResponse = gson.fromJson(jsonResponse, RegisterResponse.class);
                // Process the registration response further if needed
                loggedIn = true;
                System.out.println("Successfully registered and logged in!");
            } else {
                // Registration failed, handle accordingly
                System.out.println("Registration failed. Please try again later.");
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayPostloginOptions() {
        System.out.println("Postlogin commands: Help, Logout, Create Game, List Games, Join Game, Join Observer");
    }

    private void displayPostloginHelp() {
        System.out.println("Help - Displays available commands\n" +
                "Logout - Logs out the user\n" +
                "Create Game - Create a new game\n" +
                "List Games - List existing games\n" +
                "Join Game - Join a game\n" +
                "Join Observer - Observe a game");
    }

    private void logout() {
        try {
            URL url = new URL("http://localhost:8080/session");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Authorization", "your_auth_token");
            connection.setDoOutput(true);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Logout successful
                loggedIn = false;
                System.out.println("Logged out successfully.");
            } else {
                // Logout failed, handle accordingly
                System.out.println("Logout failed.");
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createGame() {
        try {
            URL url = new URL("http://localhost:8080/game");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "your_auth_token");
            connection.setDoOutput(true);

            // Prepare game data for creation if needed
            // String gameData = ...;

            // Write game data to the request body
            // connection.getOutputStream().write(gameData.getBytes());

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Game created successfully
                System.out.println("Game created successfully.");
            } else {
                // Game creation failed, handle accordingly
                System.out.println("Failed to create game.");
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listGames() {
        try {
            URL url = new URL("http://localhost:8080/game");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "your_auth_token");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read and process the list of games if needed
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                // Process the response as needed
                System.out.println("List of games: " + response.toString());
            } else {
                // Failed to retrieve the list of games, handle accordingly
                System.out.println("Failed to retrieve the list of games.");
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void joinGame() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter game ID: ");
            int gameId = Integer.parseInt(reader.readLine().trim());
            System.out.print("Enter team color (BLACK or WHITE): ");
            String colorStr = reader.readLine().trim().toUpperCase();
            ChessGame.TeamColor teamColorParam = ChessGame.TeamColor.valueOf(colorStr);

            URL url = new URL("http://localhost:8080/game");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Prepare the join object
            JoinObject joinObject = new JoinObject(teamColorParam, gameId);

            Gson gson = new Gson();
            String requestBody = gson.toJson(joinObject);

            connection.getOutputStream().write(requestBody.getBytes());

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Join successful, read the response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String jsonResponse = in.readLine();
                JoinResponse joinResponse = gson.fromJson(jsonResponse, JoinResponse.class);
                // Process the join response further if needed
                System.out.println("Joined the game successfully.");
            } else {
                // Join failed, handle accordingly
                System.out.println("Failed to join the game. Please try again later.");
            }

            connection.disconnect();
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void joinObserver() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter game ID: ");
            int gameId = Integer.parseInt(reader.readLine().trim());

            URL url = new URL("http://localhost:8080/game");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Prepare the join object
            JoinObject joinObject = new JoinObject(null, gameId); // Null for observer

            Gson gson = new Gson();
            String requestBody = gson.toJson(joinObject);

            connection.getOutputStream().write(requestBody.getBytes());

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Join successful, read the response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String jsonResponse = in.readLine();
                JoinResponse joinResponse = gson.fromJson(jsonResponse, JoinResponse.class);
                // Process the join response further if needed
                System.out.println("Joined as an observer.");
            } else {
                // Join failed, handle accordingly
                System.out.println("Failed to join as an observer. Please try again later.");
            }

            connection.disconnect();
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        ChessClient chessClient = new ChessClient();
//        chessClient.start();
//    }
}