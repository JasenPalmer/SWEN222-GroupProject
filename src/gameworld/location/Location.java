package gameworld.location;

import gameworld.tile.Tile;

/**
 * Represents a location that a player or an object can be in
 * @author Jasen
 *
 */
public class Location {

	public static final int DEFAULT_LOC_SIZE = 20;

	private String name;
	private String description;
	private Tile[][] tiles;

	/**
	 * @return the name of the location
	 */
	public String name() {
		return name;
	}

	/**
	 * @return the description of the location
	 */
	public String descritpion() {
		return description;
	}

	/**
	 * @return the tiles that make up this location
	 */
	public Tile[][] getTiles() {
		return tiles;
	}

	/**
	 * @return width of the location
	 */
	public int width() {
		return tiles[0].length;
	}

	/**
	 * @return height of the location
	 */
	public int height() {
		return tiles.length;
	}

}
