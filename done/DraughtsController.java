package done;

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
    private DataInputStream from;
    private DataOutputStream to;
    private ClientView token;
    private PlayerModel player;
    private LinkedList<TokenController> selectedTokens;
    private LinkedList<TokenController> playableTokens;

    // constructor
    public DraughtsController(PlayerModel player, DataInputStream input, DataOutputStream output) {
        this.player = player;
        this.from = input;
        this.to = output;
        selectedTokens = new LinkedList<TokenController>();
        playableTokens = new LinkedList<TokenController>();
    }

    // returns the currently active token
    public void activeToken(ClientView token) {
        this.token = token;
    }

    // returns the currently active player
    public boolean activePlayer() {
        return player.active();
    }

    public void run() {
        continuePlay = true;
        waitForAction = true;
        endPlay = false;

        // player 1 (white) goes first
        try {
            if (player.getPlayer() == Constants.white.getConstants()) {
                from.readInt();
                player.setTurn(true);
            }

            // while game is in progress allow players to make turns via serverInfo method
            while (continuePlay && !endPlay) {
                if (player.getPlayer() == Constants.white.getConstants()) {
                    waitForPlayerAction();
                    if (!endPlay)
                        serverInfo();
                } else if (player.getPlayer() == Constants.black.getConstants()) {
                    serverInfo();
                    if (!endPlay)
                        waitForPlayerAction();
                }
            }

            // if game over display "game over" message
            if (endPlay) {
                JOptionPane.showMessageDialog(null, "Game over");
                System.exit(0);
            }

        } catch (IOException | InterruptedException e) {
            JOptionPane.showMessageDialog(null, "Connection lost");
            System.exit(0);
        }
    }

    // updates game board
    private void update(int from, int to) {
        TokenController fromToken = token.getToken(from);
        TokenController toToken = token.getToken(to);
        toToken.setPlayerID(fromToken.getPlayer());
        fromToken.setPlayerID(Constants.empty.getConstants());
        checkJump(fromToken, toToken);
        checkKing(fromToken, toToken);
        token.play();
    }

    // passes turn information to and from ending the game when winner/loser
    private void serverInfo() throws IOException {
        player.setTurn(false);
        int from = this.from.readInt();
        if (from == Constants.loser.getConstants()) {
            from = this.from.readInt();
            int to = this.from.readInt();
            update(from, to);
            endPlay = true;
        } else if (from == Constants.winner.getConstants()) {
            endPlay = true;
            continuePlay = false;
        } else {
            int to = this.from.readInt();
            update(from, to);
        }
    }


    // sends thread to sleep while waiting for player action
    private void waitForPlayerAction() throws InterruptedException {
        player.setTurn(true);
        while (waitForAction) {
            Thread.sleep(100);
        }
        waitForAction = true;
    }

    // allows player to make their move, check if this move is a jump, check whether token has become king, deselect token
    public void makeMove(TokenController from, TokenController to) {
        to.setPlayerID(from.getPlayer());
        from.setPlayerID(Constants.empty.getConstants());
        checkJump(from, to);
        checkKing(from, to);
        deselectToken();

        // ends player's turn
        waitForAction = false;
        try {
            sendMove(from, to);
        } catch (IOException e) {
            System.out.println("Unable to make move");
        }
    }

    // handles the selection and deselection of tokens
    private void selected(TokenController t) {
        t.setSelected(true);
        selectedTokens.add(t);
        getPlayableTokens(t);
    }

    private void getPlayableTokens(TokenController t) {
        playableTokens.clear();
        playableTokens = token.playableToken(t);
        token.play();
    }

    public void selectToken(TokenController t) {
        if (selectedTokens.isEmpty()) {
            selected(t);
        } else if (selectedTokens.size() >= 1) {
            if (playableTokens.contains(t)) {
                makeMove(selectedTokens.getFirst(), t);
            } else {
                deselectToken();
                selected(t);
            }
        }
    }

    public void deselectToken() {
        for (TokenController token : selectedTokens) token.setSelected(false);
        selectedTokens.clear();
        for (TokenController token : playableTokens) { token.moveable(false); }
        playableTokens.clear();
        token.play();
    }

    // fetches and sends player moves
    private void sendMove(TokenController from, TokenController to) throws IOException {
        this.to.writeInt(from.getTokenID());
        this.to.writeInt(to.getTokenID());
    }

    // check whether the players move jumped over an opponents token
    private void checkJump(TokenController from, TokenController to) {
        if (Math.abs(from.getTokenRow() - to.getTokenRow()) == 2) {
            int r = (from.getTokenRow() + to.getTokenRow()) / 2;
            int c = (from.getTokenColumn() + to.getTokenColumn()) / 2;

            // empties square with jumped token in
            TokenController jumpedToken = token.getToken((r * 8) + c + 1);
            jumpedToken.setPlayerID(Constants.empty.getConstants());
            jumpedToken.removeKing();
        }
    }

    // check whether the players token has reach kings row and become king
    private void checkKing(TokenController from, TokenController tokenPlayed) {
        if (from.king()) {
            tokenPlayed.makeKing();
            from.removeKing();
        } else if (tokenPlayed.getTokenRow() == 7 && tokenPlayed.getPlayer() == Constants.white.getConstants()) {
            tokenPlayed.makeKing();
        } else if (tokenPlayed.getTokenRow() == 0 && tokenPlayed.getPlayer() == Constants.black.getConstants()) {
            tokenPlayed.makeKing();
        }
    }
}
