// Jessica Lavin - 2495543L

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseListener extends MouseAdapter {

    // instance variables
    private BoardView token;
    private DraughtsController controller;

    // constructor
    public void newController(DraughtsController c) {
        this.controller = c;
    }

    // If it a player's current turn let them select a token, else display wait for other player message
    public void mousePressed(MouseEvent event) {
        super.mousePressed(event);
        try {
            if (!this.controller.activePlayer()) {
                JOptionPane.showMessageDialog(null, "Not your turn, please wait for opponents move");
            } else {
                this.selectToken(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Event called upon by mouse click, selects and deselects tokens
    private void selectToken(MouseEvent event) {
        try {
            this.token = (BoardView) event.getSource();
            TokenController t = this.token.getToken();
            // if token is already selected - deselect
            if (!t.isSelected()) {
                this.controller.selectToken(t);
            }
            // else select
            else {
                this.controller.deselectToken();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
