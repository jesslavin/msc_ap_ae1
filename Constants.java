public enum Constants {

	playerOne(1),
	playerTwo(2),
	winner(1),
	loser(0),
	rows(8),
	columns(8),
	empty(0);
	
	private int value;
	
	Constants(int value) {
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}


}
