import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class Controller implements Runnable {

	private boolean continueToPlay;
	private boolean waitingForAction;
	private boolean isOver;
	private DataInputStream fromServer;
	private DataOutputStream toServer;
	private ClientView boardSquare;
	private PlayerModel player;
	private LinkedList<TokenBuild> selectedTokens;
	private LinkedList<TokenBuild> playableTokens;
	
	public Controller(PlayerModel player, DataInputStream input, DataOutputStream output){
		this.player = player;
		this.fromServer = input;
		this.toServer= output;
		
		selectedTokens = new LinkedList<TokenBuild>();
		playableTokens = new LinkedList<TokenBuild>();
	}
	
	public void setup(ClientView square){
		this.boardSquare = square;
	}

	public void run() {
		continueToPlay = true;
		waitingForAction = true;
		isOver=false;
		
		try {

			if(player.getPlayer()== Constants.playerOne.getConstants()){
				fromServer.readInt();
				player.setCurrentTurn(true);
			}
					
			while(continueToPlay && !isOver){
				if(player.getPlayer()== Constants.playerOne.getConstants()){
					waitForPlayerAction();
					if(!isOver)
						receiveInfoFromServer();
				}else if(player.getPlayer()== Constants.playerTwo.getConstants()){
					receiveInfoFromServer();
					if(!isOver)
						waitForPlayerAction();
				}
			}
			
			if(isOver){
				JOptionPane.showMessageDialog(null, "Game is over",
						"Information", JOptionPane.INFORMATION_MESSAGE, null);
				System.exit(0);
			}
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Connection lost",
					"Error", JOptionPane.ERROR_MESSAGE, null);
			System.exit(0);
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(null, "Connection interrupted",
					"Error", JOptionPane.ERROR_MESSAGE, null);
		}			
	}
	
	private void receiveInfoFromServer() throws IOException {
		player.setCurrentTurn(false);
		int from = fromServer.readInt();
		if(from== Constants.loser.getConstants()){
			from = fromServer.readInt();
			int to = fromServer.readInt();
			updateReceivedInfo(from, to);
			isOver=true;
		}else if(from== Constants.winner.getConstants()){
			isOver=true;
			continueToPlay=false;
		}else{
			int to = fromServer.readInt();
			updateReceivedInfo(from, to);
		}
	}	

	private void sendMove(TokenBuild from, TokenBuild to) throws IOException {
		toServer.writeInt(from.getTokenID());
		toServer.writeInt(to.getTokenID());
	}

	private void waitForPlayerAction() throws InterruptedException {
		player.setCurrentTurn(true);
		while(waitingForAction){
			Thread.sleep(100);
		}
		waitingForAction = true;		
	}
	
	public void move(TokenBuild from, TokenBuild to){
		to.setPlayerID(from.getPlayer());
		from.setPlayerID(Constants.empty.getConstants());
		checkCrossJump(from, to);
		checkKing(from, to);
		tokenDeselected();
		
		waitingForAction = false;
		try {
			sendMove(from, to);
		} catch (IOException e) {
			System.out.println("Sending failed");
		}		
	}

	public void tokenSelected(TokenBuild s) {
		if(selectedTokens.isEmpty()){
			addToSelected(s);
		}
		else if(selectedTokens.size()>=1){
			if(playableTokens.contains(s)){
				move(selectedTokens.getFirst(),s);
			}else{
				tokenDeselected();
				addToSelected(s);
			}
		}
	}
	
	private void addToSelected(TokenBuild token){
		token.setSelected(true);
		selectedTokens.add(token);
		getPlayableTokens(token);
	}

	public void tokenDeselected() {
		
		for(TokenBuild token: selectedTokens)
			token.setSelected(false);
		
		selectedTokens.clear();
		
		for(TokenBuild token: playableTokens){
			token.setPossibleToMove(false);
		}
		
		playableTokens.clear();
		boardSquare.activateSquare();
	}
	
	
	private void getPlayableTokens(TokenBuild s){
		playableTokens.clear();
		playableTokens = boardSquare.getPlayable(s);
		boardSquare.activateSquare();
	}
	
	public boolean currentTurn(){
		return player.whosTurn();
	}
	
	private void checkCrossJump(TokenBuild from, TokenBuild to){
		if(Math.abs(from.getTokenRow()-to.getTokenRow())==2){
			int middleRow = (from.getTokenRow() + to.getTokenRow())/2;
			int middleCol = (from.getTokenColumn() + to.getTokenColumn())/2;
			
			TokenBuild middleToken = boardSquare.getToken((middleRow*8)+middleCol+1);
			middleToken.setPlayerID(Constants.empty.getConstants());
			middleToken.removeKing();
		}
	}
	
	private void checkKing(TokenBuild from, TokenBuild movedToken){
		if(from.isKing()){
			movedToken.setKing();
			from.removeKing();
		}else if(movedToken.getTokenRow()==7 && movedToken.getPlayer()== Constants.playerOne.getConstants()){
			movedToken.setKing();
		}else if(movedToken.getTokenRow()==0 && movedToken.getPlayer()== Constants.playerTwo.getConstants()){
			movedToken.setKing();
		}
	}
	
	private void updateReceivedInfo(int from, int to){
		TokenBuild fromToken = boardSquare.getToken(from);
		TokenBuild toToken = boardSquare.getToken(to);
		toToken.setPlayerID(fromToken.getPlayer());
		fromToken.setPlayerID(Constants.empty.getConstants());
		checkCrossJump(fromToken, toToken);
		checkKing(fromToken, toToken);
		boardSquare.activateSquare();
	}
}