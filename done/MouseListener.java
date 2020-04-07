package done;

import todoview.Token;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseListener extends MouseAdapter {

    private Token token;
    private Controller controller;

    public void addListener(Controller c) {
        this.controller = c;
    }

    public void mousePressed(MouseEvent event) {
        super.mousePressed(event);

        // If it a player's current turn let them select a token, else display wait for other player message
        try {
            if (controller.currentTurn()) {
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
            token = (Token) event.getSource();
            TokenModel s = token.getToken();

            // if token is already selected - deselect
            if (s.isSelected()) {
                controller.tokenDeselected();
            }
            // else select
            else {
                controller.tokenSelected(s);
            }
        } catch (Exception e) {
            System.out.println("Error");
        }
    }
}
