package gameworld.location;

import gameworld.tile.Tile;

/**
 * An inside location is a location that does not contain any building tiles array
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
		toReturn += ""+name+"\n";
		toReturn += ""+description+"\n";
		toReturn += "inside\n";
		toReturn += tiles[0].length+" "+tiles.length+"\n";
		for(int i = 0; i < tiles.length; i++){
			for(int j = 0; j < tiles[i].length; j++){
				
				if(tiles[i][j]!=null){
					toReturn+= tiles[i][j].toString();
					toReturn+="-0";

				} else {
					toReturn += "0-0";
				}
				
				toReturn+= " ";
				
			}
			toReturn += "\n";
		}
		return toReturn;
	}
}
