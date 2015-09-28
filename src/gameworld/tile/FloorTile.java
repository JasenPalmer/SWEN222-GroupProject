package gameworld.tile;

import java.awt.Image;
import java.awt.Point;

/**
 * The most basic tile. Represents a floor square in a location
 * @author Jasen
 *
 */
public class FloorTile extends Tile {
	
	private Image image;

	public FloorTile(String name, Point pos, Image image) {
		super(name, pos);
		this.image = image;
	}
	
	public Image getImage() {
		return image;
	}
}
