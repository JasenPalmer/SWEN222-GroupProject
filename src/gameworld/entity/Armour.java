package gameworld.entity;

import gameworld.Player;
import gameworld.location.Location;

import java.awt.Point;

public class Armour extends Item {
	
	private static final long serialVersionUID = 690195706473427168L;
	
	private ArmourType armourType;

	public Armour(String name, String description, Point position,
			Location location, ArmourType armourType) {
		super(name, description, position, location);
		this.armourType = armourType;
	}

	private Armour(Armour a) {
		super(a.name, a.description, a.position, a.location);
		this.armourType = a.armourType;
	}

	public int getArmourRating() {
		if(armourType == null) {
			return 0;
		}
		return armourType.getArmourRating();
	}
	
	public ArmourType getType() {
		return armourType;
	}

	@Override
	public void interact(Player player) {
		
	}
	
	@Override
	public Item clone() {
		return new Armour(this);
	}
	
	public enum ArmourType {
		Robe(2),
		Leather(5),
		Chain(10),
		Plate(15);
		
		private final int rating;
		private ArmourType(int damage) {
			this.rating = damage;
		}
		
		public int getArmourRating() {
			return rating;
		}
	}
}
