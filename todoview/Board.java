package todoview;

import done.MouseListener;
import done.Variables;
import todomodel.TokenBuild;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class Board extends JPanel {

    private Dimension window = new Dimension(720, 720);
    private todomodel.Board board;
    private MouseListener listener;
    private LinkedList<Token> token;
    private TokenBuild[][] tokens;

    public Board(MouseListener listener) {
        setPreferredSize(window);
        setLayout(new GridLayout(8, 8));

        board = new todomodel.Board();
        this.listener = listener;
        token = new LinkedList<Token>();
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
                this.token.add(square);
                add(square);
            }
        }
    }

    public void activateSquare() {
        for (Token square : token) {
            square.setListener(listener);
        }
        repaint();
    }

    public LinkedList<TokenBuild> getPlayable(TokenBuild token) {
        return board.getPlayable(token);
    }

    public TokenBuild getToken(int i) {
        return token.get(i - 1).getToken();
    }
}
