package gameworld;

import gameworld.entity.BasicEntity;
import gameworld.entity.Chest;
import gameworld.entity.Entity;
import gameworld.entity.Key;
import gameworld.location.InsideLocation;
import gameworld.location.Location;
import gameworld.location.OutsideLocation;
import gameworld.tile.BuildingTile;
import gameworld.tile.EntranceTile;
import gameworld.tile.FloorTile;
import gameworld.tile.Tile;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * The Parser contains static methods used by the game class to load in the locations, entities and doors
 * @author Jasen
 */
public class Parser {

	private static Set<Location> locations;

	private static final String entitiesPath = "entities";
	private static final String locationsPath = "locations";
	private static final String entrancePath = "doors/doors.txt";
	
	/**
	 * list of the location files names
	 */
	private static final String[] locationNames = {
		"big-maze.txt", "DiningRoom.txt", "HallRoom1.txt", "HallRoom2.txt",
		"HallRoom3.txt", "HallRoom4.txt", "hallway.txt", "Hub.txt",
		"inside.txt", "MAP.txt", "mattsDankMap.txt", "TRoom1.txt", 
		"TRoom2.txt", "YEAH DANK.txt", "LongRoom.txt", "LootRoom.txt"
	};
	
	/**
	 * List of the entity file names
	 */
	private static final String[] entityNames = {
		"big-maze-entites.txt", "DiningRoom-entites.txt",
		"HallRoom1-entites.txt", "HallRoom2-entites.txt",
		"HallRoom3-entites.txt", "HallRoom4-entites.txt",
		"hallway-entites.txt", "Hub-entites.txt",
		"inside-entites.txt", "MapEntities.txt",
		"mattsDankMap-entites.txt", "TRoom1-entites.txt",
		"TRoom2-entites.txt", "YEAH DANK-entites.txt",
		"LongRoom-entites.txt", "LootRoom-entites.txt"
	};

	//######## Locations Parser ########//

	/**
	 * Create all locations from a folder
	 */
	public static Set<Location> loadLocations() {
		Scanner fileScan = null;
		Set<Location> locs = new HashSet<Location>();
		Scanner folderScan = null;
		try{
			//create the file holding all the location files
			for(String l : locationNames) {
				// open file
				InputStream locFile = Parser.class.getResourceAsStream(locationsPath+"/"+l);
				fileScan = new Scanner(locFile);
				// create the tile array for the location
				Location loc = parseLocationTiles(fileScan);
				if(loc != null){locs.add(loc);}
				else {throw new ParserException("Created location was null - parsing failed");}
				fileScan.close();
			}
		}catch(NullPointerException e){
			System.err.println("Path to location folder is incorrect");
			e.printStackTrace();
		}catch(ParserException e) {
			e.printStackTrace();
		}
		finally {
			if(fileScan != null) {
				fileScan.close();
			}
			if(folderScan != null) {
				folderScan.close();
			}
		}
		locations = locs;
		return locs;
	}


