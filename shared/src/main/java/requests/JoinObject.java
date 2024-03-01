package requests;

import chess.ChessGame;
import dataAccess.data.GameData;

public record JoinObject (ChessGame.TeamColor playerColor, int gameID) {

}
