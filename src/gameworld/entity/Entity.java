package gameworld.entity;

import gameworld.location.Location;

import java.awt.Point;

/**
 * An entity is any object in the game eg. a chest, a rock or a key
 * @author Jasen
 */
public interface Entity{
	
	/**
	 * @return the name of the entity
	 */
	public String getName();
	
	/**
	 * @return the description of the entity
	 */
	public String getDescription();

	/**
	 * @return the position of the entity in the world as a point
	 */
	public Point getPosition();
	
	/**
	 * sets the current position of the entity
	 */
	public void setPosition(Point position);
	
	/**
	 * @return the location that the entity is in
	 */
	public Location getLocation();
	
	/**
	 * set the current location of the entity
	 */
	public void setLocation(Location location);
	
}