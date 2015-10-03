package gameworld.entity;

import gameworld.Player;
import gameworld.tile.EntranceExitTile;
import gameworld.tile.Tile;


/**
 * This class represents a key that can be used to either open a door or a container
 * 
 * @author Jasen
 *
 */
public class Key extends Item {
	
	@Override
	public String name() {
		return "A Key";
	}
	
	@Override
	public String description() {
		return "Used to open doors or chests";
	}


	@Override
	public void interact(Player player) {
		Tile tile = player.getTile(player.getDirection());
		if(tile == null){return;}
		if((tile instanceof EntranceExitTile)) {
			//trying to unlock a door at this point
			EntranceExitTile door = (EntranceExitTile) tile;
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
