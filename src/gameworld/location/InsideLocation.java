package gameworld.location;

import gameworld.tile.Tile;

/**
 * Represents a location that a player or an object can be in
 * @author Jasen
 *
 */
public class InsideLocation extends Location{

	private static final long serialVersionUID = 43788886124239093L;

	public InsideLocation(String name, String description, Tile[][] tiles) {
		super(name, description, tiles);
	}

}
