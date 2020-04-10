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
        this.selectedTokens = new LinkedList<TokenController>();
        this.playableTokens = new LinkedList<TokenController>();
    }

    // returns the currently active player
    public boolean activePlayer() {
        return this.player.active();
    }

    // returns the currently active token
    public void activeToken(ClientView token) {
        this.token = token;
    }

    // check whether the players move jumped over an opponents token
    private void checkJump(TokenController from, TokenController to) {

        if (Math.abs(from.getTokenRow() - to.getTokenRow()) == 2) {
            int r = (from.getTokenRow() + to.getTokenRow()) / 2;
            int c = (from.getTokenColumn() + to.getTokenColumn()) / 2;
            
            // empties square with jumped token in
            TokenController jumpedToken = this.token.getToken((r * 8) + c + 1);
            jumpedToken.setPlayerID(0);
            jumpedToken.removeKing();
        }
    }

    // check whether the players token has reach kings row and become king
    private void checkKing(TokenController from, TokenController tokenPlayed) {
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

    public void deselectToken() {
        for (TokenController token : this.selectedTokens) token.setSelected(false);
        this.selectedTokens.clear();
        for (TokenController token : this.playableTokens) token.moveable(false);
        this.playableTokens.clear();
        this.token.play();
    }

    private void getPlayableTokens(TokenController t) {
        this.playableTokens.clear();
        this.playableTokens = this.token.playableToken(t);
        this.token.play();
    }

    // allows player to make their move, check if this move is a jump, check whether token has become king, deselect token
    public void makeMove(TokenController from, TokenController to) {
        to.setPlayerID(from.getPlayer());
        from.setPlayerID(0);
        this.checkJump(from, to);
        this.checkKing(from, to);
        this.deselectToken();

        // ends player's turn
        this.waitForAction = false;
        try {
            this.sendMove(from, to);
        } catch (IOException e) {
            System.out.println("Unable to make move");
        }
    }

    public void run() {
        this.continuePlay = true;
        this.waitForAction = true;
        this.endPlay = false;

        // player 1 (white) goes first
        try {
            if (this.player.getPlayer() == 1) {
                this.from.readInt();
                this.player.activePlayer(true);
            }

            // while game is in progress allow players to make turns via serverInfo method
            if (this.continuePlay && !this.endPlay) {
                do if (this.player.getPlayer() == 1) {
                    this.waitForPlayerAction();
                    if (!this.endPlay) {
                        this.serverInfo();
                    }
                } else if (this.player.getPlayer() == 2) {
                    this.serverInfo();
                    if (!this.endPlay) {
                        this.waitForPlayerAction();
                    }
                }
                while (this.continuePlay && !this.endPlay);
            }

            // if game over display "game over" message and asks player if they'd like to start a new game
            if (this.endPlay) {
                int option = JOptionPane.showConfirmDialog(
                        null,
                        "Game Over, Would you like to play again?",
                        "Game Over",
                        JOptionPane.YES_NO_OPTION);
                // opens two new client windows for each player (can't manage to get it to force close other windows - sorry)
                if (option != JOptionPane.NO_OPTION) {
                    if (option == JOptionPane.YES_OPTION) {
                        Client client = new Client();
                        client.setLocation(300, 0);
                        client.setTitle("English Draughts");
                        client.setSize(450, 450);
                        client.setVisible(true);
                    }
                } else {
                    System.exit(0);
                }
            }
        } catch(IOException | InterruptedException e){
                JOptionPane.showMessageDialog(null, "Connection lost");
                System.exit(0);
        }
    }

    // handles the selection and deselection of tokens
    private void selected(TokenController t) {
        t.setSelected(true);
        this.selectedTokens.add(t);
        this.getPlayableTokens(t);
    }

    public void selectToken(TokenController t) {
        if (!this.selectedTokens.isEmpty()) {
            if (this.selectedTokens.size() >= 1)
                if (!this.playableTokens.contains(t)) {
                this.deselectToken();
                this.selected(t);
            } else {
                this.makeMove(this.selectedTokens.getFirst(), t);
            }
        } else {
            this.selected(t);
        }
    }

    // fetches and sends player moves
    private void sendMove(TokenController from, TokenController to) throws IOException {
        this.to.writeInt(from.getTokenID());
        this.to.writeInt(to.getTokenID());
    }

    // passes turn information to and from ending the game when winner/loser
    private void serverInfo() throws IOException {
        this.player.activePlayer(false);
        int from = this.from.readInt();
        if (from != 0) {
            if (from == 1) {
                this.endPlay = true;
                this.continuePlay = false;
            } else {
                int to = this.from.readInt();
                this.update(from, to);
            }
        } else {
            from = this.from.readInt();
            int to = this.from.readInt();
            this.update(from, to);
            this.endPlay = true;
        }
    }

    // updates game board
    private void update(int from, int to) {
        TokenController fromToken = this.token.getToken(from);
        TokenController toToken = this.token.getToken(to);
        toToken.setPlayerID(fromToken.getPlayer());
        fromToken.setPlayerID(0);
        this.checkJump(fromToken, toToken);
        this.checkKing(fromToken, toToken);
        this.token.play();
    }

    // sends thread to sleep while waiting for player action
    private void waitForPlayerAction() throws InterruptedException {
        this.player.activePlayer(true);
        while (this.waitForAction) {
            Thread.sleep(100);
        }
        this.waitForAction = true;
    }
}
