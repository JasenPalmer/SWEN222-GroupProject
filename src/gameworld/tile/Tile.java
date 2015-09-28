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

	private static final long serialVersionUID = 8870726693571138560L;
	private Point position;
	private Entity entity;
	private Player player;
	protected String name;

	public Tile(String name, Point pos) {
		this.name = name;
		this.position = pos;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public Point getPos() {
		return position;
	}

	public Entity containedEntity() {
		return entity;
	}

	public Player getPlayer() {
		return player;
	}
	
	public String toString(){	
		return name.substring(0,2);
	}
}
