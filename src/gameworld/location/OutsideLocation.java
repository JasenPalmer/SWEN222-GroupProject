package gameworld.location;

import gameworld.tile.Tile;

public class OutsideLocation extends Location {

	private Tile[][] buildingTiles;

	public OutsideLocation(String name, String description, Tile[][] tiles, Tile[][] buildingTiles) {
		super(name, description, tiles);
		this.buildingTiles = buildingTiles;
	}

}
