package gameworld.entity;

import java.awt.Point;

import gameworld.Player;
import gameworld.location.Location;

/**
 * Gold is used to represent the players score. 
 * The more gold a player has the higher the score
 * @author Jasen
 *
 */
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
	public String getDescription() {
		return "Gold: "+amount;
	}

	@Override
	public void interact(Player player) {
	}

	@Override
	public Item clone() {
		return new Gold(this);
	}

	/**
	 * @return the amount of gold in the stack
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * set the amount of gold in the stack
	 * @param amount of gold to set the stack to
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

}
