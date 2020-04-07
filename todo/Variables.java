package todo;

// declares list of variables used during game

public enum Variables {

    sessionVariable(0);

    private int v;

    Variables(int i) {
        this.setVariable(i);
    }

    public int getVariable() {
        return v;
    }

    public void setVariable(int i) {
        this.v = i;
    }

}
