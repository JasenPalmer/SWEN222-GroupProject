package gameworld.entity;

import java.awt.Point;

import gameworld.Player;
import gameworld.location.Location;

/**
 * A loot bag is dropped when the player dies. 
 * It contains all of the items in the player's inventory
 * @author Jasen
 *
 */
public class LootBag extends Container {

	private static final long serialVersionUID = 808347704362836620L;

	public LootBag(String name, String description, Point position,
			Location location, Item[] items) {
		super(name, description, position, location);
		this.items = items;
	}

	@Override
	public void interact(Player player) {
		this.openedBy = player;
	}
	
	public void addItem(Item item) {
		for(int i = 0; i < items.length; i++) {
			if(items[i] == null) {
				items[i] = item;
				return;
			}
		}
	}

}
