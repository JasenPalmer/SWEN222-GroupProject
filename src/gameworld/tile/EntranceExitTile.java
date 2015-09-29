package gameworld.tile;

import java.awt.Point;


/**
 * Entrance or exit to another Location.
 * An exit maybe one way
 * @author Jasen
 *
 */
public class EntranceExitTile extends Tile {

	private static final long serialVersionUID = 3137127703217079080L;

	private EntranceExitTile exit;
	
	private boolean oneWay;

	public EntranceExitTile(String name, Point pos, boolean passable, boolean oneWay) {
		super(name, pos, passable);
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
