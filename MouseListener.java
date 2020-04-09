import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

public class MouseListener extends MouseAdapter {

	private BoardView token;
	private DraughtsController controller;

	public void addListener(DraughtsController c) {
		this.controller = c;
	}

	@Override
	public void mousePressed(MouseEvent event) {
		super.mousePressed(event);

		// If it a player's current turn let them select a token, else display wait for
		// other player message
		try {
			if (this.controller.activePlayer()) {
				this.selectToken(event);
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
			this.token = (BoardView) event.getSource();
			TokenController s = this.token.getToken();

			// if token is already selected - deselect
			if (s.isSelected()) {
				this.controller.deselectToken();
			}
			// else select
			else {
				this.controller.selectToken(s);
			}
		} catch (Exception e) {
			System.out.println("Error");
		}
	}
}
