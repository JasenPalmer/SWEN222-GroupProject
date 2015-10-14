package gameworld;

import gameworld.entity.Container;
import gameworld.entity.Item;
import gameworld.location.Location;
import gameworld.location.Location.Lights;
import gameworld.tile.Tile;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Main game class. Contains the current game's state 
 * 
 * @author Jasen
 *
 */
public class Game implements Serializable {

	private static final long serialVersionUID = 6103462087859564215L;
	
	public static enum Direction{NORTH, EAST, SOUTH, WEST};

	private Set<Location> locations;
	private Set<Player> players;

	public Game() {
		players = new HashSet<Player>();
		locations = Parser.loadLocations();
		Parser.loadEntityFiles();
		Parser.loadDoors();
		getLocation("Maze-big").setLights(Lights.OFF);
	}

	/**
	 * Get a location from the location name
	 * @param locationName - name of the location to get
	 * @return Location
	 */
	public Location getLocation(String locationName) {
		for(Location loc : locations) {
			if(loc.getName().equals(locationName)) {
				return loc;
			}
		}
		return null;
	}

	/**
	 * Add a player to the game
	 * @param player to add
	 */
	public void addPlayer(Player player) {
		players.add(player);
	}

	/**
	 * Remove the player from the game
	 * @param player to remove
	 */
	public void removePlayer(Player player) {
		player.getStandingOn().setPlayer(null);
		player.getLocation().removePlayer(player);
		players.remove(player);
	}

	/**
	 * Move a player in a direction
	 *
	 * @param player to move
	 * @param direction to move the player
	 * @return 0 if the player didn't move, 1 if the player did move and 2 if the player changed location
	 */
	public int movePlayer(String playerName, Direction direction) {
		Player player = parsePlayer(playerName);
		if(direction == null || player == null) {return 0;}
		if(player.isDead()){return 0;}
		Location oldLoc = player.getLocation();
		if(!player.move(direction, false)) {return 0;}
		Location newLoc = player.getLocation();
		if(!oldLoc.equals(newLoc)){return 2;}
		return 1;
	}

	/**
	 * Drop the item in the players inventory at the given index
	 * @param playerName - name of the player
	 * @param index - position in the inventory of the item
	 * @return true if the item was dropped
	 */
	public boolean playerDropItem(String playerName, int index) {
		Player player = parsePlayer(playerName);
		if(player == null){return false;}
		if(player.isDead()){return false;}
		if(!player.dropFromInv(index)){return false;}
		return true;
	}

	/**
	 * Have the player interact with the container in front of them
	 * 
	 * @param playerName - name of the player
	 * @return the container the player is interacting with
	 */
	public Container performAction(String playerName){
		Player player = parsePlayer(playerName);
		if(player == null){return null;}
		if(player.isDead()){return null;}
		return player.performAction();
	}

	/**
	 * Attack the player in front of the player
	 *
	 * @param attacker - player that is doing the attacking
	 * @param defender - player that is being attacked
	 * @return true if the attack was successful
	 */
	public boolean attackPlayer(String player) {
		Player attacker = parsePlayer(player);
		if(attacker.isDead()){return false;}
		if(!attacker.attack()){return false;}
		attacker.setAttacking(true);
		return true;
	}
	
	/**
	 * Add an item to a container. The container should be directly in front of the player
	 * @param player doing the interacting
	 * @param item to add
	 * @param container - to add to
	 * @return container that was added to
	 */
	public Container addItemContainer(Player player,Item item, Container container) {
		Tile tile = player.getTile(player.getFacing());
		if(tile != null) {
			if(tile.containedEntity() == null){return container;}
			if(tile.containedEntity() instanceof Container) {
				Container con = (Container) tile.containedEntity();
				con.storeItem(item);
				return con;
			}
		}
		return container;
	}
	

	/**
	 * Removes item at index from container specified
	 * @param index - index of item to be removed
	 * @param container - container to remove item from
	 */
	public Container removeItemContainer(Player player, int index, Container container) {
		Tile tile = player.getTile(player.getFacing());
		if(tile != null){
			if(tile.containedEntity() == null){return container;}
			if(tile.containedEntity() instanceof Container) {
				Container con = (Container) tile.containedEntity();
				con.getItems()[index] = null;
				return con;
			}
		}
		return container;
	}



	/**
	 * Returns the player with the given name
	 *
	 * @param user - name of player
	 * @return the player object
	 */
	public Player parsePlayer(String user) {
		for(Player p : players) {
			if(p.getName().equals(user)) {
				return p;
			}
		}
		return null;
	}

	//Getters

	public Set<Location> getLocations() {
		return locations;
	}
	public Set<Player> getPlayers() {
		return players;
	}

}
