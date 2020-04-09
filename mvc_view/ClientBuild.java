package mvc_view;

import done.ReadFile;
import mvc_controller.DraughtsController;
import mvc_model.PlayerModel;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientBuild extends JFrame {

    // game properties
    private PlayerModel player;
    private ClientView draughtsBoard;

    // network properties
    private String server;
    private int port;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public ClientBuild() {

        // fetches network properties and tries to connect each client/player
        try {
            ReadFile file = ReadFile.getProperty();
            server = file.getServer();
            port = file.getPort();
            player = new PlayerModel();
            connect();

            // catches errors and quits game if unable to connect
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unable to connect, please check configuration", "Error",
                    JOptionPane.ERROR_MESSAGE, null);
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
            JOptionPane.showMessageDialog(null, "Unable to connect player, please try again", "Error",
                    JOptionPane.ERROR_MESSAGE, null);
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
