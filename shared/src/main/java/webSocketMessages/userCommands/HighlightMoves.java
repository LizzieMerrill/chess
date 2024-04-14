package webSocketMessages.userCommands;

import chess.ChessGame;
import chess.ChessPiece;

public class HighlightMoves extends UserGameCommand {

    ChessGame game;
    public HighlightMoves(String authToken) {
        super(authToken);
    }
    public ChessGame getGame(){
        return game;
    }
    public void setGame(ChessGame game){
        this.game = game;
    }

}
