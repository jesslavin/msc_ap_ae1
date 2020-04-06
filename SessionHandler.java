import java.net.*;

public class SessionHandler implements Runnable {
	
	private Game draughts;
	private ServerPlayer playerOne;
	private ServerPlayer playerTwo;
	
	private boolean continuePlay = true;
	
	// thread construction
	public SessionHandler(Socket socketOne, Socket socketTwo){
		playerOne = new ServerPlayer(socketOne);
		playerTwo = new ServerPlayer(socketTwo);
		draughts = new Game();
	}
	
	public void run() {
		try{
				playerOne.getOutput(1);
				while(continuePlay){
					// wait for player one to make their move
					int from = playerOne.getInput();
					int to = playerOne.getInput();
					// update board accordingly
					pass(from, to);
					updateBoard(from, to);
							
					// send this data to player two
					if(draughts.isOver())
						// notifies game is over
						playerTwo.getOutput(Constants.loser.getConstants());
					int getData = playerTwo.getOutput(from);
					int sendData = playerTwo.getOutput(to);
					pass(getData,sendData);
					
					// if game is over, break out
					if(draughts.isOver()){
						playerOne.getOutput(Constants.winner.getConstants());
						continuePlay =false;
						break;
					}

					// wait for player two to make their move
					from = playerTwo.getInput();
					to = playerTwo.getInput();
					// update board accordingly
					pass(from, to);
					updateBoard(from, to);

					// send this data to player one
					if(draughts.isOver()){
						// notifies game is over
						playerOne.getOutput(Constants.loser.getConstants());
					}
					getData = playerOne.getOutput(from);
					sendData = playerOne.getOutput(to);
					pass(getData,sendData);

					// if game is over, break out
					if(draughts.isOver()){
						playerTwo.getOutput(Constants.winner.getConstants());
						continuePlay =false;
						break;
					}
				}
				
		}catch(Exception e){
			System.out.println("Connection closed");

			if(playerOne.connected())
				playerOne.closeConnection();
			
			if(playerTwo.connected())
				playerTwo.closeConnection();
			
			return;
		}
	}

	// passes data throwing exception when the connection is lost
	private void pass(int to, int from) throws Exception{
		if(to==99 || from==99){
			throw new Exception("Connection lost");
		}
	}
	
	private void updateBoard(int from, int to){
		TokenBuild fromToken = draughts.getToken(from);
		TokenBuild toToken = draughts.getToken(to);
		toToken.setPlayerID(fromToken.getPlayer());
		fromToken.setPlayerID(Constants.empty.getConstants());
		
		crossJump(fromToken, toToken);
	}
	
	private void crossJump(TokenBuild from, TokenBuild to){
		if(Math.abs(from.getTokenRow()-to.getTokenRow())==2){
			int middleRow = (from.getTokenRow() + to.getTokenRow())/2;
			int middleColumn = (from.getTokenColumn() + to.getTokenColumn())/2;
			
			TokenBuild middleToken = draughts.getToken((middleRow*8)+middleColumn+1);
			middleToken.setPlayerID(Constants.empty.getConstants());
		}
	}
}