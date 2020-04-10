// Jessica Lavin - 2495543L

import java.util.LinkedList;

public class BoardModel {

    // instance variables
    private final TokenController[][] tokens;

    // constructor
    public BoardModel() {
        tokens = new TokenController[8][8];
        setTokens();
        assignTokens();
    }

    // assigns players spaces on the board
    private void assignTokens() {

        // player one gets rows 0-2
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 8; c++)
                if (tokens[r][c].present()) tokens[r][c].setPlayerID(1);

        // player two gets rows 5-7
        for (int r = 5; r < 8; r++)
            for (int c = 0; c < 8; c++)
                if (tokens[r][c].present()) tokens[r][c].setPlayerID(2);
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
                existingToken = filled ? c % 2 == 0 : c % 2 == 1;
                i++;

                tokens[r][c] = new TokenController(i, r, c, existingToken);
            }
        }
    }

    // ends the game
    public boolean endPlay() {

        int white = 0;
        int black = 0;
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++) {
                if (tokens[r][c].getPlayer() == 1) white++;
                if (tokens[r][c].getPlayer() == 2) black++;
            }
        return white == 0 || black == 0;
    }

    // fetches individual tokens
    public TokenController getToken(final int from) {
        for (final TokenController[] tokenRow : tokens)
            for (final TokenController token : tokenRow)
                if (token.getTokenID() == from) return token;
        return null;
    }

    // returns this token
    public TokenController[][] getTokens() {
        return tokens;
    }

    // frontTokens, jump, opponentPresent and playableTokens check if the selected token can be moved and how it can be moved
    // multiple jump functionality unfulfilled
    // force jump functionality unfulfilled
    // draw functionality unfulfilled, if a player cannot make any legal moves can gets stuck on their turn
    // unhappy with final logic, feels bulky

    private void frontTokens(final LinkedList<TokenController> tokens, final int movable, final int selectedColumn) {

        if (movable < 0 || movable >= 8) {
            return;
        }
        if (selectedColumn < 0 || selectedColumn >= 7) {
        } else {
            final TokenController right = this.tokens[movable][selectedColumn + 1];
            if (right.getPlayer() == 0) {
                right.legalMove(true);
                tokens.add(right);
            }
        }

        if (selectedColumn <= 0 || selectedColumn > 8) return;
        final TokenController left = this.tokens[movable][selectedColumn - 1];
        if (left.getPlayer() != 0) return;
        left.legalMove(true);
        tokens.add(left);
    }


    private void jump(final LinkedList<TokenController> tokens, final int movable, final int selectedColumn, final int row) {

        int column;

        if (movable < 0 || movable >= 8) {
            return;
        }
        if (selectedColumn < 0 || selectedColumn >= 6) {
        } else {
            final TokenController right = this.tokens[movable][selectedColumn + 2];
            column = (selectedColumn + selectedColumn + 2) / 2;
            if (right.getPlayer() == 0 && opponentPresent(row, column)) {
                right.legalMove(true);
                tokens.add(right);
            }
        }

        if (selectedColumn <= 1 || selectedColumn > 7) {
            return;
        }
        final TokenController left = this.tokens[movable][selectedColumn - 2];
        column = (selectedColumn + selectedColumn - 2) / 2;
        if (left.getPlayer() == 0 && opponentPresent(row, column)) {
            left.legalMove(true);
            tokens.add(left);
        }
    }

    private boolean opponentPresent(final int row, final int column) {
        return tokens[row][column].opponent();
    }

    public LinkedList<TokenController> playableTokens(final TokenController selectedToken) {

        final LinkedList<TokenController> playableTokens = new LinkedList<>();

        final int selectedRow = selectedToken.getTokenRow();
        final int selectedColumn = selectedToken.getTokenColumn();

        int movable;
        if (selectedToken.getPlayer() != 1) {
            movable = selectedRow - 1;
        } else {
            movable = selectedRow + 1;
        }

        frontTokens(playableTokens, movable, selectedColumn);
        if (selectedToken.getPlayer() != 1) {
            jump(playableTokens, movable - 1, selectedColumn,
                    movable);
        } else {
            jump(playableTokens, movable + 1, selectedColumn,
                    movable);
        }
        if (!selectedToken.king()) {
            return playableTokens;
        }
        if (selectedToken.getPlayer() == 1) movable = selectedRow - 1;
        else movable = selectedRow + 1;
        frontTokens(playableTokens, movable, selectedColumn);
        jump(playableTokens, (selectedToken.getPlayer() == 1) ? movable - 1 : movable + 1, selectedColumn, movable);
        return playableTokens;
    }

}
