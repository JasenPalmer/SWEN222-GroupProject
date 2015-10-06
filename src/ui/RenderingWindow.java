package ui;

import gameworld.Animation;
import gameworld.Game;
import gameworld.Game.Direction;
import gameworld.Player;
import gameworld.location.InsideLocation;
import gameworld.location.Location;
import gameworld.location.OutsideLocation;
import gameworld.tile.EntranceExitTile;
import gameworld.tile.FloorTile;
import gameworld.tile.Tile;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class RenderingWindow extends JPanel{

	private int cameraX;
	private int cameraY;

	private Image playerImage;
	private Location location;
	private Tile[][] locationTiles;

	private Player player;
	private ApplicationWindow applicationWindow;

	private int TILESIZE = 64;

	Game.Direction direction = Direction.NORTH;

	public RenderingWindow(ApplicationWindow aw){
		new ImageStorage();
		playerImage = ImageStorage.playerImage;
		setLayout(null);
		setBounds(0,0,1050,950);
		this.applicationWindow = aw;
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
		location = player.getLocation();

		Tile[][] tiles = location.getTiles();
		locationTiles = tiles;
		Tile[][] rooms = null;

		if(location instanceof OutsideLocation){
			OutsideLocation ol = (OutsideLocation) location;
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
			updateCamera();

			offgc.fillRect(0,0,this.getWidth(), this.getHeight());
			Image image = null;
			// outside tiles
			for(int i = 0; i < tiles.length; i++){
				for(int j = tiles[i].length-1; j >=0 ; j--){

					FloorTile t = (FloorTile) tiles[i][j];
					if(t!=null) {
						// DRAWING TERRAIN
						image = ImageStorage.getImage(t.toString());
						offgc.drawImage(image, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 - cameraY, null);

						// Drawing walls if inside location
						if(location instanceof InsideLocation){
							if(i==0 || tiles[i-1][j]==null){
								offgc.drawImage(ImageStorage.wallL, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2  - cameraY, null);
							}
							if(j==tiles.length-1|| tiles[i][j+1]==null){
								offgc.drawImage(ImageStorage.wallR, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2  - cameraY, null);
							}
						}
						// DRAWING ENTITY
						if(t.containedEntity()!=null){
							image = ImageStorage.getImage(t.containedEntity().getName());
							offgc.drawImage(image, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 - cameraY - Math.abs(image.getHeight(null)-TILESIZE), null);
						}

						// DRAWING PLAYER
						if(t.getPlayer()!=null){
							Player p = t.getPlayer();
							offgc.drawImage(getPlayerImage(p), (int)((getPlayerX(p)/2) + (getPlayerY(p)/2)  - cameraX), (int)((p.getY()/4)-(p.getX()/4)) + this.getHeight()/2  - getPlayerImage(p).getHeight(null) - cameraY, null);
						}
					}

					// DRAWING ROOMS
					if(rooms!=null){
						Tile r = rooms[i][j];
						if(r!=null) {
							// Drawing 2 block high walls
							if(r instanceof EntranceExitTile){
								if(j-1 >= 0 && rooms[i][j-1]==null){
									offgc.drawImage(ImageStorage.doorUD, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 -TILESIZE/2 - cameraY, null);
								} else {
									offgc.drawImage(ImageStorage.doorLR, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 -TILESIZE/2 - cameraY, null);
								}
							}
							else{
								offgc.drawImage(ImageStorage.building, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 -TILESIZE/2 - cameraY, null);
							}
							offgc.drawImage(ImageStorage.building, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 -TILESIZE - cameraY, null);


							image = ImageStorage.building;

							// Western most point of building
							if(j-1 >= 0 && rooms[i][j-1] == null){
								image = ImageStorage.roofUD;
							}

							// Northern most point of building
							if(i+1 < rooms.length && rooms[i+1][j]==null){
								image = ImageStorage.roofLR;
							}

							// Outwards corner roof
							if(j-1 >= 0 && i+1<rooms.length && rooms[i][j-1]==null && rooms[i+1][j]==null){
								image = ImageStorage.roofCornerO;
							}
							// Inwards corner roof
							if(j-1 >= 0 && i+1 != rooms.length && rooms[i+1][j-1]==null && rooms[i][j-1] != null && rooms[i+1][j]!=null){
								image = ImageStorage.roofCornerI;
							}


							offgc.drawImage(image, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, (int) (((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 - (TILESIZE*1.5)) - cameraY, null);

						}
					}
				}

			}
		}



		private Double getPlayerX(Player p) {
			Double[] coords = {player.getX(), player.getY()};
			switch(direction){
			case NORTH:
				return p.getX();
			case WEST:
				return rotatePlayer(coords)[0];
			case SOUTH:
				return rotatePlayer(rotatePlayer(coords))[0];
			case EAST:
				return rotatePlayer(rotatePlayer(rotatePlayer(coords)))[0];
			}
			return null;
		}



		private Double getPlayerY(Player p) {
			Double[] coords = {player.getX(), player.getY()};
			switch(direction){
			case NORTH:
				return p.getY();
			case WEST:
				return rotatePlayer(coords)[1];
			case SOUTH:
				return rotatePlayer(rotatePlayer(coords))[1];
			case EAST:
				return rotatePlayer(rotatePlayer(rotatePlayer(coords)))[1];
			}
			return null;
		}

		private Double[] rotatePlayer(Double[] coords){
			int mapCenter = location.getTiles().length*64 /2;
			Double[] toReturn = new Double[2];
			if(coords[0] <= mapCenter && coords[1] <= mapCenter){
				toReturn[0] = coords[0];
				toReturn[1] = location.getTiles().length*64 - coords[0];
			}
			else if(coords[0] <= mapCenter && coords[1] > mapCenter){
				toReturn[0] = coords[1];
				toReturn[1] = coords[0];
			}
			else if(coords[0] > mapCenter && coords[1] > mapCenter){
				toReturn[0] = coords[0];
				toReturn[1] = location.getTiles().length*64 - coords[1];
			}
			else{
				toReturn[0] = location.getTiles().length*64 - coords[0];
				toReturn[1] = toReturn[1];
			}
			return toReturn;
		}

		/**
		 * Changes walkdirection for drawing depending on camera angle
		 * @param directionInt - current direction of player
		 * @param toAdd - amount to add to it (depends on camera angle)
		 * @return directionInt - directionInt after adding. Wraps around if it goes over 3
		 */
		private int addToDirInt(int directionInt, int toAdd){
			int i = 0;
			while(i<toAdd){
				directionInt++;
				if(directionInt>3){
					directionInt = 0;
				}
				i++;
			}

			return directionInt;
		}

		/**
		 * finds correct image to draw in walk cycle depending on players direction and spot in cycle
		 * @param p - The player to get image for
		 * @return image - The image to return
		 */
		private Image getPlayerImage(Player p){
			Animation animation = p.getAnimation();

			int directionInt = animation.getAnimationDirection();

			switch(direction){
				case EAST:
					directionInt = addToDirInt(directionInt, 3);
					break;
				case NORTH:
					break;
				case SOUTH:
					directionInt = addToDirInt(directionInt, 2);
					break;
				case WEST:
					directionInt = addToDirInt(directionInt, 1);
					break;
			}
			Image image = null;

			if(p.getName().equals("King Jake")){
				if(p.isDead()){
					image = ImageStorage.tree;
				}
				else{image = ImageStorage.plateWalk[directionInt][animation.getWalkFrame()];}

			} else if(p.isDead()){
				image = ImageStorage.bush;
			}
				else {
				image = ImageStorage.robeWalk[directionInt][animation.getWalkFrame()];
			}

			playerImage = image;
			return image;
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

		/**
		 * changes the camera coords to the fit player in middle of screen
		 * camera x = player rendering x position - (renderwindow size / 2)
		 *
		 * @param realCoord - int array of player x and y in the tile array
		 */
		public void updateCamera(){
			cameraX = (int) ((player.getX()/2) + (player.getY()/2) + playerImage.getWidth(null)/2) - this.getWidth()/2;
			cameraY = (int) ((player.getY()/4)-(player.getX()/4)) + this.getHeight()/2  - playerImage.getHeight(null)/2 - this.getHeight()/2;
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
}

