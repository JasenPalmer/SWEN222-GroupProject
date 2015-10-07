package gameworld.entity;

import gameworld.Player;
import gameworld.location.Location;

import java.awt.Point;
import java.io.Serializable;

public class Chest extends Container implements Serializable{

	private static final long serialVersionUID = -1295269831652028875L;
	private Player openedBy;

	public Chest(String name, String description, Point position, Location location) {
		super(name, description, position, location);
	}

	@Override
	public void interact(Player player) {
		openedBy = player;
	}

	@Override
	public Location getLocation() {
		return null;
	}

	public Player getOpenedBy() {
		return openedBy;
	}

}