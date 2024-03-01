package requests;

import chess.ChessGame;

public record JoinObject (ChessGame.TeamColor playerColor, int gameID) {

}
