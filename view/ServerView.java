package view;

import filehandler.ReadFile;
import lists.Constants;
import model.BoardModel;
import model.PlayerModel;
import model.TokenModel;

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
            textArea.append("main.Server started at port " + port + " \n");

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
                // ADD BACK IN new DataOutputStream(clientOne.getOutputStream()).writeInt(lists.Constants.playerOne.getConstants());
                new DataOutputStream(clientTwo.getOutputStream()).writeInt(Constants.black.getConstants());

                // creates a new thread for this session of two players
                Socket socketOne = clientOne;
                Socket socketTwo = clientTwo;
                Runnable thisSession = new Runnable() {
                    private BoardModel draughts = new BoardModel();
                    private PlayerModel white = new PlayerModel(socketOne);
                    private PlayerModel black = new PlayerModel(socketTwo);
                    private boolean continuePlay = true;

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
                                if (draughts.isOver())
                                    // notifies game is over
                                    black.getOutput(Constants.loser.getConstants());
                                int getData = black.getOutput(from);
                                int sendData = black.getOutput(to);
                                pass(getData, sendData);

                                // if game is over, break out
                                if (draughts.isOver()) {
                                    white.getOutput(Constants.winner.getConstants());
                                    continuePlay = false;
                                    break;
                                }

                                // wait for player two to make their move
                                from = black.getInput();
                                to = black.getInput();
                                // update board accordingly
                                pass(from, to);
                                updateBoard(from, to);

                                // send this data to player one
                                if (draughts.isOver()) {
                                    // notifies game is over
                                    white.getOutput(Constants.loser.getConstants());
                                }
                                getData = white.getOutput(from);
                                sendData = white.getOutput(to);
                                pass(getData, sendData);

                                // if game is over, break out
                                if (draughts.isOver()) {
                                    black.getOutput(Constants.winner.getConstants());
                                    continuePlay = false;
                                    break;
                                }
                            }

                        } catch (Exception e) {
                            System.out.println("Connection closed");

                            if (white.connected())
                                white.closeConnection();

                            if (black.connected())
                                black.closeConnection();

                            return;
                        }
                    }

                    // passes data throwing exception when the connection is lost
                    private void pass(int to, int from) throws Exception {
                        if (to == 99 || from == 99) {
                            throw new Exception("Connection lost");
                        }
                    }

                    // updates the board after each move
                    private void updateBoard(int from, int to) {
                        TokenModel fromToken = draughts.getToken(from);
                        TokenModel toToken = draughts.getToken(to);
                        toToken.setPlayerID(fromToken.getPlayer());
                        fromToken.setPlayerID(Constants.empty.getConstants());
                        takeToken(fromToken, toToken);
                    }

                    // method to remove token from board when taken by opposing player
                    private void takeToken(TokenModel from, TokenModel to) {
                        if (Math.abs(from.getTokenRow() - to.getTokenRow()) == 2) {
                            int middleRow = (from.getTokenRow() + to.getTokenRow()) / 2;
                            int middleColumn = (from.getTokenColumn() + to.getTokenColumn()) / 2;
                            TokenModel middleToken = draughts.getToken((middleRow * 8) + middleColumn + 1);
                            middleToken.setPlayerID(Constants.empty.getConstants());
                        }
                    }
                };
                new Thread(thisSession).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
