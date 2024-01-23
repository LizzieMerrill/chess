package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;
import java.lang.Math;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    ChessGame.TeamColor colorOfPiece;
    ChessPiece.PieceType typeOfPiece;
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
        Collection<ChessMove> movesToAddLater = new ArrayList<>();

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

        // Handle pawn promotion for each move
        for (ChessMove move : validMoves) {
            if (movingPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
                handlePawnPromotion(movesToAddLater, board, myPosition, move.getEndPosition());
            }
        }

        // Add movesToAddLater to validMoves after iteration
        validMoves.addAll(movesToAddLater);

        return validMoves;
    }



    private Collection<ChessMove> getKingMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        int[][] kingMoves = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

        for (int[] move : kingMoves) {
            int rowChange = move[0];
            int colChange = move[1];

            ChessPosition to = new ChessPosition(myPosition.getRow() + rowChange, myPosition.getColumn() + colChange);

            if (board.isValidPosition(to)) {
                ChessPiece pieceAtNewPosition = board.getPiece(to);

                if (pieceAtNewPosition == null || pieceAtNewPosition.getTeamColor() != colorOfPiece) {
                    // If the position is empty or contains an opponent's piece, add the move
                    addValidMove(validMoves, board, myPosition, rowChange, colChange, null);
                }
            }
        }

        return validMoves;
    }


    private Collection<ChessMove> getQueenMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        
        validMoves.addAll(getRookMoves(board, myPosition));
        validMoves.addAll(getBishopMoves(board, myPosition));

        return validMoves;
    }


    private Collection<ChessMove> getBishopMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        for (int[] direction : directions) {
            int rowChange = direction[0];
            int colChange = direction[1];

            ChessPosition to = new ChessPosition(myPosition.getRow() + rowChange, myPosition.getColumn() + colChange);

            while (board.isValidPosition(to)) {
                ChessPiece pieceAtNewPosition = board.getPiece(to);

                if (pieceAtNewPosition == null) {
                    // If the position is empty, add the move
                    addValidMove(validMoves, board, myPosition, to.getRow()- myPosition.getRow(), to.getColumn() - myPosition.getColumn(), null);
                } else {
                    // If the position contains a piece, check if it's an opponent's piece and then add the move
                    if (pieceAtNewPosition.getTeamColor() != colorOfPiece) {
                        addValidMove(validMoves, board, myPosition, to.getRow()- myPosition.getRow(), to.getColumn() - myPosition.getColumn(), null);
                    }
                    break;  // Stop if there is a piece, regardless of color
                }
//
//                // Move to the next diagonal position
                to = new ChessPosition(to.getRow() + rowChange, to.getColumn() + colChange);
                System.out.println(board.isValidPosition(to));
            }
        }

        return validMoves;
    }



    private Collection<ChessMove> getRookMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] direction : directions) {
            int rowChange = direction[0];
            int colChange = direction[1];

            ChessPosition to = new ChessPosition(myPosition.getRow() + rowChange, myPosition.getColumn() + colChange);

            while (board.isValidPosition(to)) {
                ChessPiece pieceAtNewPosition = board.getPiece(to);

                if (pieceAtNewPosition == null) {
                    // If the position is empty, add the move
                    addValidMove(validMoves, board, myPosition, to.getRow() - myPosition.getRow(), to.getColumn() - myPosition.getColumn(), null);
                } else {
                    // If the position contains a piece, check if it's an opponent's piece and then add the move
                    if (pieceAtNewPosition.getTeamColor() != colorOfPiece) {
                        addValidMove(validMoves, board, myPosition, to.getRow() - myPosition.getRow(), to.getColumn() - myPosition.getColumn(), null);
                    }
                    break;  // Stop if there is a piece, regardless of color
                }

                // Move to the next position in the same direction
                to = new ChessPosition(to.getRow() + rowChange, to.getColumn() + colChange);
            }
        }

        return validMoves;
    }





    private Collection<ChessMove> getKnightMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        int[][] knightMoves = {{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {2, -1}, {2, 1}};

        for (int[] move : knightMoves) {
            addValidMove(validMoves, board, myPosition, move[0], move[1], null);
        }

        return validMoves;
    }



    private void addStraightMoves(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        for (int[] direction : directions) {
            int rowChange = direction[0];
            int colChange = direction[1];

            ChessPosition to = new ChessPosition(myPosition.getRow() + rowChange, myPosition.getColumn() + colChange);

            while (board.isValidPosition(to)) {
                ChessPiece pieceAtNewPosition = board.getPiece(to);

                if (pieceAtNewPosition == null) {
                    // If the position is empty, add the move
                    addValidMove(moves, board, myPosition, rowChange, colChange, null);
                } else {
                    // If the position contains a piece, check if it's an opponent's piece and then add the move
                    if (pieceAtNewPosition.getTeamColor() != colorOfPiece) {
                        addValidMove(moves, board, myPosition, rowChange, colChange, null);
                    }
                    break;  // Stop if there is a piece, regardless of color
                }

                // Move to the next position
                to = new ChessPosition(to.getRow() + rowChange, to.getColumn() + colChange);
            }
        }
    }

















    private Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        int direction = (colorOfPiece == ChessGame.TeamColor.WHITE) ? 1 : -1;

        // Move forward
        ChessPosition moveForward = new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn());
        if (board.isValidPosition(moveForward) && board.getPiece(moveForward) == null) {
            addValidMove(validMoves, board, myPosition, direction, 0, null);

            // Move two squares forward from starting position
            if ((colorOfPiece == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2) ||
                    (colorOfPiece == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7)) {
                ChessPosition moveTwoForward = new ChessPosition(myPosition.getRow() + 2 * direction, myPosition.getColumn());
                if (board.getPiece(moveTwoForward) == null) {
                    addValidMove(validMoves, board, myPosition, 2 * direction, 0, null);
                }
            }
        }

        // Capture diagonally
        addPawnCaptureMoves(validMoves, board, myPosition, direction, -1);
        addPawnCaptureMoves(validMoves, board, myPosition, direction, 1);

        return validMoves;
    }


