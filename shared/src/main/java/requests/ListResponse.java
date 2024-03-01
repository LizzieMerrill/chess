package requests;

import dataAccess.data.GameData;

import java.util.Collection;

public record ListResponse(Collection<GameData> games, String message) {

}
