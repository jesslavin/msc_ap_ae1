import java.awt.*;
import javax.swing.*;
import java.io.DataOutputStream;
import java.net.*;

public class XServerView extends JFrame {
	
	// create frame components
	private JPanel contentPanel;
	private JTextArea textArea;
	
	// sets network properties
	private ServerSocket socket;

	// formats elements and adds them to server window
	public XServerView(){
		contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		textArea = new JTextArea();
		contentPanel.setBackground(Color.BLACK);
		textArea.setBackground(Color.BLACK);
		textArea.setEnabled(false);
		add(contentPanel);
		contentPanel.add(textArea);
	}	
	
	// establishes a connection and waits for clients to join
	public void start(){
		try {
			XFetchProperties properties = XFetchProperties.fetchInstance();
			int port = properties.fetchPort();
			
			// creates a new server socket
			socket = new ServerSocket(port);
			textArea.append("Server started at port "+ port + " \n");
			
			while(true){
				
				// waits for first client to join server
				Socket clientOne = socket.accept();
				textArea.append("First player joined successfully at ");
				textArea.append(clientOne.getInetAddress().getHostAddress() + "\n");
				textArea.append("Waiting for second player... \n");

				// waits for second client to join server
				Socket clientTwo = socket.accept();
				textArea.append("Second player joined successfully at ");
				textArea.append(clientTwo.getInetAddress().getHostAddress() +"\n");
				textArea.append("Starting game... \n");

				// opens game windows for each player
				new DataOutputStream(clientOne.getOutputStream()).writeInt(Constants.playerOne.getValue());
				new DataOutputStream(clientTwo.getOutputStream()).writeInt(Constants.playerTwo.getValue());
				
				// creates a new thread for this session of two players
				SessionHandler thisSession = new SessionHandler(clientOne, clientTwo);
				new Thread(thisSession).start();
			}
			// catches connection errors and quits the server
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}				
	}
}
