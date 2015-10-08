package gameworld.location;

import gameworld.Player;
import gameworld.entity.Entity;
import gameworld.tile.Tile;

import java.awt.Point;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a location in the game.
 * Locations are made from a 2D array of Tiles
 * @author Jasen
 *
 */
public abstract class Location implements Serializable{

	private static final long serialVersionUID = 1L;
	protected String name;
	protected String description;
	protected Tile[][] tiles;
	protected Set<Player> players;

	public Location(String name, String description, Tile[][] tiles) {
		this.name = name;
		this.description = description;
		this.tiles = tiles;
		players = new HashSet<Player>();
	}
	
	/**
	 * Add a player to this location
	 * @param player to add
	 */
	public void addPlayer(Player player) {
		players.add(player);
	}
	
	/**
	 * Removes a player from the location
	 * @param player to remove
	 */
	public void removePlayer(Player player) {
		players.remove(player);
	}

	/**
	 * @return the name of the location
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the description of the location
	 */
	public String getDescritpion() {
		return description;
	}

	/**
	 * @return the tiles that make up this location
	 */
	public Tile[][] getTiles() {
		return tiles;
	}

	/**
	 * @return width of the location
	 */
	public int width() {
		return tiles[0].length;
	}

	/**
	 * @return height of the location
	 */
	public int height() {
		return tiles.length;
	}

	/**
	 * Get the tile at a position in the location
	 * 
	 * @param pos - position of the tile
	 * @return the tile at the position
	 */
	public Tile getTileAt(Point pos) {
		if(pos.x < 0 || pos.x >= tiles.length || pos.y < 0 || pos.y >= tiles.length) {
			return null;
		}
		return tiles[pos.y][pos.x];
	}
	
	/**
	 * Update the tile at a position with a new tile
	 * @param x coordinate
	 * @param y coordinate
	 * @param t - tile
	 */
	public void setTile(int x, int y, Tile t){
		tiles[y][x] = t;
	}
	
	/**
	 * @return the entities in the location in the format that the location files use
	 */
	public String entitiesToString(){
		String toReturn = "";
		toReturn += name+"\n";
		for(int i = 0; i < tiles.length; i++){
			for(int j = 0; j < tiles.length; j++){
				if(tiles[i][j]!=null){
					Entity entity = tiles[i][j].containedEntity();
					if(entity!=null){				
						toReturn += entity.getClass().getSimpleName() +"\t"+ entity.getName() +"\t"+entity.getDescription()+"\t"+j+"\t"+i+"\n";
					}
				}
			}
		}
		return toReturn;
	}
	
	//setters

	public void setName(String name) {
		this.name = name;
		
	}

	public void setDescription(String description) {
		this.description = description;
		
	}

	/**
	 * Set the entity in a tile
	 * @param x coordinate
	 * @param y coordinate
	 * @param entity to be set
	 */
	public void setEntity(int x, int y, Entity entity) {
		if(tiles[y][x]!=null){
			tiles[y][x].setEntitiy(entity);
		}
	}
	
	/**
	 * Set the entity in a tile 
	 * @param pos - position of the tile
	 * @param entity to be set
	 */
	public void setEntity(Point pos, Entity entity) {
		if(tiles[pos.y][pos.x]!=null){
			tiles[pos.y][pos.x].setEntitiy(entity);
		}
	}
	
	
}
