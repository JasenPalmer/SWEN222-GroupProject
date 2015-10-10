package gameworld.entity;

import gameworld.Player;
import gameworld.location.Location;

import java.awt.Point;
import java.io.Serializable;
import java.util.Random;

/**
 * A chest it a type of container.
 * When a chest is created it will randomly generate 1-5 items in its inventory.
 * 
 * @author Jasen
 *
 */
public class Chest extends Container implements Serializable{

	private static final long serialVersionUID = -1295269831652028875L;
	private Player openedBy;

	public Chest(String name, String description, Point position, Location location) {
		super(name, description, position, location);
		generateItems();
	}

	/**
	 * Randomly generate 1-5 items in the chest
	 */
	private void generateItems() {
		int itemAmount = (new Random().nextInt(5))+1;
		for(int i = 0; i < itemAmount; i++) {
			getItems()[i] = createItem();
		}
	}

	/**
	 * creates a random item
	 * @return item created
	 */
	private Item createItem() {
		int itemType = (new Random().nextInt(3))+1;
		switch(itemType) {
		case 1:
			return new Shank("Shank", "A basic weapon", null, null);
		case 2:
			return new Potion("Health Potion", "Use this to heal yourself!", null, null);
		case 3:
			return new Key("A key", "Used to open doors or chests", null, null);
			default:
				return null;
		}
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