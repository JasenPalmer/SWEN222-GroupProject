package gameworld.entity;

import gameworld.Player;
import gameworld.location.Location;

import java.awt.Point;

/**
 * Armour can be equipped by a player can will reduce the amount of damage the player takes from an attack
 * according to the armour type
 * @author Jasen
 */
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

	/**
	 * @return armour rating - the amount of damage this armour blocks
	 */
	public int getArmourRating() {
		if(armourType == null) {
			return 0;
		}
		return armourType.getArmourRating();
	}
	
	/**
	 * @return the type of armour this is
	 */
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
	
	/**
	 * The type of armour determines the amount of damage reduction from an attack
	 * @author Jasen
	 */
	public enum ArmourType {
		Robe(2),
		Leather(5),
		Chain(10),
		Plate(15);
		
		private final int rating;
		private ArmourType(int damage) {
			this.rating = damage;
		}
		
		/**
		 * @return the amount of damage that this armour type blocks
		 */
		public int getArmourRating() {
			return rating;
		}
	}
}
