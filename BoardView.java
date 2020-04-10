// Jessica Lavin - 2495543L

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardView extends JPanel {

    // instance variables
    private final TokenController token;
    private boolean hover;
    private final MouseHandler handler;

    // constructor
    public BoardView(final TokenController token) {
        this.token = token;
        hover = false;
        handler = new MouseHandler();
        getListener();
    }

    // return token
    public TokenController getToken() {
        return token;
    }

    // return selected token
    public boolean isSelected() {
        return token.isSelected();
    }

    // create oval token
    private void paint(final Graphics2D gameGraphics) {
        final int padding = 10;
        gameGraphics.fillOval(padding / 2, padding / 2, getWidth() - padding, getHeight() - padding);
    }

    // sets the colours present on the board
    protected void paintComponent(final Graphics graphics) {
        final Graphics2D gameGraphics = (Graphics2D) graphics;
        paintComponents(gameGraphics);

        // creates a pink checkerboard
        if (token.present()) gameGraphics.setColor(Color.PINK);
        else {
            gameGraphics.setColor(Color.WHITE);
        }
        gameGraphics.fillRect(0, 0, getWidth(), getHeight());

        // fills in token colours for each player
        final int playerID = token.getPlayer();
        // changes to red if clicked
        if (isSelected()) {
            gameGraphics.setColor(Color.RED);
            paint(gameGraphics);
        } else if (playerID == 1 || playerID == 2) {
            // changes to red if hovered over
            if (hover) gameGraphics.setColor(Color.RED);
            else {
                // otherwise set to player token colour
                switch (playerID) {
                    case 1:  // player with ID 1 is white
                        gameGraphics.setColor(Color.WHITE);
                        break;
                    case 2:  // player with ID 2 is black
                        gameGraphics.setColor(Color.BLACK);
                        break;
                }
            }
            paint(gameGraphics);
        }

        // additional graphics for king tokens
        if (!token.king() || !token.present()) {
            return;
        }
        gameGraphics.setFont(new Font("Georgia", Font.BOLD, 20));
        gameGraphics.setColor(Color.PINK);
        gameGraphics.drawString("K", getWidth() / 2 - 8, getHeight() / 2 + 8);
    }

    // implements mouse listener
    public void getListener() {
        if (!token.movable() && token.getPlayer() != PlayerID.PlayerID.getVariable()) {
            removeMouseListener(handler);
        } else {
            removeMouseListener(handler);
            addMouseListener(handler);
        }
    }

    public void getListener(final MouseListener mouseListener) {
        if (!token.movable() && token.getPlayer() != PlayerID.PlayerID.getVariable()) {
            removeMouseListener(mouseListener);
        } else {
            removeMouseListener(mouseListener);
            addMouseListener(mouseListener);
        }
    }

    // handles mouse hover events
    class MouseHandler extends MouseAdapter {

        public void mouseEntered(final MouseEvent event) {
            super.mouseEntered(event);
            hover = true;
            repaint();
        }

        public void mouseExited(final MouseEvent event) {
            super.mouseExited(event);
            hover = false;
            repaint();
        }

    }
}
