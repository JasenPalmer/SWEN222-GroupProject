package gameworld;

import gameworld.location.Location;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Game implements Serializable {

	private static final long serialVersionUID = 1L;

	public static enum Direction{NORTH, EAST, SOUTH, WEST};

	private Set<Location> locations;
	private Set<Player> players;

	public Game() {
		players = new HashSet<Player>();
		locations = Parser.loadLocations();
		Parser.loadEntityFiles();
		Parser.loadDoors();
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
		players.remove(player);
	}


	/**
	 * Move a player in a direction
	 *
	 * @param player to move
	 * @param direction to move the player
	 * @return true if the player moved successfully
	 */
	public boolean movePlayer(String playerName, Direction direction) {
		Player player = parsePlayer(playerName);
		if(direction == null || player == null) {return false;}
		if(player.isDead()){return false;}
		if(!player.move(direction)) {return false;}
		return true;
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
	
	public boolean performAction(String playerName){
		Player player = parsePlayer(playerName);
		if(player == null){return false;}
		if(player.isDead()){return false;}
		if(!player.performAction()){return false;}
		return true;
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
		attacker.setAttacking(true);
		if(attacker.isDead()){return false;}
		if(attacker.attack()) {
			for(Player p : players) {
				if(p.getHealth() <= 0) {
					p.setDead(true);
				}
			}
		}
		return true;
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
