public enum SessionVariable {

	sessionVariable(0);

	private int var;

	SessionVariable(int i){
		this.setVariable(i);
	}

	public int getVariable() {
		return var;
	}

	public void setVariable(int i) {
		this.var = i;
	}
	
}
