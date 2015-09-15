package gameworld;

import gameworld.location.Location;
import gameworld.tile.Tile;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Game {

	public static enum Direction{NORTH, EAST, SOUTH, WEST};

	/**
	 * Amount of locations there are to create
	 */
	private static final int LOCATION_AMOUNT = 5;

	private Set<Location> locations;
	private Set<Player> players;

//	private Server server;

	public Game() {
		locations = new HashSet<Location>();
		for(int i = 0; i < LOCATION_AMOUNT; i++) {
			parseLocation("location"+i+".txt");
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
	 * Create a location from a file
	 * @param filename of location to be created
	 * @return the created location
	 */
	private Location parseLocation(String filename) {
		if(filename == null){return null;}
		try{
			// open file with filename
			Scanner scan = new Scanner(new File(filename));
			// create the tile array for the location
			Tile[][] locTiles = new Tile[Location.DEFAULT_LOC_SIZE][Location.DEFAULT_LOC_SIZE];
			//row index
			int row = 0;
			while(scan.hasNextLine()) {
				// scan line by line
				Scanner lineScan = new Scanner(scan.nextLine());
				// col index
				int col = 0;
				while(lineScan.hasNext()) {
					// create tile at [row][col]
					locTiles[row][col] = parseTile(lineScan.next());
					col++;
				}
				lineScan.close();
				row++;
			}
			scan.close();
			Location loc = new Location();

		}catch(IOException e){
			System.err.println("File not found");
		}finally {
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
		if(!player.move(direction)) {return false;}
		return true;
	}

	/**
	 * Listen to the server for events
	 */
	public void listenToServer() {
//		while(server.hasEvent()) {
//			actionEvent(server.getEvent());
//		}
	}

	/**
	 * Parse an event from the server
	 * @param event to be parsed
	 */
	private void actionEvent(ActionEvent event) {
		switch(event.getActionCommand()) {
		case "move":
			//movePlayer(event.getPlayer(), parseDirection(event.keyPress()));
			break;
		case "interact":
			//playerInteraction(event.getPlayer(), parseKeyPress(event.keyPress()));
			break;
		default:
			//throw new InvalidActionException("There was an invalid event recieved from the server");
			break;
		}
	}

	private Direction parseDirection(String keyPress) {
		switch(keyPress) {
		case"UP":
			return Direction.NORTH;
		case"RIGHT":
			return Direction.EAST;
		case"DOWN":
			return Direction.SOUTH;
		case"LEFT":
			return Direction.WEST;
			default:
				return null;
		}
	}
}
