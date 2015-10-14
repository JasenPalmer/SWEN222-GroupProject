package ui;

import gameworld.Animation;
import gameworld.Game;
import gameworld.Game.Direction;
import gameworld.Player;
import gameworld.entity.BasicEntity;
import gameworld.location.Location;
import gameworld.location.Location.Lights;
import gameworld.location.OutsideLocation;
import gameworld.tile.EntranceTile;
import gameworld.tile.EntranceTile.Type;
import gameworld.tile.Tile;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.sun.prism.j2d.paint.RadialGradientPaint;

/**
 * Class used for rendering the game isometrically.
 * @author Jake Dorne
 *
 */
public class RenderingWindow extends JPanel{
	private static final long serialVersionUID = -9113443536966121759L;
	
	private int cameraX;
	private int cameraY;
	private int width = 1050;
	private int height = 950;
	
	private Location location;
	private Player player;
	private ApplicationWindow applicationWindow;

	private int TILESIZE = 64;

	Game.Direction direction = Direction.NORTH;

	public RenderingWindow(ApplicationWindow aw){
		new ImageStorage();
		setLayout(null);
		setBounds(0,0,width,height);
		this.applicationWindow = aw;
	}

	/**
	 * Rotates tile arrays depending on the current viewing direction and then calls the isometric
	 * renderer on the resulting arrays. Then renders the lighting if the location requires it to.
	 */
	public void paint( Graphics g ) {
		super.paint(g);

		Image offscreen = createImage(width, height);
		Graphics offgc = offscreen.getGraphics();

		player = applicationWindow.getPlayer();
		location = player.getLocation();

		Tile[][] tiles = location.getTiles();
		Tile[][] rooms = null;
		
		if(location instanceof OutsideLocation){
			OutsideLocation ol = (OutsideLocation) location;
			rooms = ol.getBuildingTiles();
		}
		
		
		// a rotated version of tiles and rooms that are not how the actual arrays work
		Tile[][] tilesToDraw = tiles;
		Tile[][] roomsToDraw = rooms;
		
		
		// Rotates array depending on direction and then rendering
		switch(direction){
		case NORTH:
			isometric(tiles,rooms, offgc);
			break;
		case EAST:
			tilesToDraw = rotateCounterClockwise(tiles);
			roomsToDraw = rotateCounterClockwise(rooms);
			isometric(tilesToDraw, roomsToDraw, offgc);
			break;
		case SOUTH:
			tilesToDraw = rotateCounterClockwise(rotateCounterClockwise(tiles));
			roomsToDraw = rotateCounterClockwise(rotateCounterClockwise(rooms));
			isometric(tilesToDraw, roomsToDraw, offgc);
			break;
		case WEST:
			tilesToDraw = rotateClockwise(tiles);
			roomsToDraw = rotateClockwise(rooms);
			isometric(tilesToDraw, roomsToDraw, offgc);
			break;

		}

			g.drawImage(offscreen,0,0,null);
			
			Point[]playerPoints = new Point[location.getPlayers().size()];
			
			int index = 0;
			for(Player p: location.getPlayers()){
				int[] coords = getRealPlayerCoords(p,tilesToDraw);
				playerPoints[index] = new Point(coords[0], coords[1]);
				index++;
			}
			
			if(location.getLights()==Lights.OFF){
				lighting((Graphics2D) g, playerPoints);
			}

	}

