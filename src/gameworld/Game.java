package gameworld;

import gameworld.location.Location;
import gameworld.tile.BuildingTile;
import gameworld.tile.Tile;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

import network.NetworkEvent;
import network.Server;

public class Game {

	public static enum Direction{NORTH, EAST, SOUTH, WEST};

	/**
	 * Amount of locations there are to create
	 */
	private static final int LOCATION_AMOUNT = 5;

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
				//read name
				String name = fileScan.nextLine();
				//read description
				String desc = fileScan.nextLine();
				//read size
				int width = fileScan.nextInt();
				int height = fileScan.nextInt();
				// create the tile array for the location
				Tile[][] locTiles = new Tile[height][width];
				Location loc = parseLocationTiles(fileScan, locTiles, name, desc);
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

	private Location parseLocationTiles(Scanner file, Tile[][] locTiles, String name, String description) throws NoSuchElementException{
		Location loc;
		Tile[][] buildingTiles = new Tile[locTiles.length][locTiles[0].length];
		//row index
		int row = 0;
		while(file.hasNextLine()) {
			// scan line by line
			Scanner lineScan = new Scanner(file.nextLine());
			// col index
			int col = 0;
			while(lineScan.hasNext()) {
				// create tile at [row][col]
				Tile tile = parseTile(lineScan.next());
				if(tile instanceof BuildingTile) {
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
		return null;
	}

	private Tile parseTile(String type) {

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
