package gameworld.entity;

import java.awt.Point;

import gameworld.Player;
import gameworld.location.Location;

public class Potion extends Item {
	
	private static final long serialVersionUID = -1161009772412731963L;
	
	private static final int healAmount = 15;

	public Potion(String name, String description, Point position,
			Location location) {
		super(name, description, position, location);
	}

	@Override
	public void interact(Player player) {
		int health = player.getHealth();
		int max = player.getMaxHealth();
		if(!(health >= max)) {
			if(health + healAmount >= max) {
				player.setHealth(max);
			}
			else {
				player.setHealth(health+healAmount);
			}
		}
	}

}
