package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    ChessPosition startPos;
    ChessPosition endPos;
    ChessPiece.PieceType piece;
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        startPos = startPosition;
        endPos = endPosition;
        piece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPos;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPos;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return piece;
        //throw new RuntimeException("Not implemented");
    }


    public String toString(){
        return String.format("{%d, %d}", endPos.getRow(), endPos.getColumn());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(startPos, chessMove.startPos) && Objects.equals(endPos, chessMove.endPos) && piece == chessMove.piece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPos, endPos, piece);
    }
}
