package mvc_view;

import done.DraughtsController;
import done.TokenController;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseListener extends MouseAdapter {

    private BoardView token;
    private DraughtsController controller;

    public void addListener(DraughtsController c) {
        this.controller = c;
    }

    public void mousePressed(MouseEvent event) {
        super.mousePressed(event);

        // If it a player's current turn let them select a token, else display wait for other player message
        try {
            if (controller.activePlayer()) {
                selectToken(event);
            } else {
                JOptionPane.showMessageDialog(null, "Waiting for other player...");
            }
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    // Event called upon by mouse click, selects and deselects tokens
    private void selectToken(MouseEvent event) {
        try {
            token = (BoardView) event.getSource();
            TokenController s = token.getToken();

            // if token is already selected - deselect
            if (s.isSelected()) {
                controller.deselectToken();
            }
            // else select
            else {
                controller.selectToken(s);
            }
        } catch (Exception e) {
            System.out.println("Error");
        }
    }
}
