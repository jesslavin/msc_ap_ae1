// Jessica Lavin - 2495543L

public enum PlayerID {

    PlayerID(0);

    private int ID;

    PlayerID(int ID) {
        this.setVariable(ID);
    }

    public int getVariable() {
        return this.ID;
    }

    public void setVariable(int PlayerID) {
        this.ID = PlayerID;
    }

}
