package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    TeamColor color1;//WHOSE TURN IT IS
    ChessBoard board1;
    boolean isGameOver;


    private List<ChessMove> moveHistory = new ArrayList<>();
    public ChessGame() {
        color1 = TeamColor.WHITE;
        board1 = new ChessBoard();
        board1.resetBoard();
        isGameOver = false;
    }


    public ChessGame(TeamColor color, ChessBoard board) {
        color1 = color;
        board1 = board;
    }

    /**
     * @return Which team's turn it is
     */


    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        color1 = team;
        //throw new RuntimeException("Not implemented");
    }
    public TeamColor getTeamTurn(){ return color1; }
    public boolean getIsGameOver(){
        return isGameOver;
    }
    public void setIsGameOver(boolean isGameOver){
        this.isGameOver = isGameOver;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */



    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board1.getPiece(startPosition);

        if (piece == null) {
            return Collections.emptyList();
        }

        Collection<ChessMove> moves = new ArrayList<>();

        for (ChessMove move : piece.pieceMoves(board1, startPosition)){
            ChessBoard tempBoard = new ChessBoard();

            board1.setCopyOfBoard(tempBoard);
            if (isValidMove(move)) {
                ChessGame tempGame = new ChessGame(piece.getTeamColor(), tempBoard);



                tempBoard.addPiece(startPosition, null);
                tempBoard.addPiece(move.getEndPosition(), piece);

                if (!tempGame.isInCheck(piece.getTeamColor())) {
                    moves.add(move);
                }
            }
        }
        return moves;
    }


    public boolean isValidMove(ChessMove move) {

        if (!board1.isValidPosition(move.getEndPosition())) {
            return false;
        }

        ChessPiece piece = board1.getPiece(move.getStartPosition());

        Collection<ChessMove> validMoves = piece.pieceMoves(board1, move.getStartPosition());

        return validMoves != null && validMoves.contains(move);
    }






    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board1.getPiece(move.getStartPosition());

        if (!board1.isValidPosition(move.getStartPosition())) {
            throw new InvalidMoveException("Invalid move: Starting position is not valid.");
        }

        if (piece == null || piece.getTeamColor() != color1) {
            throw new InvalidMoveException("Invalid move: No piece at the specified starting position.");
        }

        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());

        if (validMoves != null && validMoves.contains(move)) {
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN && move.getPromotionPiece() != null) {
                board1.addPiece(move.getStartPosition(), null);

                board1.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
            } else {
                piece.setFirstMove(false);
                board1.addPiece(move.getEndPosition(), piece);
            }

            board1.addPiece(move.getStartPosition(), null);

            moveHistory.add(move);
            //switch turns
            color1 = (color1 == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;

        } else {
            throw new InvalidMoveException("Invalid move: The specified move is not valid for the given piece.");
        }
    }







    public void undoLastMove() {
        if (!moveHistory.isEmpty()) {
            ChessMove lastMove = moveHistory.remove(moveHistory.size() - 1);

            board1.undoMove(lastMove);

            color1 = (color1 == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        }
    }







    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = board1.findKing(teamColor);
        Collection<ChessMove> opponentMoves = board1.getAllMovesForOpponent(teamColor);

        for (ChessMove move : opponentMoves) {
            if (move.getEndPosition().equals(kingPosition)) {
                return true;
            }
        }

        return false;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */

    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }

        Collection<ChessMove> allMoves = getAllMovesForTeam(teamColor);

        for (ChessMove move : allMoves) {
            ChessGame tempGame = new ChessGame(teamColor, board1);
            try {
                tempGame.makeMove(move);
            } catch (InvalidMoveException e) {
                continue;
            }

            if (!tempGame.isInCheck(teamColor)) {
                return false;
            }
        }
        isGameOver = true;
        return true;
    }



    public Collection<ChessMove> getAllMovesForTeam(ChessGame.TeamColor teamColor) {
        Collection<ChessMove> allMoves = new ArrayList<>();

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board1.getPiece(pos);

                if (piece != null && piece.getTeamColor() == teamColor) {
                    ChessPosition startPosition = new ChessPosition(row, col);
                    Collection<ChessMove> moves = validMoves(startPosition);
                    if (moves != null) {
                        allMoves.addAll(moves);
                    }
                }
            }
        }

        return allMoves;
    }













    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */


    public boolean isInStalemate(TeamColor teamColor) {
        ChessBoard boardCopy = new ChessBoard();
        boardCopy.setBoard(board1.getBoard());

        Collection<ChessMove> allMoves = getAllMovesForTeam(teamColor);

        for (ChessMove move : allMoves) {
            ChessPiece piece = boardCopy.getPiece(move.getStartPosition());
            try {
                makeMove(move);
            } catch (InvalidMoveException e) {
            }

            if (!isInCheck(teamColor)) {
                undoLastMove();
                return false;
            }

            undoLastMove();
        }
        isGameOver = true;
        return true;
    }










    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        board1 = board;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board1;
        //throw new RuntimeException("Not implemented");
    }
}
