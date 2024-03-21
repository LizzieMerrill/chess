public class GameUtils {
    public GameUtils(){}

    private void formatGameList(String gameListResponse) {
        // Remove unnecessary characters from the response string
        String gameList = gameListResponse.replaceAll("[{}\"]", "");

        // Split the response string into individual game entries
        String[] games = gameList.split("games:,?");

        // Counter for numbering the games
        int gameNumber = 1;

        // Print the formatted list of games
        System.out.println("List of games:");

        for (String game : games) {
            // Remove leading and trailing whitespace
            game = game.trim();

            // Skip empty strings
            if (game.isEmpty()) {
                continue;
            }

            // Split each game entry into its attributes
            String[] attributes = game.split(",");

            // Print the game number
            System.out.print(gameNumber + ". ");

            // Print each attribute of the game
            for (String attribute : attributes) {
                // Split each attribute into key-value pairs
                String[] keyValue = attribute.trim().split(":");

                // Print key-value pair
                System.out.print(keyValue[0] + ": " + keyValue[1]);
            }

            // Move to the next line for the next game
            System.out.println();

            // Increment game number
            gameNumber++;
        }
    }

}
