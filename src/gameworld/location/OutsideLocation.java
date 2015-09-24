package gameworld.location;

import gameworld.tile.Tile;

public class OutsideLocation extends Location {

	private Tile[][] buildingTiles;

	public OutsideLocation(String name, String description, Tile[][] tiles, Tile[][] buildingTiles) {
		super(name, description, tiles);
		this.buildingTiles = buildingTiles;
	}

	public Tile[][] getBuildingTiles() {
		return buildingTiles;
	}
	
	public String toString(){
		String toReturn = null;
		toReturn += ""+name+"\n";
		toReturn += ""+description+"\n";
		for(int i = 0; i < tiles.length; i++){
			for(int j = 0; j < tiles[i].length; j++){
				
				if(tiles[i][j]!=null){
					toReturn+= tiles[i][j].toString();
				} else {
					toReturn += "0";
				}
				toReturn+= "-";
				if(buildingTiles[i][j]!=null){
					toReturn+= buildingTiles[i][j].toString();
				} else{
					toReturn += "0";
				}
				toReturn += " ";
				
			}
			toReturn += "\n";
		}
		return toReturn;
	}
	
	public void setBuildingTiles(Tile[][] tiles){
		this.buildingTiles = tiles;
	}
	
	public void setBuildingTile(int x, int y, Tile t){
		this.buildingTiles[y][x] = t;
	}
}
