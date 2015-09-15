package gameworld.tile;

import gameworld.location.Location;

import java.awt.Point;

/**
 * An entrance tile is a tile where an exit will lead to
 * @author Jasen
 *
 */
public class EntranceTile extends AbstractTile{

	public EntranceTile(Location location, Point pos) {
		super(location, pos);
	}
}
