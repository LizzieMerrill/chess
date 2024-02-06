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
    private boolean firstMove = true;
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
    // Inside ChessPiece class

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


     //   System.out.println("Valid moves for piece at " + myPosition + ": " + validMoves);
        return validMoves;
    }







    public void setFirstMove(boolean firstMove) {
      //  System.out.println("setFirstMove called for piece: " + this);
       // System.out.println("Before setFirstMove - firstMove: " + this.firstMove);

        this.firstMove = firstMove;

      //  System.out.println("After setFirstMove - firstMove: " + this.firstMove);
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
                    addValidMove(validMoves, board, myPosition, to.getRow()- myPosition.getRow(), to.getColumn() - myPosition.getColumn(), null);
                } else {//opponent check
                    if (pieceAtNewPosition.getTeamColor() != colorOfPiece) {
                        addValidMove(validMoves, board, myPosition, to.getRow()- myPosition.getRow(), to.getColumn() - myPosition.getColumn(), null);
                    }
                    break;  // stop at any piece
                }

//                //next diagonal position
                to = new ChessPosition(to.getRow() + rowChange, to.getColumn() + colChange);
              //lollllllllllllllllll  System.out.println(board.isValidPosition(to));
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
                    break;  //stop at piece
                }

                // next position
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



    private Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        int direction = (colorOfPiece == ChessGame.TeamColor.WHITE) ? 1 : -1;

        ChessPosition moveForward = new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn());
        if (board.isValidPosition(moveForward) && board.getPiece(moveForward) == null) {
            addValidMove(validMoves, board, myPosition, direction, 0, null);

            // double handling
            if ((colorOfPiece == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2) ||
                    (colorOfPiece == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7)) {
                ChessPosition moveTwoForward = new ChessPosition(myPosition.getRow() + 2 * direction, myPosition.getColumn());
                if (board.getPiece(moveTwoForward) == null) {
                    addValidMove(validMoves, board, myPosition, 2 * direction, 0, null);
                }
            }
        }

        // capture
        addPawnCaptureMoves(validMoves, board, myPosition, direction, -1);
        addPawnCaptureMoves(validMoves, board, myPosition, direction, 1);

       // System.out.println("Valid moves for pawn at " + myPosition + ": " + validMoves);
        return validMoves;
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
    private void addValidMove(Collection<ChessMove> moves, ChessBoard board, ChessPosition from, int rowChange, int colChange, ChessPiece.PieceType promotionPiece) {
        ChessPosition to = new ChessPosition(from.getRow() + rowChange, from.getColumn() + colChange);
        if(board.getPiece(from).typeOfPiece == PieceType.PAWN && isPawnPromotionRow(to)){
            addPawnPromotionMoves(moves, board, from, to);
        }
        else {
            if (board.isValidPosition(to)) {
                ChessPiece pieceAtTo = board.getPiece(to);
                if (pieceAtTo == null || pieceAtTo.getTeamColor() != colorOfPiece) {
                    moves.add(new ChessMove(from, to, null));
                }
            }
        }
    }







    private boolean isPawnPromotionRow(ChessPosition position) {
        int promotionRowWhite = 8;
        int promotionRowBlack = 1;

        return (colorOfPiece == ChessGame.TeamColor.WHITE && position.getRow() == promotionRowWhite) ||
                (colorOfPiece == ChessGame.TeamColor.BLACK && position.getRow() == promotionRowBlack);
    }



    private void addPawnPromotionMoves(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition, ChessPosition moveForward) {
        // promote without checking spots
        moves.add(new ChessMove(myPosition, moveForward, ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(myPosition, moveForward, ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(myPosition, moveForward, ChessPiece.PieceType.BISHOP));
        moves.add(new ChessMove(myPosition, moveForward, ChessPiece.PieceType.KNIGHT));
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "colorOfPiece=" + colorOfPiece +
                ", typeOfPiece=" + typeOfPiece +
                ", firstMove=" + firstMove +
                '}';
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
