// Jessica Lavin - 2495543L

import java.util.LinkedList;

public class BoardModel {

    // instance variables
    private TokenController[][] tokens;

    // constructor
    public BoardModel() {
        this.tokens = new TokenController[8][8];
        this.setTokens();
        this.assignTokens();
    }

    // assigns players spaces on the board
    private void assignTokens() {

        // player one gets rows 0-2
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 8; c++)
                if (this.tokens[r][c].present()) this.tokens[r][c].setPlayerID(1);

        // player two gets rows 5-7
        for (int r = 5; r < 8; r++)
            for (int c = 0; c < 8; c++)
                if (this.tokens[r][c].present()) this.tokens[r][c].setPlayerID(2);
    }

    // initialize 64 squares with ID, row, column whether or not they are filled with a token
    private void setTokens() {
        boolean filled, existingToken;
        int i = 0;

        // rows
        for (int r = 0; r < 8; r++) {
            filled = r % 2 == 1;

            // columns
            for (int c = 0; c < 8; c++) {
                existingToken = (filled && c % 2 == 0) || !filled && c % 2 == 1;
                i++;

                this.tokens[r][c] = new TokenController(i, r, c, existingToken);
            }
        }
    }

    // ends the game
    public boolean endPlay() {

        int white = 0;
        int black = 0;
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++) {
                if (this.tokens[r][c].getPlayer() == 1) white++;
                if (this.tokens[r][c].getPlayer() == 2) black++;
            }
        return white == 0 || black == 0;
    }

    // fetches individual tokens
    public TokenController getToken(int from) {
        for (TokenController[] tokenRow : this.tokens)
            for (TokenController token : tokenRow)
                if (token.getTokenID() == from) return token;
        return null;
    }

    // returns this token
    public TokenController[][] getTokens() {
        return this.tokens;
    }

    // frontTokens, jump, opponentPresent and playableTokens check if the selected token can be moved and how it can be moved
    // multiple jump functionality unfulfilled
    // force jump functionality unfulfilled
    // unhappy with final logic, feels bulky

    private void frontTokens(LinkedList<TokenController> tokens, int movable, int selectedColumn) {

        if (movable < 0 || movable >= 8) {
            return;
        }
        if (selectedColumn < 0 || selectedColumn >= 7) {
        } else {
            TokenController right = this.tokens[movable][selectedColumn + 1];
            if (right.getPlayer() == 0) {
                right.moveable(true);
                tokens.add(right);
            }
        }

        if (selectedColumn <= 0 || selectedColumn > 8) return;
        TokenController left = this.tokens[movable][selectedColumn - 1];
        if (left.getPlayer() != 0) return;
        left.moveable(true);
        tokens.add(left);
    }


    private void jump(LinkedList<TokenController> tokens, int movable, int selectedColumn, int row) {

        int column;

        if (movable < 0 || movable >= 8) {
            return;
        }
        if (selectedColumn < 0 || selectedColumn >= 6) {
        } else {
            TokenController right = this.tokens[movable][selectedColumn + 2];
            column = (selectedColumn + selectedColumn + 2) / 2;
            if (right.getPlayer() == 0 && this.opponentPresent(row, column)) {
                right.moveable(true);
                tokens.add(right);
            }
        }

        if (selectedColumn <= 1 || selectedColumn > 7) {
            return;
        }
        TokenController left = this.tokens[movable][selectedColumn - 2];
        column = (selectedColumn + selectedColumn - 2) / 2;
        if (left.getPlayer() == 0 && this.opponentPresent(row, column)) {
            left.moveable(true);
            tokens.add(left);
        }
    }

    private boolean opponentPresent(int row, int column) {
        return this.tokens[row][column].opponent();
    }

    public LinkedList<TokenController> playableTokens(TokenController selectedToken) {

        LinkedList<TokenController> playableTokens = new LinkedList<>();

        int selectedRow = selectedToken.getTokenRow();
        int selectedColumn = selectedToken.getTokenColumn();

        int movable;
        if (selectedToken.getPlayer() != 1) {
            movable = selectedRow - 1;
        } else {
            movable = selectedRow + 1;
        }

        this.frontTokens(playableTokens, movable, selectedColumn);
        if (selectedToken.getPlayer() != 1) {
            this.jump(playableTokens, movable - 1, selectedColumn,
                    movable);
        } else {
            this.jump(playableTokens, movable + 1, selectedColumn,
                    movable);
        }
        if (!selectedToken.king()) {
            return playableTokens;
        }
        if (selectedToken.getPlayer() == 1) movable = selectedRow - 1;
        else movable = selectedRow + 1;
        this.frontTokens(playableTokens, movable, selectedColumn);
        this.jump(playableTokens, (selectedToken.getPlayer() == 1) ? movable - 1 : movable + 1, selectedColumn, movable);
        return playableTokens;
    }

}
