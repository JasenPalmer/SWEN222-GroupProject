package gameworld.entity;


/**
 * A container is an entity that can hold Items eg. a chest
 * @author Jasen
 */
public abstract class Container implements InteractableEntity {


	/**
	 * Items that are stored in the container
	 */
	private Item[] items;
	
	/**
	 * Flag for if the container is open
	 */
	private boolean open;
	
	/**
	 * Flag for if the container if locked
	 */
	private boolean locked;
	
	/**
	 * Amount of items stored in the container
	 */
	private int itemCount; // do we even need this ?
	
	public Container(int size) {
		items = new Item[size];
		open = false;
		locked = false;
		itemCount = 0;
	}
	
	/**
	 * 
	 * @return The items in the container if the container is open otherwise null
	 */
	public Item[] getItems() {
		if(!open){return null;}
		return items;
	}
	
	/**
	 * @return true if the container is open
	 */
	public boolean isOpen() {
		return open;
	}
	
	/**
	 * @return true if the container was successfully opened
	 */
	public boolean open() {
		if(!locked) {
			open = true;
			return true;
		}
		return false;
	}
	
	/**
	 * Closes the container 
	 * which prevents items from being stored in it and 
	 * the player from seeing what is inside
	 */
	public void close() {
		open = false;
	}
	
	/**
	 * Stores an item in the container in the first available spot
	 * @param item to store
	 * @return true if the item was stored
	 */
	public boolean storeItem(Item item) {
		if(item == null){return false;}
		if(itemCount >= items.length){return false;}
		for(int i = 0; i < items.length; i++) {
			if(items[i] == null) {
				items[i] = item;
				itemCount++;
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Removes an item from the container
	 * @param item to remove
	 * @return true if the item was successfully removed
	 */
	public boolean removeItem(Item item) {
		if(item == null){return false;}
		if(itemCount <= 0) {return false;}
		for(int i = 0; i < items.length; i++) {
			if(items[i] == item){
				items[i] = null;
				itemCount--;
				return true;
			}
		}
		return false;
	}
	
}
