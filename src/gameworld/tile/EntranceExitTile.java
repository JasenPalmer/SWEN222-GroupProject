package gameworld.tile;

import gameworld.Player;
import gameworld.location.Location;

import java.awt.Point;


/**
 * Entrance or exit to another Location.
 * An exit maybe one way
 * @author Jasen
 *
 */
public class EntranceExitTile extends Tile {

	private static final long serialVersionUID = 3137127703217079080L;

	private Tile exit;
	private boolean locked;
	private Location exitLocation;

	public EntranceExitTile(String name, Point pos, boolean passable) {
		super(name, pos, passable);
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	public void setExitLoc(Location location) {
		this.exitLocation = location;
	}

	public void setExitTile(Tile exit) {
		this.exit = exit;
	}
	
	public boolean enter(Player player) {
		if(exitLocation == null || locked || exit == null) {
			return false;
		}
		player.getLocation().removePlayer(player);
		player.setLocation(exitLocation);
		exitLocation.addPlayer(player);
		player.getStandingOn().setPlayer(null);
		player.setStandingOn(exit);
		exit.setPlayer(player);
		return true;
	}
	
}
