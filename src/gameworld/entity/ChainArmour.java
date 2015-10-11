package gameworld.entity;

import gameworld.location.Location;

import java.awt.Point;

public class ChainArmour extends Armour {
	
	private static final long serialVersionUID = 4943373424970348945L;
	
	private static final int armourRating = 10;

	public ChainArmour(String name, String description, Point position,
			Location location) {
		super(name, description, position, location);
		setArmourRating(armourRating);
	}
	
	private ChainArmour(ChainArmour amour) {
		super(amour.name, amour.description, amour.position, amour.location);
		setArmourRating(armourRating);
	}

	@Override
	public Item clone() {
		return new ChainArmour(this);
	}

}
