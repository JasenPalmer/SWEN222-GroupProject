package gameworld.tile;

import java.awt.Image;
import java.awt.Point;


/**
 * Entrance or exit to another Location.
 * An exit maybe one way
 * @author Jasen
 *
 */
public class EntranceExitTile extends Tile {
	
	private EntranceExitTile exit;
	
	private boolean oneWay;

	public EntranceExitTile(Point pos, Image image, boolean oneWay) {
		super(pos, image);
		this.setOneWay(oneWay);
	}

	public boolean isOneWay() {
		return oneWay;
	}

	public void setOneWay(boolean oneWay) {
		this.oneWay = oneWay;
	}

	public EntranceExitTile getExit() {
		return exit;
	}

	public void setExit(EntranceExitTile exit) {
		this.exit = exit;
	}

}