	private static Location parseLocationTiles(Scanner file) throws ParserException{
		//read name
		if(!file.hasNextLine()){throw new ParserException("incorrect formatting in location name");}
		String name = file.nextLine();
		//read description
		if(!file.hasNextLine()){throw new ParserException("incorrect formatting in location description");}
		String desc = file.nextLine();
		if(!file.hasNextLine()){throw new ParserException("inccorect formatting in location type");}
		String locType = file.nextLine();
		locType = locType.toLowerCase();
		//read location size
		if(!file.hasNextInt()){throw new ParserException("incorrect formatting in location width");}
		int width = file.nextInt();
		if(!file.hasNextInt()){throw new ParserException("incorrect formatting in location hieght");}
		int height = file.nextInt();
		Location loc;
		Tile[][] locTiles = new Tile[height][width];
		Tile[][] buildingTiles = new Tile[height][width];
		//row index
		int row = 0;
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
					if(tile instanceof BuildingTile || tile instanceof EntranceTile) {
						buildingTiles[row][col] = tile;
					}
					if(tile instanceof EntranceTile && locType.equals("inside")) {
						locTiles[row][col] = tile;
					}
					else if(tile instanceof EntranceTile && locType.equals("outside")) {
						locTiles[row][col] = tile;
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
		if(locType.equals("outside")){
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
			case "EnB":
				tile = new EntranceTile("Entrance", new Point(x,y), false, EntranceTile.Type.BUILDING);
				break;
			case "EnV":
				tile = new EntranceTile("Entrance", new Point(x,y), false, EntranceTile.Type.INVISIBLE);
				break;
			case "Fl":
				tile = new FloorTile("Floor", new Point(x,y),true);
			default:
				break;
		}
		return tile;
	}

	//######## Entity Parser ########//


	/**
	 * Load all the entities for every location
	 */
	public static void loadEntityFiles() {
		Scanner fileScan = null;
		try {
			for(String e : entityNames) {
				InputStream entFile = Parser.class.getResourceAsStream(entitiesPath+"/"+e);
				fileScan = new Scanner(entFile);
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
		}catch(ParserException e) {
			e.printStackTrace();
		}finally {
			if(fileScan != null) {
				fileScan.close();
			}
		}
	}

	private static Location getLocation(String locationName) {
		for(Location loc : locations) {
			if(loc.getName().equals(locationName)) {
				return loc;
			}
		}
		return null;
	}

	private static List<Entity> parseEntities(Scanner fileScan,
			Location loc) throws ParserException{

		List<Entity> list = new ArrayList<Entity>();
		while(fileScan.hasNextLine()) {
			String line = fileScan.nextLine();
			Scanner lineScan = new Scanner(line);
			lineScan.useDelimiter("\\t");
			//lineScan.next();
			if(!lineScan.hasNext()){
				System.err.println("Entity file formatted incorrectly");
				continue;
			}
			String entType = lineScan.next();
			Entity entity = parseEntity(entType, lineScan, loc);
			if(entity == null){throw new ParserException("Entity format incorrect ");}
			list.add(entity);
		}
		return list;
	}

	private static Entity parseEntity(String type, Scanner scan, Location loc)  throws ParserException {
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
		default:
			throw new ParserException("Unknown entity type - "+type);
		}
	}

	//######## Entrance Parser ########//

	/**
	 * Load all doors for all locations
	 */
	public static void loadEntrances() {
		Scanner fileScan = null;
		try {
			fileScan = new Scanner(Parser.class.getResource(entrancePath).openStream());
			while(fileScan.hasNextLine()) {
				String doorScan = fileScan.nextLine();
				//skip commented lines
				if(doorScan.startsWith("#")) {continue;}
				String[] elements = doorScan.split("\\t");
				if(elements.length != 6){throw new ParserException("Entrance formatted incorrectly - "+doorScan);}
				Location fromLocation = getLocation(elements[0]);
				if(fromLocation == null){throw new ParserException("location -"+elements[0]+" - was not found");}
				int y1 = Integer.parseInt(elements[1]);
				int x1 = Integer.parseInt(elements[2]);
				Tile tile = fromLocation.getTileAt(new Point(y1,x1));
				if(tile == null){ throw new ParserException("Position ("+x1+","+y1+") was out of bounds for location "+elements[0]);}
				if(!(tile instanceof EntranceTile)) {
					throw new ParserException("Tile at ("+x1+","+y1+") in location "+fromLocation.getName()+" wasn't an EntranceExitTile");
				}
				EntranceTile fromTile = (EntranceTile) tile;
				Location toLocation = getLocation(elements[3]);
				if(toLocation == null){throw new ParserException("location -"+elements[3]+" - was not found");}
				fromTile.setExitLoc(toLocation);
				int y2 = Integer.parseInt(elements[4]);
				int x2 = Integer.parseInt(elements[5]);
				Tile toTile = toLocation.getTileAt(new Point(y2,x2));
				if(toTile == null){ throw new ParserException("Position ("+x2+","+y2+") was out of bounds for location "+elements[3]);}
				fromTile.setExitTile(toTile);
			}

		}catch(FileNotFoundException e) {
			System.err.println("Entrance file not found");
			e.printStackTrace();
		}catch(ParserException e) {
			e.printStackTrace();
		}catch(NumberFormatException e) {
			System.err.println("Incorrect format with TO and/or FROM positions");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(fileScan != null) {
				fileScan.close();
			}
		}
	}



}
