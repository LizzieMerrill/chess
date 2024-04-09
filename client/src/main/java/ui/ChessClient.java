//package ui;
//
//import chess.ChessGame;
//import com.google.gson.*;
//import model.GameData;
//import model.UserData;
//import requests.*;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Scanner;
//
//public class ChessClient {
//    private boolean loggedIn;
//    String authToken;
//    GameUtils gameUtils = new GameUtils();
//
//    public ChessClient() {
//        this.loggedIn = false;
//    }
//
//    public void start() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Welcome to Chess Pregame!");
//
//        while (true) {
//            if (!loggedIn) {
//                displayPreloginOptions();
//                String input = scanner.nextLine().trim().toLowerCase();
//                switch (input) {
//                    case "help":
//                        displayPreloginHelp();
//                        break;
//                    case "quit":
//                        System.out.println("Goodbye!");
//                        return;
//                    case "login":
//                        login();
//                        break;
//                    case "register":
//                        register();
//                        break;
//                    default:
//                        System.out.println("Invalid command. Please try again.");
//                }
//            } else {
//                displayPostloginOptions();
//                String input = scanner.nextLine().trim().toLowerCase();
//                switch (input) {
//                    case "help":
//                        displayPostloginHelp();
//                        break;
//                    case "logout":
//                        logout();
//                        break;
//                    case "create game":
//                        createGame();
//                        break;
//                    case "list games":
//                        listGames();
//                        break;
//                    case "join game":
//                        joinGame();
//                        break;
//                    case "join observer":
//                        joinObserver();
//                        break;
//                    default:
//                        System.out.println("Invalid command. Please try again.");
//                }
//            }
//        }
//    }
//
//    private void displayPreloginOptions() {
//        System.out.println("Prelogin commands: \u001B[33mHelp\u001B[0m, \u001B[33mQuit\u001B[0m, \u001B[33mLogin\u001B[0m, \u001B[33mRegister\u001B[0m");
//    }
//
////    private void displayPreloginHelp() {
////        System.out.println("Help - Displays available commands\n" +
////                "Quit - Exits the program\n" +
////                "Login - Login with existing credentials\n" +
////                "Register - Register a new account");
////    }
//
//    private void displayPreloginHelp() {
//        // Format command words as yellow
//        System.out.println("\u001B[33mHelp - Displays available commands\n" +
//                "Quit - Exits the program\n" +
//                "Login - Login with existing credentials\n" +
//                "Register - Register a new account\u001B[0m");
//    }
//
//    private void login() {
//        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//            System.out.print("Enter username: ");
//            String username = reader.readLine().trim();
//            System.out.print("Enter password: ");
//            String password = reader.readLine().trim();
//            System.out.print("Enter email: ");
//            String email = reader.readLine().trim();
//
//            URL url = new URL("http://localhost:8080/session");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setDoOutput(true);
//
//            UserData userData = new UserData(username, password, email);
//            userData.setUsername(username);
//            userData.setPassword(password);
//            userData.setEmail(email);
//
//            Gson gson = new Gson();
//            String requestBody = gson.toJson(userData);
//
//            connection.getOutputStream().write(requestBody.getBytes());
//
//            int responseCode = connection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                String jsonResponse = in.readLine();
//                RegisterResponse loginResponse = gson.fromJson(jsonResponse, RegisterResponse.class);
//                if (loginResponse.message() == null) {
//                    authToken = loginResponse.authToken();
//                    loggedIn = true;
//                    //System.out.println("Successfully logged in!");
//                    System.out.println("\u001B[36mSuccessfully logged in!\u001B[0m");
//                }
//            } else {
//                //System.out.println("Login failed. Please check your credentials.");
//                System.out.println("\u001B[31mLogin failed. Please check your credentials.\u001B[0m");
//            }
//
//            connection.disconnect();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void register() {
//        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//            System.out.print("Enter email: ");
//            String email = reader.readLine().trim();
//            System.out.print("Enter username: ");
//            String username = reader.readLine().trim();
//            System.out.print("Enter password: ");
//            String password = reader.readLine().trim();
//
//            URL url = new URL("http://localhost:8080/user");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setDoOutput(true);
//
//            UserData userData = new UserData(username, password, email);
//            userData.setEmail(email);
//            userData.setUsername(username);
//            userData.setPassword(password);
//
//            Gson gson = new Gson();
//            String requestBody = gson.toJson(userData);
//
//            connection.getOutputStream().write(requestBody.getBytes());
//
//            int responseCode = connection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                String jsonResponse = in.readLine();
//                RegisterResponse registerResponse = gson.fromJson(jsonResponse, RegisterResponse.class);
//                if (registerResponse.message() == null) {
//                    authToken = registerResponse.authToken();
//                    loggedIn = true;
//                    System.out.println("\u001B[36mSuccessfully registered and logged in!\u001B[0m");
//                }
//            } else {
//                System.out.println("\u001B[31mRegistration failed. Please try again later.\u001B[0m");
//            }
//
//            connection.disconnect();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void logout() {
//        try {
//            URL url = new URL("http://localhost:8080/session");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("DELETE");
//            connection.setRequestProperty("Authorization", authToken);
//            connection.setDoOutput(true);
//
//            int responseCode = connection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                loggedIn = false;
//                authToken = null;
//                System.out.println("\u001B[36mLogged out successfully.\u001B[0m");
//            } else {
//                System.out.println("\u001B[31mLogout failed.\u001B[0m");
//            }
//
//            connection.disconnect();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    private void displayPostloginOptions() {
//        System.out.println("Postlogin commands: \u001B[33mHelp\u001B[0m, \u001B[33mLogout\u001B[0m, \u001B[33mCreate Game\u001B[0m, \u001B[33mList Games\u001B[0m, \u001B[33mJoin Game\u001B[0m, \u001B[33mJoin Observer\u001B[0m");
//    }
//
//    private void displayPostloginHelp() {
//        System.out.println("\u001B[33mHelp - Displays available commands\n" +
//                "Logout - Logs out the user\n" +
//                "Create Game - Create a new game\n" +
//                "List Games - List existing games\n" +
//                "Join Game - Join a game\n" +
//                "Join Observer - Observe a game\u001B[0m");
//    }
//
//
//    private void createGame() {
//        try {
//            URL url = new URL("http://localhost:8080/game");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setRequestProperty("Authorization", authToken);
//            connection.setDoOutput(true);
//
//
//            System.out.print("Enter game name: ");
//            Scanner scanner = new Scanner(System.in);
//            String gameName = scanner.nextLine().trim();
//
//            GameNameObject gameNameObject = new GameNameObject(gameName);
//
//            String requestBody = new Gson().toJson(gameNameObject);
//
//            connection.getOutputStream().write(requestBody.getBytes());
//
//            int responseCode = connection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                String jsonResponse = in.readLine();
//                CreateResponse createResponse = new Gson().fromJson(jsonResponse, CreateResponse.class);
//                System.out.println("\u001B[36mGame created successfully. Game ID: \u001B[0m" + createResponse.gameID());
//            } else {
//                System.out.println("\u001B[31mFailed to create game.\u001B[0m");
//            }
//
//            connection.disconnect();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
////    private void listGames() {
////        try {
////            URL url = new URL("http://localhost:8080/game");
////            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
////            connection.setRequestMethod("GET");
////            connection.setRequestProperty("Authorization", authToken);
////
////            int responseCode = connection.getResponseCode();
////            if (responseCode == HttpURLConnection.HTTP_OK) {
////                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
////                String inputLine;
////                StringBuilder response = new StringBuilder();
////                while ((inputLine = in.readLine()) != null) {
////                    response.append(inputLine);
////                }
////                in.close();
////                String formattedGames = gameUtils.formatGameList(response.toString());
////                System.out.println(formattedGames);
////            } else {
////                System.out.println("\u001B[31mFailed to retrieve the list of games.\u001B[0m");
////            }
////
////            connection.disconnect();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////    }
//
//
////    private void listGames() {
////        try {
////            URL url = new URL("http://localhost:8080/game");
////            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
////            connection.setRequestMethod("GET");
////            connection.setRequestProperty("Authorization", authToken);
////
////            int responseCode = connection.getResponseCode();
////            if (responseCode == HttpURLConnection.HTTP_OK) {
////                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
////                StringBuilder response = new StringBuilder();
////                String inputLine;
////                while ((inputLine = in.readLine()) != null) {
////                    response.append(inputLine);
////                }
////                in.close();
////
////                // Parse the response JSON array of games
////                JsonArray gamesArray = null;
////
////                // Inside your method
////                try {
////                    // Your existing code to retrieve the JSON response
////                    // Parse the JSON string into a JsonElement
////                    JsonElement jsonElement = JsonParser.parseString(response.toString());
////
////                    // Check if the parsed element is a JsonArray
////                    if (jsonElement.isJsonArray()) {
////                        // Cast the JsonElement to JsonArray
////                        gamesArray = jsonElement.getAsJsonArray();
////
////                        // Iterate over each game in the array
////                        for (int i = 0; i < gamesArray.size(); i++) {
////                            JsonObject gameObject = (JsonObject) gamesArray.get(i);
////                            String formattedGame = formatGame(gameObject);
////                            System.out.println(formattedGame);
////                        }
////                    } else {
////                        // Handle the case where the JSON response is not an array
////                        System.err.println("JSON response is either null or not an array.");
////                    }
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////            } else {
////                // Format failure message as red
////                System.out.println("\u001B[31mFailed to retrieve the list of games.\u001B[0m");
////            }
////
////            connection.disconnect();
////        } catch (IOException | JsonIOException e) {
////            e.printStackTrace();
////        }
////    }
//
//
//
//
//
//
//
//
//
////    private void listGames() {
////        try {
////            URL url = new URL("http://localhost:8080/game");
////            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
////            connection.setRequestMethod("GET");
////            connection.setRequestProperty("Authorization", authToken);
////
////            int responseCode = connection.getResponseCode();
////            if (responseCode == HttpURLConnection.HTTP_OK) {
////                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
////                StringBuilder response = new StringBuilder();
////                String inputLine;
////                while ((inputLine = in.readLine()) != null) {
////                    response.append(inputLine);
////                }
////                in.close();
////
////                // Parse the response JSON and extract the array of games
////                try {
////                    JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
////                    if (jsonResponse.has("games")) {
////                        JsonArray jsonArray = jsonResponse.getAsJsonArray("games");
////                        if (jsonArray.size() == 0) {
////                            System.out.println("No games found.");
////                        } else {
////                            // Iterate over each game in the array
////                            for (int i = 0; i < jsonArray.size(); i++) {
////                                JsonObject gameObject = jsonArray.get(i).getAsJsonObject();
////                                String formattedGame = formatGame(gameObject);
////                                System.out.println(formattedGame);
////                            }
////                        }
////                    } else {
////                        System.err.println("Response does not contain a 'games' array.");
////                    }
////                } catch (Exception e) {
////                    //System.err.println("Error parsing JSON response: " + e.getMessage());
////                    System.err.println("Empty");
////                    e.printStackTrace();
////                }
////            } else {
////                // Format failure message as red
////                System.out.println("\u001B[31mFailed to retrieve the list of games. HTTP Error Code: " + responseCode + "\u001B[0m");
////            }
////
////            connection.disconnect();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////    }
////
////
////    private String formatGame(JsonObject gameObject) {
////        // Extract game attributes and format them as needed
////        String gameName = gameObject.get("gameName").getAsString();
////        String whiteUser = "N/A";
////        String blackUser = "N/A";
////        if(gameObject.get("whiteUsername") != null){
////            whiteUser = gameObject.get("whiteUsername").getAsString();
////        }
////        if(gameObject.get("blackUsername") != null){
////            blackUser = gameObject.get("blackUsername").getAsString();
////        }
////        int gameId = gameObject.get("gameID").getAsInt();
////
////        // Construct and return the formatted game string
////        return String.format("Game ID: %d\nGame Name: %s\nWhite User: %s\nBlack User: %s\n", gameId, gameName, whiteUser, blackUser);
////    }
//
//
//
//
//
//
//
//    private void listGames() {
//        try {
//            URL url = new URL("http://localhost:8080/game");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setRequestProperty("Authorization", authToken);
//
//            int responseCode = connection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                StringBuilder response = new StringBuilder();
//                String inputLine;
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//                }
//                in.close();
//
//                // Parse the response JSON and extract the array of games
//                try {
//                    JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
//                    if (jsonResponse.has("games")) {
//                        JsonArray jsonArray = jsonResponse.getAsJsonArray("games");
//                        if (jsonArray.size() == 0) {
//                            System.out.println("No games found.");
//                        } else {
//                            // Iterate over each game in the array
//                            for (int i = 0; i < jsonArray.size(); i++) {
//                                JsonObject gameObject = jsonArray.get(i).getAsJsonObject();
//                                String formattedGame = formatGame(gameObject, i + 1); // Adjusted to count from one
//                                System.out.println(formattedGame);
//                            }
//                        }
//                    } else {
//                        System.err.println("Response does not contain a 'games' array.");
//                    }
//                } catch (Exception e) {
//                    //System.err.println("Error parsing JSON response: " + e.getMessage());
//                    System.err.println("Empty");
//                    e.printStackTrace();
//                }
//            } else {
//                // Format failure message as red
//                System.out.println("\u001B[31mFailed to retrieve the list of games. HTTP Error Code: " + responseCode + "\u001B[0m");
//            }
//
//            connection.disconnect();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private String formatGame(JsonObject gameObject, int index) {
//        // Extract game attributes and format them as needed
//        String gameName = gameObject.get("gameName").getAsString();
//        String whiteUser = "N/A";
//        String blackUser = "N/A";
//        if(gameObject.get("whiteUsername") != null){
//            whiteUser = gameObject.get("whiteUsername").getAsString();
//        }
//        if(gameObject.get("blackUsername") != null){
//            blackUser = gameObject.get("blackUsername").getAsString();
//        }
//
//        // Construct and return the formatted game string
//        return String.format("%d. Game Name: %s\nWhite User: %s\nBlack User: %s\n", index, gameName, whiteUser, blackUser);
//    }
//
//
//
//
//
//    private void joinGame() {
//        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//            System.out.print("Enter game ID: ");
//            int gameId = Integer.parseInt(reader.readLine().trim());
//            System.out.print("Enter team color (BLACK or WHITE): ");
//            String colorStr = reader.readLine().trim().toUpperCase();
//            ChessGame.TeamColor teamColorParam = ChessGame.TeamColor.valueOf(colorStr);
//
//            URL url = new URL("http://localhost:8080/game");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("PUT");
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setRequestProperty("Authorization", authToken);
//            connection.setDoOutput(true);
//
//            JoinObject joinObject = new JoinObject(teamColorParam, gameId);
//
//            Gson gson = new Gson();
//            String requestBody = gson.toJson(joinObject);
//
//            connection.getOutputStream().write(requestBody.getBytes());
//
//            int responseCode = connection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                String jsonResponse = in.readLine();
//                JoinResponse joinResponse = gson.fromJson(jsonResponse, JoinResponse.class);
//                System.out.println("\u001B[36mJoined the game successfully.\u001B[0m");
//                drawStartBoards();
//            } else {
//                System.out.println("\u001B[31mFailed to join the game. Please try again later.\u001B[0m");
//            }
//
//            connection.disconnect();
//        } catch (IOException | IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void joinObserver() {
//        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//            System.out.print("Enter game ID: ");
//            int gameId = Integer.parseInt(reader.readLine().trim());
//
//            URL url = new URL("http://localhost:8080/game");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("PUT");
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setRequestProperty("Authorization", authToken);
//            connection.setDoOutput(true);
//            JoinObject joinObject = new JoinObject(null, gameId); // Null for observer
//
//            Gson gson = new Gson();
//            String requestBody = gson.toJson(joinObject);
//
//            connection.getOutputStream().write(requestBody.getBytes());
//
//            int responseCode = connection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                String jsonResponse = in.readLine();
//                JoinResponse joinResponse = gson.fromJson(jsonResponse, JoinResponse.class);
//                System.out.println("Joined as an observer.");
//                drawStartBoards();
//            } else {
//                System.out.println("Failed to join as an observer. Please try again later.");
//            }
//
//            connection.disconnect();
//        } catch (IOException | IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void drawStartBoards() {
//        // Define the size of the chessboard
//        int size = 8;
//        String color;
//
//        // Define the characters for different pieces
//        char[][] pieces = {
//                {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'}, // Black pieces
//                {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'}, // Black pawns
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, // Empty row
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, // Empty row
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, // Empty row
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, // Empty row
//                {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'}, // White pawns
//                {'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'}  // White pieces
//        };
//
//        // Draw the chessboard twice, once with white pieces at the bottom and once with black pieces at the bottom
//        for (int orientation = 0; orientation < 2; orientation++) {
//            if(orientation == 0){
//                System.out.println("Black POV:\n");
//            }
//            else{
//                System.out.println("White POV:\n");
//            }
//            for (int row = 0; row < size; row++) {
//                for (int col = 0; col < size; col++) {
//                    // Calculate the color of the square
//                    boolean isWhiteSquare = (row + col) % 2 == 0;
//                    // Color variables for terminal printing
//                    String bgWhite = "\u001B[47m";
//                    String bgBlack = "\u001B[40m";
//                    String fgDarkRed = "\u001B[31;2m";
//                    String resetColor = "\u001B[0m";
//
//                    // Color the background based on the square color
//                    if ((orientation == 0 && isWhiteSquare) || (orientation == 1 && !isWhiteSquare)) {
//                        System.out.print(bgWhite);
//                    } else {
//                        System.out.print(bgBlack);
//                    }
//
//                    // Print the piece or empty square
//                    char piece = pieces[(orientation == 0) ? row : size - row - 1][col];
//                    // Color black team letters red
//                    if (Character.isLowerCase(piece)) {
//                        System.out.print(fgDarkRed);
//                    }
//                    System.out.print(" " + Character.toUpperCase(piece) + " ");
//                    // Reset the color after printing each square
//                    System.out.print(resetColor);
//                }
//                System.out.println();
//            }
//            System.out.println();
//        }
//    }
//
//
////    public static void main(String[] args) {
////        ChessClient chessClient = new ChessClient();
////        chessClient.start();
////    }
//}

