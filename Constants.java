public enum XConstants {

	// two players
	playerOne(1),
	playerTwo(2),

	// winner and a loser
	winner(1),
	loser(0),

	// eight rows and columns on the board
	rows(8),
	columns(8),

	// empty squares are not used
	empty(0);
	
	private int con;
	XConstants(int con) {
		this.con = con;
	}
	
	public int fetchCon(){
		return this.con;
	}


}
