import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.LinkedList;

import javax.swing.JPanel;

public class ClientView extends JPanel {

	private Dimension window = new Dimension(720, 720);
	private BoardModel board;
	private MouseListener listener;
	private LinkedList<BoardView> token;
	private TokenController[][] tokens;

	public ClientView(MouseListener listener) {
		this.setPreferredSize(this.window);
		this.setLayout(new GridLayout(8, 8));

		this.board = new BoardModel();
		this.listener = listener;
		this.token = new LinkedList<BoardView>();
		this.tokens = this.board.getTokens();

		this.buildSquares();
	}

	private void buildSquares() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				BoardView square = new BoardView(this.tokens[i][j]);
				if (square.getToken().moveable() || square.getToken().getPlayer() == Variables.variable.getVariable()) {
					square.addMouseListener(this.listener);
				}
				this.token.add(square);
				this.add(square);
			}
		}
	}

	public TokenController getToken(int i) {
		return this.token.get(i - 1).getToken();
	}

	public void play() {
		for (BoardView square : this.token) {
			square.setListener(this.listener);
		}
		this.repaint();
	}

	public LinkedList<TokenController> playableToken(TokenController token) {
		return this.board.playableTokens(token);
	}
}
