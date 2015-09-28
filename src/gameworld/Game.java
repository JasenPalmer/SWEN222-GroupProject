package gameworld;

import gameworld.location.InsideLocation;
import gameworld.location.Location;
import gameworld.location.OutsideLocation;
import gameworld.tile.BuildingTile;
import gameworld.tile.EntranceExitTile;
import gameworld.tile.FloorTile;
import gameworld.tile.Tile;

import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

import javax.imageio.ImageIO;

import ui.RenderingWindow;

public class Game implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static enum Direction{NORTH, EAST, SOUTH, WEST};

	private Set<Location> locations;
	private Set<Player> players;

	public Game() {
		players = new HashSet<Player>();
		locations = new HashSet<Location>();
		parseLocationFolder("locations");
		for(Location l : locations) {
			System.out.println(l.toString());
		}
		System.out.println(locations.size());
	}
	
	public static void main(String[] args) {
		new Game();
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
					//break blocks up into tiles to be parsed
					Tile tile = parseTile(blockScanner.next(), col, row);
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
			loc = new OutsideLocation(name, desc, locTiles, buildingTiles);
		}
		else{
			loc = new InsideLocation(name, desc, locTiles);
		}
		
		System.out.println("GAME PRINTING");
		for(int i = 0; i < loc.getTiles().length; i++){
			for(int j = loc.getTiles()[i].length-1; j >=0 ; j--){
				System.out.print(loc.getTiles()[i][j]+" ");
			}
			System.out.println("");
		}
		return loc;
	}

	private Tile parseTile(String type, int x, int y) {
		Tile tile = null;
		System.out.println(type);
		switch(type) {
			case "Gr":
				tile = new FloorTile("Grass", new Point(x,y));
				break;
			case "Ro":
				tile = new FloorTile("Rock", new Point(x,y));
				break;
			case "Bu":
				tile = new BuildingTile("Building", new Point(x,y));
				break;
			case "Wa":
				tile = new FloorTile("Water", new Point(x,y));
				break;
			case "En":
				tile = new EntranceExitTile("Entrance", new Point(x,y), true);
				break;
			default:
				break;
		}
		return tile;
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
	public boolean movePlayer(String playerName, Direction direction) {
		Player player = parsePlayer(playerName);
		if(direction == null || player == null) {return false;}
		if(!player.move(direction)) {return false;}
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
	
	public String toString(){
		return "this is the best game ever";
	}
}
