package mapeditor;

import gameworld.Game;
import gameworld.location.InsideLocation;
import gameworld.location.Location;
import gameworld.location.OutsideLocation;
import gameworld.tile.EntranceExitTile;
import gameworld.tile.FloorTile;
import gameworld.tile.Tile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import ui.RenderingWindow;


public class EditorCanvas extends JPanel {

	private Location location;
	private String view = "Shit";

	private int MAPHEIGHT = 15;
	private int MAPWIDTH = 15;
	private int TILESIZE = 64;
	private int cameraX = 0;
	private int cameraY = 0;

	Game.Direction direction = Game.Direction.NORTH;
	
	private static Image grass;
	private static Image building;
	private static Image water;
	private static Image rock;
	private Image doorUD;
	private static Image doorLR;
	private Image roofLR;
	private Image roofUD;
	private Image roofCornerO;
	private Image roofCornerI;

	/**
	 * Create the panel.
	 */
	public EditorCanvas(Location location) {
		setImages();
		setLayout(null);
		setBounds(0,0,2000,750);

		if(location instanceof OutsideLocation){
			location = (OutsideLocation) location;
		} else {
			location = (InsideLocation) location;
		}
		this.location = location;
	}

	/**
	 * Setting images to files in images folder. Will be changed when location parser is working for some tiles. (grass, water, rock will be gone).
	 */
	private void setImages() {
		try{
			grass = ImageIO.read(new File("src/ui/images/terrain/Grass.png"));
			building = ImageIO.read(new File("src/ui/images/buildings/Room.png"));
			water = ImageIO.read(new File("src/ui/images/terrain/Water.png"));
			rock = ImageIO.read(new File("src/ui/images/terrain/Rock.png"));
			doorUD = ImageIO.read(new File("src/ui/images/buildings/DoorUD.png"));
			doorLR = ImageIO.read(new File("src/ui/images/buildings/DoorLR.png"));
			roofLR = ImageIO.read(new File("src/ui/images/buildings/RoofLR.png"));
			roofUD = ImageIO.read(new File("src/ui/images/buildings/RoofUD.png"));
			roofCornerO = ImageIO.read(new File("src/ui/images/buildings/RoofCornerO.png"));
			roofCornerI = ImageIO.read(new File("src/ui/images/buildings/RoofCornerI.png"));


		}catch(IOException e){
			System.err.println(e.getLocalizedMessage());
		}
	}
	
	/**
	 * Returns image to match given name
	 * @param name - type of image wanted
	 * @return image based on name
	 */
	public static Image getImage(String name){
		switch(name){
		case "Gr":
			return grass;
		case "Ro":
			return rock;
		case "Wa":
			return water;
		case "Bu":
			return building;
		case "En":
			return doorLR;
		}
		return null;
	}

	public void paint(Graphics g){
		Tile[][] tiles = location.getTiles();
		Tile[][] rooms = null;

		if(location instanceof OutsideLocation){
			OutsideLocation l = (OutsideLocation) location;
			rooms = l.getBuildingTiles();
		}

		Image offscreen = createImage(MAPWIDTH*TILESIZE, MAPHEIGHT*TILESIZE);
		Graphics offgc = offscreen.getGraphics();

//		for(int i = 0; i < tiles.length; i++){
//			for(int j = 0; j <tiles.length ; j++){
//				if(tiles[i][j]!=null){
//					System.out.print(tiles[i][j].containedEntity()+" ");
//				} else{
//					System.out.print("null ");
//				}
//			}
//			System.out.println("");
//		}
		
		switch(direction){
		case NORTH:
			if(view.equals("Render")){
				isometric(tiles,rooms, offgc);
			} else {
				shittyDraw(tiles,rooms,offgc);
			}
			break;
		case EAST:
			if(view.equals("Render")){
				isometric(rotate(tiles), rotate(rooms), offgc);
			} else {
				shittyDraw(rotate(tiles), rotate(rooms), offgc);
			}
			break;
		case SOUTH:
			if(view.equals("Render")){
				isometric(rotate(rotate(tiles)), rotate(rotate(rooms)), offgc);
			} else {
				shittyDraw(rotate(rotate(tiles)), rotate(rotate(rooms)), offgc);
			}
			break;
		case WEST:
			if(view.equals("Render")){
				isometric(rotate(rotate(rotate(tiles))), rotate(rotate(rotate(rooms))), offgc);
			} else {
				shittyDraw(rotate(rotate(rotate(tiles))), rotate(rotate(rotate(rooms))), offgc);
			}
			break;

		}

		g.drawImage(offscreen,0,0,null);
	}



