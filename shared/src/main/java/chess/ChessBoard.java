package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    ChessPiece piece1 = null;
    ChessPosition position1 = null;
    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        piece1 = piece;
        position1 = position;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {//????????
//        if(position != null){
//
//        }
//        else{
//            return
//        }
// Iterate through the pieces on the chessboard
        for (int i = 1; i <= 8; ++i) {
            for (int j = 1; j <= 8; ++j) {
                // Check if the current piece's position matches the specified position
                if (position.equals(new ChessPosition(i, j))) {
                    // If a matching piece is found, return that piece
                    return getPieceAtPosition(new ChessPosition(i, j));
                }
            }
        }
        // If no matching piece is found, return null
        return null;


        //throw new RuntimeException("Not implemented");
    }
    private ChessPiece getPieceAtPosition(ChessPosition position) {
        // Check if the position is valid
        if (isValidPosition(position)) {
            // Check if there is a piece at the specified position
            if (position.equals(position1)) {
                return piece1;
            }
        }
        // If the position is not valid or no piece is found, return null
        return null;
    }
    public boolean isValidPosition(ChessPosition position) {
        return position != null && position.getRow() >= 1 && position.getRow() <= 8 &&
                position.getColumn() >= 1 && position.getColumn() <= 8;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
//        public void addPiece(ChessPosition position, ChessPiece piece) {
//            piece1 = piece;
//            position1 = position;
//            //throw new RuntimeException("Not implemented");
//        }
        addPiece(new ChessPosition(1,1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(1,2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1,3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1,4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(1,5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(1,6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1,7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1,8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        //white pawns
        for(int i = 1; i < 9; ++i){
            addPiece(new ChessPosition(2,i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        }
        addPiece(new ChessPosition(8,1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8,2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8,3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8,4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(8,5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(8,6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8,7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8,8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        //black pawns
        for(int i = 1; i < 9; ++i){
            addPiece(new ChessPosition(7,i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }
        //throw new RuntimeException("Not implemented");
    }
}
