package gameworld.tile;

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

	public EntranceExitTile(String name, Point pos, boolean oneWay) {
		super(name, pos);
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