	/**
	 * Iterates through arrays of tiles drawing the map terrain, then iterates through
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
				FloorTile t = (FloorTile) tiles[i][j];
				Tile r = rooms[i][j];
				Image image = null;

				// DRAWING TERRAIN
				if(t!=null) {
					image = getImage(t.toString());
					g.drawImage(image, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2  - cameraY, null);
					
					// DRAWING ENTITY
					if(t.containedEntity()!=null){
						image = RenderingWindow.getImage(t.containedEntity().name());
						g.drawImage(image, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 -TILESIZE - cameraY - image.getHeight(null)/2, null);
					}

				}

				// DRAWING ROOMS
				if(r!=null) {
					// Drawing 2 block high walls
					if(r instanceof EntranceExitTile){
						if(j-1 >= 0 && rooms[i][j-1]==null){
							g.drawImage(doorUD, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 -TILESIZE/2 - cameraY, null);
						} else {
							g.drawImage(doorLR, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 -TILESIZE/2 - cameraY, null);
						}
					}
					else{
							g.drawImage(building, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 -TILESIZE/2 - cameraY, null);
					}

					// wall block on top of wall / door for 2 high building.
					g.drawImage(building, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 -TILESIZE - cameraY, null);

					// default img for non edge and corner roofs
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
					g.drawImage(image, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, (int) (((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 - (TILESIZE*1.5)) - cameraY, null);
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
	public void shittyDraw(Tile[][] tiles, Tile[][] rooms, Graphics offgc){
		for(int i = 0; i < tiles.length; i++){
			for(int j = 0; j < tiles[i].length; j++){
				FloorTile t = (FloorTile) tiles[i][j];
				Tile r = rooms[i][j];
				if(t!=null){
					Image image = getImage(t.toString());
					offgc.drawImage(image, j*TILESIZE - cameraX, i*TILESIZE-image.getHeight(null)+TILESIZE - cameraY, null);
					
					// DRAWING ENTITY
					if(t.containedEntity()!=null){
						System.out.println("DRAWING IMG");
						image = RenderingWindow.getImage(t.containedEntity().name());
						System.out.println(image);
						offgc.drawImage(image, j*TILESIZE - cameraX, i*TILESIZE-image.getHeight(null)+TILESIZE - cameraY - TILESIZE/2, null);
					}
				}
				if(r!=null){
					Image image = building;
					offgc.drawImage(image, j*TILESIZE - cameraX, i*TILESIZE-image.getHeight(null)+TILESIZE - cameraY, null);
				} if(t==null && r==null) {
					offgc.setColor(Color.WHITE);
					offgc.fillRect(j*TILESIZE - cameraX, i*TILESIZE - cameraY, TILESIZE, TILESIZE);
					offgc.setColor(Color.BLACK);
					offgc.drawRect(j*TILESIZE - cameraX, i*TILESIZE - cameraY, TILESIZE, TILESIZE);
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

	public void setView(String string) {
		view = string;

	}
	
	public int getCameraX() {
		return cameraX;
	}

	public void setCameraX(int cameraX) {
		this.cameraX = cameraX;
	}

	public int getCameraY() {
		return cameraY;
	}

	public void setCameraY(int cameraY) {
		this.cameraY = cameraY;
	}


}
