package mapeditor;

import gameworld.Game;
import gameworld.Player;
import gameworld.location.Location;
import gameworld.tile.BuildingTile;
import gameworld.tile.EntranceTile;
import gameworld.tile.Tile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class EditorCanvas extends JPanel {

	private Location location;
	
	private int MAPHEIGHT = 15;
	private int MAPWIDTH = 15;
	private int TILESIZE = 64;
	
	Game.Direction direction = Game.Direction.NORTH;
	
	private Image backgroundImage;
	private Image grass;
	private Image building;
	private Image water;
	private Image rock;
	private Image room;
	private Image doorUD;
	private Image doorLR;
	private Image roofLR;
	private Image roofUD;
	private Image roofCornerO;
	private Image roofCornerI;
	
	/**
	 * Create the panel.
	 */
	public EditorCanvas(Location location) {
		this.location = location;
		setImages();
	}
	
	/**
	 * Setting images to files in images folder. Will be changed when location parser is working for some tiles. (grass, water, rock will be gone).
	 */
	private void setImages() {
		try{
			backgroundImage = ImageIO.read(new File("src/ui/images/renderingWindowTemp.jpg"));
			grass = ImageIO.read(new File("src/ui/images/terrain/Grass.png"));
			building = ImageIO.read(new File("src/ui/images/buildings/Room.png"));
			water = ImageIO.read(new File("src/ui/images/terrain/Water.png"));
			rock = ImageIO.read(new File("src/ui/images/terrain/Rock.png"));
			room = ImageIO.read(new File("src/ui/images/buildings/Room.png"));
			doorUD = ImageIO.read(new File("src/ui/images/buildings/DoorUD.png"));
			doorLR = ImageIO.read(new File("src/ui/images/buildings/DoorLR.png"));
			roofLR = ImageIO.read(new File("src/ui/images/buildings/RoofLR.png"));
			roofUD = ImageIO.read(new File("src/ui/images/buildings/RoofUD.png"));
			roofCornerO = ImageIO.read(new File("src/ui/images/buildings/RoofCornerO.png"));
			roofCornerI = ImageIO.read(new File("src/ui/images/buildings/RoofCornerI.png"));
			
			
		}catch(IOException e){
			System.out.println(e.getLocalizedMessage());
		}
	}
	
	public void paint(Graphics g){
		Tile[][] tiles = location.getTiles();
		Tile[][] rooms = location.getTiles(); // CHANGE TO GET BUILDINGS/ROOM WHEN IMPLEMENTED. THIS IS IMPORTANT.
//		Item[][] items = location.getItems();
		
		Image offscreen = createImage(MAPWIDTH*TILESIZE, MAPHEIGHT*TILESIZE);
		Graphics offgc = offscreen.getGraphics();
		
		switch(direction){
		case NORTH:
			isometric(tiles,rooms, offgc);
			//drawNorth(tiles,rooms,offgc);	
			break;
		case EAST:
			isometric(rotate(tiles), rotate(rooms), offgc);		
			break;			
		case SOUTH:
			isometric(rotate(rotate(tiles)), rotate(rotate(rooms)), offgc);		
			break;	
		case WEST:
			isometric(rotate(rotate(rotate(tiles))), rotate(rotate(rotate(rooms))), offgc);	
			break;
			
		}

		g.drawImage(offscreen,0,0,null);
	}
	


	/**
	 * Iterates through arrays of tiles drawing the location terrain, then iterates through
	 * room tiles to draw buildings depending on the arrangement of tiles.
	 * 
	 * new x position = x/2 + y/2 ( + or - constants to fit properly such as imageHeight)
	 * new y position = y/4 - x/4 ( + or - constants to fit properly such as imageHeight)
	 * 
	 * 
	 * @param tiles - 2D array of terrain to be drawn
	 * @param rooms - 2D array of rooms to be drawn
	 * @param g - the graphics
	 */
	public void isometric(Tile[][] tiles, Tile[][] rooms, Graphics g){

		g.fillRect(0,0,this.getWidth(), this.getHeight());
		
		// outside tiles
		for(int i = 0; i < tiles.length; i++){
			for(int j = tiles[i].length-1; j >=0 ; j--){
				Tile t = tiles[i][j];
				if(t!=null) {
					// DRAWING TERRAIN
					
					Image image = t.getImg();
					g.drawImage(image, (j*TILESIZE/2) + (i*TILESIZE/2), ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 , null);
					
					// DRAWING ROOMS
					
					Tile r = rooms[i][j];
					if(r!=null) {					
						// Drawing 2 block high walls
						if(r instanceof EntranceTile){
							if(j-1 >= 0 && rooms[i][j-1]==null){
								g.drawImage(doorUD, (j*TILESIZE/2) + (i*TILESIZE/2), ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 -TILESIZE/2, null);
							} else {
								g.drawImage(doorLR, (j*TILESIZE/2) + (i*TILESIZE/2), ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 -TILESIZE/2, null);
							}
						}
						else{
							g.drawImage(building, (j*TILESIZE/2) + (i*TILESIZE/2), ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 -TILESIZE/2, null);
						}
						g.drawImage(building, (j*TILESIZE/2) + (i*TILESIZE/2), ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 -TILESIZE, null);
						

						image = building;
						
						// Western most point of building
						if(j-1 >= 0 && rooms[i][j-1] == null){
							image = roofUD;
						}

						// Northern most point of building
						if(i+1 < rooms.length && rooms[i+1][j]==null){
							image = roofLR;
						}

						// Outwards corner roof
						if(j-1 >= 0 && i+1<rooms.length && rooms[i][j-1]==null && rooms[i+1][j]==null){
							image = roofCornerO;
						}
						// Inwards corner roof
						if(j-1 >= 0 && i+1 != rooms.length && rooms[i+1][j-1]==null && rooms[i][j-1] != null && rooms[i+1][j]!=null){
							image = roofCornerI;
						}
						
					
						g.drawImage(image, (j*TILESIZE/2) + (i*TILESIZE/2), (int) (((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 - (TILESIZE*1.5)), null);

					}
				}
			}

		}
	}
	
	/**
	 * Draws the map from a BOV for making maps because I couldn't get clicking on positions working properly. :~(
	 * @param tiles - array of location tiles
	 * @param rooms - array of location buildingtiles
	 * @param offgc - graphics to draw to
	 */
	public void shittyDraw(Tile[][] tiles, BuildingTile[][] rooms, Graphics offgc){
		for(int i = 0; i < tiles.length; i++){
			for(int j = 0; j < tiles[i].length; j++){
				Tile t = tiles[i][j];
				if(t!=null){
					Image image = t.getImg();
					offgc.drawImage(image, j*TILESIZE, i*TILESIZE-image.getHeight(null)+TILESIZE, null);
				} else {
					offgc.setColor(Color.WHITE);
					offgc.fillRect(j*TILESIZE, i*TILESIZE, TILESIZE, TILESIZE);
					offgc.setColor(Color.BLACK);
					offgc.drawRect(j*TILESIZE, i*TILESIZE, TILESIZE, TILESIZE);
				}
			}
		}
		
	}
	
	public Tile[][] rotate(Tile[][] tiles){
		Tile[][] newTiles = new Tile[tiles.length][tiles[0].length];
		// drawing floor
		for(int i = 0; i < tiles.length; i++){
			for(int j = 0; j < tiles[i].length; j++){
				newTiles[(newTiles.length-1)-j][i] = tiles[i][j];
			}
		}
		return newTiles;
	}
	

	
	public void setDirection(Game.Direction d){
		direction = d;
	}

}
