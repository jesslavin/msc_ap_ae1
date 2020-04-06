import java.util.LinkedList;

public class Board {

    private TokenBuild[][] tokens;

    public Board() {
        tokens = new TokenBuild[8][8];

        setTokens();
        assignTokens();
    }

    private void setTokens() {
        boolean rowInitialFilled, isFilled;
        int count = 0;

        // rows
        for (int rows = 0; rows < Constants.rows.getConstants(); rows++) {
            rowInitialFilled = rows % 2 == 1;

            // columns
            for (int columns = 0; columns < Constants.columns.getConstants(); columns++) {
                isFilled = (rowInitialFilled && columns % 2 == 0) || !rowInitialFilled && columns % 2 == 1;
                count++;

                tokens[rows][columns] = new TokenBuild(count, rows, columns, isFilled);
            }
        }
    }

    public TokenBuild[][] getTokens() {
        return this.tokens;
    }

    public int totalTokens() {
        return tokens.length;
    }

    private void assignTokens() {

        // player one gets rows 0-2
        for (int r = 0; r < 3; r++) {
            // columns
            for (int c = 0; c < Constants.columns.getConstants(); c++) {
                if (tokens[r][c].getFilled()) {
                    tokens[r][c].setPlayerID(Constants.playerOne.getConstants());
                }
            }
        }

        // player two gets rows 5-7
        for (int r = 5; r < 8; r++) {
            // columns
            for (int c = 0; c < Constants.columns.getConstants(); c++) {
                if (tokens[r][c].getFilled()) {
                    tokens[r][c].setPlayerID(Constants.playerTwo.getConstants());
                }
            }
        }
    }

    public LinkedList<TokenBuild> getPlayable(TokenBuild selectedToken) {

        LinkedList<TokenBuild> playableTokens = new LinkedList<TokenBuild>();

        int selectedRow = selectedToken.getTokenRow();
        int selectedColumn = selectedToken.getTokenColumn();

        int movableRow = (selectedToken.getPlayer() == 1) ? selectedRow + 1 : selectedRow - 1;

        // check two front tokens
        twoFrontTokens(playableTokens, movableRow, selectedColumn);
        crossJumpFront(playableTokens, (selectedToken.getPlayer() == 1) ? movableRow + 1 : movableRow - 1, selectedColumn, movableRow);
        if (selectedToken.isKing()) {
            movableRow = (selectedToken.getPlayer() == 1) ? selectedRow - 1 : selectedRow + 1;
            twoFrontTokens(playableTokens, movableRow, selectedColumn);
            crossJumpFront(playableTokens, (selectedToken.getPlayer() == 1) ? movableRow - 1 : movableRow + 1, selectedColumn, movableRow);
        }
        return playableTokens;
    }

    // check two front tokens
    private void twoFrontTokens(LinkedList<TokenBuild> pack, int movableRow, int selectedCol) {

        if (movableRow >= 0 && movableRow < 8) {
            //right corner
            if (selectedCol >= 0 && selectedCol < 7) {
                TokenBuild rightCorner = tokens[movableRow][selectedCol + 1];
                if (rightCorner.getPlayer() == 0) {
                    rightCorner.setPossibleToMove(true);
                    pack.add(rightCorner);
                }
            }

            //left upper corner
            if (selectedCol > 0 && selectedCol <= 8) {
                TokenBuild leftCorner = tokens[movableRow][selectedCol - 1];
                if (leftCorner.getPlayer() == 0) {
                    leftCorner.setPossibleToMove(true);
                    pack.add(leftCorner);
                }
            }
        }
    }

    // cross jump - two front
    private void crossJumpFront(LinkedList<TokenBuild> pack, int movableRow, int selectedCol, int middleRow) {

        int middleCol;

        if (movableRow >= 0 && movableRow < 8) {
            // right upper corner
            if (selectedCol >= 0 && selectedCol < 6) {
                TokenBuild rightCorner = tokens[movableRow][selectedCol + 2];
                middleCol = (selectedCol + selectedCol + 2) / 2;
                if (rightCorner.getPlayer() == 0 && opponentPresent(middleRow, middleCol)) {
                    rightCorner.setPossibleToMove(true);
                    pack.add(rightCorner);
                }
            }

            // left upper corner
            if (selectedCol > 1 && selectedCol <= 7) {
                TokenBuild leftCorner = tokens[movableRow][selectedCol - 2];
                middleCol = (selectedCol + selectedCol - 2) / 2;
                if (leftCorner.getPlayer() == 0 && opponentPresent(middleRow, middleCol)) {
                    leftCorner.setPossibleToMove(true);
                    pack.add(leftCorner);
                }
            }
        }
    }

    private boolean opponentPresent(int row, int column) {
        return tokens[row][column].opponentToken();
    }
}
