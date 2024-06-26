package communicators;

import WebSocket.NotificationHandler;
import ui.ChessClient;
import webSocketMessages.serverMessages.Notification;
import java.util.Scanner;
import ui.EscapeSequences;

import static java.awt.Color.*;
import static org.glassfish.grizzly.Interceptor.RESET;

public class Repl implements NotificationHandler {
    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl, this);
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to chess. Sign in to start.");
        System.out.print(client.help());
        //client.help();

        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print("\u001B[12m" + result + "\u001B[0m");
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public void notify(Notification notification) {
        System.out.println(RED + notification.getMessage());
        printPrompt();
    }

    private void printPrompt() {
        System.out.print("\n" + ">>> " + "\u001B[46m");
    }

}