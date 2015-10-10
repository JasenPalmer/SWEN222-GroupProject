package gameworld.entity;

import gameworld.location.Location;

import java.awt.Point;

public class ShankWeapon extends Weapon {

	private static final long serialVersionUID = 4835987546300861362L;
	
	private static final int DAMAGE = 10;

	public ShankWeapon(String name, String description, Point position,
			Location location) {
		super(name, description, position, location);
		setDamage(DAMAGE);
	}
}