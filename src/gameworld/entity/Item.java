package gameworld.entity;

import gameworld.location.Location;

import java.awt.Point;
import java.io.Serializable;

import ui.panels.ItemIcon;


/**
 * Items are things that a player can store in their inventory eg. a key
 * @author Jasen
 */
public abstract class Item implements InteractableEntity, Serializable{

	private static final long serialVersionUID = 6844825781934457545L;
	
	private ItemIcon icon;
	
	/**
	 * The location that the item is in
	 */
	protected Location location;
	
	/**
	 * The position of the item in the location
	 */
	protected Point position;
	
	/**
	 * Name of the item
	 */
	protected String name;
	
	/**
	 * Item's description
	 */
	protected String description;
	
	
	public Item(String name, String description, Point position, Location location) {
		this.name = name;
		this.description = description;
		this.position = position;
		this.location = location;
		this.icon = new ItemIcon(this,name,description);
	}
	
	public abstract Item clone();
	
	@Override
	public Point getPosition() {
		return position;
	}
	
	public void setPosition(Point position) {
		this.position = position;
	}
	
	@Override
	public Location getLocation() {
		return location;
	}
	
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
	
	public ItemIcon getIcon(){return this.icon;}
}
