package gameworld.entity;

import gameworld.Player;
import gameworld.location.Location;
import gameworld.tile.EntranceTile;
import gameworld.tile.Tile;

import java.awt.Point;
import java.io.Serializable;


/**
 * This class represents a key that can be used to either open a door or a container
 * 
 * @author Jasen
 *
 */
public class Key extends Item implements Serializable{
	
	public Key(String name, String description, Point position, Location location) {
		super(name, description, position, location);
	}


	private static final long serialVersionUID = -1062125803835909442L;


	@Override
	public String getName() {
		return "A Key";
	}
	
	@Override
	public String getDescription() {
		return "Used to open doors or chests";
	}


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
		if(container.isLocked()) {
			// unlock the container
			container.setLocked(false);
		}
	}

}