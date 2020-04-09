package mvc_model;

import done.Constants;

import java.util.LinkedList;

public class BoardModel {

    private TokenModel[][] tokens;

    public BoardModel() {
        tokens = new TokenModel[8][8];
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

                tokens[r][c] = new TokenModel(i, r, c, existingToken);
            }
        }
    }

    public TokenModel[][] getTokens() {
        return this.tokens;
    }

    private void assignTokens() {

        // player one gets rows 0-2
        for (int r = 0; r < 3; r++) {
            // columns
            for (int c = 0; c < Constants.columns.getConstants(); c++) {
                if (tokens[r][c].filled()) {
                    tokens[r][c].setPlayerID(Constants.white.getConstants());
                }
            }
        }

        // player two gets rows 5-7
        for (int r = 5; r < 8; r++) {
            // columns
            for (int c = 0; c < Constants.columns.getConstants(); c++) {
                if (tokens[r][c].filled()) {
                    tokens[r][c].setPlayerID(Constants.black.getConstants());
                }
            }
        }
    }

    public TokenModel getToken(int from) {
        for (TokenModel[] sRows : tokens) {
            for (TokenModel s : sRows) {
                if (s.getTokenID() == from) {
                    return s;
                }

            }
        }
        return null;
    }

    public boolean isOver() {

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

    public LinkedList<TokenModel> getPlayable(TokenModel selectedToken) {

        LinkedList<TokenModel> playableTokens = new LinkedList<TokenModel>();

        int selectedRow = selectedToken.getTokenRow();
        int selectedColumn = selectedToken.getTokenColumn();

        int movableRow = (selectedToken.getPlayer() == 1) ? selectedRow + 1 : selectedRow - 1;

        // check two front tokens
        twoFrontTokens(playableTokens, movableRow, selectedColumn);
        crossJumpFront(playableTokens, (selectedToken.getPlayer() == 1) ? movableRow + 1 : movableRow - 1, selectedColumn, movableRow);
        if (selectedToken.king()) {
            movableRow = (selectedToken.getPlayer() == 1) ? selectedRow - 1 : selectedRow + 1;
            twoFrontTokens(playableTokens, movableRow, selectedColumn);
            crossJumpFront(playableTokens, (selectedToken.getPlayer() == 1) ? movableRow - 1 : movableRow + 1, selectedColumn, movableRow);
        }
        return playableTokens;
    }

    // check two front tokens
    private void twoFrontTokens(LinkedList<TokenModel> pack, int movableRow, int selectedCol) {

        if (movableRow >= 0 && movableRow < 8) {
            //right corner
            if (selectedCol >= 0 && selectedCol < 7) {
                TokenModel rightCorner = tokens[movableRow][selectedCol + 1];
                if (rightCorner.getPlayer() == 0) {
                    rightCorner.moveable(true);
                    pack.add(rightCorner);
                }
            }

            //left upper corner
            if (selectedCol > 0 && selectedCol <= 8) {
                TokenModel leftCorner = tokens[movableRow][selectedCol - 1];
                if (leftCorner.getPlayer() == 0) {
                    leftCorner.moveable(true);
                    pack.add(leftCorner);
                }
            }
        }
    }

    // cross jump - two front
    private void crossJumpFront(LinkedList<TokenModel> pack, int movableRow, int selectedCol, int middleRow) {

        int middleCol;

        if (movableRow >= 0 && movableRow < 8) {
            // right upper corner
            if (selectedCol >= 0 && selectedCol < 6) {
                TokenModel rightCorner = tokens[movableRow][selectedCol + 2];
                middleCol = (selectedCol + selectedCol + 2) / 2;
                if (rightCorner.getPlayer() == 0 && opponentPresent(middleRow, middleCol)) {
                    rightCorner.moveable(true);
                    pack.add(rightCorner);
                }
            }

            // left upper corner
            if (selectedCol > 1 && selectedCol <= 7) {
                TokenModel leftCorner = tokens[movableRow][selectedCol - 2];
                middleCol = (selectedCol + selectedCol - 2) / 2;
                if (leftCorner.getPlayer() == 0 && opponentPresent(middleRow, middleCol)) {
                    leftCorner.moveable(true);
                    pack.add(leftCorner);
                }
            }
        }
    }

    private boolean opponentPresent(int row, int column) {
        return tokens[row][column].opponentToken();
    }
}
