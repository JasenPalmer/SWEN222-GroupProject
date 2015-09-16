package gameworld.tile;

import gameworld.location.Location;

import java.awt.Point;

/**
 * The most basic tile. Represents a floor square in a location
 * @author Jasen
 *
 */
public class FloorTile extends AbstractTile {

	public FloorTile(Location location, Point pos) {
		super(location, pos);
	}

}
