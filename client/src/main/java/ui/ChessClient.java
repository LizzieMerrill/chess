package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import model.UserData;
import requests.*;
import ui.GameUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ChessClient {
    private boolean loggedIn;
    String authToken;
    GameUtils gameUtils = new GameUtils();

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

            UserData userData = new UserData(username, password, email);
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
                    System.out.println("Successfully logged in!");
                }
            } else {
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

            UserData userData = new UserData(username, password, email);
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
                    System.out.println("Successfully registered and logged in!");
                }
            } else {
                System.out.println("Registration failed. Please try again later.");
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logout() {
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
                System.out.println("Logged out successfully.");
            } else {
                System.out.println("Logout failed.");
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

    private void createGame() {
        try {
            URL url = new URL("http://localhost:8080/game");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", authToken);
            connection.setDoOutput(true);


            System.out.print("Enter game name: ");
            Scanner scanner = new Scanner(System.in);
            String gameName = scanner.nextLine().trim();

            GameNameObject gameNameObject = new GameNameObject(gameName);

            String requestBody = new Gson().toJson(gameNameObject);

            connection.getOutputStream().write(requestBody.getBytes());

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String jsonResponse = in.readLine();
                CreateResponse createResponse = new Gson().fromJson(jsonResponse, CreateResponse.class);
                System.out.println("Game created successfully. Game ID: " + createResponse.gameID());
            } else {
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
            connection.setRequestProperty("Authorization", authToken);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                String formattedGames = gameUtils.formatGameList(response.toString());
                System.out.println("List of games:\n" + formattedGames);
            } else {
                System.out.println("Failed to retrieve the list of games.");
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private String toString(StringBuilder response){
//
//    }

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
            connection.setRequestProperty("Authorization", authToken);
            connection.setDoOutput(true);

            JoinObject joinObject = new JoinObject(teamColorParam, gameId);

            Gson gson = new Gson();
            String requestBody = gson.toJson(joinObject);

            connection.getOutputStream().write(requestBody.getBytes());

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String jsonResponse = in.readLine();
                JoinResponse joinResponse = gson.fromJson(jsonResponse, JoinResponse.class);
                System.out.println("Joined the game successfully.");
                drawStartBoards();
            } else {
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
            connection.setRequestProperty("Authorization", authToken);
            connection.setDoOutput(true);
            JoinObject joinObject = new JoinObject(null, gameId); // Null for observer

            Gson gson = new Gson();
            String requestBody = gson.toJson(joinObject);

            connection.getOutputStream().write(requestBody.getBytes());

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String jsonResponse = in.readLine();
                JoinResponse joinResponse = gson.fromJson(jsonResponse, JoinResponse.class);
                System.out.println("Joined as an observer.");
                drawStartBoards();
            } else {
                System.out.println("Failed to join as an observer. Please try again later.");
            }

            connection.disconnect();
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void drawStartBoards() {
        // Define the size of the chessboard
        int size = 8;

        // Define the characters for different pieces
        char[][] pieces = {
                {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'}, // Black pieces
                {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'}, // Black pawns
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, // Empty row
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, // Empty row
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, // Empty row
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, // Empty row
                {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'}, // White pawns
                {'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'}  // White pieces
        };

        // Draw the chessboard twice, once with white pieces at the bottom and once with black pieces at the bottom
        for (int orientation = 0; orientation < 2; orientation++) {
            System.out.println("Chessboard (Orientation " + (orientation + 1) + "):");
            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    // Calculate the color of the square
                    boolean isWhiteSquare = (row + col) % 2 == 0;
                    // Color variables for terminal printing
                    String bgWhite = "\u001B[47m";
                    String bgBlack = "\u001B[40m";
                    String fgDarkRed = "\u001B[31;2m";
                    String resetColor = "\u001B[0m";

                    // Color the background based on the square color
                    if ((orientation == 0 && isWhiteSquare) || (orientation == 1 && !isWhiteSquare)) {
                        System.out.print(bgWhite);
                    } else {
                        System.out.print(bgBlack);
                    }

                    // Print the piece or empty square
                    char piece = pieces[(orientation == 0) ? row : size - row - 1][col];
                    // Color black team letters red
                    if (Character.isLowerCase(piece)) {
                        System.out.print(fgDarkRed);
                    }
                    System.out.print(" " + Character.toUpperCase(piece) + " ");
                    // Reset the color after printing each square
                    System.out.print(resetColor);
                }
                System.out.println();
            }
            System.out.println();
        }
    }


//    public static void main(String[] args) {
//        ChessClient chessClient = new ChessClient();
//        chessClient.start();
//    }
}