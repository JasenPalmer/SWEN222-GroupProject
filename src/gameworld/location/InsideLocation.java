package gameworld.location;

import gameworld.tile.Tile;

/**
 * Represents a location that a player or an object can be in
 * @author Jasen
 *
 */
public class InsideLocation extends Location{

	public InsideLocation(String name, String description, Tile[][] tiles) {
		super(name, description, tiles);
	}

}
