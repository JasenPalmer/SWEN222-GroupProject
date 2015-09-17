package gameworld.entity;

import gameworld.location.InsideLocation;

import java.awt.Point;

/**
 * An entity is any object in the game eg. a chest, a rock or a key
 * @author Jasen
 */
public interface Entity {
	
	/**
	 * @return the name of the entity
	 */
	public String name();
	
	/**
	 * @return the description of the entity
	 */
	public String description();

	/**
	 * @return the position of the entity in the world as a point
	 */
	public Point position();
	
	/**
	 * @return the location that the entity is in
	 */
	public InsideLocation location();
	
	
}
