// Jessica Lavin - 2495543L

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PlayerModel {

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private int player;
    private boolean currentTurn;

    public PlayerModel() {
        this.activePlayer(false);
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

    public int getPlayer() {
        return this.player;
    }

    public void setPlayer(int player) {
        this.player = player;
        PlayerID.PlayerID.setVariable(player);
    }

    public void activePlayer(boolean currentTurn) {
        this.currentTurn = currentTurn;
    }
}
