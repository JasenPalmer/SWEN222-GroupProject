package gameworld.location;

import gameworld.tile.Tile;

public class OutsideLocation extends Location {

	private static final long serialVersionUID = 1991918042442774460L;
	
	private Tile[][] buildingTiles;

	public OutsideLocation(String name, String description, Tile[][] tiles, Tile[][] buildingTiles) {
		super(name, description, tiles);
		this.buildingTiles = buildingTiles;
	}
	
	public String toString(){
		String toReturn = "";
		toReturn += ""+name+"\n";
		toReturn += ""+description+"\n";
		toReturn += tiles[0].length+" "+tiles.length+"\n";
		for(int i = 0; i < tiles.length; i++){
			for(int j = 0; j < tiles[i].length; j++){
				
				if(tiles[i][j]!=null){
					toReturn+= tiles[i][j].toString();
				} else {
					toReturn+="0";
				}
				
				toReturn+= "-";
				
				if(buildingTiles[i][j]!=null){
					toReturn+= buildingTiles[i][j].toString();
				} else {
					toReturn += "0";
				}
				toReturn += " ";
				
			}
			toReturn += "\n";
		}
		return toReturn;
	}
	
	public Tile[][] getBuildingTiles() {
		return buildingTiles;
	}
	
	public void setBuildingTiles(Tile[][] tiles){
		this.buildingTiles = tiles;
	}
	
	public void setBuildingTile(int x, int y, Tile t){
		this.buildingTiles[y][x] = t;
	}


	
}
