//package deserializers;
//
//import chess.ChessPiece;
//import com.google.gson.GsonBuilder;
//import model.GameData;
//
//public class DeserializerGameMod {
//    public GameData deserializer(String stringToChange) {
//        GsonBuilder gsonBuilder = new GsonBuilder()
//                .registerTypeAdapter(ChessPiece.class, new PieceAdapter());
//        return gsonBuilder.create().fromJson(stringToChange, GameData.class);
//    }
//}