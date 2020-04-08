package done;

import view.ClientBuild;

import javax.swing.*;

public class Client {

    public static void main(String[] args) {

        // creates a new client window
        ClientBuild client = new ClientBuild();

        // sets the size, location and visibly of client window
        client.setLocation(300, 0);
        client.setTitle("English Draughts");
        client.setSize(450, 450);
        client.setVisible(true);

        // stops client running when window is closed
        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
