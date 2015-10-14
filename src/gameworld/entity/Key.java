package gameworld.entity;

import gameworld.Player;
import gameworld.location.Location;
import gameworld.tile.EntranceTile;
import gameworld.tile.Tile;

import java.awt.Point;
import java.io.Serializable;


/**
 * This class represents a key that can be used to unlock a container or an EntranceTile
 * @author Jasen
 */
public class Key extends Item implements Serializable{

	private static final long serialVersionUID = -1062125803835909442L;

	public Key(String name, String description, Point position, Location location) {
		super(name, description, position, location);
	}

	private Key(Key key) {
		super(key.name, key.description, key.position, key.location);
	}

	/**
	 * Interacting with a key causes it to be used unlock the container or Entrance (if there is one)
	 * in front of the player. 
	 */
	@Override
	public void interact(Player player) {
		Tile tile = player.getTile(player.getFacing());
		if(tile == null){return;}
		if((tile instanceof EntranceTile)) {
			//trying to unlock a door at this point
			EntranceTile door = (EntranceTile) tile;
			if(door.isLocked()){
				//unlock the door
				door.setLocked(false);
			}
		}
		Entity ent = tile.containedEntity();
		// there is no Container to unlock
		if(ent == null){return;}
		// is an entity but its not a container
		if(!(ent instanceof Container)){ return;}
		Container container = (Container) ent;
		//if the container is locked
		if(container.isLocked()) {
			// unlock the container
			container.setLocked(false);
		}
	}

	@Override
	public Item clone() {
		return new Key(this);
	}

}