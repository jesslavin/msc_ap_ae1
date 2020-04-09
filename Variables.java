// declares list of variables used during game

public enum Variables {

    variable(0);

    private int v;

    Variables(int v) {
        this.setVariable(v);
    }

    public int getVariable() {
        return v;
    }

    public void setVariable(int i) {
        this.v = i;
    }

}
