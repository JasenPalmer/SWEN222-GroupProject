package gameworld.location;

import gameworld.tile.Tile;

/**
 * Represents a location that a player or an object can be in
 * @author Jasen
 *
 */
public class InsideLocation extends Location{

	private static final long serialVersionUID = 43788886124239093L;

	public InsideLocation(String name, String description, Tile[][] tiles) {
		super(name, description, tiles);
	}

	public String toString(){
		String toReturn = "";
		toReturn += "Inside\n";
		toReturn += ""+name+"\n";
		toReturn += ""+description+"\n";
		toReturn += tiles[0].length+" "+tiles.length+"\n";
		for(int i = 0; i < tiles.length; i++){
			for(int j = 0; j < tiles[i].length; j++){
				
				if(tiles[i][j]!=null){
					toReturn+= tiles[i][j].toString();
					toReturn+="-";
					if(tiles[i][j].containedEntity()!=null){
						toReturn+=tiles[i][j].containedEntity().name().substring(0,2);
					} else{
						toReturn+="0";
					}
				} else {
					toReturn += "0-0";
				}
				
				toReturn+= "-0 ";
				
			}
			toReturn += "\n";
		}
		return toReturn;
	}
}
