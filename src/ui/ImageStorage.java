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
	public static Image floor;
	public static Image wallL;
	public static Image wallR;
	public static Image wallCorner;
	public static Image table;
	public static Image chest;
	public static Image chair;
	public static Image bag;
	public static Image insideDoorL;
	public static Image insideDoorR;

	static Image[][] robeWalk = new Image[4][9];
	static Image[][] robeSpear = new Image[4][8];
	
	static Image[][] leatherWalk = new Image[4][9];
	static Image[][] leatherSpear = new Image[4][8];
	
	static Image[][] chainWalk = new Image[4][9];
	static Image[][] chainSpear = new Image[4][8];
	
	static Image[][] plateWalk = new Image[4][9];
	static Image[][] plateSpear = new Image[4][8];


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

			// outside building images
			building = ImageIO.read(new File("src/ui/images/buildings/Room.png"));
			doorUD = ImageIO.read(new File("src/ui/images/buildings/DoorUD.png"));
			doorLR = ImageIO.read(new File("src/ui/images/buildings/DoorLR.png"));
			roofLR = ImageIO.read(new File("src/ui/images/buildings/RoofLR.png"));
			roofUD = ImageIO.read(new File("src/ui/images/buildings/RoofUD.png"));
			roofCornerO = ImageIO.read(new File("src/ui/images/buildings/RoofCornerO.png"));
			roofCornerI = ImageIO.read(new File("src/ui/images/buildings/RoofCornerI.png"));

			// inside building images
			floor = ImageIO.read(new File("src/ui/images/inside/Floor.png"));
			wallL = ImageIO.read(new File("src/ui/images/inside/WallLeft.png"));
			wallR = ImageIO.read(new File("src/ui/images/inside/WallRight.png"));
			wallCorner = ImageIO.read(new File("src/ui/images/inside/WallCorner.png"));
			insideDoorL = ImageIO.read(new File("src/ui/images/inside/DoorL.png"));
			insideDoorR = ImageIO.read(new File("src/ui/images/inside/DoorR.png"));
			// player images
			playerImage = ImageIO.read(new File("src/ui/images/player/robe/0.png"));


			// entity images
			tree = ImageIO.read(new File("src/ui/images/entities/Tree.png"));
			bush = ImageIO.read(new File("src/ui/images/entities/Bush.png"));
			table = ImageIO.read(new File("src/ui/images/entities/Table.png"));
			chest = ImageIO.read(new File("src/ui/images/entities/Chest.png"));
			chair = ImageIO.read(new File("src/ui/images/entities/Chair.png"));
			bag = ImageIO.read(new File("src/ui/images/entities/Bag.png"));
			
//			robewalking
			for(int i  = 0; i < robeWalk.length; i++){
				for(int j = 0; j < robeWalk[i].length; j++){
					Image img = ImageIO.read(new File("src/ui/images/player/robe/movement/robe-"+i+"-move-"+j+".png"));
					robeWalk[i][j] = img;
					if(i<robeSpear.length && j<robeSpear[i].length){
						img = ImageIO.read(new File("src/ui/images/player/robe/attack/robe-"+i+"-attack-"+j+".png"));
						robeSpear[i][j] = img;
					}
				}
			}

//			leatherwalking
			for(int i  = 0; i < leatherWalk.length; i++){
				for(int j = 0; j < leatherWalk[i].length; j++){
					Image img = ImageIO.read(new File("src/ui/images/player/leather/movement/leather-"+i+"-move-"+j+".png"));
					leatherWalk[i][j] = img;
					if(i<leatherSpear.length && j<leatherSpear[i].length){
						img = ImageIO.read(new File("src/ui/images/player/leather/attack/leather-"+i+"-attack-"+j+".png"));
						leatherSpear[i][j] = img;
					}
				}
			}

//			chainwalking
			for(int i  = 0; i < chainWalk.length; i++){
				for(int j = 0; j < chainWalk[i].length; j++){
					Image img = ImageIO.read(new File("src/ui/images/player/chain/movement/chain-"+i+"-move-"+j+".png"));
					chainWalk[i][j] = img;
					if(i<chainSpear.length && j<chainSpear[i].length){
						img = ImageIO.read(new File("src/ui/images/player/chain/attack/chain-"+i+"-attack-"+j+".png"));
						chainSpear[i][j] = img;
					}
				}
			}

//			platewalking
			for(int i  = 0; i < plateWalk.length; i++){
				for(int j = 0; j < plateWalk[i].length; j++){
					Image img = ImageIO.read(new File("src/ui/images/player/plate/movement/plate-"+i+"-move-"+j+".png"));
					plateWalk[i][j] = img;
					if(i<plateSpear.length && j<plateSpear[i].length){
						img = ImageIO.read(new File("src/ui/images/player/plate/attack/plate-"+i+"-attack-"+j+".png"));
						plateSpear[i][j] = img;
					}
				}
			}


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
			case "Grass":
				return grass;
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
			case "Fl":
				return floor;

			// ENTITIES
			case "Bush":
				return bush;
			case "Tree":
				return tree;
			case "Table":
				return table;
			case "Chest":
				return chest;
			case "Key":
				return bush;
			case "Chair":
				return chair;
			case "Bag":
				return bag;
		}
		return null;
	}
}
