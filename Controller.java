import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class Controller implements Runnable {

    private boolean continueToPlay;
    private boolean waitingForAction;
    private boolean isOver;
    private DataInputStream fromServer;
    private DataOutputStream toServer;
    private Board boardSquare;
    private PlayerModel player;
    private LinkedList<TokenModel> selectedTokens;
    private LinkedList<TokenModel> playableTokens;

    public Controller(PlayerModel player, DataInputStream input, DataOutputStream output) {
        this.player = player;
        this.fromServer = input;
        this.toServer = output;

        selectedTokens = new LinkedList<TokenModel>();
        playableTokens = new LinkedList<TokenModel>();
    }

    public void setup(Board square) {
        this.boardSquare = square;
    }

    public void run() {
        continueToPlay = true;
        waitingForAction = true;
        isOver = false;

        try {

            if (player.getPlayer() == Constants.white.getConstants()) {
                fromServer.readInt();
                player.setCurrentTurn(true);
            }

            while (continueToPlay && !isOver) {
                if (player.getPlayer() == Constants.white.getConstants()) {
                    waitForPlayerAction();
                    if (!isOver)
                        receiveInfoFromServer();
                } else if (player.getPlayer() == Constants.black.getConstants()) {
                    receiveInfoFromServer();
                    if (!isOver)
                        waitForPlayerAction();
                }
            }

            if (isOver) {
                JOptionPane.showMessageDialog(null, "done.Game is over",
                        "Information", JOptionPane.INFORMATION_MESSAGE, null);
                System.exit(0);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Connection lost",
                    "Error", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        } catch (InterruptedException e) {
            JOptionPane.showMessageDialog(null, "Connection interrupted",
                    "Error", JOptionPane.ERROR_MESSAGE, null);
        }
    }

    private void receiveInfoFromServer() throws IOException {
        player.setCurrentTurn(false);
        int from = fromServer.readInt();
        if (from == Constants.loser.getConstants()) {
            from = fromServer.readInt();
            int to = fromServer.readInt();
            updateReceivedInfo(from, to);
            isOver = true;
        } else if (from == Constants.winner.getConstants()) {
            isOver = true;
            continueToPlay = false;
        } else {
            int to = fromServer.readInt();
            updateReceivedInfo(from, to);
        }
    }

    private void sendMove(TokenModel from, TokenModel to) throws IOException {
        toServer.writeInt(from.getTokenID());
        toServer.writeInt(to.getTokenID());
    }

    private void waitForPlayerAction() throws InterruptedException {
        player.setCurrentTurn(true);
        while (waitingForAction) {
            Thread.sleep(100);
        }
        waitingForAction = true;
    }

    public void move(TokenModel from, TokenModel to) {
        to.setPlayerID(from.getPlayer());
        from.setPlayerID(Constants.empty.getConstants());
        checkCrossJump(from, to);
        checkKing(from, to);
        tokenDeselected();

        waitingForAction = false;
        try {
            sendMove(from, to);
        } catch (IOException e) {
            System.out.println("Sending failed");
        }
    }

    public void tokenSelected(TokenModel s) {
        if (selectedTokens.isEmpty()) {
            addToSelected(s);
        } else if (selectedTokens.size() >= 1) {
            if (playableTokens.contains(s)) {
                move(selectedTokens.getFirst(), s);
            } else {
                tokenDeselected();
                addToSelected(s);
            }
        }
    }

    private void addToSelected(TokenModel token) {
        token.setSelected(true);
        selectedTokens.add(token);
        getPlayableTokens(token);
    }

    public void tokenDeselected() {

        for (TokenModel token : selectedTokens)
            token.setSelected(false);

        selectedTokens.clear();

        for (TokenModel token : playableTokens) {
            token.setPossibleToMove(false);
        }

        playableTokens.clear();
        boardSquare.activateSquare();
    }


    private void getPlayableTokens(TokenModel s) {
        playableTokens.clear();
        playableTokens = boardSquare.getPlayable(s);
        boardSquare.activateSquare();
    }

    public boolean currentTurn() {
        return player.whosTurn();
    }

    private void checkCrossJump(TokenModel from, TokenModel to) {
        if (Math.abs(from.getTokenRow() - to.getTokenRow()) == 2) {
            int middleRow = (from.getTokenRow() + to.getTokenRow()) / 2;
            int middleCol = (from.getTokenColumn() + to.getTokenColumn()) / 2;

            TokenModel middleToken = boardSquare.getToken((middleRow * 8) + middleCol + 1);
            middleToken.setPlayerID(Constants.empty.getConstants());
            middleToken.removeKing();
        }
    }

    private void checkKing(TokenModel from, TokenModel movedToken) {
        if (from.isKing()) {
            movedToken.setKing();
            from.removeKing();
        } else if (movedToken.getTokenRow() == 7 && movedToken.getPlayer() == Constants.white.getConstants()) {
            movedToken.setKing();
        } else if (movedToken.getTokenRow() == 0 && movedToken.getPlayer() == Constants.black.getConstants()) {
            movedToken.setKing();
        }
    }

    private void updateReceivedInfo(int from, int to) {
        TokenModel fromToken = boardSquare.getToken(from);
        TokenModel toToken = boardSquare.getToken(to);
        toToken.setPlayerID(fromToken.getPlayer());
        fromToken.setPlayerID(Constants.empty.getConstants());
        checkCrossJump(fromToken, toToken);
        checkKing(fromToken, toToken);
        boardSquare.activateSquare();
    }
}
