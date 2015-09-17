package gameworld.entity;

import java.awt.Point;

import gameworld.Player;
import gameworld.location.InsideLocation;


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
	private InsideLocation location;
	
	/**
	 * The position of the item in the location
	 */
	private Point position;
	
	@Override
	public Point position() {
		return position;
	}


	@Override
	public InsideLocation location() {
		return location;
	}
	
	/**
	 * @return Player that is holding the item. Null if no player is holding it
	 */
	public Player heldBy() {
		return heldBy;
	}
	
	public boolean pickup(Player player) {
		boolean toReturn = player.pickupItem(this);
		if(toReturn){
			// player picked up the item so remove location
			location = null;
			// and the item is now held by the player
			heldBy = player;
		}
		return toReturn;
	}
	
}
