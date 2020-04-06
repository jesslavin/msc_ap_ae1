import java.awt.Color;

public enum GameColours {

	pink(Color.PINK),
	white(Color.WHITE),
	black(Color.BLACK),
	red(Color.RED);

	private Color colour;
	GameColours(Color colour){
		this.colour = colour;
	}
	public Color getColour(){
		return this.colour;
	}

	public static Color setPlayerColour(int playerID){
		if(playerID==1){
			return white.getColour();
		}
		else if(playerID==2){
			return black.getColour();
		}
		return null;
	}

	public static Color setActiveColour(){
			return red.getColour();
	}
}
