import java.awt.Color;

public enum XBoardColours {

	// board colours
	pink(Color.PINK),
	white(Color.WHITE),
	black(Color.BLACK),
	red(Color.RED);

	// constructor
	private Color colour;
	XBoardColours(Color colour){
		this.colour = colour;
	}
	public Color getColour(){
		return this.colour;
	}

	// set colour of each players counters
	public static Color setPlayerColour(int playerID){
		if(playerID==1){
			return white.getColour();
		}
		else if(playerID==2){
			return black.getColour();
		}
		return null;
	}

	// set colour of counter when active
	public static Color setActiveColour(){
			return red.getColour();
	}
}
