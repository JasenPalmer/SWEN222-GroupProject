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

	public Tile(Point pos, Image image) {
		position = pos;
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
}
