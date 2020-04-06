import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class Squares extends JPanel {
	
	private TokenBuild token;
	private boolean hover;
	private MouseHandler handler;
	
	//Constructor
	public Squares(TokenBuild token){
		this.token = token;
		this.hover = false;
		handler = new MouseHandler();
		setListener();		
	}
	
	protected void paintComponent(Graphics graphics){
		Graphics2D gameGraphics = (Graphics2D) graphics;
		super.paintComponents(gameGraphics);
		
		// creates a pink checkerboard
		gameGraphics.setColor(GameColours.pink.getColour());
		if(token.getFilled()){
			gameGraphics.fillRect(0, 0, getWidth(), getHeight());
		}
		
		// fills in token colours for each player
		int playerID = token.getPlayer();
		// changes to red if clicked
		if(isSelected()){
			gameGraphics.setColor(GameColours.setActiveColour());
			paint(gameGraphics);
		}else{
			if(playerID==1 || playerID == 2){
				// changes to red if hovered over
				if(hover){
					gameGraphics.setColor(GameColours.setActiveColour());
				}else{
					// otherwise set to player token colour
					gameGraphics.setColor(GameColours.setPlayerColour(playerID));
				}
				paint(gameGraphics);
			}
		}
		
		// additional graphics for king tokens
		if(token.isKing() && token.getFilled()){
			gameGraphics.setFont(new Font("Georgia",Font.BOLD,20));
			gameGraphics.setColor(Color.PINK);
			gameGraphics.drawString("K", getWidth()/2-8, getHeight()/2+8);
		}
	}
	
	public void setListener(){
		if(token.playable() || token.getPlayer()== Variables.sessionVariable.getVariable()){
			this.removeMouseListener(handler);
			this.addMouseListener(handler);
		}else{
			this.removeMouseListener(handler);
		}
	}
	
	public void setListener(MouseListener mouseListener){
		setListener();
		if(token.playable() || token.getPlayer()== Variables.sessionVariable.getVariable()){
			this.removeMouseListener(mouseListener);
			this.addMouseListener(mouseListener);
		}else{
			this.removeMouseListener(mouseListener);
		}
	}
	
	// return token
	public TokenBuild getToken(){
		return this.token;
	}
	
	// return selected token
	public boolean isSelected(){
		return this.token.isSelected();
	}


	private void paint(Graphics2D gameGraphics){
		int padding= 24;
		gameGraphics.fillOval(padding/2, padding/2, getWidth()-padding, getHeight()-padding);
	}
	

	class MouseHandler extends MouseAdapter {
		
		public void mouseEntered(MouseEvent e){	
			super.mouseEntered(e);
			hover = true;
			repaint();
		}
		
		public void mouseExited(MouseEvent e){
			super.mouseExited(e);
			hover = false;
			repaint();
		}

	}
}
