import communicators.Repl;
import ui.ChessClient;

public class Main {
//public static void main(String[] args) throws DataAccessException, IOException {
//    ChessClient chessClient = new ChessClient();
//    chessClient.start();
//}
        public static void main(String[] args) {
            var serverUrl = "http://localhost:8080";
            if (args.length == 1) {
                serverUrl = args[0];
            }
            new Repl(serverUrl).run();
        }
}