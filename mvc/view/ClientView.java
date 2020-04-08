package view;

import model.BoardModel;
import model.TokenModel;
import done.Variables;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class ClientView extends JPanel {

    private Dimension window = new Dimension(720, 720);
    private BoardModel board;
    private MouseListener listener;
    private LinkedList<TokenView> token;
    private TokenModel[][] tokens;

    public ClientView(MouseListener listener) {
        setPreferredSize(window);
        setLayout(new GridLayout(8, 8));

        board = new BoardModel();
        this.listener = listener;
        token = new LinkedList<TokenView>();
        tokens = board.getTokens();

        buildSquares();
    }

    private void buildSquares() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                TokenView square = new TokenView(tokens[i][j]);
                if (square.getToken().playable() || square.getToken().getPlayer() == Variables.variable.getVariable()) {
                    square.addMouseListener(listener);
                }
                this.token.add(square);
                add(square);
            }
        }
    }

    public void activateSquare() {
        for (TokenView square : token) {
            square.setListener(listener);
        }
        repaint();
    }

    public LinkedList<TokenModel> getPlayable(TokenModel token) {
        return board.getPlayable(token);
    }

    public TokenModel getToken(int i) {
        return token.get(i - 1).getToken();
    }
}
