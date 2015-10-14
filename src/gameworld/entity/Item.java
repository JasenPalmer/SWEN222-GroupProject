package gameworld.entity;

import gameworld.location.Location;

import java.awt.Point;
import java.io.Serializable;


/**
 * Items are entities that a player can store in their inventory eg. a key
 * @author Jasen
 */
public abstract class Item implements InteractableEntity, Serializable{

	private static final long serialVersionUID = 6844825781934457545L;

	protected String name;
	protected String description;
	protected Point position;
	protected Location location;

	public Item(String name, String description, Point position, Location location) {
		this.name = name;
		this.description = description;
		this.position = position;
		this.location = location;
	}
	
	public abstract Item clone();
	
	@Override
	public Point getPosition() {
		return position;
	}
	
	@Override
	public void setPosition(Point position) {
		this.position = position;
	}
	
	@Override
	public Location getLocation() {
		return location;
	}
	@Override
	public void setLocation(Location location) {
		this.location = location;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	public String toString() {
		return name;
	}
	
}
