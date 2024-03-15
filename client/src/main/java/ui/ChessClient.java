package ui;

import java.util.Scanner;

public class ChessClient {
    private boolean loggedIn;

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
        // Implement login functionality
        loggedIn = true;
        System.out.println("Successfully logged in!");
    }

    private void register() {
        // Implement registration functionality
        loggedIn = true;
        System.out.println("Successfully registered and logged in!");
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
        // Implement logout functionality
        loggedIn = false;
        System.out.println("Logged out successfully.");
    }

    private void createGame() {
        // Implement creating a new game
        System.out.println("Game created successfully.");
    }

    private void listGames() {
        // Implement listing existing games
        System.out.println("List of games:");
    }

    private void joinGame() {
        // Implement joining a game
        System.out.println("Joined the game successfully.");
    }

    private void joinObserver() {
        // Implement joining as an observer
        System.out.println("Joined as an observer.");
    }

//    public static void main(String[] args) {
//        ChessClient chessClient = new ChessClient();
//        chessClient.start();
//    }
}