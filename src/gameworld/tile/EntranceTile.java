package gameworld.tile;

import gameworld.Player;
import gameworld.location.Location;

import java.awt.Point;


/**
 * Entrance to another Location.
 * Trying to stand on this tile should move the player to the set exit location.
 * Before an Entrance is used the exitLocation and exitTile should be set.
 * The Entrance also will have to be unlocked before it can be used
 * @author Jasen
 *
 */
public class EntranceTile extends Tile {

	private static final long serialVersionUID = 3137127703217079080L;

	private Tile exitTile;
	private boolean locked;
	private Location exitLocation;
	private Type type;

	public enum Type {
		BUILDING,
		INVISIBLE,
		TREE
	}

	public EntranceTile(String name, Point pos, boolean passable, Type type) {
		super(name, pos, passable);
		this.type = type;
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
		this.exitTile = exit;
	}

	public boolean enter(Player player) {
		if(exitLocation == null || locked || exitTile == null) {
			return false;
		}
		player.getLocation().removePlayer(player);
		player.setLocation(exitLocation);
		exitLocation.addPlayer(player);
		player.getStandingOn().setPlayer(null);
		player.setStandingOn(exitTile);
		exitTile.setPlayer(player);
		player.setPosition(exitTile.getPosition());
		return true;
	}

}