package ui;

import com.google.gson.Gson;
import dataAccess.dao.AuthDAO;
import dataAccess.dao.GameDAO;
import dataAccess.dao.SQLAuthDAO;
import dataAccess.dao.SQLGameDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.net.URI;
import java.util.Collection;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ChessClient {
    private boolean loggedIn;
    private boolean inGame;
    private String authToken;
    private Session session;
    private Gson gson = new Gson();
    GameDAO gameDAO = new SQLGameDAO();
    AuthDAO authDAO = new SQLAuthDAO();

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
                if(inGame){}//TODO
                bruh
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
            WebSocketClient client = new WebSocketClient();
            client.start();

            URI uri = new URI("ws://localhost:8080/connect");
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            WebSocketListener listener = new WebSocketListener() {
                @Override
                public void onWebSocketClose(int i, String s) {

                }

                @Override
                public void onWebSocketConnect(Session session) {

                }

                @Override
                public void onWebSocketError(Throwable throwable) {

                }

                @Override
                public void onWebSocketBinary(byte[] bytes, int i, int i1) {

                }

                @Override
                public void onWebSocketText(String s) {

                }
            };
            Future<Session> future = client.connect(listener, uri, request);
            session = future.get(2, TimeUnit.SECONDS);

            UserData userData = getUserDataFromInput();

            UserGameCommand command = new UserGameCommand(, "LOGIN", gson.toJson(userData));
            session.getRemote().sendString(gson.toJson(command));

            CountDownLatch latch = new CountDownLatch(1);
            listener.setLatch(latch);
            latch.await(2, TimeUnit.SECONDS);

            if (listener.getResponse() != null && listener.getResponse().equalsIgnoreCase("success")) {
                authToken = listener.getAuthToken();
                loggedIn = true;
                System.out.println("\u001B[36mSuccessfully logged in!\u001B[0m");
            } else {
                System.out.println("\u001B[31mLogin failed. Please check your credentials.\u001B[0m");
            }

            client.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private UserData getUserDataFromInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();
        return new UserData(username, password, email);
    }

    private void register() {
        try {
            WebSocketClient client = new WebSocketClient();
            client.start();

            URI uri = new URI("ws://localhost:8080/connect");
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            WebSocketListener listener = new WebSocketListener();
            Future<Session> future = client.connect(listener, uri, request);
            session = future.get(2, TimeUnit.SECONDS);

            UserData userData = getUserDataFromInput();

            UserGameCommand command = new UserGameCommand("REGISTER", gson.toJson(userData));
            session.getRemote().sendString(gson.toJson(command));

            CountDownLatch latch = new CountDownLatch(1);
            listener.setLatch(latch);
            latch.await(2, TimeUnit.SECONDS);

            if (listener.getResponse() != null && listener.getResponse().equalsIgnoreCase("success")) {
                authToken = listener.getAuthToken();
                loggedIn = true;
                System.out.println("\u001B[36mSuccessfully registered and logged in!\u001B[0m");
            } else {
                System.out.println("\u001B[31mRegistration failed. Please try again later.\u001B[0m");
            }

            client.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logout() {
        boolean player = false;
        try {
            UserGameCommand command = new UserGameCommand(authToken);
            if(loggedIn) {
                Collection<GameData> games = gameDAO.getAllGameData();
                for (GameData game : games) {
                    if(authDAO.getAuthToken(command.getAuthString()).getUsername() == game.getBlackUsername()
                    || authDAO.getAuthToken(command.getAuthString()).getUsername() == game.getWhiteUsername()){
                        player = true;
                    }
                }
                if(player){
                    //resign logic and break
                }
                else{
                    //leave game or log out
                }
                session.getRemote().sendString(gson.toJson(command));
                loggedIn = false;
                authToken = null;
                System.out.println("\u001B[36mLogged out successfully.\u001B[0m");
            }
            System.out.println("\u001B[36mAlready logged out.\u001B[0m");
        } catch (Exception e) {
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
        // Implement your logic for creating a game
    }

    private void listGames() {
        // Implement your logic for listing games
    }

    private void joinGame() {
        // Implement your logic for joining a game
    }

    private void joinObserver() {
        // Implement your logic for joining as an observer
    }

    public static void main(String[] args) {
        ChessClient chessClient = new ChessClient();
        chessClient.start();
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
}