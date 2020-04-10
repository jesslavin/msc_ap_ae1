// Jessica Lavin - 2495543L

public enum PlayerID {

    PlayerID(0);

    private int ID;

    PlayerID(final int ID) {
        setVariable(ID);
    }

    public int getVariable() {
        return ID;
    }

    public void setVariable(final int PlayerID) {
        ID = PlayerID;
    }

}
