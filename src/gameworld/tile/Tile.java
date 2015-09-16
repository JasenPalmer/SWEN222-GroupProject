package gameworld.tile;

import gameworld.entity.Entity;
import gameworld.location.Location;

import java.awt.Point;

/**
 * Represents a square in a location
 * @author Jasen
 *
 */
public interface Tile {
	
	/**
	 * @return the position of the tile in the location as a Point
	 */
	public Point getPos();
	
	/**
	 * @return the entity that is contained in the tile or null if none is contained
	 */
	public Entity containedEntity();
	
	/**
	 * add an entity to the tile
	 */
	public void setEntity(Entity entity);
	
	/**
	 * @return the location that this tile is in
	 */
	public Location getLocation();
}
