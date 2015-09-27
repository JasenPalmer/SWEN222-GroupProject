package gameworld.tile;

import java.awt.Image;
import java.awt.Point;

/**
 * The most basic tile. Represents a floor square in a location
 * @author Jasen
 *
 */
public class FloorTile extends Tile {

	public FloorTile(String name, Point pos, Image image) {
		super(name, pos, image);
	}

}
