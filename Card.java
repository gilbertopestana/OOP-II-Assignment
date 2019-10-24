package assignment5;

public class Card implements Comparable<Card>{
	long id;
	String name;
	Rank rank;
	long price;
	
	public Card (long id, String name, Rank rank) {
		this.id = id;
		this.name = name;
		this.rank = rank;
		this.price = 0;
	}

	@Override
	public String toString() {
		return "Card [id=" + id + ", name=" + name + ", rank=" + rank + ", price=" + price + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((rank == null) ? 0 : rank.hashCode());
		return result;
	}
	
	/*
	 * check if 2 object <Card> are equal by comparing their id, name and rank 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		Card card = (Card) obj;
		if (this.id != card.id)
			return false;
		if (this.name == null) {
			if (card.name != null)
				return false;
		} else if (!this.name.equals(card.name))
			return false;
		if (this.rank != card.rank)
			return false;
		return true;
	}
	
	/*
	 * Use to compare cards by RANK, name and id (in that specific order)
	 * so if two cards have the same Rank, they will be compare by their name
	 * and if name is equals as well they will be compare by their ID's
	 * < is represented by a -1
	 * = is represented by a 0
	 * > is represented by a 1
	 */
	@Override
	public int compareTo(Card other) {
		int result = 0;
		if (result == 0 && this.rank.getValue() > other.rank.getValue())
			result = 1;
		else
			result = (result == 0 && this.rank.getValue() < other.rank.getValue())? -1 : 0;
		if (result == 0)
			result = this.name.compareTo(other.name);
		if (result == 0)
			result = this.id>other.id? 1:(this.id<other.id?-1:0);
		return result;
	}


	
	
}
