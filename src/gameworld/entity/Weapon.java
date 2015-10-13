package gameworld.entity;

import gameworld.Player;
import gameworld.location.Location;

import java.awt.Point;

/**
 * A weapon can be equipped by a player so that when a player attacks another player the attacking player will 
 * deal the amount of damage associated with the weapon type
 * @author Jasen
 *
 */
public class Weapon extends Item {

	private static final long serialVersionUID = -6003314303346702099L;

	//private int damage = 5;
	private WeaponType weaponType;

	public Weapon(String name, String description, Point position,
			Location location, WeaponType type) {
		super(name, description, position, location);
		weaponType = type;
	}

	private Weapon(Weapon weapon) {
		super(weapon.name, weapon.description, weapon.position, weapon.location);
		this.weaponType = weapon.weaponType;
	}

	@Override
	public void interact(Player player) {
		//not sure what this should do
	}

	public int getDamage() {
		if(weaponType == null){
			return 0;
		}
		return weaponType.getDamage();
	}

	public WeaponType getType() {
		return weaponType;
	}

	@Override
	public Item clone() {
		return new Weapon(this);
	}

	public enum WeaponType {
		Shank(15),
		Spear(30);

		private final int damage;
		private WeaponType(int damage) {
			this.damage = damage;
		}

		public int getDamage() {
			return damage;
		}
	}


}
