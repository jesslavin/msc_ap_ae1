public class Game {

	private TokenBuild[][] tokens;
	
	// constructor
	public Game(){
		tokens = new TokenBuild[8][8];
		
		initializeTokens();
		assignPlayers();
	}

	// initialize 64 squares with ID, row, column whether or not they are filled with a token
	private void initializeTokens() {
		boolean rowInitialFilled, isFilled;
		int count = 0;
		
		//Rows
		for(int r = 0; r< Constants.rows.getConstants(); r++){
			rowInitialFilled = r % 2 == 1;
			
			//Columns
			for(int c = 0; c< Constants.columns.getConstants(); c++){
				isFilled = (rowInitialFilled && c % 2 == 0) || !rowInitialFilled && c % 2 == 1;
				count++;
				
				tokens[r][c] = new TokenBuild(count, r, c, isFilled);
			}
		}		
	}
	
	private void assignPlayers() {
		
		// player one gets rows 0-2
		for(int r=0;r<3;r++){					
			// columns
			for(int c = 0; c< Constants.columns.getConstants(); c++){
				if(tokens[r][c].getFilled()){
					tokens[r][c].setPlayerID(Constants.playerOne.getConstants());
				}
			}
		}
		
		// player two gets rows 5-7
		for(int r=5;r<8;r++){					
			// columns
			for(int c = 0; c< Constants.columns.getConstants(); c++){
				if(tokens[r][c].getFilled()){
					tokens[r][c].setPlayerID(Constants.playerTwo.getConstants());
				}
			}
		}
	}

	public TokenBuild getToken(int from) {
		for(TokenBuild[] sRows: tokens){
			for(TokenBuild s: sRows){
				if(s.getTokenID()==from){
					return s;
				}
					
			}
		}		
		return null;
	}

	public boolean isOver(){
		
		int playerOne = 0;
		int playerTwo = 0;
		for(int r = 0; r< Constants.rows.getConstants(); r++){
			for(int c = 0; c< Constants.columns.getConstants(); c++){
				if(tokens[r][c].getPlayer()==1)
					playerOne++;
				
				if(tokens[r][c].getPlayer()==2)
					playerTwo++;
			}
		}

		return playerOne == 0 || playerTwo == 0;
	}
}
