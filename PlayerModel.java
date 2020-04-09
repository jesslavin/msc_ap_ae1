import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

public class PlayerModel {

	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private int player;
	private boolean currentTurn;

	public PlayerModel() {
		this.setTurn(false);

	}

	public PlayerModel(Socket socket) {
		this.socket = socket;

		try {
			this.input = new DataInputStream(this.socket.getInputStream());
			this.output = new DataOutputStream(this.socket.getOutputStream());

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Unable to connect player");
			System.exit(0);
		}
	}

	public boolean active() {
		return this.currentTurn;
	}

	public void closeConnection() {
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean connected() {
		return this.socket.isConnected();
	}

	public int getInput() {
		int i = 0;
		try {
			i = this.input.readInt();
			return i;
		} catch (IOException e) {
			System.out.println("No response from player");
			return 99;
		}
	}

	public int getOutput(int i) {
		try {
			this.output.writeInt(i);
			return 1;
		} catch (IOException e) {
			System.out.println("Unable to find player");
			return 99;
		}
	}

	public int getPlayer() {
		return this.player;
	}

	public void setPlayer(int player) {
		this.player = player;
		Variables.variable.setVariable(player);
	}

	public void setTurn(boolean currentTurn) {
		this.currentTurn = currentTurn;
	}
}
