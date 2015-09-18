package gameworld.tile;

import gameworld.location.InsideLocation;

import java.awt.Image;
import java.awt.Point;

/**
 * An entrance tile is a tile where an exit will lead to
 * @author Jasen
 *
 */
public class EntranceTile extends AbstractTile{

	public EntranceTile(InsideLocation location, Point pos, Image image) {
		super(location, pos, image);
	}
}
