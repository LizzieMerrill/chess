package inGameHandlers;

import chess.ChessBoard;

public class DrawBoardHandler {
    public void draw(ChessBoard board){
        //extract each position and draw whats there
    }
    public void draw(char[][] pieces) {
        // Define the size of the chessboard
        int size = 8;
        String color;

        // Draw the chessboard twice, once with white pieces at the bottom and once with black pieces at the bottom
        for (int orientation = 0; orientation < 2; orientation++) {
            if(orientation == 0){
                System.out.println("Black POV:\n");
            }
            else{
                System.out.println("White POV:\n");
            }
            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    // Calculate the color of the square
                    boolean isWhiteSquare = (row + col) % 2 == 0;
                    // Color variables for terminal printing
                    String bgWhite = "\u001B[47m";
                    String bgBlack = "\u001B[40m";
                    String fgDarkRed = "\u001B[31;2m";
                    String resetColor = "\u001B[0m";

                    // Color the background based on the square color
                    if ((orientation == 0 && isWhiteSquare) || (orientation == 1 && !isWhiteSquare)) {
                        System.out.print(bgWhite);
                    } else {
                        System.out.print(bgBlack);
                    }

                    // Print the piece or empty square
                    char piece = pieces[(orientation == 0) ? row : size - row - 1][col];
                    // Color black team letters red
                    if (Character.isLowerCase(piece)) {
                        System.out.print(fgDarkRed);
                    }
                    System.out.print(" " + Character.toUpperCase(piece) + " ");
                    // Reset the color after printing each square
                    System.out.print(resetColor);
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}
