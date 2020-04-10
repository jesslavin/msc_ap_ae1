// Jessica Lavin - 2495543L

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client extends JFrame {

    // instance variables
    private PlayerModel player;
    private ClientView draughtsBoard;
    private String server;
    private int port;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    // constructor
    public Client() {
        // fetches network properties and tries to connect each client/player
        try {
            server = Configuration.getServer();
            port = Configuration.getPort();
            player = new PlayerModel();
            connect();
            // catches errors and quits game if unable to connect
        } catch (final Exception e) {
            JOptionPane.showMessageDialog(null, "Unable to connect");
            System.exit(0);
        }

    }

    public static void main(final String[] args) {

        // creates a new client window
        final Client client = new Client();

        // sets the size, location and visibly of client window
        client.setLocation(300, 0);
        client.setTitle("English Draughts");
        client.setSize(450, 450);
        client.setVisible(true);

    }

    private void build(final DraughtsController c) {

        // Creates new mouse listener and adds this to the controller
        final MouseListener listener = new MouseListener();
        listener.newController(c);

        // Sets up a new draughts board and with a mouse listener
        draughtsBoard = new ClientView(listener);
        c.activeToken(draughtsBoard);
        add(draughtsBoard);
    }

    private void connect() {

        try {
            socket = new Socket(server, port);

            // handles errors in separate thread
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            // sets player ID white = 1 black = 2
            player.setPlayer(input.readInt());

            // calls on controller to start game creating new threads to serve each client/player
            final DraughtsController session = new DraughtsController(player, input, output);
            build(session);
            new Thread(session).start();

            // catches errors and quits game if errors are present
        } catch (final IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to connect");
            System.exit(0);
        }
    }

}
