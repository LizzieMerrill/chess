package deserializers;

import com.google.gson.*;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;

import java.lang.reflect.Type;

public class DeserializerServerMessage implements JsonDeserializer<ServerMessage> {
    @Override
    public ServerMessage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        GsonBuilder gson = new GsonBuilder();
        String commandType = jsonElement.getAsJsonObject().get("CommandType").getAsString();
        return switch (commandType) {
            case "LOAD_GAME" -> gson.create().fromJson(jsonElement, LoadGame.class);
            case "ERROR" -> gson.create().fromJson(jsonElement, Error.class);
            case "NOTIFICATION" -> gson.create().fromJson(jsonElement, Notification.class);
            default -> throw new UnsupportedOperationException("the class is wrong for DeserializerServerMessage gson");
        };
    }
}