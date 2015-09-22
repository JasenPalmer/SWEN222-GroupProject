package gameworld.tile;

import java.awt.Image;
import java.awt.Point;

/**
 * The most basic tile. Represents a floor square in a location
 * @author Jasen
 *
 */
public class FloorTile extends Tile {

	public FloorTile(Point pos, Image image) {
		super(pos, image);
	}

}
