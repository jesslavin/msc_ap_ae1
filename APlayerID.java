// Jessica Lavin - 2495543L

public enum APlayerID {

    PlayerID(0);

    private int ID;

    APlayerID(int ID) {
        this.setVariable(ID);
    }

    public int getVariable() {
        return this.ID;
    }

    public void setVariable(int PlayerID) {
        this.ID = PlayerID;
    }

}
