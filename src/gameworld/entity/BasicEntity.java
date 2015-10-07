package gameworld.entity;

import gameworld.location.Location;

import java.awt.Point;


/**
 * A basic entity is an entity that cannot be interacted with eg a tree
 * @author Jasen
 */
public class BasicEntity implements Entity {
	
	private String name;
	private String description;
	private Point position;
	private Location location;
	
	public BasicEntity(String name, String description, Point position) {
		this.name = name;
		this.description = description;
		this.position = position;
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

	@Override
	public String type() {
		return "basic";
	}
}
