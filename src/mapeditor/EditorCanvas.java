package mapeditor;

import gameworld.Game;
import gameworld.location.InsideLocation;
import gameworld.location.Location;
import gameworld.location.OutsideLocation;
import gameworld.tile.BuildingTile;
import gameworld.tile.EntranceExitTile;
import gameworld.tile.Tile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class EditorCanvas extends JPanel {

	private Location location;
	private String view = "Shit";
	
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
		if(location instanceof OutsideLocation){
			location = (OutsideLocation) location;
		} else {
			location = (InsideLocation) location;
		}
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
		Tile[][] rooms = null;
		//Entity[][] entities = location.getEntities();
		
		if(location instanceof OutsideLocation){
			OutsideLocation l = (OutsideLocation) location;
			rooms = l.getBuildingTiles();
			System.out.println("PLS");
		}
		
		if(rooms!=null){
			for(int i = 0; i < rooms.length; i++){
				for(int j = 0; j < rooms[i].length ; j++){
					System.out.print(rooms[i][j]+" ");
				}
				System.out.println("");
			}
		}
		
		Image offscreen = createImage(MAPWIDTH*TILESIZE, MAPHEIGHT*TILESIZE);
		Graphics offgc = offscreen.getGraphics();
		
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
				Tile t = tiles[i][j];
				Tile r = rooms[i][j];
				Image image = null;
				
				// DRAWING TERRAIN
				if(t!=null) {
					image = t.getImg();
					g.drawImage(image, (j*TILESIZE/2) + (i*TILESIZE/2), ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 , null);
					
				}
				
				// DRAWING ROOMS	
				if(r!=null) {
					System.out.println("THE ROOM IS NOT FUCKING NULL");
					// Drawing 2 block high walls
					if(r instanceof EntranceExitTile){
						System.out.println("AN ENTRANCE");
						if(j-1 >= 0 && rooms[i][j-1]==null){
							g.drawImage(doorUD, (j*TILESIZE/2) + (i*TILESIZE/2), ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 -TILESIZE/2, null);
						} else {
							g.drawImage(doorLR, (j*TILESIZE/2) + (i*TILESIZE/2), ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 -TILESIZE/2, null);
						}
					}
					else{
						System.out.println("NOT AN ENTRANCE");
							g.drawImage(building, (j*TILESIZE/2) + (i*TILESIZE/2), ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 -TILESIZE/2, null);
					}
					
					// wall block on top of wall / door for 2 high building.
					g.drawImage(building, (j*TILESIZE/2) + (i*TILESIZE/2), ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 -TILESIZE, null);
					
					// default img for non edge and corner roofs
					image = building;
						
					// Western most point of building
					if(j-1 >= 0 && rooms[i][j-1] == null){
						System.out.println("roofUD");
						image = roofUD;
					}

					// Northern most point of building
					if(i+1 < rooms.length && rooms[i+1][j]==null){
						System.out.println("roofLR");
						image = roofLR;
					}
					// Outwards corner roof
					if(j-1 >= 0 && i+1<rooms.length && rooms[i][j-1]==null && rooms[i+1][j]==null){
						System.out.println("roofCornerO");
						image = roofCornerO;
					}
					// Inwards corner roof
					if(j-1 >= 0 && i+1 != rooms.length && rooms[i+1][j-1]==null && rooms[i][j-1] != null && rooms[i+1][j]!=null){
						System.out.println("roofCornerI");
						image = roofCornerI;
					}
					System.out.println("FUCKING DRAWING");
					g.drawImage(image, (j*TILESIZE/2) + (i*TILESIZE/2), (int) (((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 - (TILESIZE*1.5)), null);
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
				Tile t = tiles[i][j];
				Tile r = rooms[i][j];
				if(t!=null){
					Image image = t.getImg();
					offgc.drawImage(image, j*TILESIZE, i*TILESIZE-image.getHeight(null)+TILESIZE, null);
				}
				if(r!=null){
					Image image = r.getImg();
					offgc.drawImage(image, j*TILESIZE, i*TILESIZE-image.getHeight(null)+TILESIZE, null);	
				} if(t==null && r==null) {
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

	public void setView(String string) {
		view = string;
		
	}

}
