package gameworld.entity;

import gameworld.Player;
import gameworld.location.Location;

import java.awt.Point;

public abstract class Armour extends Item {
	
	private static final long serialVersionUID = 690195706473427168L;
	
	private int armourRating = 0;

	public Armour(String name, String description, Point position,
			Location location) {
		super(name, description, position, location);
	}

	public int getArmourRating() {
		return armourRating;
	}

	public void setArmourRating(int armourRating) {
		this.armourRating = armourRating;
	}

	@Override
	public void interact(Player player) {
		
	}
}
