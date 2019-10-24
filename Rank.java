package assignment5;


public enum Rank {
    UNIQUE(10),
    RARE(5),
    UNCOMMON(2),
    COMMON(1);
	
	private int value;

	/*
	 * contains the value of each card
	*/
	Rank(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
}

