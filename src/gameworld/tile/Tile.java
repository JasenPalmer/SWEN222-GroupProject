package gameworld.tile;

import gameworld.Player;
import gameworld.entity.Entity;

import java.awt.Point;
import java.io.Serializable;

/**
 * A Tile represents a square in a location.
 * A tile may contain a Player or an Entity
 * @author Jasen
 *
 */
public abstract class Tile implements Serializable{

	private static final long serialVersionUID = 1L;
	private Point position;
	private Entity entity;
	private Player player;
	protected String name;
	private boolean passable;

	public Tile(String name, Point pos, boolean passable) {
		this.name = name;
		this.position = pos;
		this.setPassable(passable);
	}

	/**
	 * set the entity that in on this tile
	 * @param entity
	 */
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	/**
	 * @return the position of this tile
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * @return the entity that is on this tile
	 */
	public Entity containedEntity() {
		return entity;
	}

	/**
	 * set the entity of this tile
	 * @param e
	 */
	public void setEntitiy(Entity e){
		this.entity = e;
	}

	/**
	 * @return the player that is standing on this tile
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * set the player that is standing on this tile
	 * @param player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	/**
	 * @return the name of this tile
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return true if a player can move over this tile
	 */
	public boolean isPassable() {
		return passable;
	}

	/**
	 * set whether or not a player can move over this tile
	 * @param passable
	 */
	public void setPassable(boolean passable) {
		this.passable = passable;
	}
	
	public String toString(){
		return name.substring(0,2);
	}

}
