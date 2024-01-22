package chess;

import java.util.Collection;
import java.util.ArrayList;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    ChessGame.TeamColor colorOfPiece = null;
    ChessPiece.PieceType typeOfPiece = null;
//    ChessBoard board1 = null;
//    ChessPosition myPosition1 = null;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        colorOfPiece = pieceColor;
        typeOfPiece = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return colorOfPiece;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return typeOfPiece;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();

    ChessPiece movingPiece = board.getPiece(myPosition);


        switch (movingPiece.typeOfPiece) {
            case KING:
                validMoves.addAll(getKingMoves(board, myPosition));
                break;
            case QUEEN:
                validMoves.addAll(getQueenMoves(board, myPosition));
                break;
            case BISHOP:
                validMoves.addAll(getBishopMoves(board, myPosition));
                break;
            case KNIGHT:
                validMoves.addAll(getKnightMoves(board, myPosition));
                break;
            case ROOK:
                validMoves.addAll(getRookMoves(board, myPosition));
                break;
            case PAWN:
                validMoves.addAll(getPawnMoves(board, myPosition));
                break;
        }

        return validMoves;
        //throw new RuntimeException("Not implemented");
    }

    private Collection<ChessMove> getKingMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        // Implement logic for king moves (simple example: one square in any direction)
        addValidMove(validMoves, board, myPosition, 1, 0, null);
        addValidMove(validMoves, board, myPosition, -1, 0, null);
        addValidMove(validMoves, board, myPosition, 0, 1, null);
        addValidMove(validMoves, board, myPosition, 0, -1, null);
        addValidMove(validMoves, board, myPosition, 1, 1, null);
        addValidMove(validMoves, board, myPosition, -1, -1, null);
        addValidMove(validMoves, board, myPosition, 1, -1, null);
        addValidMove(validMoves, board, myPosition, -1, 1, null);

        return validMoves;
    }

    private Collection<ChessMove> getQueenMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        // Implement logic for queen moves (simple example: combines rook and bishop movements)
        validMoves.addAll(getRookMoves(board, myPosition));
        validMoves.addAll(getBishopMoves(board, myPosition));

        return validMoves;
    }

    private Collection<ChessMove> getBishopMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        // Implement logic for bishop moves (simple example: diagonal movements)
        addDiagonalMoves(validMoves, board, myPosition);

        return validMoves;
    }

    private Collection<ChessMove> getKnightMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        // Implement logic for knight moves (simple example: L-shaped moves)
        int[][] knightMoves = {{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {2, -1}, {2, 1}};

        for (int[] move : knightMoves) {
            addValidMove(validMoves, board, myPosition, move[0], move[1], null);
        }

        return validMoves;
    }

    private Collection<ChessMove> getRookMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        // Implement logic for rook moves (simple example: horizontal and vertical movements)
        addHorizontalVerticalMoves(validMoves, board, myPosition);

        return validMoves;
    }

    private Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        // Implement logic for pawn moves (simple example: one square forward, two squares on first move, and captures diagonally)
        int direction = (colorOfPiece == ChessGame.TeamColor.WHITE) ? 1 : -1;

        // One square forward
        addValidMove(validMoves, board, myPosition, direction, 0, null);

        // Two squares forward on the first move
        if ((colorOfPiece == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2) ||
                (colorOfPiece == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7)) {
            addValidMove(validMoves, board, myPosition, 2 * direction, 0, null);
        }

        // Capture moves diagonally with promotion logic
        addValidMove(validMoves, board, myPosition, direction, 1, ChessPiece.PieceType.QUEEN);
        addValidMove(validMoves, board, myPosition, direction, -1, ChessPiece.PieceType.QUEEN);

        // Normal captures without promotion
        ChessPosition captureLeft = new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn() - 1);
        ChessPosition captureRight = new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn() + 1);
        if (isCaptureValid(board, captureLeft)) {
            validMoves.add(new ChessMove(myPosition, captureLeft, null));
        }
        if (isCaptureValid(board, captureRight)) {
            validMoves.add(new ChessMove(myPosition, captureRight, null));
        }

        return validMoves;
    }

    private boolean isCaptureValid(ChessBoard board, ChessPosition position) {
        // Check if the position is valid on the board
        if (!board.isValidPosition(position)) {
            return false;
        }

        // Check if there is an opponent's piece at the specified position
        ChessPiece opponentPiece = board.getPiece(position);
        return opponentPiece != null && opponentPiece.getTeamColor() != colorOfPiece;
    }

    // Helper methods for common movements

    private void addValidMove(Collection<ChessMove> moves, ChessBoard board, ChessPosition from, int rowChange, int colChange, ChessPiece.PieceType promotionPiece) {
        ChessPosition to = new ChessPosition(from.getRow() + rowChange, from.getColumn() + colChange);

        if (board.isValidPosition(to)) {
            // Check for pawn promotion
            if (promotionPiece != null && isPawnPromotionRow(to)) {
                moves.add(new ChessMove(from, to, promotionPiece));
            } else {
                moves.add(new ChessMove(from, to, null));
            }
        }
    }

    // Helper method to check if the pawn is in the promotion row
    private boolean isPawnPromotionRow(ChessPosition position) {
        int promotionRowWhite = 8;
        int promotionRowBlack = 1;

        return (colorOfPiece == ChessGame.TeamColor.WHITE && position.getRow() == promotionRowWhite) ||
                (colorOfPiece == ChessGame.TeamColor.BLACK && position.getRow() == promotionRowBlack);
    }

    private void addHorizontalVerticalMoves(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
        // Horizontal moves
        for (int col = 1; col <= 8; col++) {
            if (col != myPosition.getColumn()) {
                addValidMove(moves, board, myPosition, 0, col - myPosition.getColumn(), null);
            }
        }

        // Vertical moves
        for (int row = 1; row <= 8; row++) {
            if (row != myPosition.getRow()) {
                addValidMove(moves, board, myPosition, row - myPosition.getRow(), 0, null);
            }
        }
    }

    private void addDiagonalMoves(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                if (row != myPosition.getRow() && col != myPosition.getColumn() &&
                        Math.abs(row - myPosition.getRow()) == Math.abs(col - myPosition.getColumn())) {
                    addValidMove(moves, board, myPosition, row - myPosition.getRow(), col - myPosition.getColumn(), null);
                }
            }
        }
    }
}
