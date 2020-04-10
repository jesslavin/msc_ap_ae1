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
            this.server = Configuration.getServer();
            this.port = Configuration.getPort();
            this.player = new PlayerModel();
            this.connect();
            // catches errors and quits game if unable to connect
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unable to connect");
            System.exit(0);
        }

    }


    private void build(DraughtsController c) {

        // Creates new mouse listener and adds this to the controller
        MouseListener listener = new MouseListener();
        listener.newController(c);

        // Sets up a new draughts board and with a mouse listener
        this.draughtsBoard = new ClientView(listener);
        c.activeToken(this.draughtsBoard);
        this.add(this.draughtsBoard);
    }

    private void connect() {

        try {
            this.socket = new Socket(this.server, this.port);

            // handles errors in separate thread
            this.input = new DataInputStream(this.socket.getInputStream());
            this.output = new DataOutputStream(this.socket.getOutputStream());

            // sets player ID first player = 1 second player = 2
            this.player.setPlayer(this.input.readInt());

            // calls on controller to start game creating new threads to serve each client/player
            DraughtsController session = new DraughtsController(this.player, this.input, this.output);
            this.build(session);
            new Thread(session).start();

            // catches errors and quits game if errors are present
            } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to connect");
            System.exit(0);
        }
    }

    public static void main(String[] args) {

        // creates a new client window
        Client client = new Client();

        // sets the size, location and visibly of client window
        client.setLocation(300, 0);
        client.setTitle("English Draughts");
        client.setSize(450, 450);
        client.setVisible(true);

        // stops client running when window is closed
        client.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

}
