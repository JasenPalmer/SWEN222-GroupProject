package ui;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageStorage {
	public static Image grass;
	public static Image building;
	public static Image water;
	public static Image rock;
	public static Image doorLR;
	public static Image playerImage;
	public static Image doorUD;
	public static Image roofLR;
	public static Image roofUD;
	public static Image roofCornerO;
	public static Image roofCornerI;
	public static Image tree;
	public static Image bush;
	
	static Image[][] robeWalk = new Image[4][9];
	static Image[][] leatherWalk = new Image[4][9];
	static Image[][] chainWalk = new Image[4][9];
	static Image[][] plateWalk = new Image[4][9];

	
	public ImageStorage(){
		setImages();
	}

	/**
	 * Setting images to files in images folder. Will be changed when map parser is working for some tiles. (grass, water, rock will be gone).
	 */
	private void setImages() {
		try{
			// terrain images
			grass = ImageIO.read(new File("src/ui/images/terrain/Grass.png"));
			water = ImageIO.read(new File("src/ui/images/terrain/Water.png"));
			rock = ImageIO.read(new File("src/ui/images/terrain/Rock.png"));
			
			// building images
			building = ImageIO.read(new File("src/ui/images/buildings/Room.png"));
			doorUD = ImageIO.read(new File("src/ui/images/buildings/DoorUD.png"));
			doorLR = ImageIO.read(new File("src/ui/images/buildings/DoorLR.png"));
			roofLR = ImageIO.read(new File("src/ui/images/buildings/RoofLR.png"));
			roofUD = ImageIO.read(new File("src/ui/images/buildings/RoofUD.png"));
			roofCornerO = ImageIO.read(new File("src/ui/images/buildings/RoofCornerO.png"));
			roofCornerI = ImageIO.read(new File("src/ui/images/buildings/RoofCornerI.png"));
			
			// player images
			playerImage = ImageIO.read(new File("src/ui/images/player/robe/0.png"));
			
			// entity images
			tree = ImageIO.read(new File("src/ui/images/entities/tree.png"));
			bush = ImageIO.read(new File("src/ui/images/entities/tree.png"));
			
//			robewalking
			for(int i  = 0; i < robeWalk.length; i++){
				for(int j = 0; j < robeWalk[i].length; j++){
					Image img = ImageIO.read(new File("src/ui/images/player/robe/movement/robe-"+i+"-move-"+j+".png"));
					robeWalk[i][j] = img; 
				}
			}
			
//			leatherwalking
			for(int i  = 0; i < leatherWalk.length; i++){
				for(int j = 0; j < leatherWalk[i].length; j++){
					Image img = ImageIO.read(new File("src/ui/images/player/leather/movement/leather-"+i+"-move-"+j+".png"));
					leatherWalk[i][j] = img; 
				}
			}
			
//			chainwalking
			for(int i  = 0; i < leatherWalk.length; i++){
				for(int j = 0; j < leatherWalk[i].length; j++){
					Image img = ImageIO.read(new File("src/ui/images/player/chain/movement/chain-"+i+"-move-"+j+".png"));
					chainWalk[i][j] = img; 
				}
			}
			
//			platewalking
			for(int i  = 0; i < leatherWalk.length; i++){
				for(int j = 0; j < leatherWalk[i].length; j++){
					Image img = ImageIO.read(new File("src/ui/images/player/plate/movement/plate-"+i+"-move-"+j+".png"));
					plateWalk[i][j] = img; 
				}
			}
			
			
		}catch(IOException e){
			System.err.println(e.getLocalizedMessage());
		}
	}
}
