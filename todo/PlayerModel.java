package todo;

public class PlayerModel {

    private int player;
    private boolean currentTurn;

    public PlayerModel() {
        setCurrentTurn(false);
    }

    public int getPlayer() {
        return player;
    }


    public void setPlayer(int player) {
        this.player = player;
        Variables.sessionVariable.setVariable(player);
    }


    public boolean whosTurn() {
        return currentTurn;
    }


    public void setCurrentTurn(boolean currentTurn) {
        this.currentTurn = currentTurn;
    }


}
