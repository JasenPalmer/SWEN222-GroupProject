package mapeditor;

import gameworld.Game;
import gameworld.Player;
import gameworld.entity.BasicEntity;
import gameworld.location.InsideLocation;
import gameworld.location.Location;
import gameworld.location.OutsideLocation;
import gameworld.tile.EntranceTile;
import gameworld.tile.FloorTile;
import gameworld.tile.Tile;
import gameworld.tile.EntranceTile.Type;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import ui.ImageStorage;
import ui.RenderingWindow;


public class EditorCanvas extends JPanel {

	private Location location;
	private String view = "Shit";

	private int MAPHEIGHT = 25;
	private int MAPWIDTH = 25;
	private int TILESIZE = 64;
	private int cameraX = 0;
	private int cameraY = 0;

	Game.Direction direction = Game.Direction.NORTH;

	/**
	 * Create the panel.
	 */
	public EditorCanvas(Location location) {
		setLayout(null);
		setBounds(0,0,2000,750);

		if(location instanceof OutsideLocation){
			location = (OutsideLocation) location;
		} else {
			location = (InsideLocation) location;
		}
		this.location = location;
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
						g.drawImage(image, x, y, null);
						
						// DRAWING ENTITY
						if(t.containedEntity()!=null){
							if(t.containedEntity() instanceof BasicEntity){
								image = ImageStorage.getImage(t.containedEntity().getName());
							} else {
								image = ImageStorage.getImage(t.containedEntity().getClass().getSimpleName());
							}
							
							g.drawImage(image, x, y - Math.abs(image.getHeight(null)-TILESIZE/2), null);


						}

					}
					else{
						if(!(t instanceof EntranceTile)){
							g.drawImage(image, x, y, null);
							
							// top left wall
							if(i==0 || tiles[i-1][j]==null){
								g.drawImage(ImageStorage.wallL, x, y, null);
							}
							// top right wall
							if(j==tiles.length-1|| tiles[i][j+1]==null){
								g.drawImage(ImageStorage.wallR, x, y, null);
							}
						} else {
							EntranceTile et = (EntranceTile) t;
							if(et.getType()!=Type.INVISIBLE){
								if(tiles[i][j-1]!=null){
									g.drawImage(ImageStorage.insideDoorR, x, y, null);
								}
								if(tiles[i+1][j]!=null){
									g.drawImage(ImageStorage.insideDoorL, x, y, null);
								}
								if(tiles[i][j+1]!=null || tiles[i-1][j]!=null){
									g.drawImage(ImageStorage.floor, x, y, null);
								}
							}
						}
						
						if(t.containedEntity()!=null){
							if(t.containedEntity() instanceof BasicEntity){
								image = ImageStorage.getImage(t.containedEntity().getName());
							} else {
								image = ImageStorage.getImage(t.containedEntity().getClass().getSimpleName());
							}
							g.drawImage(image, x, y, null);
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
								EntranceTile et = (EntranceTile) r;
								if(et.getType()!=Type.BUILDING){
									// dont draw shit
								} else {
									g.drawImage(ImageStorage.doorUD, x, y-TILESIZE/2, null);
								}
							} else {
								EntranceTile et = (EntranceTile) r;
								if(et.getType()!=Type.BUILDING){
									// dont draw shit
								} else {
									g.drawImage(ImageStorage.doorLR, x, y-TILESIZE/2, null);
								}
							}
						}
						else{
							// draw wall if no door
							g.drawImage(ImageStorage.building, x, y-TILESIZE/2, null);
						}		
						
						
						EntranceTile et = null;
						if(r instanceof EntranceTile){
							et = (EntranceTile)r;
						}
						
						if(et==null || et.getType()==Type.BUILDING){
								// second wall above wall/door
								g.drawImage(ImageStorage.building, x, y-TILESIZE, null);
								
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
		
		
								g.drawImage(image, x, (int) (y - 1.5*TILESIZE), null);
						}
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
	public void shittyDraw(Tile[][] tiles, Tile[][] rooms, Graphics offgc){
		for(int i = 0; i < tiles.length; i++){
			for(int j = 0; j < tiles[i].length; j++){
				Tile t = tiles[i][j];
				Tile r = null;
				if(rooms!=null){
					r = rooms[i][j];
				}
				if(t!=null){
					Image image = ImageStorage.getImage(t.toString());
					offgc.drawImage(image, j*TILESIZE - cameraX, i*TILESIZE-image.getHeight(null)+TILESIZE - cameraY, null);

					if(t.containedEntity()!=null){
						if(t.containedEntity() instanceof BasicEntity){
							image = ImageStorage.getImage(t.containedEntity().getName());
						} else {
							image = ImageStorage.getImage(t.containedEntity().getClass().getSimpleName());
						}
						
						if(location instanceof OutsideLocation){
							offgc.drawImage(image, j*TILESIZE - cameraX, i*TILESIZE - cameraY - Math.abs(image.getHeight(null)-TILESIZE/2), null);
						} else {
							offgc.drawImage(image, j*TILESIZE - cameraX, i*TILESIZE - cameraY, null);
						}

					}
				}
				if(r!=null){
					Image image = ImageStorage.building;
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
		if(tiles!=null){
			Tile[][] newTiles = new Tile[tiles.length][tiles[0].length];
			// drawing floor
			for(int i = 0; i < tiles.length; i++){
				for(int j = 0; j < tiles[i].length; j++){
					newTiles[(newTiles.length-1)-j][i] = tiles[i][j];
				}
			}
			return newTiles;
		} else {
			return null;
		}

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
