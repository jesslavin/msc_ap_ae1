public enum Constants {

	playerOne(1),
	playerTwo(2);
	
	private int value;
	
	Constants(int value) {
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}


}
