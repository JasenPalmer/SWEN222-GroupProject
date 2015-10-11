package gameworld.entity.weapon;

import gameworld.entity.Item;
import gameworld.location.Location;

import java.awt.Point;

public class SpearWeapon extends Weapon {

	private static final long serialVersionUID = 228225523401044106L;
	
	private static final int DAMAGE = 25;

	public SpearWeapon(String name, String description, Point position,
			Location location) {
		super(name, description, position, location);
		setDamage(DAMAGE);
	}

	public SpearWeapon(SpearWeapon spearWeapon) {
		super(spearWeapon.name, spearWeapon.description, spearWeapon.position, spearWeapon.location);
		setDamage(DAMAGE);
	}

	@Override
	public Item clone() {
		return new SpearWeapon(this);
	}

}
