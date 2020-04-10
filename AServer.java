// Jessica Lavin - 2495543L

import javax.swing.*;

public class AServer {

    public static void main(String[] args) {

        // creates a new server window
        ServerView server = new ServerView();

        // sets the size, location and visibly of server window
        server.setLocation(0, 0);
        server.setTitle("Game Server");
        server.setSize(300, 200);
        server.setVisible(true);

        // stops server running when window is closed
        server.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // starts connection to the server
        server.start();
    }
}
