// Jessica Lavin - 2495543L

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseListener extends MouseAdapter {

    // instance variables
    private BoardView token;
    private DraughtsController controller;

    // constructor
    public void newController(final DraughtsController c) {
        controller = c;
    }

    // If it a player's current turn let them select a token, else display wait for other player message
    public void mousePressed(final MouseEvent event) {
        super.mousePressed(event);
        try {
            if (!controller.activePlayer()) {
                JOptionPane.showMessageDialog(null, "Not your turn, please wait for opponents move");
            } else {
                selectToken(event);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    // Event called upon by mouse click, selects and deselects tokens
    private void selectToken(final MouseEvent event) {
        try {
            token = (BoardView) event.getSource();
            final TokenController t = token.getToken();
            // if token is already selected - deselect
            if (!t.isSelected()) {
                controller.selectToken(t);
            }
            // else select
            else {
                controller.deselectToken();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
