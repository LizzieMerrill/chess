package ui;

import com.google.gson.*;
import deserializers.DeserializerUserGameCommand;
import exception.ResponseException;
import inGameHandlers.DrawBoardHandler;
import inGameHandlers.LegalMovesHandler;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import requests.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;
import com.google.gson.Gson;
import dataAccess.access.DataAccessException;
import dataAccess.dao.AuthDAO;
import dataAccess.dao.GameDAO;
import dataAccess.dao.SQLAuthDAO;
import WebSocket.NotificationHandler;
import dataAccess.dao.SQLGameDAO;
import org.eclipse.jetty.websocket.api.Session;
import server.ServerFacade;
import webSocketMessages.userCommands.*;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import WebSocket.WebSocketFacade;
@WebSocket
public class ChessClient {
    private boolean loggedIn;
    String authToken;
    private boolean inGame;
    private Session session;
    private final String serverUrl;
    GameDAO gameDAO = new SQLGameDAO();
    AuthDAO authDAO = new SQLAuthDAO();
    private State state = State.SIGNEDOUT;
    Scanner scanner = new Scanner(System.in);
    private WebSocketFacade ws;
    private ServerFacade server = null;
    private NotificationHandler notificationHandler = null;

    public enum State {
        SIGNEDOUT,
        SIGNEDIN
    }

    public ChessClient(String serverUrl, NotificationHandler notificationHandler) {
        this.serverUrl = serverUrl;
        server = new ServerFacade(serverUrl);
        this.loggedIn = false;
        this.notificationHandler = notificationHandler;
    }


