package gameworld.entity;

import gameworld.location.Location;

import java.awt.Point;
import java.io.Serializable;


/**
 * A basic entity is an entity that cannot be interacted with eg a tree
 * @author Jasen
 */
public class BasicEntity implements Entity, Serializable {
	
	private static final long serialVersionUID = 3434114193644905945L;
	private String name;
	private String description;
	private Point position;
	private Location location;
	
	public BasicEntity(String name, String description, Point position, Location location) {
		this.name = name;
		this.description = description;
		this.position = position;
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

	@Override
	public Point getPosition() {
		return position;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setPosition(Point position) {
		this.position = position;
	}
}