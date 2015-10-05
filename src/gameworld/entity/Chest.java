package gameworld.entity;

import gameworld.Player;
import gameworld.location.Location;

import java.awt.Point;

public class Chest extends Container {

	private Player openedBy;

	public Chest(int size) {
		super(size);
	}

	@Override
	public void interact(Player player) {
		openedBy = player;
	}

	@Override
	public String getName() {
		return "A Chest";
	}

	@Override
	public String getDescription() {
		return "Could container sick loot";
	}

	@Override
	public Point getPosition() {
		return null;
	}

	@Override
	public Location getLocation() {
		return null;
	}

	public Player getOpenedBy() {
		return openedBy;
	}

}
