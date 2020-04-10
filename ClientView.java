// Jessica Lavin - 2495543L

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class ClientView extends JPanel {

    // instance variables
    private final Dimension window = new Dimension(720, 720);
    private final BoardModel board;
    private final MouseListener listener;
    private final LinkedList<BoardView> token;
    private final TokenController[][] tokens;

    // constructor
    public ClientView(final MouseListener listener) {
        setPreferredSize(window);
        setLayout(new GridLayout(8, 8));
        board = new BoardModel();
        this.listener = listener;
        token = new LinkedList<>();
        tokens = board.getTokens();
        build();
    }

    // builds the draughts board
    private void build() {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                final BoardView token = new BoardView(tokens[i][j]);
                if (!token.getToken().movable() && token.getToken().getPlayer() != PlayerID.PlayerID.getVariable()) {
                } else {
                    token.addMouseListener(listener);
                }
                this.token.add(token);
                add(token);
            }
    }

    public TokenController getToken(final int t) {
        return token.get(t - 1).getToken();
    }

    // implements the board
    public void play() {
        for (final BoardView token : token) token.getListener(listener);
        repaint();
    }

    public LinkedList<TokenController> playableToken(final TokenController token) {
        return board.playableTokens(token);
    }
}
