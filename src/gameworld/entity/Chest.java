package gameworld.entity;

import gameworld.Player;
import gameworld.location.Location;

import java.awt.Point;

public class Chest extends Container {

	public Chest(int size) {
		super(size);
		
	}

	@Override
	public void interact(Player player) {
		//probably should display the inventory of the chest when it is interacted with
	}

	@Override
	public String name() {
		return "A Chest";
	}

	@Override
	public String description() {
		return "Could container sick loot";
	}

	@Override
	public Point position() {
		return null;
	}

	@Override
	public Location location() {
		return null;
	}

}
