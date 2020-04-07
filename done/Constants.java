package done;

// declares list of variables used during game

public enum Constants {

    white(1), // player 1 (white)
    black(2), // player 2 (black)
    winner(1),
    loser(0),
    rows(8),
    columns(8),
    empty(0);

    private int con;

    Constants(int i) {
        this.con = i;
    }

    public int getConstants() {
        return this.con;
    }

}