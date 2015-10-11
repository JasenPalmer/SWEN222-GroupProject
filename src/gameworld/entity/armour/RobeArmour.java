package gameworld.entity.armour;

import gameworld.entity.Item;
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
	
	private RobeArmour(RobeArmour armour) {
		super(armour.name, armour.description, armour.position, armour.location);
		setArmourRating(armourRating);
	}

	@Override
	public Item clone() {
		return new RobeArmour(this);
	}

}