import chess.*;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
    }

//    public static class Server {
//
//        public int run(int desiredPort) {
//            Spark.port(desiredPort);
//
//            Spark.staticFiles.location("web");
//
//            // Register your endpoints and handle exceptions here.
//
//            Spark.awaitInitialization();
//            return Spark.port();
//        }
//
//        public void stop() {
//            Spark.stop();
//            Spark.awaitStop();
//        }
//    }
}