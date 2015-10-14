package gameworld.entity;

import gameworld.Player;
import gameworld.location.Location;

import java.awt.Point;
import java.io.Serializable;
import java.util.Random;

/**
 * A chest it a type of container.
 * When a chest is created it will randomly generate items in its inventory.
 * @author Jasen
 */
public class Chest extends Container implements Serializable{

	private static final long serialVersionUID = -1295269831652028875L;
	
	private static final int maxItemAmount = 3;
	
	private static final int commonChance = 60;
	//private static final int rareChance = 30;
	private static final int epicChance = 10;

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
				//armour
				new Armour("Leather Armour", "Leather Armour",null, null, Armour.ArmourType.Leather),
				//misc
				new Potion("Potion", "Health Potion", null, null)
		};

		rareItems = new Item[] {
				//weapon
				new Weapon("Spear","Spear", null, null, Weapon.WeaponType.Spear),
				//armour
				new Armour("Chain Armour", "Chain Armour", null, null, Armour.ArmourType.Chain),
				// misc
				new Key("Key", "A Key. Used to unlock locks", null, null),
		};

		epicItems = new Item[] {
				//weapon
				//armour
				new Armour("Plate Armour", "Plate Armour", null, null,Armour.ArmourType.Plate),
				//misc
				new Gold("Gold", "Gold", null, null,(new Random().nextInt(5)+1))
		};
	}

	/**
	 * Randomly generate items in the chest
	 */
	private void generateItems() {
		int itemAmount = (new Random().nextInt(maxItemAmount))+1;
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
		if(itemType < commonChance) {
			return createCommon();
		}
		else if(itemType >=commonChance && itemType < 100-epicChance) {
			return createRare();
		}
		else {
			return createEpic();
		}
	}

	/**
	 * creates a common level item
	 * @return random common item
	 */
	private Item createCommon() {
		int index = new Random().nextInt(commonItems.length);
		return commonItems[index].clone();
	}

	/**
	 * creates a rare level item
	 * @return random rare item
	 */
	private Item createRare() {
		int index = new Random().nextInt(rareItems.length);
		return rareItems[index].clone();
	}

	/**
	 * creates a epic level item
	 * @return random epic level item
	 */
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
			toReturn = toReturn+item.toString()+",";
		}
		toReturn = toReturn+"}";
		return toReturn;
	}

}