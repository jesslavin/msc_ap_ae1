package mvc_view;

import done.Variables;
import mvc_model.BoardModel;
import done.TokenController;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class ClientView extends JPanel {

    private Dimension window = new Dimension(720, 720);
    private BoardModel board;
    private MouseListener listener;
    private LinkedList<BoardView> token;
    private TokenController[][] tokens;

    public ClientView(MouseListener listener) {
        setPreferredSize(window);
        setLayout(new GridLayout(8, 8));

        board = new BoardModel();
        this.listener = listener;
        token = new LinkedList<BoardView>();
        tokens = board.getTokens();

        buildSquares();
    }

    private void buildSquares() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                BoardView square = new BoardView(tokens[i][j]);
                if (square.getToken().moveable() || square.getToken().getPlayer() == Variables.variable.getVariable()) {
                    square.addMouseListener(listener);
                }
                this.token.add(square);
                add(square);
            }
        }
    }

    public void play() {
        for (BoardView square : token) {
            square.setListener(listener);
        }
        repaint();
    }

    public LinkedList<TokenController> playableToken(TokenController token) {
        return board.playableTokens(token);
    }

    public TokenController getToken(int i) {
        return token.get(i - 1).getToken();
    }
}