/*
*
*
*
* private void handlePawnPromotion(Collection<ChessMove> validMoves, ChessBoard board, ChessPosition from, ChessPosition to) {
        ChessPiece pawn = board.getPiece(from);

        // Check if the pawn has reached the promotion rank
        if ((pawn.getTeamColor() == ChessGame.TeamColor.WHITE && to.getColumn() == 7) ||
                (pawn.getTeamColor() == ChessGame.TeamColor.BLACK && to.getColumn() == 0)) {

            // Generate promotion moves for each promotion type
            for (ChessPiece.PieceType promotionType : ChessPiece.PieceType.values()) {
                if (promotionType != ChessPiece.PieceType.PAWN) {
                    ChessMove promotionMove = new ChessMove(from, to, promotionType);
                    validMoves.add(promotionMove);
                }
            }
        } else {
            // If not a promotion, add the move as is
            validMoves.add(new ChessMove(from, to, null));
        }
    }
*
*
* */



    private boolean isPawnPromotionRow(ChessPosition position) {
        int promotionRowWhite = 8;
        int promotionRowBlack = 1;

        return (colorOfPiece == ChessGame.TeamColor.WHITE && position.getRow() == promotionRowWhite) ||
                (colorOfPiece == ChessGame.TeamColor.BLACK && position.getRow() == promotionRowBlack);
    }



    private void handlePawnPromotion(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition, ChessPosition moveForward) {
        if (isPawnPromotionRow(moveForward)) {
            addPawnPromotionMoves(moves, board, myPosition, moveForward);
        }
    }

    private void addPawnPromotionMoves(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition, ChessPosition moveForward) {
        // Add pawn promotion moves without checking for an empty target position
        moves.add(new ChessMove(myPosition, moveForward, ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(myPosition, moveForward, ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(myPosition, moveForward, ChessPiece.PieceType.BISHOP));
        moves.add(new ChessMove(myPosition, moveForward, ChessPiece.PieceType.KNIGHT));
    }


    private void addPawnPromotionCaptureMoves(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition, ChessPosition moveForward, int colChange) {
        ChessPosition capturePosition = new ChessPosition(moveForward.getRow(), moveForward.getColumn() + colChange);
        if (board.isValidPosition(capturePosition)) {
            ChessPiece pieceAtCapture = board.getPiece(capturePosition);
            if (pieceAtCapture != null && pieceAtCapture.getTeamColor() != colorOfPiece) {
                // Add pawn promotion moves with capture only if the position is valid
                moves.add(new ChessMove(myPosition, capturePosition, ChessPiece.PieceType.QUEEN));
                moves.add(new ChessMove(myPosition, capturePosition, ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(myPosition, capturePosition, ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(myPosition, capturePosition, ChessPiece.PieceType.KNIGHT));
            }
        }
    }










    private void addPawnCaptureMoves(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition, int rowChange, int colChange) {
        ChessPosition capturePosition = new ChessPosition(myPosition.getRow() + rowChange, myPosition.getColumn() + colChange);
        if (board.isValidPosition(capturePosition)) {
            ChessPiece pieceAtCapture = board.getPiece(capturePosition);
            if (pieceAtCapture != null && pieceAtCapture.getTeamColor() != colorOfPiece) {
                addValidMove(moves, board, myPosition, rowChange, colChange, null);
            }
        }
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










//    private Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition myPosition) {
//        Collection<ChessMove> validMoves = new ArrayList<>();
//
//        int direction = (colorOfPiece == ChessGame.TeamColor.WHITE) ? 1 : -1;
//
//        addValidMove(validMoves, board, myPosition, direction, 0, null);
//
//        if ((colorOfPiece == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2) ||
//                (colorOfPiece == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7)) {
//            addValidMove(validMoves, board, myPosition, 2 * direction, 0, null);
//        }
//
//        addValidMove(validMoves, board, myPosition, direction, 1, ChessPiece.PieceType.QUEEN);
//        addValidMove(validMoves, board, myPosition, direction, -1, ChessPiece.PieceType.QUEEN);
//
//        ChessPosition captureLeft = new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn() - 1);
//        ChessPosition captureRight = new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn() + 1);
//
//        if (isCaptureValid(board, captureLeft)) {
//            validMoves.add(new ChessMove(myPosition, captureLeft, null));
//        }
//        if (isCaptureValid(board, captureRight)) {
//            validMoves.add(new ChessMove(myPosition, captureRight, null));
//        }
//
//        return validMoves;
//    }
//
//
//    private boolean isCaptureValid(ChessBoard board, ChessPosition position) {
//        // Check if the position is valid on the board
//        if (!board.isValidPosition(position)) {
//            return false;
//        }
//
//        // Check if there is an opponent's piece at the specified position
//        ChessPiece opponentPiece = board.getPiece(position);
//        return opponentPiece != null && opponentPiece.getTeamColor() != colorOfPiece;
//    }




















    // Helper methods for common movements

    private void addValidMove(Collection<ChessMove> moves, ChessBoard board, ChessPosition from, int rowChange, int colChange, ChessPiece.PieceType promotionPiece) {
        ChessPosition to = new ChessPosition(from.getRow() + rowChange, from.getColumn() + colChange);

        if (board.isValidPosition(to)) {
            ChessPiece pieceAtTo = board.getPiece(to);
            if (pieceAtTo == null || pieceAtTo.getTeamColor() != colorOfPiece) {
                // If the position is empty or contains an opponent's piece, add the move
                moves.add(new ChessMove(from, to, null));
            }
        }
    }


    // Helper method to check if the pawn is in the promotion row
//    private boolean isPawnPromotionRow(ChessPosition position) {
//        int promotionRowWhite = 8;
//        int promotionRowBlack = 1;
//
//        return (colorOfPiece == ChessGame.TeamColor.WHITE && position.getRow() == promotionRowWhite) ||
//                (colorOfPiece == ChessGame.TeamColor.BLACK && position.getRow() == promotionRowBlack);
//    }



    private void addHorizontalVerticalMoves(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
        addStraightMoves(moves, board, myPosition);
    }

    private void addDiagonalMoves(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        for (int[] direction : directions) {
            int rowChange = direction[0];
            int colChange = direction[1];
            ChessPosition to = new ChessPosition(myPosition.getRow() + rowChange, myPosition.getColumn() + colChange);

            // Continue adding valid moves along the diagonal until an invalid position is encountered
            while (board.isValidPosition(to)) {
                addValidMove(moves, board, myPosition, rowChange, colChange, null);
                to = new ChessPosition(to.getRow() + rowChange, to.getColumn() + colChange);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return colorOfPiece == that.colorOfPiece && typeOfPiece == that.typeOfPiece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(colorOfPiece, typeOfPiece);
    }
}
