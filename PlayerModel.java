// Jessica Lavin - 2495543L

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PlayerModel {

    // instance variables
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private int player;
    private boolean currentTurn;

    // constructor
    public PlayerModel() {
        activePlayer(false);
    }

    // creates the socket and connects players
    public PlayerModel(final Socket socket) {
        this.socket = socket;

        try {
            input = new DataInputStream(this.socket.getInputStream());
            output = new DataOutputStream(this.socket.getOutputStream());

        } catch (final IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to connect player");
            System.exit(0);
        }
    }

    // handles player turns
    public boolean active() {
        return currentTurn;
    }

    // handles player connections
    public boolean connected() {
        return socket.isConnected();
    }

    // closes the connection
    public void closeConnection() {
        try {
            socket.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    // fetches and passes player input
    public int getInput() {
        int i = 0;
        try {
            i = input.readInt();
            return i;
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return i;
    }

    public int getOutput(final int i) {
        try {
            output.writeInt(i);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return i;
    }

    // fetches current player
    public int getPlayer() {
        return player;
    }

    // sets this player as the active player
    public void setPlayer(final int player) {
        this.player = player;
        PlayerID.PlayerID.setVariable(player);
    }

    public void activePlayer(final boolean currentTurn) {
        this.currentTurn = currentTurn;
    }
}
