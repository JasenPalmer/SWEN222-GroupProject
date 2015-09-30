package ui;

import gameworld.Game;
import gameworld.Game.Direction;
import gameworld.Player;
import gameworld.location.Location;
import gameworld.location.OutsideLocation;
import gameworld.tile.EntranceExitTile;
import gameworld.tile.FloorTile;
import gameworld.tile.Tile;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class RenderingWindow extends JPanel{

	private Image backgroundImage;
	private static Image grass;
	private static Image building;
	private static Image water;
	private static Image rock;
	private static Image doorLR;
	private Image playerImage;
	private Image doorUD;
	private Image roofLR;
	private Image roofUD;
	private Image roofCornerO;
	private Image roofCornerI;

	private int cameraX;
	private int cameraY;
	
	private Tile[][] locationTiles;

	private Player player;
	private ApplicationWindow applicationWindow;

	private int TILESIZE = 64;

	Game.Direction direction = Direction.NORTH;

	public RenderingWindow(ApplicationWindow aw){
		setLayout(null);
		setBounds(0,0,1050,750);
		this.applicationWindow = aw;
		setImages();
	}

	/**
	 * Setting images to files in images folder. Will be changed when map parser is working for some tiles. (grass, water, rock will be gone).
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
			playerImage = ImageIO.read(new File("src/ui/images/player/0.png"));

		}catch(IOException e){
			System.out.println(e.getLocalizedMessage());
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

	public static Image createImage(String imagename) {
		Image image = null;
		try {
			image = ImageIO.read(new File("src/ui/images/terrain/"+imagename+".png"));
		}catch(Exception e) {
			System.err.println("Error: "+e);
		}
		return image;
	}

	/**
	 * Rotates tile arrays depending on the current viewing direction and then calls the isometric
	 * renderer on the resulting arrays
	 */
	public void paint( Graphics g ) {

		super.paint(g);

		Image offscreen = createImage(this.getWidth(), this.getHeight());
		Graphics offgc = offscreen.getGraphics();

		player = applicationWindow.getPlayer();
		Location l = player.getLocation();

		Tile[][] tiles = l.getTiles();
		locationTiles = tiles;
		Tile[][] rooms = null;

		if(l instanceof OutsideLocation){
			OutsideLocation ol = (OutsideLocation) l;
			rooms = ol.getBuildingTiles();
		}

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
		 * new x position = x/2 + y/2 ( + or - constants to fit properly such as imageHeight)
		 * new y position = y/4 - x/4 ( + or - constants to fit properly such as imageHeight)
		 *
		 *
		 * @param tiles - 2D array of terrain to be drawn
		 * @param rooms - 2D array of rooms to be drawn
		 * @param offgc - the graphics
		 */
		public void isometric(Tile[][] tiles, Tile[][] rooms, Graphics offgc){
			updateCamera(getRealPlayerCoords(tiles));

			offgc.fillRect(0,0,this.getWidth(), this.getHeight());

			// outside tiles
			for(int i = 0; i < tiles.length; i++){
				for(int j = tiles[i].length-1; j >=0 ; j--){
					
					FloorTile t = (FloorTile) tiles[i][j];
					if(t!=null) {
						// DRAWING TERRAIN
						
						Image image = getImage(t.toString());
						offgc.drawImage(image, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 - cameraY, null);

						// DRAWING PLAYER
						if(t.getPos().equals(player.getPosition())){
							offgc.drawImage(playerImage, (j*TILESIZE/2) + (i*TILESIZE/2) + playerImage.getWidth(null)/2 - cameraX, ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2  - playerImage.getHeight(null)/2 - cameraY, null);
						}

						// DRAWING ROOMS
						if(rooms!=null){
							Tile r = rooms[i][j];
							if(r!=null) {
								// Drawing 2 block high walls
								if(r instanceof EntranceExitTile){
									if(j-1 >= 0 && rooms[i][j-1]==null){
										offgc.drawImage(doorUD, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 -TILESIZE/2 - cameraY, null);
									} else {
										offgc.drawImage(doorLR, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 -TILESIZE/2 - cameraY, null);
									}
								}
								else{
									offgc.drawImage(building, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 -TILESIZE/2 - cameraY, null);
								}
								offgc.drawImage(building, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 -TILESIZE - cameraY, null);


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


								offgc.drawImage(image, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, (int) (((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 - (TILESIZE*1.5)) - cameraY, null);

							}
						}
					}
				}

			}
		}

		
		/**
		 * gets the player x and y in the current tile array regardless of rotation.
		 * @param tiles
		 * @return int of player x and y
		 */
		private int[] getRealPlayerCoords(Tile[][] tiles) {
			int[] xy = new int[2];
			for(int i = 0; i < tiles.length; i++){
				for(int j = tiles[i].length-1; j >=0 ; j--){
					if(tiles[i][j]!=null && tiles[i][j].getPos().equals(player.getPosition())){
						xy[0] = j;
						xy[1] = i;
					}
				}
			}
			return xy;
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
			player.setDirection(d);
		}

		/**
		 * @return current direction of renderer/camera
		 */
		public Direction getDirection(){
			return direction;
		}

		/**
		 * changes the camera coords to the fit player in middle of screen
		 * camera x = player rendering x position - (renderwindow size / 2)
		 * 
		 * @param realCoord - int array of player x and y in the tile array
		 */
		public void updateCamera(int[] realCoord){
			int playerX = realCoord[0];
			int playerY = realCoord[1];
		
			cameraX = (int) ((playerX*TILESIZE/2) + (playerY*TILESIZE/2) + playerImage.getWidth(null)/2) - this.getWidth()/2;
			cameraY = (int) ((playerY*TILESIZE/4)-(playerX*TILESIZE/4)) + this.getHeight()/2  - playerImage.getHeight(null)/2 - this.getHeight()/2;
	}
}