	/**
	 * Creates a black square to cover map in darkness and then erases and draws an oval over player 
	 * using gradient effect to reveal area around them
	 * @param g2d - Graphics object
	 * @param playerPoints - List of points of all players to be used in lighting
	 */
	private void lighting(Graphics2D g2d, Point[] playerPoints) {
		  int radius = TILESIZE*2;
		  
		  // specifying where each corresponding colour in colour array starts in gradient paint
		  float[] distanceIntervals = {0.2f,0.4f,0.6f,0.8f,1f};
		  
		  // colours to be used in gradient going from centre colour out to edge of circle colour.
		  Color[] colours = {new Color(0,0,10,0),new Color(0,0,10,62), new Color(0,0,10,125),new Color(0,0,10,187), new Color(0,0,10,250)};
		  
		  
		  Image image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		  Graphics2D g = (Graphics2D) image.getGraphics();

		  // filling the rest of the image in black for darkness
		  g.setColor(new Color(0, 0, 10, 250));
		  g.fillRect(0, 0 , width, height);
		  
		  // the sets graphics object to clear the overlapping pixel of the destination when next drawn on. 
		  // So drawing an oval with a gradient over a rectangle clears the rectangle below it.
		  g.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN, 1f));
		  
		  for(Point position: playerPoints){
			  int x = (position.x*TILESIZE/2) + (position.y*TILESIZE/2) - cameraX - radius + TILESIZE/2;
			  int y = (position.y*TILESIZE/4)-(position.x*TILESIZE/4) + height/2 - cameraY - radius;
			  g.setPaint(new RadialGradientPaint(x+radius, y+radius, radius, distanceIntervals, colours));
			  g.fillOval(x, y, radius*2, radius*2);
		  }


		  g.dispose();
		  
		  g2d.drawImage(image, 0, 0, null);
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
			//updating camera based on player position
			updateCamera(getRealPlayerCoords(player, tiles));
			
			// filling background in with black square
			offgc.fillRect(0,0,width, height);
			
			Image image = null;
			
			for(int i = 0; i < tiles.length; i++){
				for(int j = tiles[i].length-1; j >=0 ; j--){
					// converting x or y to work in isometric view
					int x = (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX;
					int y = ((i*TILESIZE/4)-(j*TILESIZE/4)) + height/2 - cameraY;

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
							// FLOOR, WALLS AND ENTRANCES FOR INSIDE LOCATION
							if(!(t instanceof EntranceTile)){
								// drawing floor
								offgc.drawImage(image, x, y, null);
								
								// top left wall
								if(i==0 || tiles[i-1][j]==null){
									offgc.drawImage(ImageStorage.wallL, x, y, null);
								}
								// top right wall
								if(j==tiles.length-1|| tiles[i][j+1]==null){
									offgc.drawImage(ImageStorage.wallR, x, y, null);
								}
							} else {
								EntranceTile et = (EntranceTile) t;
								if(et.getType()!=Type.INVISIBLE){
									if(tiles[i][j-1]!=null){
										offgc.drawImage(ImageStorage.insideDoorR, x, y, null);
									}
									if(tiles[i+1][j]!=null){
										offgc.drawImage(ImageStorage.insideDoorL, x, y, null);
									}
									if(tiles[i][j+1]!=null || tiles[i-1][j]!=null){
										offgc.drawImage(ImageStorage.floor, x, y, null);
									}
								}
							}
							
							// ENTITES FOR INSIDE LOCATION
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
							// Drawing 2 block high walls with the bottom being door if needed
							if(r instanceof EntranceTile){
								if(j-1 >= 0 && rooms[i][j-1]==null){
									EntranceTile et = (EntranceTile) r;
									if(et.getType()!=Type.BUILDING){
										// dont draw shit
									} else {
										offgc.drawImage(ImageStorage.doorUD, x, y-TILESIZE/2, null);
									}
								} else {
									EntranceTile et = (EntranceTile) r;
									if(et.getType()!=Type.BUILDING){
										// dont draw shit
									} else {
										offgc.drawImage(ImageStorage.doorLR, x, y-TILESIZE/2, null);
									}
								}
							}
							else{
								// draw wall if no door
								offgc.drawImage(ImageStorage.building, x, y-TILESIZE/2, null);
							}		
							
							
							EntranceTile et = null;
							if(r instanceof EntranceTile){
								et = (EntranceTile)r;
							}
							
							if(et==null || et.getType()==Type.BUILDING){
							// if the entrance is part of a building, or it is just a building wall, draw a building around it.
									// second wall above wall/door
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
			
									// Outwards corner roof
									if(j-1 >= 0 && i+1<rooms.length && rooms[i][j-1]==null && rooms[i+1][j]==null){
										image = ImageStorage.roofCornerO;
									}
									// Inwards corner roof
									if(j-1 >= 0 && i+1 != rooms.length && rooms[i+1][j-1]==null && rooms[i][j-1] != null && rooms[i+1][j]!=null){
										image = ImageStorage.roofCornerI;
									}
			
			
									offgc.drawImage(image, x, (int) (y - 1.5*TILESIZE), null);
							}
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
		 * If the player is not attacking, it finds the correct frame in the walk cycle as well as the animation direction.
		 * If the player is attacking, it finds the correct frame and animation in one of the attacking animation cycles.
		 * 
		 * @param p - The player to get image for
		 * @return image - The image to return
		 */
		private Image getPlayerImage(Player p){
			Animation animation = p.getAnimation();
			int directionInt = animation.getAnimationDirection();
			Image image = null;

			// changing animation direction based on camera direction
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

			int armour = 0;
			int weapon = 0;

			if(p.getArmour()!=null){
				armour = p.getArmour().getType().ordinal();
			}
			if(p.getWeapon()!=null){
				weapon = p.getWeapon().getType().ordinal();
			}



			if(p.isAttacking()){
				switch(weapon){
					case 0:
						image = ImageStorage.shanking[armour][directionInt][animation.getAttackFrame()];
						break;
					case 1:
						image = ImageStorage.spearing[armour][directionInt][animation.getAttackFrame()];
						break;
				}
				applicationWindow.cycleAnimations();
			} else {
				image = ImageStorage.walking[armour][directionInt][animation.getWalkFrame()];
			}
			
			return image;
		}

		/**
		 * If the player's position is (0,0) when the map is rotated, the position of the player will still remain 0,0.
		 * This method gets the coordinates the player is at in the rotated version of the array.
		 * If the map is rotated to the east, this method would return (map.length, 0) despite the player position still being (0,0).
		 * @param tiles
		 * @return int of player x and y
		 */
		private int[] getRealPlayerCoords(Player p, Tile[][] tiles) {
			int[] xy = new int[2];
			for(int i = 0; i < tiles.length; i++){
				for(int j = tiles[i].length-1; j >=0 ; j--){
					if(tiles[i][j]!=null && tiles[i][j].getPosition().equals(p.getPosition())){
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
		public Tile[][] rotateCounterClockwise(Tile[][] tiles){
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
		 *
		 * Rotates given array of tiles 90 degrees clockwise.
		 * @param tiles - array to be rotated
		 * @return rotated array
		 */
		public Tile[][] rotateClockwise(Tile[][] tiles){
			Tile[][] newTiles = null;
			if(tiles!=null){
				newTiles = new Tile[tiles.length][tiles[0].length];
				for(int i = 0; i < tiles.length; i++){
					for(int j = 0; j < tiles[i].length; j++){
						newTiles[j][tiles.length-1-i] = tiles[i][j];
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
		 * camera y = player rendering y position - tilesize/2
		 * @param realCoord - int array of player x and y in the tile array
		 */
		public void updateCamera(int[] realCoord){
			int playerX = realCoord[0];
			int playerY = realCoord[1];
			cameraX = (int) ((playerX*TILESIZE/2) + (playerY*TILESIZE/2) + TILESIZE/2) - width/2;
			cameraY = (int) ((playerY*TILESIZE/4)-(playerX*TILESIZE/4))  - TILESIZE/2;
			
		}

}
