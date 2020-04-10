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
        this.activePlayer(false);
    }

    // creates the socket and connects players
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

    // handles player turns
    public boolean active() {
        return this.currentTurn;
    }

    // handles player connections
    public boolean connected() {
        return this.socket.isConnected();
    }

    // closes the connection
    public void closeConnection() {
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // fetches and passes player input
    public int getInput() {
        int i = 0;
        try {
            i = this.input.readInt();
            return i;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return i;
    }

    public int getOutput(int i) {
        try {
            this.output.writeInt(i);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return i;
    }

    // fetches current player
    public int getPlayer() {
        return this.player;
    }

    // sets this player as the active player
    public void setPlayer(int player) {
        this.player = player;
        PlayerID.PlayerID.setVariable(player);
    }

    public void activePlayer(boolean currentTurn) {
        this.currentTurn = currentTurn;
    }
}
