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
	
	private Item[]possibleItems;

	public Chest(String name, String description, Point position, Location location) {
		super(name, description, position, location);
		initializeArray();
		generateItems();
	}
	
	
	

	private void initializeArray() {
		possibleItems = new Item[] {
				//weapons
				new ShankWeapon("Shank", "A basic weapon", null, null),
				new SpearWeapon("A spear","Stab stab", null, null),
				//armour
				new PlateArmour("Plate armour", "Provides the highest protection", null, null),
				new ChainArmour("Chain armour", "Provides good protection", null, null),
				new LeatherArmour("Leather armour", "Provides basic protection",null, null),
				new RobeArmour("Robe armour", "Provides very basic protection", null, null),
				//misc
				//new Key("A key", "Used to open doors or chests", null, null),
				new Potion("Health Potion", "Use this to heal yourself!", null, null)
				};
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
		int itemType = (new Random().nextInt(possibleItems.length));
		return possibleItems[itemType].clone();		
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