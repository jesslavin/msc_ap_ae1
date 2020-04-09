package done;

import done.Constants;
import done.TokenController;

import java.util.LinkedList;

public class BoardModel {

    private TokenController[][] tokens;

    public BoardModel() {
        tokens = new TokenController[8][8];
        setTokens();
        assignTokens();
    }

    // initialize 64 squares with ID, row, column whether or not they are filled with a token
    private void setTokens() {
        boolean filled, existingToken;
        int i = 0;

        // rows
        for (int r = 0; r < Constants.rows.getConstants(); r++) {
            filled = r % 2 == 1;

            // columns
            for (int c = 0; c < Constants.columns.getConstants(); c++) {
                existingToken = (filled && c % 2 == 0) || !filled && c % 2 == 1;
                i++;

                tokens[r][c] = new TokenController(i, r, c, existingToken);
            }
        }
    }

    public TokenController[][] getTokens() {
        return this.tokens;
    }

    private void assignTokens() {

        // player one gets rows 0-2
        for (int r = 0; r < 3; r++) {
            // columns
            for (int c = 0; c < Constants.columns.getConstants(); c++) {
                if (tokens[r][c].present()) {
                    tokens[r][c].setPlayerID(Constants.white.getConstants());
                }
            }
        }

        // player two gets rows 5-7
        for (int r = 5; r < 8; r++) {
            // columns
            for (int c = 0; c < Constants.columns.getConstants(); c++) {
                if (tokens[r][c].present()) {
                    tokens[r][c].setPlayerID(Constants.black.getConstants());
                }
            }
        }
    }

    // fetches individual tokens
    public TokenController getToken(int from) {
        for (TokenController[] tokenRow : tokens) {
            for (TokenController token : tokenRow) {
                if (token.getTokenID() == from) {
                    return token;
                }

            }
        }
        return null;
    }

    // playableTokens, frontTokens, jump and opponentPresent check if the selected token can be moved and how it can be moved

    public LinkedList<TokenController> playableTokens(TokenController selectedToken) {

        LinkedList<TokenController> playableTokens = new LinkedList<>();

        int selectedRow = selectedToken.getTokenRow();
        int selectedColumn = selectedToken.getTokenColumn();

        int movable = (selectedToken.getPlayer() == 1) ? selectedRow + 1 : selectedRow - 1;

        frontTokens(playableTokens, movable, selectedColumn);
        jump(playableTokens, (selectedToken.getPlayer() == 1) ? movable + 1 : movable - 1, selectedColumn, movable);
        if (selectedToken.king()) {
            movable = (selectedToken.getPlayer() == 1) ? selectedRow - 1 : selectedRow + 1;
            frontTokens(playableTokens, movable, selectedColumn);
            jump(playableTokens, (selectedToken.getPlayer() == 1) ? movable - 1 : movable + 1, selectedColumn, movable);
        }
        return playableTokens;
    }

    private void frontTokens(LinkedList<TokenController> tokens, int movable, int selectedColumn) {

        if (movable >= 0 && movable < 8) {
            if (selectedColumn >= 0 && selectedColumn < 7) {
                TokenController right = this.tokens[movable][selectedColumn + 1];
                if (right.getPlayer() == 0) {
                    right.moveable(true);
                    tokens.add(right);
                }
            }

            if (selectedColumn > 0 && selectedColumn <= 8) {
                TokenController left = this.tokens[movable][selectedColumn - 1];
                if (left.getPlayer() == 0) {
                    left.moveable(true);
                    tokens.add(left);
                }
            }
        }
    }

    private void jump(LinkedList<TokenController> tokens, int movable, int selectedColumn, int row) {

        int column;

        if (movable >= 0 && movable < 8) {
            if (selectedColumn >= 0 && selectedColumn < 6) {
                TokenController right = this.tokens[movable][selectedColumn + 2];
                column = (selectedColumn + selectedColumn + 2) / 2;
                if (right.getPlayer() == 0 && opponentPresent(row, column)) {
                    right.moveable(true);
                    tokens.add(right);
                }
            }

            if (selectedColumn > 1 && selectedColumn <= 7) {
                TokenController left = this.tokens[movable][selectedColumn - 2];
                column = (selectedColumn + selectedColumn - 2) / 2;
                if (left.getPlayer() == 0 && opponentPresent(row, column)) {
                    left.moveable(true);
                    tokens.add(left);
                }
            }
        }
    }

    private boolean opponentPresent(int row, int column) {
        return tokens[row][column].opponent();
    }

    // ends the game
    public boolean endPlay() {

        int white = 0;
        int black = 0;
        for (int r = 0; r < Constants.rows.getConstants(); r++) {
            for (int c = 0; c < Constants.columns.getConstants(); c++) {
                if (tokens[r][c].getPlayer() == 1)
                    white++;

                if (tokens[r][c].getPlayer() == 2)
                    black++;
            }
        }

        return white == 0 || black == 0;
    }

}


