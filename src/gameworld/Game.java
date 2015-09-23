package gameworld;

import gameworld.location.InsideLocation;
import gameworld.location.Location;
import gameworld.location.OutsideLocation;
import gameworld.tile.BuildingTile;
import gameworld.tile.Tile;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

import network.NetworkEvent;
import network.Server;

public class Game implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static enum Direction{NORTH, EAST, SOUTH, WEST};

	/**
	 * Amount of locations there are to create
	 */
	private static final int LOCATION_AMOUNT = 5;
//this
	private Server server;

	private Set<Location> locations;
	private Set<Player> players;

	public Game() {
		locations = new HashSet<Location>();
		for(int i = 0; i < LOCATION_AMOUNT; i++) {
			parseLocationFolder("locations");
		}
	}

	/**
	 * Create a server for a multiplayer game
	 */
	public void createServer() {
		//server = new Server();
		// create a thread to listen to the server for actions
		new Thread(new Runnable(){public void run(){listenToServer();}}).start();
	}

	/**
	 * Create all locations from a folder
	 * @param locationsPath - path to locations folder
	 */
	private void parseLocationFolder(String locationsPath) {
		Scanner fileScan = null;
		try{
			//create the file holding all the location files
			File locFolder = new File("/locations");
			for(File file : locFolder.listFiles()) {
				// open file
				fileScan = new Scanner(file);
				// create the tile array for the location
				Location loc = parseLocationTiles(fileScan);
				if(loc != null){locations.add(loc);}
				fileScan.close();
			}
		}catch(NullPointerException e){
			System.err.println("Path to location folder is incorrect - "+e);
		}catch(FileNotFoundException e){
			System.err.println("File was not found - "+e);
		}catch(NoSuchElementException e){
			System.err.println("A location file has incorrect formatting - "+e);
		}
		finally {
			if(fileScan != null) {
				fileScan.close();
			}
		}
	}

	private Location parseLocationTiles(Scanner file) throws NoSuchElementException{
		//read name
		String name = file.nextLine();
		//read description
		String desc = file.nextLine();
		//read size
		int width = file.nextInt();
		int height = file.nextInt();
		Location loc;
		Tile[][] locTiles = new Tile[height][width];
		Tile[][] buildingTiles = new Tile[height][width];
		//row index
		int row = 0;
		boolean outside = false;
		while(file.hasNextLine()) {
			// scan line by line
			Scanner lineScan = new Scanner(file.nextLine());
			// col index
			int col = 0;
			while(lineScan.hasNext()) {
				// create tile at [row][col]
				Tile tile = parseTile(lineScan.next());
				if(tile instanceof BuildingTile) {
					outside = true;
					buildingTiles[row][col] = tile;
					locTiles[row][col] = null;
				}
				else {
					locTiles[row][col] = tile;
					buildingTiles[row][col] = null;
				}
				col++;
			}
			lineScan.close();
			row++;
		}
		if(outside){
			loc = new OutsideLocation(name, desc, locTiles, buildingTiles);
		}
		else{
			loc = new InsideLocation(name, desc, locTiles);
		}
		return loc;
	}

	private Tile parseTile(String type) {
		switch(type) {
		case "building":
			//return new BuildingTile();
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
	 * Move a player in a direction
	 *
	 * @param player to move
	 * @param direction to move the player
	 * @return true if the player moved successfully
	 */
	public boolean movePlayer(Player player, Direction direction) {
		if(direction == null || player == null) {return false;}
		if(!player.move(direction)) {return false;}
		return true;
	}

	/**
	 * Listen to the server for events
	 */
	private void listenToServer() {
//		while(server.hasEvent()) {
//			actionEvent(server.getEvent());
//		}
	}

	/**
	 * Parse an event from the server
	 * @param event to be parsed
	 */
	private void actionEvent(NetworkEvent event) {
		switch(event.getType()) {
		case KEY_PRESS:
			//movePlayer(event.getPlayer(), parseDirection(event.getKeyCode()));
			break;
		default:
			//throw new InvalidActionException("There was an invalid event recieved from the server");
			break;
		}
	}

	private Direction parseDirection(int keyPress) {
		switch(keyPress) {
		case KeyEvent.VK_W:
			return Direction.NORTH;
		case KeyEvent.VK_D:
			return Direction.EAST;
		case KeyEvent.VK_S:
			return Direction.SOUTH;
		case KeyEvent.VK_A:
			return Direction.WEST;
			default:
				return null;
		}
	}


	public Set<Location> getLocations() {
		return locations;
	}
	public Set<Player> getPlayers() {
		return players;
	}

}
