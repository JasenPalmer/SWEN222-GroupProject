package gameworld.entity;

import gameworld.Player;
import gameworld.location.Location;

import java.awt.Point;


/**
 * Items are things that a player can store in their inventory eg. a key
 * @author Jasen
 */
public abstract class Item implements InteractableEntity{
	
	/**
	 * Player that is holding the item null if no player is holding it
	 */
	private Player heldBy;
	
	/**
	 * The location that the item is in
	 */
	private Location location;
	
	/**
	 * The position of the item in the location
	 */
	private Point position;
	
	@Override
	public Point position() {
		return position;
	}


	@Override
	public Location location() {
		return location;
	}
	
	/**
	 * @return Player that is holding the item. Null if no player is holding it
	 */
	public Player heldBy() {
		return heldBy;
	}
	
}
