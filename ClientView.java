// Jessica Lavin - 2495543L

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class ClientView extends JPanel {

    // instance variables
    private Dimension window = new Dimension(720, 720);
    private BoardModel board;
    private MouseListener listener;
    private LinkedList<BoardView> token;
    private TokenController[][] tokens;

    // constructor
    public ClientView(MouseListener listener) {
        this.setPreferredSize(this.window);
        this.setLayout(new GridLayout(8, 8));
        this.board = new BoardModel();
        this.listener = listener;
        this.token = new LinkedList<>();
        this.tokens = this.board.getTokens();
        this.build();
    }

    // builds the draughts board
    private void build() {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                BoardView token = new BoardView(this.tokens[i][j]);
                if (!token.getToken().moveable() && token.getToken().getPlayer() != PlayerID.PlayerID.getVariable()) {
                } else {
                    token.addMouseListener(this.listener);
                }
                this.token.add(token);
                this.add(token);
            }
    }

    public TokenController getToken(int t) {
        return this.token.get(t - 1).getToken();
    }

    // implements the board
    public void play() {
        for (BoardView token : this.token) token.getListener(this.listener);
        this.repaint();
    }

    public LinkedList<TokenController> playableToken(TokenController token) {
        return this.board.playableTokens(token);
    }
}
