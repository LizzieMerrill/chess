package inGameHandlers;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import dataAccess.access.DataAccessException;
import dataAccess.dao.GameDAO;
import dataAccess.dao.SQLGameDAO;
import static ui.EscapeSequences.*;

public class DrawBoardHandler {

    int gameId;
    ChessBoard board;
    GameDAO gameDAO = new SQLGameDAO();
    public DrawBoardHandler(String... params) throws DataAccessException {
        gameId =  Integer.parseInt(params[0]);
        board = gameDAO.getGame(gameId).getBoard();
        draw(board);
    }

    public void draw(ChessBoard board){
        //extract each position and draw whats there
        // Define the size of the chessboard
        int size = 8;
        String color;

        // Draw the chessboard twice, once with white pieces at the bottom and once with black pieces at the bottom
        for (int orientation = 0; orientation < 2; orientation++) {
            if(orientation == 0){
                System.out.println("Black POV:\n");
            }
            else{
                System.out.println("White POV:\n");
            }
            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    // Calculate the color of the square
                    boolean isWhiteSquare = (row + col) % 2 == 0;
                    // Color variables for terminal printing
                    String bgWhite = "\u001B[47m";
                    String bgBlack = "\u001B[40m";
                    String resetColor = "\u001B[0m";

                    // Color the background based on the square color
                    if ((orientation == 0 && isWhiteSquare) || (orientation == 1 && !isWhiteSquare)) {
                        System.out.print(bgWhite);
                    } else {
                        System.out.print(bgBlack);
                    }

                    // Print the piece or empty square
                    ChessPosition pos = new ChessPosition(((orientation == 0) ? row : size - row - 1),col);
                    ChessPiece piece = board.getPiece(pos);
                    String pieceSymbol = pieceTypeChecker(piece);

                    System.out.print(pieceSymbol);
                    // Reset the color after printing each square
                    System.out.print(resetColor);
                }
                System.out.println();
            }
            System.out.println();
        }
    }
    public String pieceTypeChecker(ChessPiece piece){
        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            if(piece.getPieceType() == ChessPiece.PieceType.KING){
                return WHITE_KING;
            }
            if(piece.getPieceType() == ChessPiece.PieceType.QUEEN){
                return WHITE_QUEEN;
            }
            if(piece.getPieceType() == ChessPiece.PieceType.BISHOP){
                return WHITE_BISHOP;
            }
            if(piece.getPieceType() == ChessPiece.PieceType.ROOK){
                return WHITE_ROOK;
            }
            if(piece.getPieceType() == ChessPiece.PieceType.KNIGHT){
                return WHITE_KNIGHT;
            }
            if(piece.getPieceType() == ChessPiece.PieceType.PAWN){
                return WHITE_PAWN;
            }
        }
        else{
            if(piece.getPieceType() == ChessPiece.PieceType.KING){
                return BLACK_KING;
            }
            if(piece.getPieceType() == ChessPiece.PieceType.QUEEN){
                return BLACK_QUEEN;
            }
            if(piece.getPieceType() == ChessPiece.PieceType.BISHOP){
                return BLACK_BISHOP;
            }
            if(piece.getPieceType() == ChessPiece.PieceType.ROOK){
                return BLACK_ROOK;
            }
            if(piece.getPieceType() == ChessPiece.PieceType.KNIGHT){
                return BLACK_KNIGHT;
            }
            if(piece.getPieceType() == ChessPiece.PieceType.PAWN){
                return BLACK_PAWN;
            }
        }
        return EMPTY;//empty space
    }
}
