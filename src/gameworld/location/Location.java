package gameworld.location;

import gameworld.entity.Entity;
import gameworld.tile.Tile;

import java.awt.Point;
import java.io.Serializable;

public abstract class Location implements Serializable{

	private static final long serialVersionUID = 1L;
	protected String name;
	protected String description;
	protected Tile[][] tiles;

	public Location(String name, String description, Tile[][] tiles) {
		this.name = name;
		this.description = description;
		this.tiles = tiles;
	}

	/**
	 * @return the name of the location
	 */
	public String name() {
		return name;
	}

	/**
	 * @return the description of the location
	 */
	public String descritpion() {
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
	 * @param pos - position of the tile
	 * @return the tile at the position
	 */
	public Tile getTileAt(Point pos) {
		if(pos.x < 0 || pos.x >= tiles.length || pos.y < 0 || pos.y >= tiles.length) {
			return null;
		}
		return tiles[pos.y][pos.x];
	}
	
	public void setTiles(Tile[][] tiles){
		this.tiles = tiles;
	}
	
	public void setTile(int x, int y, Tile t){
		tiles[y][x] = t;
	}
	
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

	public void setName(String name) {
		this.name = name;
		
	}

	public void setDescription(String description) {
		this.description = description;
		
	}

	public void setEntity(int x, int y, Entity entity) {
		if(tiles[y][x]!=null){
			System.out.println("Setting entity");
			tiles[y][x].setEntitiy(entity);
		}
	}
	
	public void setEntity(Point pos, Entity entity) {
		if(tiles[pos.y][pos.x]!=null){
			System.out.println("Setting entity");
			tiles[pos.y][pos.x].setEntitiy(entity);
		}
	}
	
	
}
