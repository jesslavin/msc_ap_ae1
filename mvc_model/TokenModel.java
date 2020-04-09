package mvc_model;

import done.Constants;
import done.Variables;

public class TokenModel {

    private int TokenID;
    private int TokenRow;
    private int TokenColumn;
    private boolean isKing;
    private boolean space;
    private boolean selected;
    private boolean isPossibleToMove;
    private int playerID;

    public TokenModel(int TokenID, int TokenRow, int TokenColumn, boolean isFilled) {
        this.TokenID = TokenID;
        this.TokenRow = TokenRow;
        this.TokenColumn = TokenColumn;
        this.setSpace(isFilled);

        if (this.space) {
            this.playerID = Constants.empty.getConstants();
        }

        this.isKing = false;
        this.selected = false;
        this.isPossibleToMove = false;
    }

    public boolean filled() {
        return space;
    }

    private void setSpace(boolean space) {
        this.space = space;
    }

    public void setPlayerID(int ID) {
        this.playerID = ID;
    }

    public int getPlayer() {
        return this.playerID;
    }

    public int getTokenID() {
        return this.TokenID;
    }

    public int getTokenRow() {
        return this.TokenRow;
    }

    public int getTokenColumn() {
        return this.TokenColumn;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean playable() {
        return isPossibleToMove;
    }

    public void moveable(boolean isPossibleToMove) {
        this.isPossibleToMove = isPossibleToMove;
    }

    public boolean opponentToken() {
        return playerID != Variables.variable.getVariable() && playerID != Constants.empty.getConstants();
    }

    public boolean king() {
        return isKing;
    }

    public void makeKing() {
        this.isKing = true;
    }

    public void removeKing() {
        this.isKing = false;
    }
}
