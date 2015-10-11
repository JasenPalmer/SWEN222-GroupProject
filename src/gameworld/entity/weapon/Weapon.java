package gameworld.entity.weapon;

import gameworld.Player;
import gameworld.entity.Item;
import gameworld.location.Location;

import java.awt.Point;

public abstract class Weapon extends Item {

	private static final long serialVersionUID = -6003314303346702099L;
	
	private int damage = 5;

	public Weapon(String name, String description, Point position,
			Location location) {
		super(name, description, position, location);
	}
	
	@Override
	public void interact(Player player) {
		//not sure what this should do
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

}
