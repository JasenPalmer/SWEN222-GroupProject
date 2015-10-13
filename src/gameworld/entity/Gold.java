package gameworld.entity;

import java.awt.Point;

import gameworld.Player;
import gameworld.location.Location;

public class Gold extends Item {

	private static final long serialVersionUID = -9194662540726500047L;
	
	private int amount;

	public Gold(String name, String description, Point position,
			Location location, int amount) {
		super(name, description, position, location);
		this.amount = amount;
	}
	
	private Gold(Gold g) {
		super(g.name, g.description, g.position, g.location);
		this.amount = g.amount;
	}

	@Override
	public void interact(Player player) {

	}

	@Override
	public Item clone() {
		return new Gold(this);
	}



	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

}
