import chess.*;
import dataAccess.access.DataAccessException;
import ui.ChessClient;

import java.io.IOException;

public class Main {
//    public static void main(String[] args) {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Client: " + piece);
//        //create phase 5
//    }
public static void main(String[] args) throws DataAccessException, IOException {
    ChessClient chessClient = new ChessClient();
    chessClient.start();
}
}