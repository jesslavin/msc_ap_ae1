import javax.swing.*;
import java.io.*;
import java.net.*;

public class XClientBuild extends JFrame {

	// game properties
	private Player player;
	private ClientView draughtsBoard;

	// network properties
	private String server;
	private int port;
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;

	public XClientBuild() {

		// fetches network properties and tries to connect each client/player
		try {
			XFetchProperties pm = XFetchProperties.fetchInstance();
			server = pm.fetchServer();
			port = pm.fetchPort();
			player = new Player();
			connect();

			// catches errors and quits game if unable to connect
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Unable to connect, please check configuration",  "Error",
					JOptionPane.ERROR_MESSAGE, null);
			System.exit(0);
		}

	}

	private void connect() {

		try {
			socket = new Socket(server, port);

			// sets player ID first player = 1 second player = 2
			player.setPlayerID(input.readInt());

			// handles errors in separate thread
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());

			// calls on controller to start game creating new threads to serve each client/player
			Controller session = new Controller(player, input, output);
			build(session);
			new Thread(session).start();

			// catches errors and quits game if errors are present
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Unable to connect player, please try again", "Error",
					JOptionPane.ERROR_MESSAGE, null);
			System.exit(0);
		}
	}

	private void build(Controller controller) {

		// Creates new mouse listener and adds this to the controller
		MouseListener listener = new MouseListener();
		listener.addListener(controller);

		// Sets up a new draughts board and with a mouse listener
		draughtsBoard = new ClientView(listener);
		controller.setup(draughtsBoard);
		add(draughtsBoard);
	}
}
