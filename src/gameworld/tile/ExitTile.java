package gameworld.tile;

import gameworld.location.Location;

import java.awt.Image;
import java.awt.Point;

/**
 * An exit tile is a tile that the player can stand on to leave the current location and move to the connected location
 * @author Jasen
 *
 */
public class ExitTile extends AbstractTile {

	private EntranceTile exitPoint;

	public ExitTile(Location location, Point pos, Image image) {
		super(location, pos, image);
	}

	/**
	 * @return the tile that this exit connects to
	 */
	public EntranceTile getExitPoint() {
		return exitPoint;
	}

	/**
	 * @param exitPoint - the tile that this exit should connect to
	 */
	public void setExitPoint(EntranceTile exitPoint) {
		this.exitPoint = exitPoint;
	}

}
