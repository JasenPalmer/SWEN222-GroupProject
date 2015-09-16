package gameworld.tile;

import gameworld.Player;
import gameworld.entity.Entity;
import gameworld.location.InsideLocation;
import gameworld.location.Location;

import java.awt.Point;

/**
 * Abstract tile class
 * @author Jasen
 *
 */
public abstract class AbstractTile implements Tile {

	private Point position;
	private Entity entity;
	private Location location;
	private Player player;

	public AbstractTile(Location location, Point pos) {
		position = pos;
		this.location = location;
	}

	@Override
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	@Override
	public Point getPos() {
		return position;
	}

	@Override
	public Entity containedEntity() {
		return entity;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public Player getPlayer() {
		return player;
	}
}
