package ui;

import gameworld.Game;
import gameworld.Game.Direction;
import gameworld.Player;
import gameworld.location.Location;
import gameworld.location.OutsideLocation;
import gameworld.tile.BuildingTile;
import gameworld.tile.FloorTile;
import gameworld.tile.Tile;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import main.Main;

public class RenderingWindow extends JPanel{
	
	private Image backgroundImage;
	private Player player;
	
	private int TILESIZE = 64;
	
	Game.Direction direction = Direction.NORTH;
	
	public RenderingWindow(){
		setLayout(null);
		setBounds(50,30,950,750);
		
		try{
		backgroundImage = ImageIO.read(new File("src/ui/images/renderingWindowTemp.jpg"));
		}catch(IOException e){
			System.out.println(e.getLocalizedMessage());
		}
	}
	
	/**
	 * Rotates tile arrays depending on the current viewing direction and then calls the isometric
	 * renderer on the resulting arrays
	 */
	public void paint( Graphics g ) { 
		super.paint(g);
		
		Image offscreen = createImage(this.getWidth(), this.getHeight());
		Graphics offgc = offscreen.getGraphics();

//		Location l = player.getLocation();
//		Tile[][] tiles = l.getTiles();
//		//Tile[][] rooms = l.getRooms();
//		//Item[][] items = l.getItems();
//		
		
		Image grass = null;
		Image building = null;
		try {
			grass = ImageIO.read(new File("src/ui/images/terrain/Grass.png"));
			building = ImageIO.read(new File("src/ui/images/buildings/Room.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		// Example location. To be changed later.
		Location l = null;
		Tile[][] tiles = {	{new FloorTile(l, new Point(0,0), grass), new FloorTile(l, new Point(0,1), grass), new FloorTile(l, new Point(0,2), grass), new FloorTile(l, new Point(0,3), grass), new FloorTile(l, new Point(0,4), grass)},
							{new FloorTile(l, new Point(1,0), grass), new FloorTile(l, new Point(1,1), grass), new FloorTile(l, new Point(1,2), grass), new FloorTile(l, new Point(1,3), grass), new FloorTile(l, new Point(1,4), grass)},
							{new FloorTile(l, new Point(2,0), grass), new FloorTile(l, new Point(2,1), grass), new FloorTile(l, new Point(2,2), grass), new FloorTile(l, new Point(2,3), grass), new FloorTile(l, new Point(2,4), grass)},
							{new FloorTile(l, new Point(3,0), grass), new FloorTile(l, new Point(3,1), grass), new FloorTile(l, new Point(3,2), grass), new FloorTile(l, new Point(3,3), grass), new FloorTile(l, new Point(3,4), grass)},
							{new FloorTile(l, new Point(4,0), grass), new FloorTile(l, new Point(4,1), grass), new FloorTile(l, new Point(4,2), grass), new FloorTile(l, new Point(4,3), grass), new FloorTile(l, new Point(4,4), grass)}
						};
		
		Tile[][] rooms = {	{null,null,null,null,null},
							{null,null,new BuildingTile(l, new Point(1,2), building), new BuildingTile(l, new Point(1,3), building),null},
							{null,null,new BuildingTile(l, new Point(2,2), building),new BuildingTile(l, new Point(2,2), building),null},
							{null,null,null,null,null},
							{null,null,null,null,null}
						};
		
		l = new OutsideLocation("Test", "This is a test Location", tiles, rooms);
		
		
		

		// Rotates array depending on direction and then rendering
		switch(direction){
		case NORTH:
			isometric(tiles,rooms, offgc);
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
		 * Iterates through arrays of tiles drawing the map terrain, then iterates through
		 * room tiles to draw buildings depending on the arrangement of tiles.
		 * 
		 * new x position = x/2 + y/2
		 * new y position = y/4 - x/4
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
						Image image = t.getImg();
						g.drawImage(image, (j*TILESIZE/2) + (i*TILESIZE/2), ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 , null);

					}
				}
			}

			// room tiles
			for(int i = 0; i < rooms.length; i++){
				for(int j = rooms[i].length-1; j >=0 ; j--){
					BuildingTile r = (BuildingTile) rooms[i][j];
					if(r!=null) {
						Image image = null;
						try {
							image = ImageIO.read(new File("src/ui/images/buildings/Room.png"));
						} catch (IOException e) {
							e.printStackTrace();
						}
						

						// Drawing 2 block high walls
						g.drawImage(image, (j*TILESIZE/2) + (i*TILESIZE/2), ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 -TILESIZE/2, null);
						g.drawImage(image, (j*TILESIZE/2) + (i*TILESIZE/2), ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 -TILESIZE, null);
						


						// Western most point of building
						if(j-1 >= 0 && rooms[i][j-1] == null){
							try {
								image = ImageIO.read(new File("src/ui/images/buildings/RoofUD.png"));
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

						// Northern most point of building
						if(i+1 < rooms.length && rooms[i+1][j]==null){
							try {
								image = ImageIO.read(new File("src/ui/images/buildings/RoofLR.png"));
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

						// Outwards corner roof
						if(j-1 >= 0 && i+1<rooms.length && rooms[i][j-1]==null && rooms[i+1][j]==null){
							try {
								image = ImageIO.read(new File("src/ui/images/buildings/RoofCornerO.png"));
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						// Inwards corner roof
						if(j-1 >= 0 && i+1 != rooms.length && rooms[i+1][j-1]==null && rooms[i][j-1] != null && rooms[i+1][j]!=null){
							try {
								image = ImageIO.read(new File("src/ui/images/buildings/RoofCornerI.png"));
							} catch (IOException e) {
								e.printStackTrace();
							}
						}


						g.drawImage(image, (j*TILESIZE/2) + (i*TILESIZE/2), (int) (((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 - (TILESIZE*1.5)), null);
						
					}
				}
			}
		}

		/**
		 * 
		 * Rotates given array of tiles 90 degrees counter-clockwise.
		 * @param tiles - array to be rotated
		 * @return rotated array
		 */
		public Tile[][] rotate(Tile[][] tiles){
			Tile[][] newTiles = new Tile[tiles.length][tiles[0].length];
			for(int i = 0; i < tiles.length; i++){
				for(int j = 0; j < tiles[i].length; j++){
					newTiles[(newTiles.length-1)-j][i] = tiles[i][j];
				}
			}
			return newTiles;
		}
		
		
		/**
		 * Sets direction of renderer. May possibly end up being stored in another class?
		 * @param d - new direction
		 */
		public void setDirection(Game.Direction d){
			direction = d;
		}
		
		/**
		 * @return current direction of renderer/camera
		 */
		public Direction getDirection(){
			return direction;
		}
}

