package gameworld.entity;

import gameworld.location.Location;

import java.awt.Point;
import java.io.Serializable;


/**
 * Items are things that a player can store in their inventory eg. a key
 * @author Jasen
 */
public abstract class Item implements InteractableEntity, Serializable{

	private static final long serialVersionUID = 6844825781934457545L;
	
	/**
	 * The location that the item is in
	 */
	private Location location;
	
	/**
	 * The position of the item in the location
	 */
	private Point position;
	
	/**
	 * Name of the item
	 */
	private String name;
	
	/**
	 * Item's description
	 */
	private String description;
	
	
	public Item(String name, String description, Point position, Location location) {
		this.name = name;
		this.description = description;
		this.position = position;
		this.location = location;
	}
	
	@Override
	public Point getPosition() {
		return position;
	}


	@Override
	public Location getLocation() {
		return location;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
}
