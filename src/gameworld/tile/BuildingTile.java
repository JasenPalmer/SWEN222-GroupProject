package gameworld.tile;

import java.awt.Point;

/**
 * A BuildingTile is a Tile that has a building on it
 * 
 * @author Jasen
 *
 */
public class BuildingTile extends Tile {

	private static final long serialVersionUID = -7878335571226880684L;

	public BuildingTile(String name, Point pos, boolean passable) {
		super(name, pos, passable);
	}

}
