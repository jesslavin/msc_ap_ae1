package todo;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class ClientView extends JPanel {

    private Dimension window = new Dimension(720, 720);
    private Board board;
    private MouseListener listener;
    private LinkedList<Token> squares;
    private TokenBuild[][] tokens;

    public ClientView(MouseListener listener) {
        setPreferredSize(window);
        setLayout(new GridLayout(8, 8));

        board = new Board();
        this.listener = listener;
        squares = new LinkedList<Token>();
        tokens = board.getTokens();

        buildSquares();
    }

    private void buildSquares() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Token square = new Token(tokens[i][j]);
                if (square.getToken().playable() || square.getToken().getPlayer() == Variables.sessionVariable.getVariable()) {
                    square.addMouseListener(listener);
                }
                this.squares.add(square);
                add(square);
            }
        }
    }

    public void activateSquare() {
        for (Token square : squares) {
            square.setListener(listener);
        }
        repaint();
    }

    public LinkedList<TokenBuild> getPlayable(TokenBuild token) {
        return board.getPlayable(token);
    }

    public TokenBuild getToken(int i) {
        return squares.get(i - 1).getToken();
    }
}
