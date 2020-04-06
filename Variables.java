public enum Variables {

	sessionVariable(0);

	private int var;

	Variables(int i){
		this.setVariable(i);
	}

	public int getVariable() {
		return var;
	}

	public void setVariable(int i) {
		this.var = i;
	}
	
}
