// Jessica Lavin - 2495543L

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardView extends JPanel {

    private TokenController token;
    private boolean hover;
    private MouseHandler handler;

    // Constructor
    public BoardView(TokenController token) {
        this.token = token;
        this.hover = false;
        this.handler = new MouseHandler();
        this.setListener();
    }

    // return token
    public TokenController getToken() {
        return this.token;
    }

    // return selected token
    public boolean isSelected() {
        return this.token.isSelected();
    }

    private void paint(Graphics2D gameGraphics) {
        int padding = 10;
        gameGraphics.fillOval(padding / 2, padding / 2, this.getWidth() - padding, this.getHeight() - padding);
    }

    protected void paintComponent(Graphics graphics) {
        Graphics2D gameGraphics = (Graphics2D) graphics;
        super.paintComponents(gameGraphics);

        // creates a pink checkerboard
        if (this.token.present()) {
            gameGraphics.setColor(Color.PINK);
        } else {
            gameGraphics.setColor(Color.WHITE);
        }
        gameGraphics.fillRect(0, 0, this.getWidth(), this.getHeight());

        // fills in token colours for each player
        int playerID = this.token.getPlayer();
        // changes to red if clicked
        if (this.isSelected()) {
            gameGraphics.setColor(Color.RED);
            this.paint(gameGraphics);
        } else {
            if (playerID == 1 || playerID == 2) {
                // changes to red if hovered over
                if (this.hover) {
                    gameGraphics.setColor(Color.RED);
                } else {
                    // otherwise set to player token colour
                    if (playerID == 1) {
                        gameGraphics.setColor(Color.WHITE);
                    } else {
                        gameGraphics.setColor(Color.BLACK);
                    }
                }
                this.paint(gameGraphics);
            }
        }

        // additional graphics for king tokens
        if (this.token.king() && this.token.present()) {
            gameGraphics.setFont(new Font("Georgia", Font.BOLD, 20));
            gameGraphics.setColor(Color.PINK);
            gameGraphics.drawString("K", this.getWidth() / 2 - 8, this.getHeight() / 2 + 8);
        }
    }

    public void setListener() {
        if (this.token.moveable() || this.token.getPlayer() == APlayerID.PlayerID.getVariable()) {
            this.removeMouseListener(this.handler);
            this.addMouseListener(this.handler);
        } else {
            this.removeMouseListener(this.handler);
        }
    }

    public void setListener(AMouseListener mouseListener) {
        if (this.token.moveable() || this.token.getPlayer() == APlayerID.PlayerID.getVariable()) {
            this.removeMouseListener(mouseListener);
            this.addMouseListener(mouseListener);
        } else {
            this.removeMouseListener(mouseListener);
        }
    }

    class MouseHandler extends MouseAdapter {

        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
            BoardView.this.hover = true;
            BoardView.this.repaint();
        }

        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
            BoardView.this.hover = false;
            BoardView.this.repaint();
        }

    }
}
