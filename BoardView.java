// Jessica Lavin - 2495543L

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardView extends JPanel {

    // instance variables
    private TokenController token;
    private boolean hover;
    private MouseHandler handler;

    // constructor
    public BoardView(TokenController token) {
        this.token = token;
        this.hover = false;
        this.handler = new MouseHandler();
        this.getListener();
    }

    // return token
    public TokenController getToken() {
        return this.token;
    }

    // return selected token
    public boolean isSelected() {
        return this.token.isSelected();
    }

    // create oval token
    private void paint(Graphics2D gameGraphics) {
        int padding = 10;
        gameGraphics.fillOval(padding / 2, padding / 2, this.getWidth() - padding, this.getHeight() - padding);
    }

    // sets the colours present on the board
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
        if (!this.isSelected()) {
            if (playerID == 1 || playerID == 2) {
                // changes to red if hovered over
                if (!this.hover) {
                    // otherwise set to player token colour
                    switch (playerID) {
                        case 1:  // player with ID 1 is white
                            gameGraphics.setColor(Color.WHITE);
                            break;
                        case 2:  // player with ID 2 is black
                            gameGraphics.setColor(Color.BLACK);
                            break;
                    }
                } else {
                    gameGraphics.setColor(Color.RED);
                }
                this.paint(gameGraphics);
            }
        } else {
            gameGraphics.setColor(Color.RED);
            this.paint(gameGraphics);
        }

        // additional graphics for king tokens
        if (this.token.king() && this.token.present()) {
            gameGraphics.setFont(new Font("Georgia", Font.BOLD, 20));
            gameGraphics.setColor(Color.PINK);
            gameGraphics.drawString("K", this.getWidth() / 2 - 8, this.getHeight() / 2 + 8);
        }
    }

    // implements mouse listener
    public void getListener() {
        if (this.token.moveable() || this.token.getPlayer() == PlayerID.PlayerID.getVariable()) {
            this.removeMouseListener(this.handler);
            this.addMouseListener(this.handler);
        } else {
            this.removeMouseListener(this.handler);
        }
    }

    public void getListener(MouseListener mouseListener) {
        if (this.token.moveable() || this.token.getPlayer() == PlayerID.PlayerID.getVariable()) {
            this.removeMouseListener(mouseListener);
            this.addMouseListener(mouseListener);
        } else {
            this.removeMouseListener(mouseListener);
        }
    }

    // handles mouse hover events
    class MouseHandler extends MouseAdapter {

        public void mouseEntered(MouseEvent event) {
            super.mouseEntered(event);
            BoardView.this.hover = true;
            BoardView.this.repaint();
        }

        public void mouseExited(MouseEvent event) {
            super.mouseExited(event);
            BoardView.this.hover = false;
            BoardView.this.repaint();
        }

    }
}
