// Jessica Lavin - 2495543L

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;

// class that acts as a communicator between the view and model classes

public class DraughtsController implements Runnable {

    // instance variables
    private boolean continuePlay;
    private boolean waitForAction;
    private boolean endPlay;
    private final DataInputStream from;
    private final DataOutputStream to;
    private ClientView token;
    private final PlayerModel player;
    private final LinkedList<TokenController> selectedTokens;
    private LinkedList<TokenController> playableTokens;

    // constructor
    public DraughtsController(final PlayerModel player, final DataInputStream input, final DataOutputStream output) {
        this.player = player;
        from = input;
        to = output;
        selectedTokens = new LinkedList<TokenController>();
        playableTokens = new LinkedList<TokenController>();
    }

    // returns the currently active player
    public boolean activePlayer() {
        return player.active();
    }

    // returns the currently active token
    public void activeToken(final ClientView token) {
        this.token = token;
    }

    // check whether the players move jumped over an opponents token
    private void checkJump(final TokenController from, final TokenController to) {

        if (Math.abs(from.getTokenRow() - to.getTokenRow()) == 2) {
            final int r = (from.getTokenRow() + to.getTokenRow()) / 2;
            final int c = (from.getTokenColumn() + to.getTokenColumn()) / 2;

            // empties square with jumped token in
            final TokenController jumpedToken = token.getToken((r * 8) + c + 1);
            jumpedToken.setPlayerID(0);
            jumpedToken.removeKing();
        }
    }

    // check whether the players token has reach kings row and become king
    private void checkKing(final TokenController from, final TokenController tokenPlayed) {
        if (!from.king()) if (tokenPlayed.getTokenRow() != 7 || tokenPlayed.getPlayer() != 1) {
            if (tokenPlayed.getTokenRow() == 0 && tokenPlayed.getPlayer() == 2) tokenPlayed.makeKing();
        } else {
            tokenPlayed.makeKing();
        }
        else {
            tokenPlayed.makeKing();
            from.removeKing();
        }
    }

    // fetches player tokens
    private void getPlayableTokens(final TokenController t) {
        playableTokens.clear();
        playableTokens = token.playableToken(t);
        token.play();
    }

    // allows player to make their move, check if this move is a jump, check whether token has become king, deselect token
    public void makeMove(final TokenController from, final TokenController to) {
        to.setPlayerID(from.getPlayer());
        from.setPlayerID(0);
        checkJump(from, to);
        checkKing(from, to);
        deselectToken();

        // ends player's turn
        waitForAction = false;
        try {
            sendMove(from, to);
        } catch (final IOException e) {
            System.out.println("Unable to make move");
        }
    }

    public void run() {
        continuePlay = true;
        waitForAction = true;
        endPlay = false;

        // player 1 (white) goes first
        try {
            if (player.getPlayer() == 1) {
                from.readInt();
                player.activePlayer(true);
            }

            // while game is in progress allow players to make turns via serverInfo method
            if (continuePlay && !endPlay) {
                do if (player.getPlayer() == 1) {
                    waitForPlayerAction();
                    if (!endPlay) {
                        serverInfo();
                    }
                } else if (player.getPlayer() == 2) {
                    serverInfo();
                    if (!endPlay) {
                        waitForPlayerAction();
                    }
                }
                while (continuePlay && !endPlay);
            }

            // if game over display "game over" message and asks player if they'd like to start a new game
            if (endPlay) {
                final int option = JOptionPane.showConfirmDialog(
                        null,
                        "Game Over, Would you like to play again?",
                        "Game Over",
                        JOptionPane.YES_NO_OPTION);
                // opens two new client windows for each player (can't manage to get it to force close other windows - sorry)
                if (option != JOptionPane.NO_OPTION) {
                    if (option == JOptionPane.YES_OPTION) {
                        final Client client = new Client();
                        client.setLocation(300, 0);
                        client.setTitle("English Draughts");
                        client.setSize(450, 450);
                        client.setVisible(true);
                    }
                } else {
                    System.exit(0);
                }
            }
        } catch (final IOException | InterruptedException e) {
            JOptionPane.showMessageDialog(null, "Connection lost");
            System.exit(0);
        }
    }

    // handles the selection and deselection of tokens
    private void selected(final TokenController t) {
        t.setSelected(true);
        selectedTokens.add(t);
        getPlayableTokens(t);
    }

    // select token logic
    public void selectToken(final TokenController t) {
        if (!selectedTokens.isEmpty()) {
            if (selectedTokens.size() >= 1)
                if (!playableTokens.contains(t)) {
                    deselectToken();
                    selected(t);
                } else {
                    makeMove(selectedTokens.getFirst(), t);
                }
        } else {
            selected(t);
        }
    }

    // deselect token logic
    public void deselectToken() {
        for (final TokenController token : selectedTokens) token.setSelected(false);
        selectedTokens.clear();
        for (final TokenController token : playableTokens) token.legalMove(false);
        playableTokens.clear();
        token.play();
    }

    // fetches and sends player moves
    private void sendMove(final TokenController from, final TokenController to) throws IOException {
        this.to.writeInt(from.getTokenID());
        this.to.writeInt(to.getTokenID());
    }

    // passes turn information to and from ending the game when winner/loser
    private void serverInfo() throws IOException {
        player.activePlayer(false);
        int from = this.from.readInt();
        if (from != 0) {
            if (from == 1) {
                endPlay = true;
                continuePlay = false;
            } else {
                final int to = this.from.readInt();
                update(from, to);
            }
        } else {
            from = this.from.readInt();
            final int to = this.from.readInt();
            update(from, to);
            endPlay = true;
        }
    }

    // updates game board
    private void update(final int from, final int to) {
        final TokenController fromToken = token.getToken(from);
        final TokenController toToken = token.getToken(to);
        toToken.setPlayerID(fromToken.getPlayer());
        fromToken.setPlayerID(0);
        checkJump(fromToken, toToken);
        checkKing(fromToken, toToken);
        token.play();
    }

    // sends thread to sleep while waiting for player action
    private void waitForPlayerAction() throws InterruptedException {
        player.activePlayer(true);
        while (waitForAction) {
            Thread.sleep(100);
        }
        waitForAction = true;
    }
}
