package gameworld;

import gameworld.entity.BasicEntity;
import gameworld.entity.Chest;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

public class Parser {
	
	private static Set<Location> locations;
	
	//######## Locations Parser ########//
	
	/**
	 * Create all locations from a folder
	 * @param locationsPath - path to locations folder
	 */
	public static Set<Location> loadLocations(String locationsPath) {
		Scanner fileScan = null;
		Set<Location> locs = new HashSet<Location>();
		try{
			//create the file holding all the location files
			File locFolder = new File("src/"+locationsPath);
			for(File file : locFolder.listFiles()) {
				// open file
				fileScan = new Scanner(file);
				// create the tile array for the location
				Location loc = parseLocationTiles(fileScan);
				if(loc != null){locs.add(loc);}
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
		locations = locs;
		return locs;
	}
	
	private static Location parseLocationTiles(Scanner file) throws NoSuchElementException{
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
		file.nextLine();
		while(file.hasNextLine()) {
			// scan line by line
			Scanner lineScan = new Scanner(file.nextLine());
			int col = 0;
			while(lineScan.hasNext()) {
				// break the line up into blocks
				Scanner blockScanner = new Scanner(lineScan.next());
				blockScanner.useDelimiter("-");
				while(blockScanner.hasNext()) {
					String temp = blockScanner.next();
					//otherwise create a tile
					Tile tile = parseTile(temp, col, row);
					if(tile == null){continue;}
					if(tile instanceof BuildingTile) {
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

		return loc;
	}
	
	private static Tile parseTile(String type, int x, int y) {
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
	
	//######## Entity Parser ########//
	
	public static void loadEntityFiles() {
		Scanner fileScan = null;
		try {
			File folder = new File("src/entities");
			for(File entList : folder.listFiles()) {
				fileScan = new Scanner(entList);
				String location = fileScan.nextLine();
				Location loc = getLocation(location);
				if(loc == null){
					System.err.println("Location with the name "+location+" was not found");
					continue;
				}
				List<Entity> ents = parseEntities(fileScan, loc);
				for(Entity i : ents) {
					loc.setEntity(i.getPosition(), i);
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(fileScan != null) {
				fileScan.close();
			}
		}
	}
	
	private static Location getLocation(String locationName) {
		for(Location loc : locations) {
			if(loc.name().equals(locationName)) {
				return loc;
			}
		}
		return null;
	}

	private static List<Entity> parseEntities(Scanner fileScan,
			Location loc) {
		List<Entity> list = new ArrayList<Entity>();
		while(fileScan.hasNextLine()) {
			String line = fileScan.nextLine();
			Scanner lineScan = new Scanner(line);
			lineScan.useDelimiter("\\t");
			//lineScan.next();
			if(!lineScan.hasNext()){
				System.err.println("Entity file formatted incorrectly(maybe?)");
				continue;
			}
			String entType = lineScan.next();
			Entity entity = parseEntity(entType, lineScan, loc);
			if(entity == null){System.err.println("Entity format incorrect");}
			list.add(entity);
		}
		return list;
	}
	
	private static Entity parseEntity(String type, Scanner scan, Location loc) {
		if(!scan.hasNext()) {return null;}
		// get entity name
		String name = scan.next();
		if(!scan.hasNext()) {return null;}
		// get entity description
		String desc = scan.next();
		if(!scan.hasNextInt()) {return null;}
		// x position
		int xPos = scan.nextInt();
		if(!scan.hasNextInt()) {return null;}
		// y position
		int yPos = scan.nextInt();
		switch(type) {
		case "Key":
			return new Key(name, desc, new Point(xPos,yPos), loc);
		case "Chest":
			return new Chest(name, desc, new Point(xPos, yPos), loc);
		case "BasicEntity":
			return new BasicEntity(name, desc, new Point(xPos,yPos), loc);
		}
		return null;
	}
	
	//######## Door Parser ########//
	
	public static Map<EntranceExitTile,EntranceExitTile> loadDoors() {
		try {
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
}
