package gameworld.entity.weapon;

import gameworld.Player;
import gameworld.entity.Item;
import gameworld.location.Location;

import java.awt.Point;

public class Weapon extends Item {

	private static final long serialVersionUID = -6003314303346702099L;
	
	//private int damage = 5;
	private WeaponType weaponType;

	public Weapon(String name, String description, Point position,
			Location location, WeaponType type) {
		super(name, description, position, location);
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
		return weaponType.getDamage();
	}
	
	public WeaponType getType() {
		return weaponType;
	}
	
	public enum WeaponType {
		Shank(15),
		Spear(40);
		
		private final int damage;
		private WeaponType(int damage) {
			this.damage = damage;
		}
		
		public int getDamage() {
			return damage;
		}
	}

	@Override
	public Item clone() {
		return new Weapon(this);
	}

}
