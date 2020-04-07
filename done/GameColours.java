package done;

import java.awt.*;

// declares list of colours used in game board

public enum GameColours {

    pink(Color.PINK), // square colour
    white(Color.WHITE), // player 1 (white) colour
    black(Color.BLACK), // player 2 (black) colour
    red(Color.RED); // selected token colour

    private Color c;

    GameColours(Color colour) {
        this.c = colour;
    }

    public static Color setPlayerColour(int playerID) {
        if (playerID == 1) {
            return white.getColour();
        } else if (playerID == 2) {
            return black.getColour();
        }
        return null;
    }

    public static Color setActiveColour() {
        return red.getColour();
    }

    public Color getColour() {
        return this.c;
    }
}
