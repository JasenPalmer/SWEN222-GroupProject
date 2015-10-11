package gameworld.entity;

import gameworld.Player;
import gameworld.entity.armour.ChainArmour;
import gameworld.entity.armour.LeatherArmour;
import gameworld.entity.armour.PlateArmour;
import gameworld.entity.armour.RobeArmour;
import gameworld.entity.weapon.ShankWeapon;
import gameworld.entity.weapon.SpearWeapon;
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
	
	private Item[] epicItems; // 10%
	private Item[] rareItems; // 30%
	private Item[] commonItems; // 60%

	public Chest(String name, String description, Point position, Location location) {
		super(name, description, position, location);
		initializeArrays();
		generateItems();
	}

	/**
	 * adds all items to arrays.
	 * Used when creating random items based on rarity
	 */
	private void initializeArrays() {
		commonItems = new Item[] {
				//weapons
				new ShankWeapon("Shank", "A basic weapon", null, null),
				//armour
				new RobeArmour("Robe armour", "Provides very basic protection", null, null),
				new LeatherArmour("Leather armour", "Provides basic protection",null, null),
				//misc
				new Potion("Health Potion", "Use this to heal yourself!", null, null)
		};
		
		rareItems = new Item[] {
				//weapon
				new SpearWeapon("A spear","Stab stab", null, null),
				//armour
				new ChainArmour("Chain armour", "Provides good protection", null, null),
				// misc
				new Key("A key", "Used to open doors or chests", null, null),
		};
		
		epicItems = new Item[] {
				//weapon
				//armour
				new PlateArmour("Plate armour", "Provides the highest protection", null, null)
				//misc
		};
	}

	/**
	 * Randomly generate 1-5 items in the chest
	 */
	private void generateItems() {
		int itemAmount = (new Random().nextInt(5))+1;
		for(int i = 0; i < itemAmount; i++) {
			items[i] = createItem();
		}
	}

	/**
	 * creates item - takes into account item rarity
	 * @return item created
	 */
	private Item createItem() {
		int itemType = new Random().nextInt(100);
		if(itemType < 60) {
			return createCommon();
		}
		else if(itemType >=60 && itemType < 90) {
			return createRare();
		}
		else {
			return createEpic();
		}
	}
	
	private Item createCommon() {
		int index = new Random().nextInt(commonItems.length);
		return commonItems[index].clone();
	}
	
	private Item createRare() {
		int index = new Random().nextInt(rareItems.length);
		return rareItems[index].clone();
	}

	private Item createEpic() {
		int index = new Random().nextInt(epicItems.length);
		return epicItems[index].clone();
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
	
	public String toString() {
		String toReturn = "{";
		for(Item item : items) {
			if(item == null){
				continue;
			}
			toReturn = toReturn+","+item.toString();
		}
		toReturn = toReturn+"}";
		return toReturn;
	}

}