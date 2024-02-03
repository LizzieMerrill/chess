package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    TeamColor color1 = null;
    ChessBoard board1 = null;
    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return color1;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        color1 = team;
        //throw new RuntimeException("Not implemented");
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
//        if(board1.getPiece(startPosition) == null){
//            return null;
//        }
//        else{
//
//        }



        ChessPiece piece = board1.getPiece(startPosition);

        if (piece == null || piece.getTeamColor() != color1) {
            return null;
        } else {
            return piece.pieceMoves(board1, startPosition);
        }
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
//    public void makeMove(ChessMove move) throws InvalidMoveException {
//        ChessPiece piece = board1.getPiece(move.getStartPosition());
//
//        if (piece == null || piece.getTeamColor() != color1) {
//            throw new InvalidMoveException("Invalid move: No piece at the specified starting position.");
//        }
//
//        Collection<ChessMove> validMoves = piece.pieceMoves(board1, move.getStartPosition());
//
//        if (validMoves != null && validMoves.contains(move)) {
//            board1.addPiece(move.getEndPosition(), piece);
//
//            // Check for pawn promotion
//            if (piece.getPieceType() == ChessPiece.PieceType.PAWN && move.getPromotionPiece() != null) {
//                board1.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
//            }
//
//            // Switch turns
//            color1 = (color1 == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
//        } else {
//            throw new InvalidMoveException("Invalid move: The specified move is not valid for the given piece.");
//        }
//
//
//
//
//        //throw new RuntimeException("Not implemented");
//    }

    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board1.getPiece(move.getStartPosition());

        if (piece == null || piece.getTeamColor() != color1) {
            throw new InvalidMoveException("Invalid move: No piece at the specified starting position.");
        }

        Collection<ChessMove> validMoves = piece.pieceMoves(board1, move.getStartPosition());

        if (validMoves != null && validMoves.contains(move)) {
            // Check for pawn promotion
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN && move.getPromotionPiece() != null) {
                // Remove the original pawn from the starting position
                board1.addPiece(move.getStartPosition(), null);

                // Add the promoted piece to the ending position
                board1.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
            } else {
                // Perform the move as usual
                board1.addPiece(move.getEndPosition(), piece);
            }

            // Remove the original piece from the starting position
            board1.addPiece(move.getStartPosition(), null);

            // Switch turns
            color1 = (color1 == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        } else {
            throw new InvalidMoveException("Invalid move: The specified move is not valid for the given piece.");
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

        Collection<ChessMove> allMoves = board1.getAllMovesForTeam(teamColor);

        for (ChessMove move : allMoves) {
            ChessGame tempGame = new ChessGame();
            tempGame.setBoard(board1);
            try {
                tempGame.makeMove(move);
            } catch (InvalidMoveException e) {
                continue;
            }

            if (!tempGame.isInCheck(teamColor)) {
                return false;
            }
        }

        return true;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

        if (isInCheck(teamColor)) {
            return false;
        }

        Collection<ChessMove> allMoves = board1.getAllMovesForTeam(teamColor);

        for (ChessMove move : allMoves) {
            ChessGame tempGame = new ChessGame();
            tempGame.setBoard(board1);//tempGame.setBoard(new ChessBoard(board1));//??????????????
            try {
                tempGame.makeMove(move);
            } catch (InvalidMoveException e) {
                continue;
            }

            if (!tempGame.isInCheck(teamColor)) {
                return false;
            }
        }

        return true;
        //throw new RuntimeException("Not implemented");
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
