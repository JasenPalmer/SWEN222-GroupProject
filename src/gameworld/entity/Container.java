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
	 * Flag for if the container if locked
	 */
	private boolean locked;
	
	public Container(int size) {
		items = new Item[size];
		locked = true;
	}
	
	/**
	 * 
	 * @return The items in the container if the container is open otherwise null
	 */
	public Item[] getItems() {
		return items;
	}

	/**
	 * Stores an item in the container in the first available spot
	 * @param item to store
	 * @return true if the item was stored
	 */
	public boolean storeItem(Item item) {
		if(item == null){return false;}
		for(int i = 0; i < items.length; i++) {
			if(items[i] == null) {
				items[i] = item;
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
		for(int i = 0; i < items.length; i++) {
			if(items[i].equals(item)){
				items[i] = null;
				return true;
			}
		}
		return false;
	}
	
	public boolean isLocked(){
		return locked;
	}
	
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
}
