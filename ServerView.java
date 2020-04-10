// Jessica Lavin - 2495543L

import javax.swing.*;
import java.awt.*;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerView extends JFrame {

    // create frame components
    private final JPanel jPanel;
    private final JTextArea textArea;

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
            final int port = Configuration.getPort();

            // creates a new server socket
            socket = new ServerSocket(port);
            textArea.append("Server started at port " + port + " \n");

            while (true) {

                // waits for first client to join server
                final Socket clientOne = socket.accept();
                textArea.append("First player joined successfully at ");
                textArea.append(clientOne.getInetAddress().getHostAddress() + "\n");
                textArea.append("Waiting for second player... \n");

                // waits for second client to join server
                final Socket clientTwo = socket.accept();
                textArea.append("Second player joined successfully at ");
                textArea.append(clientTwo.getInetAddress().getHostAddress() + "\n");
                textArea.append("Starting game... \n");

                // opens game windows for each player
                new DataOutputStream(clientOne.getOutputStream()).writeInt(1);
                new DataOutputStream(clientTwo.getOutputStream()).writeInt(2);

                // creates a new thread for this session of two players
                final Socket socketOne = clientOne;
                final Socket socketTwo = clientTwo;
                final Runnable thisSession = new Runnable() {
                    private final BoardModel draughts = new BoardModel();
                    private final PlayerModel white = new PlayerModel(socketOne);
                    private final PlayerModel black = new PlayerModel(socketTwo);
                    private boolean continuePlay = true;

                    // passes data throwing exception when the connection is lost
                    private void pass(final int to, final int from) throws Exception {
                        while (to == 99 || from == 99) {
                            throw new Exception("Connection lost");
                        }
                    }

                    public void run() {
                        try {
                            white.getOutput(1);
                            while (continuePlay) {
                                // wait for player one to make their move
                                int from = white.getInput();
                                int to = white.getInput();
                                // update board accordingly
                                pass(from, to);
                                updateBoard(from, to);

                                // send this data to player two
                                if (!draughts.endPlay()) {
                                } else {
                                    // notifies game is over
                                    black.getOutput(0);
                                }
                                int get = black.getOutput(from);
                                int send = black.getOutput(to);
                                pass(get, send);

                                if (!draughts.endPlay()) {
                                    // wait for player two to make their move
                                    from = black.getInput();
                                    to = black.getInput();
                                    // update board accordingly
                                    pass(from, to);
                                    updateBoard(from, to);

                                    // send this data to player one
                                    if (draughts.endPlay()) {
                                        // notifies game is over
                                        white.getOutput(0);
                                    }
                                    get = white.getOutput(from);
                                    send = white.getOutput(to);
                                    pass(get, send);

                                    // if game is over, break out
                                    if (draughts.endPlay()) {
                                        black.getOutput(1);
                                        continuePlay = false;
                                        break;
                                    }
                                } else {
                                    white.getOutput(1);
                                    continuePlay = false;
                                    break;
                                }

                            }

                        } catch (final Exception e) {
                            System.out.println("Connection closed");

                            if (white.connected()) white.closeConnection();

                            if (black.connected()) black.closeConnection();

                            return;
                        }
                    }

                    // method to remove token from board when taken by opposing player
                    private void takeToken(final TokenController from, final TokenController to) {
                        if (Math.abs(from.getTokenRow() - to.getTokenRow()) == 2) {
                            final int row = (from.getTokenRow() + to.getTokenRow()) / 2;
                            final int column = (from.getTokenColumn() + to.getTokenColumn()) / 2;
                            final TokenController middleToken = draughts.getToken((row * 8) + column + 1);
                            middleToken.setPlayerID(0);
                        }
                    }

                    // updates the board after each move
                    private void updateBoard(final int from, final int to) {
                        final TokenController fromToken = draughts.getToken(from);
                        final TokenController toToken = draughts.getToken(to);
                        toToken.setPlayerID(fromToken.getPlayer());
                        fromToken.setPlayerID(0);
                        takeToken(fromToken, toToken);
                    }
                };
                // starts a new thread to handle particular session
                new Thread(thisSession).start();
            }

        } catch (final Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
