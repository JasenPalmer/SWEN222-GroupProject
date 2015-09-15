package gameworld;

import gameworld.entity.InteractableEntity;
import gameworld.entity.Item;
import gameworld.location.Location;
import gameworld.tile.Tile;

import java.awt.Point;

public class Player {

	/**
	 * Default size of the players inventory
	 */
	private static final int DEFAULT_INV_SIZE = 4;

	/**
	 * Players name
	 */
	private String name;

	/**
	 * The items that the player has in their inventory
	 */
	private  Item[] inventory;
	/**
	 * The amount of items the player has in their inventory
	 */
	private int itemCount;

	/**
	 * Flag for if the player is holding an entity
	 */
	private boolean holding;

	/**
	 * The entity that the player is holding.
	 * A player may only hold one Interactable Entity but can store multiple Items in their inventory
	 */
	private InteractableEntity held;

	/**
	 * The current location the player is in
	 */
	private Location location;

	/**
	 * The current position of the player
	 */
	private Point position;



	public Player(String name) {
		this.name = name;
		inventory = new Item[DEFAULT_INV_SIZE];
		holding = false;
		itemCount = 0;
	}

	/**
	 * Removes the entity held by the player
	 * @return the entity held by the player or null if the player isn't holding any entity
	 */
	public InteractableEntity drop() {
		InteractableEntity toReturn = held;
		held = null;
		holding = false;
		return toReturn;
	}

	/**
	 *
	 * @param entity for the player to hold
	 * @return true if the player is now holding the entity or false if the player is already holding an entity
	 */
	public boolean hold(InteractableEntity entity) {
		if(held != null){return false;}
		held = entity;
		holding = true;
		return true;
	}

	/**
	 * Removes an item from the players inventory
	 *Direction
	 * @param item to be removed
	 * @return true if the item was successfully removed
	 */
	public boolean dropFromInv(Item item) {
		for(int i = 0; i < inventory.length; i++) {
			if(inventory[i].equals(item)) {
				inventory[i] = null;
				itemCount++;
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds an item to the first available slot in the players inventory
	 * @param item to be added
	 * @return true if the item was successfully added
	 */
	public boolean pickupItem(Item item) {
		if(itemCount >= inventory.length) {return false;}
		for(int i = 0; i < inventory.length; i++) {
			if(inventory[i] == null) {
				inventory[i] = item;
				return true;
			}
		}
		return false;
	}

	public Tile getTile(Game.Direction dir) {
		Tile[][] array = location.getTiles();
		switch(dir) {
		case NORTH:
			if(position.y-1 >= 0){return array[position.y-1][position.x];}
			break;
		case EAST:
			if(position.x+1 < location.width()){return array[position.y][position.x+1];}
			break;
		case SOUTH:
			if(position.y+1 < location.height()){return array[position.y+1][position.x];}
			break;
		case WEST:
			if(position.x-1 >= 0){return array[position.y][position.x-1];}
			break;
		default:
			break;
		}
		return null;
	}

	/**
	 * Moves the player if a direction if it can
	 *
	 * @param dir - direction to move
	 * @return true if the player moved otherwise false
	 */
	public boolean move(Game.Direction dir) {
		Tile tile = getTile(dir);
		if(tile == null){return false;}
		if(tile.containedEntity() != null){return false;}
		moveDir(dir);
		return true;
	}


	private void moveDir(Game.Direction dir) {
		switch(dir) {
		case NORTH:
			position = new Point(position.x, position.y-1);
			break;
		case EAST:
			position = new Point(position.x+1, position.y);
			break;
		case SOUTH:
			position = new Point(position.x, position.y+1);
			break;
		case WEST:
			position = new Point(position.x-1, position.y);
			break;
		default:
			break;
		}
	}

	// getters and setters

	/**
	 * @return if the player is holding an entity
	 */
	public boolean isHolding() {
		return holding;
	}

	public String getName() {
		return name;
	}

	public Point getPosition() {
		return position;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}


}
