// Jessica Lavin - 2495543L

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AMouseListener extends MouseAdapter {

    // instance variables
    private ABoardView token;
    private DraughtsController controller;

    public void newController(DraughtsController c) {
        this.controller = c;
    }

    public void mousePressed(MouseEvent event) {
        super.mousePressed(event);
        // If it a player's current turn let them select a token, else display wait for other player message
        try {
            if (this.controller.activePlayer()) {
                this.selectToken(event);
            } else {
                JOptionPane.showMessageDialog(null, "Waiting for other player...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Event called upon by mouse click, selects and deselects tokens
    private void selectToken(MouseEvent event) {
        try {
            this.token = (ABoardView) event.getSource();
            TokenController t = this.token.getToken();
            // if token is already selected - deselect
            if (t.isSelected()) {
                this.controller.deselectToken();
            }
            // else select
            else {
                this.controller.selectToken(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
