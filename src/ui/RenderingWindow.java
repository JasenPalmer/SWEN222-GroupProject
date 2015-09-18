package ui;

import gameworld.Game;
import gameworld.Game.Direction;
import gameworld.Player;
import gameworld.location.Location;
import gameworld.tile.BuildingTile;
import gameworld.tile.Tile;

import java.awt.Graphics;
import java.awt.Image;
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
		

		offgc.fillRect(0,0,this.getWidth(), this.getHeight());
		offgc.drawImage(backgroundImage, 0,0, null);
//		Location l = player.getLocation();
//		Tile[][] tiles = l.getTiles();
//		//Tile[][] rooms = l.getRooms();
//		//Item[][] items = l.getItems();
//			

//	
//		switch(direction){
//		case NORTH:
//			//isometric(tiles,rooms, offgc);
//			break;
//		case EAST:
//			//isometric(rotate(tiles), rotate(rooms), offgc);		
//			break;			
//		case SOUTH:
//			//isometric(rotate(rotate(tiles)), rotate(rotate(rooms)), offgc);		
//			break;	
//		case WEST:
//			//isometric(rotate(rotate(rotate(tiles))), rotate(rotate(rotate(rooms))), offgc);	
//			break;
//				
//		}
//
//			
			g.drawImage(offscreen,0,0,null);

		}
		
	
		
		/**
		 * Iterates through arrays of tiles drawing the map terrain, then iterates through
		 * room tiles to draw buildings depending on the arrangement of tiles.
		 * 
		 * new x position = x/2 + y/2
		 * new y position = y/2 - x/2
		 * 
		 * 
		 * @param tiles - 2D array of terrain to be drawn
		 * @param rooms - 2D array of rooms to be drawn
		 * @param g - the graphics
		 */
		public void isometric(Tile[][] tiles, Tile[][] rooms, Graphics g){
			Image offscreen = createImage(this.getWidth()*TILESIZE, this.getHeight()*TILESIZE);
			Graphics offgc = offscreen.getGraphics();

			// outside tiles
			for(int i = 0; i < tiles.length; i++){
				for(int j = tiles[i].length-1; j >=0 ; j--){
					Tile t = tiles[i][j];
					if(t!=null) {
						Image image = null; //t.getImg().getImage();
						offgc.drawImage(image, (j*TILESIZE/2) + (i*TILESIZE/2), ((i*TILESIZE/4)-(j*TILESIZE/4)) + 500 , null);

					}
				}
			}

			// room tiles
			for(int i = 0; i < rooms.length; i++){
				for(int j = rooms[i].length-1; j >=0 ; j--){
					BuildingTile r = (BuildingTile) rooms[i][j];
					if(r!=null) {
						Image image = new ImageIcon(Main.class.getResource("buildings/Room.png")).getImage();
						

						// walls
						offgc.drawImage(image, (j*TILESIZE/2) + (i*TILESIZE/2), ((i*TILESIZE/4)-(j*TILESIZE/4)) + 500 -TILESIZE/2, null);
						offgc.drawImage(image, (j*TILESIZE/2) + (i*TILESIZE/2), ((i*TILESIZE/4)-(j*TILESIZE/4)) + 500 -TILESIZE, null);
						


						// Side of room
						if(j-1 >= 0 && rooms[i][j-1] == null){
							image = new ImageIcon(Main.class.getResource("buildings/RoofUD.png")).getImage();
						}

						// Bottom of room
						if(i+1 < rooms.length && rooms[i+1][j]==null){
							image = new ImageIcon(Main.class.getResource("buildings/RoofLR.png")).getImage();
						}

						// Outwards corner roof
						if(j-1 >= 0 && i+1<rooms.length && rooms[i][j-1]==null && rooms[i+1][j]==null){
							image = new ImageIcon(Main.class.getResource("buildings/RoofCornerO.png")).getImage();
						}
						// Inwards corner roof
						if(j-1 >= 0 && i+1 != rooms.length && rooms[i+1][j-1]==null && rooms[i][j-1] != null && rooms[i+1][j]!=null){
							image = new ImageIcon(Main.class.getResource("buildings/RoofCornerI.png")).getImage();
						}


						offgc.drawImage(image, (j*TILESIZE/2) + (i*TILESIZE/2), (int) (((i*TILESIZE/4)-(j*TILESIZE/4)) + 500 - (TILESIZE*1.5)), null);
						
					}
				}
			}
			g.drawImage(offscreen,0,0,null);
		}

		/**
		 * 
		 * Rotates given array of tiles 90 degrees counter-clockwise.
		 * @param tiles - array to be rotated
		 * @return rotated array
		 */
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
		
		
		/**
		 * Sets direction of renderer. May possibly end up being stored in another class?
		 * @param d - new direction
		 */
		public void setDirection(Game.Direction d){
			direction = d;
		}
}

