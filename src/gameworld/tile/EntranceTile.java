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



	public EntranceTile(String name, Point pos, boolean passable, Type type) {
		super(name, pos, passable);
		this.type = type;
	}

	/**
	 * @return true if the entrance is locked
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * lock or unlock the entrance
	 * @param locked
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	/**
	 * set the location that this entrance leads to
	 * @param location
	 */
	public void setExitLoc(Location location) {
		this.exitLocation = location;
	}

	/**
	 * set the tile that is the exit point for this entrance
	 * @param exit - tile that is the exit point
	 */
	public void setExitTile(Tile exit) {
		this.exitTile = exit;
	}

	/**
	 * have a player move through this entrance to a new location
	 * @param player that is entering this entrance
	 * @return true if the player successfully moved through into the new location
	 */
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

	/**
	 * @return the type of entrance this is
	 */
	public Type getType() {
		return type;
	}
	
	@Override
	public String toString(){
		if(type==Type.BUILDING){
			return "EnB";
		} else {
			return "EnV";
		}
	}
	
	/**
	 * The different types of entrances there can be.
	 * This determines the image that gets drawn for an entrance
	 * @author Jasen
	 */
	public enum Type {
		BUILDING,
		INVISIBLE,
	}

}
