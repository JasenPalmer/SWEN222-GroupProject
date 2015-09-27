package gameworld.tile;

import java.awt.Image;
import java.awt.Point;

/**
 * A BuildingTile is a Tile that has a building on it
 * 
 * @author Jasen
 *
 */
public class BuildingTile extends Tile {

	public BuildingTile(String name, Point pos, Image image) {
		super(name, pos, image);
	}

}
