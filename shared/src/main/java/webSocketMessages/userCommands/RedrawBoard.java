package webSocketMessages.userCommands;

import chess.ChessBoard;
import chess.ChessGame;

public class RedrawBoard extends UserGameCommand {
    ChessGame game;
    public RedrawBoard(String authToken, ChessGame game) {
        super(authToken);
    }
    public ChessGame getGame(){
        return game;
    }
}
