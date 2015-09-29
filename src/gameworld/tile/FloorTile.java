package gameworld.tile;

import java.awt.Point;

/**
 * The most basic tile. Represents a floor square in a location
 * @author Jasen
 *
 */
public class FloorTile extends Tile {

	private static final long serialVersionUID = -8663027195457766041L;

	public FloorTile(String name, Point pos, boolean passable) {
		super(name, pos, passable);
	}

}
