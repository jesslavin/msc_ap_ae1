package model;

import lists.Variables;

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

    public PlayerModel(Socket socket) {
        this.socket = socket;

        try {
            input = new DataInputStream(this.socket.getInputStream());
            output = new DataOutputStream(this.socket.getOutputStream());

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to connect player, please try again", "Error",
                    JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
    }

    public PlayerModel() {
        setCurrentTurn(false);

    }

    public int getPlayer() {
        return player;
    }


    public void setPlayer(int player) {
        this.player = player;
        Variables.sessionVariable.setVariable(player);
    }


    public boolean whosTurn() {
        return currentTurn;
    }


    public void setCurrentTurn(boolean currentTurn) {
        this.currentTurn = currentTurn;
    }

    public int getOutput(int i) {
        try {
            this.output.writeInt(i);
            return 1;
            // Connection successful
        } catch (IOException e) {
            System.out.println("Player not found");
            return 99;
            // Connection failure
        }
    }

    public int getInput() {
        int i = 0;
        try {
            i = this.input.readInt();
            return i;
            // Connection successful
        } catch (IOException e) {
            System.out.println("No response");
            return 99;
            // Connection failure
        }
    }

    public void closeConnection() {
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean connected() {
        return socket.isConnected();
    }
}
