package gameworld.tile;

import gameworld.Player;
import gameworld.entity.Entity;

import java.awt.Image;
import java.awt.Point;

/**
 * A Tile represents a square in a location.
 * A tile may contain a Player or an Entity
 * @author Jasen
 *
 */
public abstract class Tile {

	private Point position;
	private Entity entity;
	private Player player;
	private Image image;
	protected String name;

	public Tile(String name, Point pos, Image image) {
		this.name = name;
		this.position = pos;
		this.image = image;
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
	
	public Image getImg(){
		return image;
	}
	
	public String toString(){	
		return name.substring(0,2);
	}
}
