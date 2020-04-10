// Jessica Lavin - 2495543L

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
            this.playerID = 0;
        }

        this.king = false;
        this.selected = false;
        this.movable = false;
    }

    public int getPlayer() {
        return this.playerID;
    }

    public int getTokenColumn() {
        return this.tokenColumn;
    }

    public int getTokenID() {
        return this.tokenID;
    }

    public int getTokenRow() {
        return this.tokenRow;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean king() {
        return this.king;
    }

    public void makeKing() {
        this.king = true;
    }

    public boolean moveable() {
        return this.movable;
    }

    public void moveable(boolean moveable) { this.movable = moveable; }

    public boolean opponent() {
        return this.playerID != APlayerID.PlayerID.getVariable() && this.playerID != 0;
    }

    public boolean present() {
        return this.empty;
    }

    public void removeKing() {
        this.king = false;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    private void Token(boolean empty) {
        this.empty = empty;
    }
}
