package ui;
public class GameUtils {
    public GameUtils(){}

    String formatGameList(String gameListResponse) {
        // Remove unnecessary characters from the response string
        String gameList = gameListResponse.replaceAll("[{}\"]", "");

        // Split the response string into individual game entries
        String[] games = gameList.split("games:,?");

        // Counter for numbering the games
        int gameNumber = 1;

        // StringBuilder to build the formatted game list
        StringBuilder formattedGameList = new StringBuilder("List of games:\n\n");

        for (String game : games) {
            // Remove leading and trailing whitespace
            game = game.trim();

            // Skip empty strings
            if (game.isEmpty()) {
                continue;
            }

            // Split each game entry into its attributes
            String[] attributes = game.split(",");

            // Append game number
            formattedGameList.append(gameNumber).append(". ");

            // Initialize variables to store game attributes
            String gameName = null;
            String whiteUser = null;
            String blackUser = null;
            String gameId = null;

            // Extract attributes of the game
            for (String attribute : attributes) {
                // Split each attribute into key-value pairs
                String[] keyValue = attribute.trim().split(":");

                // Store the attribute values based on the key
                if (keyValue.length == 2) {
                    if (keyValue[0].equals("gameName")) {
                        gameName = keyValue[1].trim();
                    } else if (keyValue[0].equals("whiteUsername")) {
                        whiteUser = keyValue[1].trim();
                    } else if (keyValue[0].equals("blackUsername")) {
                        blackUser = keyValue[1].trim();
                    } else if (keyValue[0].equals("gameID")) {
                        gameId = keyValue[1].trim();
                    }
                }
            }

            // Append game attributes in the specified format
            if (gameName != null) {
                formattedGameList.append("Name: ").append(gameName).append("\n");
            }
            if (whiteUser != null) {
                formattedGameList.append("White User: ").append(whiteUser).append("\n");
            }
            if (blackUser != null) {
                formattedGameList.append("Black User: ").append(blackUser).append("\n");
            }
            if (gameId != null) {
                formattedGameList.append("ID: ").append(gameId).append("\n");
            }

            // Increment game number
            gameNumber++;

            // Add a new line between games
            formattedGameList.append("\n");
        }

        // Return the formatted game list as a string
        return formattedGameList.toString();
    }


}
//package ui;
//
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//
//public class GameUtils {
//
//    public static String formatGameList(String json) {
//        StringBuilder formatted = new StringBuilder();
//        int count = 1;
//
//        try {
//            JsonObject gamesObject = new JsonObject(json);
//            JsonArray gamesArray = gamesObject.getAsJsonArray("games");
//            for (int i = 0; i < gamesArray.size(); i++) {
//                JsonObject gameObj = gamesArray.getAsJsonObject(i);
//                String gameName = gameObj.getAsString("gameName");
//                String whitePlayer = gameObj.optString("whiteUsername", "No white player");
//                String blackPlayer = gameObj.optString("blackUsername", "No black player");
//
//                formatted.append(count).append(". ").append(gameName).append("\n");
//                formatted.append("   White Player: ").append(whitePlayer).append("\n");
//                formatted.append("   Black Player: ").append(blackPlayer).append("\n\n");
//                count++;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return formatted.toString();
//    }
//}
