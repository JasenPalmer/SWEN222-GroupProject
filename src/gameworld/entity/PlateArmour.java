package gameworld.entity;

import gameworld.location.Location;

import java.awt.Point;

public class PlateArmour extends Armour {

	private static final long serialVersionUID = -7669837385362357936L;
	
	private static final int armourRating = 15;

	public PlateArmour(String name, String description, Point position,
			Location location) {
		super(name, description, position, location);
		setArmourRating(armourRating);
	}
	
	private PlateArmour(PlateArmour armour) {
		super(armour.name, armour.description, armour.position, armour.location);
		setArmourRating(armourRating);
	}

	@Override
	public Item clone() {
		return new PlateArmour(this);
	}

}
