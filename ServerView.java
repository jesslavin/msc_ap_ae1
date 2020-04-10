import javax.swing.*;
import java.awt.*;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerView extends JFrame {

    // create frame components
    private JPanel jPanel;
    private JTextArea textArea;

    // sets network properties
    private ServerSocket socket;

    // formats elements and adds them to server window
    public ServerView() {
        this.jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.textArea = new JTextArea();
        this.jPanel.setBackground(Color.BLACK);
        this.textArea.setBackground(Color.BLACK);
        this.textArea.setEnabled(false);
        this.add(this.jPanel);
        this.jPanel.add(this.textArea);
    }

    // establishes a connection and waits for clients to join
    public void start() {
        try {
            ReadFile properties = ReadFile.getProperty();
            int port = properties.getPort();

            // creates a new server socket
            this.socket = new ServerSocket(port);
            this.textArea.append("Server started at port " + port + " \n");

            while (true) {

                // waits for first client to join server
                Socket clientOne = this.socket.accept();
                this.textArea.append("First player joined successfully at ");
                this.textArea.append(clientOne.getInetAddress().getHostAddress() + "\n");
                this.textArea.append("Waiting for second player... \n");

                // MOVE LATER
                new DataOutputStream(clientOne.getOutputStream()).writeInt(1);

                // waits for second client to join server
                Socket clientTwo = this.socket.accept();
                this.textArea.append("Second player joined successfully at ");
                this.textArea.append(clientTwo.getInetAddress().getHostAddress() + "\n");
                this.textArea.append("Starting game... \n");

                // opens game windows for each player
                new DataOutputStream(clientTwo.getOutputStream()).writeInt(2);

                // creates a new thread for this session of two players
                Socket socketOne = clientOne;
                Socket socketTwo = clientTwo;
                Runnable thisSession = new Runnable() {
                    private BoardModel draughts = new BoardModel();
                    private PlayerModel white = new PlayerModel(socketOne);
                    private PlayerModel black = new PlayerModel(socketTwo);
                    private boolean continuePlay = true;

                    // passes data throwing exception when the connection is lost
                    private void pass(int to, int from) throws Exception {
                        if (to == 99 || from == 99) {
                            throw new Exception("Connection lost");
                        }
                    }

                    @Override
                    public void run() {
                        try {
                            this.white.getOutput(1);
                            while (this.continuePlay) {
                                // wait for player one to make their move
                                int from = this.white.getInput();
                                int to = this.white.getInput();
                                // update board accordingly
                                this.pass(from, to);
                                this.updateBoard(from, to);

                                // send this data to player two
                                if (this.draughts.endPlay()) {
                                    // notifies game is over
                                    this.black.getOutput(0);
                                }
                                int get = this.black.getOutput(from);
                                int send = this.black.getOutput(to);
                                this.pass(get, send);

                                // if game is over, break out
                                if (this.draughts.endPlay()) {
                                    this.white.getOutput(1);
                                    this.continuePlay = false;
                                    break;
                                }

                                // wait for player two to make their move
                                from = this.black.getInput();
                                to = this.black.getInput();
                                // update board accordingly
                                this.pass(from, to);
                                this.updateBoard(from, to);

                                // send this data to player one
                                if (this.draughts.endPlay()) {
                                    // notifies game is over
                                    this.white.getOutput(0);
                                }
                                get = this.white.getOutput(from);
                                send = this.white.getOutput(to);
                                this.pass(get, send);

                                // if game is over, break out
                                if (this.draughts.endPlay()) {
                                    this.black.getOutput(1);
                                    this.continuePlay = false;
                                    break;
                                }
                            }

                        } catch (Exception e) {
                            System.out.println("Connection closed");

                            if (this.white.connected()) {
                                this.white.closeConnection();
                            }

                            if (this.black.connected()) {
                                this.black.closeConnection();
                            }

                            return;
                        }
                    }

                    // method to remove token from board when taken by opposing player
                    private void takeToken(TokenController from, TokenController to) {
                        if (Math.abs(from.getTokenRow() - to.getTokenRow()) == 2) {
                            int middleRow = (from.getTokenRow() + to.getTokenRow()) / 2;
                            int middleColumn = (from.getTokenColumn() + to.getTokenColumn()) / 2;
                            TokenController middleToken = this.draughts.getToken((middleRow * 8) + middleColumn + 1);
                            middleToken.setPlayerID(0);
                        }
                    }

                    // updates the board after each move
                    private void updateBoard(int from, int to) {
                        TokenController fromToken = this.draughts.getToken(from);
                        TokenController toToken = this.draughts.getToken(to);
                        toToken.setPlayerID(fromToken.getPlayer());
                        fromToken.setPlayerID(0);
                        this.takeToken(fromToken, toToken);
                    }
                };
                // starts a new thread to handle particular session
                new Thread(thisSession).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
