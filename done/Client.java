package done;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client extends JFrame {

    // game properties
    private PlayerModel player;
    private ClientView draughtsBoard;

    // network properties
    private String server;
    private int port;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public static void main(String[] args) {

        // creates a new client window
        Client client = new Client();

        // sets the size, location and visibly of client window
        client.setLocation(300, 0);
        client.setTitle("English Draughts");
        client.setSize(450, 450);
        client.setVisible(true);

        // stops client running when window is closed
        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public Client() {

        // fetches network properties and tries to connect each client/player
        try {
            ReadFile file = ReadFile.getProperty();
            server = file.getServer();
            port = file.getPort();
            player = new PlayerModel();
            connect();

            // catches errors and quits game if unable to connect
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unable to connect");
            System.exit(0);
        }

    }

    private void connect() {

        try {
            socket = new Socket(server, port);

            // handles errors in separate thread
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            // sets player ID first player = 1 second player = 2
            player.setPlayer(input.readInt());

            // calls on controller to start game creating new threads to serve each client/player
            DraughtsController session = new DraughtsController(player, input, output);
            build(session);
            new Thread(session).start();

            // catches errors and quits game if errors are present
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to connect");
            System.exit(0);
        }
    }

    private void build(DraughtsController c) {

        // Creates new mouse listener and adds this to the controller
        MouseListener listener = new MouseListener();
        listener.addListener(c);

        // Sets up a new draughts board and with a mouse listener
        draughtsBoard = new ClientView(listener);
        c.activeToken(draughtsBoard);
        add(draughtsBoard);
    }
}

