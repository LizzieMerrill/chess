import chess.*;
import ui.ChessClient;

public class Main {
//    public static void main(String[] args) {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Client: " + piece);
//        //create phase 5
//    }
public static void main(String[] args) {
    ChessClient chessClient = new ChessClient();
    chessClient.start();
}
}