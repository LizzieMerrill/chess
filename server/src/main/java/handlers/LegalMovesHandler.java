package handlers;

import chess.*;
import dataAccess.access.DataAccessException;
import dataAccess.dao.GameDAO;
import dataAccess.dao.SQLGameDAO;

import java.util.Scanner;
import java.util.Collection;

public class LegalMovesHandler {
    int gameId;
    ChessBoard board;
    GameDAO gameDAO = new SQLGameDAO();
    Scanner scanner = new Scanner(System.in);
    public LegalMovesHandler(String... params) throws DataAccessException {
        gameId =  Integer.parseInt(params[0]);
        board = gameDAO.getGame(gameId).getBoard();
        listLegalMoves(board);
    }
    public LegalMovesHandler(ChessGame game){
        board = game.getBoard();
        listLegalMoves(board);
    }
    public Collection<ChessMove> listLegalMoves(ChessBoard board){
        System.out.print("Enter the piece's position that you want to list the legal moves for.\n" +
                "Row Number (1-8): ");
        int row = scanner.nextInt();
        System.out.print("Column Number (1-8): ");
        int col = scanner.nextInt();
        ChessPosition position = new ChessPosition(row, col);
        ChessPiece piece = board.getPiece(position);
        if(piece == null){
            Error emptyError = new Error("No piece at this position, try again!");
        }
        else{
            return piece.pieceMoves(board, position);
        }
        return null;
    }
}
