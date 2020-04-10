// Jessica Lavin - 2495543L

// class that acts as a controllers token logic communicating with the views and models

public class TokenController {

    // instance variables
    private final int tokenID;
    private final int tokenRow;
    private final int tokenColumn;
    private boolean king;
    private boolean empty;
    private boolean selected;
    private boolean movable;
    private int playerID;

    // constructor
    public TokenController(final int tokenID, final int tokenRow, final int tokenColumn, final boolean present) {
        this.tokenID = tokenID;
        this.tokenRow = tokenRow;
        this.tokenColumn = tokenColumn;
        Token(present);

        if (empty) {
            playerID = 0;
        }

        king = false;
        selected = false;
        movable = false;
    }

    public int getPlayer() {
        return playerID;
    }

    public int getTokenColumn() {
        return tokenColumn;
    }

    public int getTokenID() {
        return tokenID;
    }

    public int getTokenRow() {
        return tokenRow;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(final boolean selected) {
        this.selected = selected;
    }

    public boolean king() {
        return king;
    }

    public void makeKing() {
        king = true;
    }

    public boolean movable() {
        return movable;
    }

    public void legalMove(final boolean movable) {
        this.movable = movable;
    }

    public boolean opponent() {
        return playerID != PlayerID.PlayerID.getVariable() && playerID != 0;
    }

    public boolean present() {
        return empty;
    }

    public void removeKing() {
        king = false;
    }

    public void setPlayerID(final int playerID) {
        this.playerID = playerID;
    }

    private void Token(final boolean empty) {
        this.empty = empty;
    }
}
