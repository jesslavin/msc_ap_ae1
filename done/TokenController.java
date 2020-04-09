package done;

public class TokenController {

    private int tokenID;
    private int tokenRow;
    private int tokenColumn;
    private boolean king;
    private boolean empty;
    private boolean selected;
    private boolean movable;
    private int playerID;

    public TokenController(int tokenID, int tokenRow, int tokenColumn, boolean present) {
        this.tokenID = tokenID;
        this.tokenRow = tokenRow;
        this.tokenColumn = tokenColumn;
        this.Token(present);

        if (this.empty) {
            this.playerID = Constants.empty.getConstants();
        }

        this.king = false;
        this.selected = false;
        this.movable = false;
    }

    public boolean present() {
        return empty;
    }

    private void Token(boolean empty) {
        this.empty = empty;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayer() {
        return this.playerID;
    }

    public int getTokenID() {
        return this.tokenID;
    }

    public int getTokenRow() {
        return this.tokenRow;
    }

    public int getTokenColumn() {
        return this.tokenColumn;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean moveable() {
        return movable;
    }

    public void moveable(boolean moveable) {
        this.movable = moveable;
    }

    public boolean opponent() {
        return playerID != Variables.variable.getVariable() && playerID != Constants.empty.getConstants();
    }

    public boolean king() {
        return king;
    }

    public void makeKing() {
        this.king = true;
    }

    public void removeKing() {
        this.king = false;
    }
}
