package ui;

import gameworld.Animation;
import gameworld.Game;
import gameworld.Game.Direction;
import gameworld.Player;
import gameworld.entity.BasicEntity;
import gameworld.location.Location;
import gameworld.location.OutsideLocation;
import gameworld.tile.EntranceTile;
import gameworld.tile.FloorTile;
import gameworld.tile.Tile;

import java.awt.Graphics;
import java.awt.Image;

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

			// do lighting here and draw final img over it.

//			Image lighting = createImage(this.getWidth(), this.getHeight());
//			Graphics lightingGraphic = lighting.getGraphics();
//			Color c = new Color(0,0,0,200);
//			lightingGraphic.setColor(c);
//			lightingGraphic.fillRect(0,0,this.getWidth(),this.getHeight());
//			
//			int playerX =  (player.getPosition().x*TILESIZE/2) + (player.getPosition().y*TILESIZE/2) + ImageStorage.playerImage.getWidth(null)/2 - cameraX;
//			int playerY =  ((player.getPosition().y*TILESIZE/4)-(player.getPosition().x*TILESIZE/4)) + this.getHeight()/2  - playerImage.getHeight(null)/2 - cameraY;
//			
//			c = new Color(0,0,0,100);
//			lightingGraphic.setColor(c);
//			lightingGraphic.fillOval(playerX, playerY, TILESIZE, TILESIZE);
//			
//			g.drawImage(lighting, 0, 0, this.getWidth(), this.getHeight(), null);
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
			Image image = null;
			// outside tiles
			for(int i = 0; i < tiles.length; i++){
				for(int j = tiles[i].length-1; j >=0 ; j--){
					int x = (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX;
					int y = ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 - cameraY;
					
					Tile t = tiles[i][j];
					if(t!=null) {
						image = ImageStorage.getImage(t.toString());

						
						if(location instanceof OutsideLocation){
							// FLOOR + ENTITES FOR OUTSIDE
							offgc.drawImage(image, x, y, null);
							
							// DRAWING ENTITY
							if(t.containedEntity()!=null){
								if(t.containedEntity() instanceof BasicEntity){
									image = ImageStorage.getImage(t.containedEntity().getName());
								} else {
									image = ImageStorage.getImage(t.containedEntity().getClass().getSimpleName());
								}
								
								offgc.drawImage(image, x, y - Math.abs(image.getHeight(null)-TILESIZE/2), null);


							}

							// DRAWING PLAYER
							if(t.getPlayer()!=null){
								Player p = t.getPlayer();
								offgc.drawImage(getPlayerImage(p), x, (int)(y - TILESIZE*0.75), null);
							}
							
						}
						else{
							if(!(t instanceof EntranceTile)){
								offgc.drawImage(image, x, y, null);
							}
							// FLOOR + ENTITES FOR INSIDE
							if(i==0 || tiles[i-1][j]==null){
								if(t instanceof EntranceTile){
									if(tiles[i+1][j] instanceof FloorTile){
										offgc.drawImage(ImageStorage.insideDoorL, x, y, null);
									}
								}
								else {
									offgc.drawImage(ImageStorage.wallL, x, y, null);
								}
							}
							if(j==tiles.length-1|| tiles[i][j+1]==null){
								if(t instanceof EntranceTile){
									if(tiles[i][j-1] instanceof FloorTile){
										offgc.drawImage(ImageStorage.insideDoorR, x, y, null);
									}
								}
								else {
									offgc.drawImage(ImageStorage.wallR, x, y, null);
								}
							}
							
							if(t.containedEntity()!=null){
								if(t.containedEntity() instanceof BasicEntity){
									image = ImageStorage.getImage(t.containedEntity().getName());
								} else {
									image = ImageStorage.getImage(t.containedEntity().getClass().getSimpleName());
								}
								offgc.drawImage(image, x, y, null);
							}
							
							// DRAWING PLAYER
							if(t.getPlayer()!=null){
								Player p = t.getPlayer();
								offgc.drawImage(getPlayerImage(p), x, y, null);
							}
						}

					}

					// DRAWING ROOMS
					if(rooms!=null){
						Tile r = rooms[i][j];
						if(r!=null) {
							// Drawing 2 block high walls
							if(r instanceof EntranceTile){
								if(j-1 >= 0 && rooms[i][j-1]==null){
									offgc.drawImage(ImageStorage.doorUD, x, y-TILESIZE/2, null);
								} else {
									offgc.drawImage(ImageStorage.doorLR, x, y-TILESIZE/2, null);
								}
							}
							else{
								offgc.drawImage(ImageStorage.building, x, y-TILESIZE/2, null);
							}
							offgc.drawImage(ImageStorage.building, x, y-TILESIZE, null);


							image = ImageStorage.building;

							// Western most point of building
							if(j-1 >= 0 && rooms[i][j-1] == null){
								image = ImageStorage.roofUD;
							}

							// Northern most point of building
							if(i+1 < rooms.length && rooms[i+1][j]==null){
								image = ImageStorage.roofLR;
							}

							// Inwards corner roof
							if(j-1 >= 0 && i+1 < rooms.length && rooms[i+1][j-1]==null && rooms[i][j-1] != null && rooms[i+1][j]!=null){
								image = ImageStorage.roofCornerI;
							}
							
							// Outwards corner roof
							if(j-1 >= 0 && i+1<rooms.length && rooms[i][j-1]==null && rooms[i+1][j]==null){
								image = ImageStorage.roofCornerO;
							}



							offgc.drawImage(image, x, (int) (y - 1.5*TILESIZE), null);

						}
					}
				}

			}
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
			Image image = null;
			
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
			
			int armour = p.getArmour().getType().ordinal();
			int weapon = p.getWeapon().getType().ordinal();
			
			if(p.isAttacking()){
				switch(weapon){
					case 0:
						image = ImageStorage.shanking[armour][directionInt][animation.getAttackFrame()];
						break;
					case 1:
						image = ImageStorage.spearing[armour][directionInt][animation.getAttackFrame()];
						break;
				}
			} else {
				image = ImageStorage.walking[armour][directionInt][animation.getWalkFrame()];
			}
			

			
			
			if(p.isDead()){
				image = ImageStorage.tree;
			}
			
			playerImage = image;
			return image;
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
					if(tiles[i][j]!=null && tiles[i][j].getPosition().equals(player.getPosition())){
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
			Tile[][] newTiles = null;
			if(tiles!=null){
				newTiles = new Tile[tiles.length][tiles[0].length];
				for(int i = 0; i < tiles.length; i++){
					for(int j = 0; j < tiles[i].length; j++){
						newTiles[(newTiles.length-1)-j][i] = tiles[i][j];
					}
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
		public void updateCamera(int[] realCoord){
			int playerX = realCoord[0];
			int playerY = realCoord[1];
			if(playerImage!=null){
			cameraX = (int) ((playerX*TILESIZE/2) + (playerY*TILESIZE/2) + playerImage.getWidth(null)/2) - this.getWidth()/2;
			cameraY = (int) ((playerY*TILESIZE/4)-(playerX*TILESIZE/4)) + this.getHeight()/2  - playerImage.getHeight(null)/2 - this.getHeight()/2;
			}
		}

}
