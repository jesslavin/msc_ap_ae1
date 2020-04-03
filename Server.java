import javax.swing.JFrame;

public class Server {

	public static void main(String[] args) {

		// creates a new server window
		ServerView server = new ServerView();

		// sets the size and visibly of server window
		server.setLocation(20, 20);
		server.setTitle("Game Server");
		server.setSize(500,250);
		server.setVisible(true);

		// stops server running when window is closed
		server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// starts connection to the server
		server.start();
	}
}
