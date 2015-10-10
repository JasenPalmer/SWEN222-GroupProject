package gameworld.entity;

import gameworld.location.Location;

import java.awt.Point;

public class RobeArmour extends Armour {

	private static final long serialVersionUID = -5265672396845909846L;
	
	private static final int armourRating = 2;

	public RobeArmour(String name, String description, Point position,
			Location location) {
		super(name, description, position, location);
		setArmourRating(armourRating);
	}

}
