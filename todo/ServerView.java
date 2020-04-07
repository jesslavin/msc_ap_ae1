package todo;

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
        jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        textArea = new JTextArea();
        jPanel.setBackground(Color.BLACK);
        textArea.setBackground(Color.BLACK);
        textArea.setEnabled(false);
        add(jPanel);
        jPanel.add(textArea);
    }

    // establishes a connection and waits for clients to join
    public void start() {
        try {
            ReadFile properties = ReadFile.getProperty();
            int port = properties.getPort();

            // creates a new server socket
            socket = new ServerSocket(port);
            textArea.append("done.Server started at port " + port + " \n");

            while (true) {

                // waits for first client to join server
                Socket clientOne = socket.accept();
                textArea.append("First player joined successfully at ");
                textArea.append(clientOne.getInetAddress().getHostAddress() + "\n");
                textArea.append("Waiting for second player... \n");

                // DELETE LATER
                new DataOutputStream(clientOne.getOutputStream()).writeInt(Constants.white.getConstants());

                // waits for second client to join server
                Socket clientTwo = socket.accept();
                textArea.append("Second player joined successfully at ");
                textArea.append(clientTwo.getInetAddress().getHostAddress() + "\n");
                textArea.append("Starting game... \n");

                // opens game windows for each player
                // ADD BACK IN new DataOutputStream(clientOne.getOutputStream()).writeInt(Constants.playerOne.getConstants());
                new DataOutputStream(clientTwo.getOutputStream()).writeInt(Constants.black.getConstants());

                // creates a new thread for this session of two players
                Socket socketOne = clientOne;
                Socket socketTwo = clientTwo;
                Runnable thisSession = new Runnable() {
                    private Game draughts = new Game();
                    private ServerPlayer playerOne = new ServerPlayer(socketOne);
                    private ServerPlayer playerTwo = new ServerPlayer(socketTwo);
                    private boolean continuePlay = true;

                    public void run() {
                        try {
                            playerOne.getOutput(1);
                            while (continuePlay) {
                                // wait for player one to make their move
                                int from = playerOne.getInput();
                                int to = playerOne.getInput();
                                // update board accordingly
                                pass(from, to);
                                updateBoard(from, to);

                                // send this data to player two
                                if (draughts.isOver())
                                    // notifies game is over
                                    playerTwo.getOutput(Constants.loser.getConstants());
                                int getData = playerTwo.getOutput(from);
                                int sendData = playerTwo.getOutput(to);
                                pass(getData, sendData);

                                // if game is over, break out
                                if (draughts.isOver()) {
                                    playerOne.getOutput(Constants.winner.getConstants());
                                    continuePlay = false;
                                    break;
                                }

                                // wait for player two to make their move
                                from = playerTwo.getInput();
                                to = playerTwo.getInput();
                                // update board accordingly
                                pass(from, to);
                                updateBoard(from, to);

                                // send this data to player one
                                if (draughts.isOver()) {
                                    // notifies game is over
                                    playerOne.getOutput(Constants.loser.getConstants());
                                }
                                getData = playerOne.getOutput(from);
                                sendData = playerOne.getOutput(to);
                                pass(getData, sendData);

                                // if game is over, break out
                                if (draughts.isOver()) {
                                    playerTwo.getOutput(Constants.winner.getConstants());
                                    continuePlay = false;
                                    break;
                                }
                            }

                        } catch (Exception e) {
                            System.out.println("Connection closed");

                            if (playerOne.connected())
                                playerOne.closeConnection();

                            if (playerTwo.connected())
                                playerTwo.closeConnection();

                            return;
                        }
                    }

                    // passes data throwing exception when the connection is lost
                    private void pass(int to, int from) throws Exception {
                        if (to == 99 || from == 99) {
                            throw new Exception("Connection lost");
                        }
                    }

                    private void updateBoard(int from, int to) {
                        TokenBuild fromToken = draughts.getToken(from);
                        TokenBuild toToken = draughts.getToken(to);
                        toToken.setPlayerID(fromToken.getPlayer());
                        fromToken.setPlayerID(Constants.empty.getConstants());

                        crossJump(fromToken, toToken);
                    }

                    private void crossJump(TokenBuild from, TokenBuild to) {
                        if (Math.abs(from.getTokenRow() - to.getTokenRow()) == 2) {
                            int middleRow = (from.getTokenRow() + to.getTokenRow()) / 2;
                            int middleColumn = (from.getTokenColumn() + to.getTokenColumn()) / 2;

                            TokenBuild middleToken = draughts.getToken((middleRow * 8) + middleColumn + 1);
                            middleToken.setPlayerID(Constants.empty.getConstants());
                        }
                    }
                };
                new Thread(thisSession).start();
            }
            // catches connection errors and quits the server
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
