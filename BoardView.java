import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardView extends JPanel {

    private TokenController token;
    private boolean hover;
    private MouseHandler handler;

    //Constructor
    public BoardView(TokenController token) {
        this.token = token;
        this.hover = false;
        handler = new MouseHandler();
        setListener();
    }

    protected void paintComponent(Graphics graphics) {
        Graphics2D gameGraphics = (Graphics2D) graphics;
        super.paintComponents(gameGraphics);

        // creates a pink checkerboard
        gameGraphics.setColor(BoardColours.pink.getColour());
        if (token.present()) {
            gameGraphics.fillRect(0, 0, getWidth(), getHeight());
        }

        // fills in token colours for each player
        int playerID = token.getPlayer();
        // changes to red if clicked
        if (isSelected()) {
            gameGraphics.setColor(BoardColours.setActiveColour());
            paint(gameGraphics);
        } else {
            if (playerID == 1 || playerID == 2) {
                // changes to red if hovered over
                if (hover) {
                    gameGraphics.setColor(BoardColours.setActiveColour());
                } else {
                    // otherwise set to player token colour
                    gameGraphics.setColor(BoardColours.setPlayerColour(playerID));
                }
                paint(gameGraphics);
            }
        }

        // additional graphics for king tokens
        if (token.king() && token.present()) {
            gameGraphics.setFont(new Font("Georgia", Font.BOLD, 20));
            gameGraphics.setColor(Color.PINK);
            gameGraphics.drawString("K", getWidth() / 2 - 8, getHeight() / 2 + 8);
        }
    }

    public void setListener() {
        if (token.moveable() || token.getPlayer() == Variables.variable.getVariable()) {
            this.removeMouseListener(handler);
            this.addMouseListener(handler);
        } else {
            this.removeMouseListener(handler);
        }
    }

    public void setListener(MouseListener mouseListener) {
        setListener();
        if (token.moveable() || token.getPlayer() == Variables.variable.getVariable()) {
            this.removeMouseListener(mouseListener);
            this.addMouseListener(mouseListener);
        } else {
            this.removeMouseListener(mouseListener);
        }
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
        int padding = 24;
        gameGraphics.fillOval(padding / 2, padding / 2, getWidth() - padding, getHeight() - padding);
    }


    class MouseHandler extends MouseAdapter {

        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
            hover = true;
            repaint();
        }

        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
            hover = false;
            repaint();
        }

    }
}
