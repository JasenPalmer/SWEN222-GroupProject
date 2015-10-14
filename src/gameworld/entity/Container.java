package gameworld.entity;

import gameworld.Player;
import gameworld.location.Location;

import java.awt.Point;
import java.io.Serializable;


/**
 * A container is an entity that can hold Items eg. a chest
 * @author Jasen
 */
public abstract class Container implements InteractableEntity, Serializable {

	private static final long serialVersionUID = 3833387180228381148L;

	private static final int INV_SIZE = 18;

	protected Item[] items;

	protected Player openedBy;
	protected boolean locked;

	private String name;
	private String description;
	private Point position;
	private Location location;

	public Container(String name, String description, Point position, Location location) {
		this.name = name;
		this.description = description;
		this.position = position;
		items = new Item[INV_SIZE];
		locked = true;
		this.location = location;
	}

	/**
	 * @return the items in the container
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

	/**
	 * @return true if the container is locked
	 */
	public boolean isLocked(){
		return locked;
	}

	/**
	 * set the locked status of the container
	 * @param locked
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Point getPosition() {
		return position;
	}
	
	@Override
	public void setPosition(Point position) {
		this.position = position;
	}

	@Override
	public Location getLocation() {
		return location;
	}
	
	@Override
	public void setLocation(Location location) {
		this.location = location;
	}


}
