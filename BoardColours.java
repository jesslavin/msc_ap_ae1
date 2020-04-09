import java.awt.*;

// declares list of colours used in game board

public enum BoardColours {

    pink(Color.PINK), // square colour
    white(Color.WHITE), // player 1 (white) colour
    black(Color.BLACK), // player 2 (black) colour
    red(Color.RED); // selected/active token colour

    private Color c;

    BoardColours(Color c) {
        this.c = c;
    }

    public static Color setPlayerColour(int playerID) {
        if (playerID == 1) {
            return white.getColour(); // if player ID = 1 set token colour to white
        } else if (playerID == 2) {
            return black.getColour();  // if player ID = 2 set token colour to black
        }
        return null;
    }

    public static Color setActiveColour() { // when token is hovered over or selected set colour to red
        return red.getColour();
    }

    public Color getColour() {
        return this.c;
    }
}
