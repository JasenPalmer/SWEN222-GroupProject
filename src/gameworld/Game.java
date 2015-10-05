package gameworld;

import gameworld.entity.BasicEntity;
import gameworld.entity.Entity;
import gameworld.entity.Key;
import gameworld.location.InsideLocation;
import gameworld.location.Location;
import gameworld.location.OutsideLocation;
import gameworld.tile.BuildingTile;
import gameworld.tile.EntranceExitTile;
import gameworld.tile.FloorTile;
import gameworld.tile.Tile;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

public class Game implements Serializable {

	private static final long serialVersionUID = 1L;

	public static enum Direction{NORTH, EAST, SOUTH, WEST};

	private Set<Location> locations;
	private Set<Player> players;

	public Game() {
		players = new HashSet<Player>();
		locations = new HashSet<Location>();
		parseLocationFolder("locations");
	}

	/**
	 * Create all locations from a folder
	 * @param locationsPath - path to locations folder
	 */
	private void parseLocationFolder(String locationsPath) {
		Scanner fileScan = null;
		try{
			//create the file holding all the location files
			File locFolder = new File("src/"+locationsPath);
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
		//read location size
		int width = file.nextInt();
		int height = file.nextInt();
		Location loc;
		Tile[][] locTiles = new Tile[height][width];
		Tile[][] buildingTiles = new Tile[height][width];
		Entity[][] entities = new Entity[height][width];
		//row index
		int row = 0;
		boolean outside = false;
		while(file.hasNextLine()) {
			// scan line by line
			Scanner lineScan = new Scanner(file.nextLine());
			int col = 0;
			while(lineScan.hasNext()) {
				// break the line up into blocks
				Scanner blockScanner = new Scanner(lineScan.next());
				blockScanner.useDelimiter("-");
				while(blockScanner.hasNext()) {
					String temp =  blockScanner.next();
					//if the next block segment is an entity create it
					Entity ent = parseEntity(temp,col,row);
					if(ent != null){
						entities[row][col] = ent;
						continue;
					}
					//otherwise create a tile
					Tile tile = parseTile(temp, col, row);
					if(tile == null){continue;}
					if(tile instanceof BuildingTile || tile instanceof EntranceExitTile) {
						outside = true;
						buildingTiles[row][col] = tile;
					}
					else {
						locTiles[row][col] = tile;
					}
				}
				blockScanner.close();
				col += 1;
			}
			lineScan.close();
			row += 1;
		}
		if(outside){
			loc = new OutsideLocation(name, desc, locTiles, buildingTiles, entities);
		}
		else{
			loc = new InsideLocation(name, desc, locTiles, entities);
		}

		return loc;
	}

	private Tile parseTile(String type, int x, int y) {
		Tile tile = null;
		switch(type) {
			case "Gr":
				tile = new FloorTile("Grass", new Point(x,y), true);
				break;
			case "Ro":
				tile = new FloorTile("Rock", new Point(x,y), true);
				break;
			case "Bu":
				tile = new BuildingTile("Building", new Point(x,y), false);
				break;
			case "Wa":
				tile = new FloorTile("Water", new Point(x,y), false);
				break;
			case "En":
				tile = new EntranceExitTile("Entrance", new Point(x,y), false, true);
				break;
			default:
				break;
		}
		return tile;
	}

	private Entity parseEntity(String type, int col, int row) {
		Entity ent = null;
		switch(type) {
		case "Rock":
			ent = new BasicEntity("Rock", "A rock", new Point(col, row));
		case "Tree":
			ent = new BasicEntity("Tree","A tree", new Point(col, row));
		case "Table":
			ent = new BasicEntity("Table","A table", new Point(col, row));
		case "key":
			ent = new Key();
		}

		return ent;
	}


	/**
	 * Add a player to the game
	 * @param player to add
	 */
	public void addPlayer(Player player) {
		players.add(player);
	}

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
		if(!player.move(direction)) {return false;}
		return true;
	}

	public boolean playerPickup(String playerName) {
		Player player = parsePlayer(playerName);
		if(player == null){return false;}
		if(!player.pickupItem()){return false;}
		return true;
	}

	public boolean attackPlayer(Player attacker, Player defender) {
		if(attacker.attack(defender)){
			removePlayer(defender);
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

	public Set<Location> getLocations() {
		return locations;
	}
	public Set<Player> getPlayers() {
		return players;
	}

}
