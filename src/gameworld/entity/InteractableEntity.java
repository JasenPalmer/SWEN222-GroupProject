package gameworld.entity;

import gameworld.Player;


/**
 * An interactable is a type of entity that the player can interact with eg. any item or a chest.
 * @author Jasen
 */
public interface InteractableEntity extends Entity {

	/**
	 * have a player interact with the entity
	 */
	public void interact(Player player);
}
