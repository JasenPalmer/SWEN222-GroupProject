package gameworld.entity;

import java.awt.Point;

import gameworld.Player;
import gameworld.location.Location;

/**
 * A potion can be used by a player to regain some of the players missing health
 * @author Jasen
 *
 */
public class Potion extends Item {

	private static final long serialVersionUID = -1161009772412731963L;

	public static final int healAmount = 20;

	public Potion(String name, String description, Point position,
			Location location) {
		super(name, description, position, location);
	}

	private Potion(Potion pot) {
		super(pot.name, pot.description, pot.position, pot.location);
	}

	/**
	 * Heal the player
	 */
	@Override
	public void interact(Player player) {
		int health = player.getHealth();
		int max = player.getMaxHealth();
		if (!(health >= max)) {
			if (health + healAmount >= max) {
				player.setHealth(max);
			} else {
				player.setHealth(health + healAmount);
			}
		}
	}

	@Override
	public Item clone() {
		return new Potion(this);
	}

}