    public String help() {
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

    public String eval(String input) {
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
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        Gson gson = new GsonBuilder().registerTypeAdapter(UserGameCommand.class, new DeserializerUserGameCommand()).create();
        var userGameCommand = gson.fromJson(message, UserGameCommand.class);
        switch (userGameCommand.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(session, message);
            case JOIN_OBSERVER -> joinObserver(session, message);
            case LEAVE -> leaveGameHandler(session, message);
            case MAKE_MOVE -> makeMoveHandler(session, message);
            case RESIGN -> resignHandler(session, message);
        }
        session.getRemote().sendString("WebSocket response: " + message);
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
                }
            } else {
                //System.out.println("Login failed. Please check your credentials.");
                System.out.println("\u001B[31mLogin failed. Please check your credentials.\u001B[0m");
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
                }
            } else {
                System.out.println("\u001B[31mRegistration failed. Please try again later.\u001B[0m");
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
                System.out.println("\u001B[36mLogged out successfully.\u001B[0m");
            } else {
                System.out.println("\u001B[31mLogout failed.\u001B[0m");
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void redrawBoard(String... params) throws ResponseException, DataAccessException {
        assertSignedIn();
        if (params.length == 1) {
            try {
                var id = Integer.parseInt(params[0]);
                var game = getGame(id);
                if (game != null) {
                    new DrawBoardHandler(params).draw(game.getBoard());
                }
            } catch (NumberFormatException ignored) {
            }
        }
        throw new ResponseException(400, "Game does not exist");//throw new error object?
    }

    private void legalMoves(String... params) throws ResponseException, DataAccessException {
        assertSignedIn();
        if (params.length == 1) {
            try {
                var id = Integer.parseInt(params[0]);
                var game = getGame(id);
                if (game != null) {
                    new LegalMovesHandler(params);
                }
            } catch (NumberFormatException ignored) {
            }
        }
        throw new ResponseException(400, "Game does not exist");//throw new error object?
    }






    private String formatGame(JsonObject gameObject, int index) {
        // Extract game attributes and format them as needed
        String gameName = gameObject.get("gameName").getAsString();
        String whiteUser = "N/A";
        String blackUser = "N/A";
        if (gameObject.get("whiteUsername") != null) {
            whiteUser = gameObject.get("whiteUsername").getAsString();
        }
        if (gameObject.get("blackUsername") != null) {
            blackUser = gameObject.get("blackUsername").getAsString();
        }

        // Construct and return the formatted game string
        return String.format("%d. Game Name: %s\nWhite User: %s\nBlack User: %s\n", index, gameName, whiteUser, blackUser);
    }

    private void displayPreLoginOptions() {
        System.out.println("Pre-Login Commands: Help, Quit, Login, Register");
    }

    private void displayPreLoginHelp() {
        System.out.println("Help - Displays available commands\n" +
                "Quit - Exits the program\n" +
                "Login - Login with existing credentials\n" +
                "Register - Register a new account");
    }

    private void displayInGameOptions() {
        System.out.println("In-Game Commands: Help, Redraw Chess Board, Leave, Make Move, Resign, Highlight Legal Moves");
    }

    private void displayInGameHelp() {
        System.out.println("Help - Displays available commands\n" +
                "Redraw Chess Board - Redraws the chess board\n" +
                "Leave - Removes user from current game and redirects to post login options\n" +
                "Make Move - User can make a move that reflects for all players in the game\n" +
                "Resign - User forfeits the match, only works if user is a player and not an observer. Does not leave game\n" +
                "Highlight Legal Moves - Allows user to select a piece for which they want to view all" +
                " legal moves. Private command to user, not displayed to other players");
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

    private void resignHandler(Session session, String message) throws DataAccessException, IOException {
//        boolean player = false;
//        int resignId = -1;
//        Collection<GameData> games = gameDAO.getAllGameData();
//        UserGameCommand command = new UserGameCommand(authToken);
//        for (GameData game : games) {
//            if(authDAO.getAuthToken(command.getAuthString()).getUsername() == game.getBlackUsername()
//                    || authDAO.getAuthToken(command.getAuthString()).getUsername() == game.getWhiteUsername()){
//                player = true;
//                resignId = game.getGameID();
//            }
//        }
//        if(player){
//            //resign logic and break
//            command = new Resign(resignId, authToken);
//        }
//        else{
//            Error resignError = new Error("You cannot resign a game you are not playing in.");
//        }
        session.getRemote().sendString("RESIGN");
    }

    private void leaveGameHandler(Session session, String message) throws DataAccessException, IOException {
//        int leaveId = -1;
//        UserGameCommand command = new UserGameCommand(authToken);
////        Collection<GameData> games = gameDAO.getAllGameData();
////        for (GameData game : games) {
////            if(authDAO.getAuthToken(command.getAuthString()).getUsername() == game.getBlackUsername()
////                    || authDAO.getAuthToken(command.getAuthString()).getUsername() == game.getWhiteUsername()){
////                leaveId = game.getGameID();
////            }
////        }
//        //JUST ASK THEM WHAT GAME THEY WANT TO LEAVE OR ACCESS WHICH GAME THEY ARE IN
//        if(leaveId != -1){
//            //leave logic and break
//            command = new Leave(leaveId, authToken);
//        }
//        else{
//            Error leaveError = new Error("You cannot leave a game that you are not playing or observing.");
//        }
        session.getRemote().sendString("LEAVE");
    }

    private void makeMoveHandler(Session session, String message) throws DataAccessException, IOException {
//        boolean player = false;
//        int playersGameId = -1;
//        Collection<GameData> games = gameDAO.getAllGameData();
//        UserGameCommand command = new UserGameCommand(authToken);
//        for (GameData game : games) {
//            if(authDAO.getAuthToken(command.getAuthString()).getUsername() == game.getBlackUsername()
//                    || authDAO.getAuthToken(command.getAuthString()).getUsername() == game.getWhiteUsername()){
//                player = true;
//                playersGameId = game.getGameID();
//            }
//        }
//        if(player){
//            //resign logic and break
//            command = new Resign(playersGameId, authToken);
//        }
//        else{
//            Error resignError = new Error("You cannot resign a game you are not playing in.");
//        }
        session.getRemote().sendString("MAKE_MOVE");
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

    private void joinPlayer(Session session, String message) throws IOException {
        session.getRemote().sendString("JOIN_PLAYER");
    }

    private void joinObserver(Session session, String message) throws IOException {
        session.getRemote().sendString("JOIN_OBSERVER");
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
//                String inputLine;
//                StringBuilder response = new StringBuilder();
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//                }
//                in.close();
//                String formattedGames = gameUtils.formatGameList(response.toString());
//                System.out.println(formattedGames);
//            } else {
//                System.out.println("\u001B[31mFailed to retrieve the list of games.\u001B[0m");
//            }
//
//            connection.disconnect();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


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
//                // Parse the response JSON array of games
//                JsonArray gamesArray = null;
//
//                // Inside your method
//                try {
//                    // Your existing code to retrieve the JSON response
//                    // Parse the JSON string into a JsonElement
//                    JsonElement jsonElement = JsonParser.parseString(response.toString());
//
//                    // Check if the parsed element is a JsonArray
//                    if (jsonElement.isJsonArray()) {
//                        // Cast the JsonElement to JsonArray
//                        gamesArray = jsonElement.getAsJsonArray();
//
//                        // Iterate over each game in the array
//                        for (int i = 0; i < gamesArray.size(); i++) {
//                            JsonObject gameObject = (JsonObject) gamesArray.get(i);
//                            String formattedGame = formatGame(gameObject);
//                            System.out.println(formattedGame);
//                        }
//                    } else {
//                        // Handle the case where the JSON response is not an array
//                        System.err.println("JSON response is either null or not an array.");
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
//                // Format failure message as red
//                System.out.println("\u001B[31mFailed to retrieve the list of games.\u001B[0m");
//            }
//
//            connection.disconnect();
//        } catch (IOException | JsonIOException e) {
//            e.printStackTrace();
//        }
//    }









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
//                                String formattedGame = formatGame(gameObject);
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
//
//    private String formatGame(JsonObject gameObject) {
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
//        int gameId = gameObject.get("gameID").getAsInt();
//
//        // Construct and return the formatted game string
//        return String.format("Game ID: %d\nGame Name: %s\nWhite User: %s\nBlack User: %s\n", gameId, gameName, whiteUser, blackUser);
//    }






//            client.start();
//
//            URI uri = new URI("ws://localhost:8080/connect");
//            ClientUpgradeRequest request = new ClientUpgradeRequest();
//            WebSocketListener listener = new WebSocketListener() {
//                @Override
//                public void onWebSocketClose(int i, String s) {
//
//                }
//
//                @Override
//                public void onWebSocketConnect(Session session) {
//
//                }
//
//                @Override
//                public void onWebSocketError(Throwable throwable) {
//
//                }
//
//                @Override
//                public void onWebSocketBinary(byte[] bytes, int i, int i1) {
//
//                }
//
//                @Override
//                public void onWebSocketText(String s) {
//
//                }
//            };
//            Future<Session> future = client.connect(listener, uri, request);
//            session = future.get(2, TimeUnit.SECONDS);
//
//            UserData userData = getUserDataFromInput();
//
//            UserGameCommand command = new UserGameCommand("REGISTER", gson.toJson(userData));
//            session.getRemote().sendString(gson.toJson(command));
//
//            CountDownLatch latch = new CountDownLatch(1);
//            listener.setLatch(latch);
//            latch.await(2, TimeUnit.SECONDS);
//
//            if (listener.getResponse() != null && listener.getResponse().equalsIgnoreCase("success")) {
//                authToken = listener.getAuthToken();
//                loggedIn = true;
//                System.out.println("\u001B[36mSuccessfully registered and logged in!\u001B[0m");
//            } else {
//                System.out.println("\u001B[31mRegistration failed. Please try again later.\u001B[0m");
//            }
//
//            client.stop();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private void logout() {
//        boolean player = false;
//        try {
//            UserGameCommand command = new UserGameCommand(authToken);
//            if(loggedIn) {
//                Collection<GameData> games = gameDAO.getAllGameData();
//                for (GameData game : games) {
//                    if(authDAO.getAuthToken(command.getAuthString()).getUsername() == game.getBlackUsername()
//                    || authDAO.getAuthToken(command.getAuthString()).getUsername() == game.getWhiteUsername()){
//                        player = true;
//                    }
//                }
//                if(player){
//                    //resign logic and break
//                }
//                else{
//                    //leave game or log out
//                }
//                session.getRemote().sendString(gson.toJson(command));
//                loggedIn = false;
//                authToken = null;
//                System.out.println("\u001B[36mLogged out successfully.\u001B[0m");
//            }
//            System.out.println("\u001B[36mAlready logged out.\u001B[0m");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private void login() {
//        try {
//            WebSocketClient client = new WebSocketClient();
//            client.start();
//
//            URI uri = new URI("ws://localhost:8080/connect");
//            ClientUpgradeRequest request = new ClientUpgradeRequest();
//            WebSocketListener listener = new WebSocketListener() {
//                @Override
//                public void onWebSocketClose(int i, String s) {
//
//                }
//
//                @Override
//                public void onWebSocketConnect(Session session) {
//
//                }
//
//                @Override
//                public void onWebSocketError(Throwable throwable) {
//
//                }
//
//                @Override
//                public void onWebSocketBinary(byte[] bytes, int i, int i1) {
//
//                }
//
//                @Override
//                public void onWebSocketText(String s) {
//
//                }
//            };
//            Future<Session> future = client.connect(listener, uri, request);
//            session = future.get(2, TimeUnit.SECONDS);
//
//            UserData userData = getUserDataFromInput();
//
//            UserGameCommand command = new UserGameCommand(, "LOGIN", gson.toJson(userData));
//            session.getRemote().sendString(gson.toJson(command));
//
//            CountDownLatch latch = new CountDownLatch(1);
//            listener.setLatch(latch);
//            latch.await(2, TimeUnit.SECONDS);
//
//            if (listener.getResponse() != null && listener.getResponse().equalsIgnoreCase("success")) {
//                authToken = listener.getAuthToken();
//                loggedIn = true;
//                System.out.println("\u001B[36mSuccessfully logged in!\u001B[0m");
//            } else {
//                System.out.println("\u001B[31mLogin failed. Please check your credentials.\u001B[0m");
//            }
//
//            client.stop();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


//    private void joinPlayer() {
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
//    public void start() throws DataAccessException, IOException {
//
//        System.out.println("Welcome to Chess Pregame!");
//
//        while (true) {
//            if (!loggedIn) {
//                displayPreLoginOptions();
//                String input = scanner.nextLine().trim().toLowerCase();
//                switch (input) {
//                    case "help":
//                        displayPreLoginHelp();
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
//                if(inGame){
//                    displayInGameOptions();
//                    String input = scanner.nextLine().trim().toLowerCase();
//                    switch (input){
//                        case "help":
//                            displayInGameHelp();
//                            break;
//                        case "redraw chess board":
//                            redrawBoard();
//                            break;
//                        case "leave":
//                            leaveGameHandler(session, "");
//                            break;
//                        case "make move":
//                            makeMoveHandler(session, "");
//                            break;
//                        case "resign":
//                            resignHandler(session, "");
//                            break;
//                        case "highlight legal moves":
//                            legalMovesHandler();
//                            break;
//                        default:
//                            System.out.println("Invalid command. Please try again.");
//                    }
//                }
//                else {
//                    displayPostloginOptions();
//                    String input = scanner.nextLine().trim().toLowerCase();
//                    switch (input) {
//                        case "help":
//                            displayPostloginHelp();
//                            break;
//                        case "logout":
//                            logout();
//                            break;
//                        case "create game":
//                            createGame();
//                            break;
//                        case "list games":
//                            listGames();
//                            break;
//                        case "join player":
//                            joinPlayer(session, "");
//                            break;
//                        case "join observer":
//                            joinObserver(session, "");
//                            break;
//                        default:
//                            System.out.println("Invalid command. Please try again.");
//                    }
//                }
//            }
//        }
//    }

//    private void displayPreloginOptions() {
//        System.out.println("Prelogin commands: \u001B[33mHelp\u001B[0m, \u001B[33mQuit\u001B[0m, \u001B[33mLogin\u001B[0m, \u001B[33mRegister\u001B[0m");
//    }
//
//    private void displayPreloginHelp() {
//        // Format command words as yellow
//        System.out.println("\u001B[33mHelp - Displays available commands\n" +
//                "Quit - Exits the program\n" +
//                "Login - Login with existing credentials\n" +
//                "Register - Register a new account\u001B[0m");
//    }
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
//private void listGames() {
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