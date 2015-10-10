package gameworld.entity;

import gameworld.location.Location;

import java.awt.Point;

public class LeatherArmour extends Armour {
	
	private static final int armourRating = 5;

	private static final long serialVersionUID = -632832829772054450L;

	public LeatherArmour(String name, String description, Point position,
			Location location) {
		super(name, description, position, location);
		setArmourRating(armourRating);
	}
}
