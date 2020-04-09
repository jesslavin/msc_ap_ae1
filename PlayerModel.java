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
            JOptionPane.showMessageDialog(null, "Unable to connect player");
            System.exit(0);
        }
    }

    public PlayerModel() {
        setTurn(false);

    }

    public int getPlayer() {
        return player;
    }


    public void setPlayer(int player) {
        this.player = player;
        Variables.variable.setVariable(player);
    }


    public boolean active() {
        return currentTurn;
    }


    public void setTurn(boolean currentTurn) {
        this.currentTurn = currentTurn;
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
