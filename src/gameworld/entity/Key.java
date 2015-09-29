package gameworld.entity;

import gameworld.Player;


/**
 * This class represents a key that can be used to either open a door or a chest
 * 
 * @author Jasen
 *
 */
public class Key extends Item {
	

	public Key() {
		
	}
	
	
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
	}

}
