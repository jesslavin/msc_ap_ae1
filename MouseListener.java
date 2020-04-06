import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseListener extends MouseAdapter {

    private Squares squares;
    private Controller controller;

    public void addListener(Controller c) {
        this.controller = c;
    }

    public void mousePressed(MouseEvent event) {
        super.mousePressed(event);

        try {
            if (controller.currentTurn()) {
                selectCounter(event);
            } else {
                JOptionPane.showMessageDialog(null, "Other player to make their move",
                        "Error", JOptionPane.ERROR_MESSAGE, null);
            }
        } catch (Exception e) {
            System.out.println("Error");
        }


    }

    private void selectCounter(MouseEvent event) {
        try {
            squares = (Squares) event.getSource();
            TokenBuild s = squares.getToken();

            // if token is already selected - deselect
            if (s.isSelected()) {
                controller.tokenDeselected();
            }
            // else select
            else {
                controller.tokenSelected(s);
            }
        } catch (Exception e) {
            System.out.println("error");
        }
    }
}
