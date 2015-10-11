package ui.panels;

import gameworld.entity.Item;

import java.awt.Image;

import javax.swing.ImageIcon;

public class ItemIcon {
	
	private String Name;
	private String Description;
	private ImageIcon image;
	private String potionImage = "src/ui/images/items/potionInventItem.png";
	private String katanaImage = "src/ui/images/items/katana-image.png";
	private String shankImage = "src/ui/images/items/shankInventImageSize.png";
	private String helmet1Image = "src/ui/images/items/helmet-1.png";
	private String helmet2Image = "src/ui/images/items/helmet-2.png";
	private int x,y;
	private boolean equipable;
	private String type;
	private Item item;
	
	public ItemIcon(Item item , String n, String d){
		this.Name = n;
		this.Description = d;
		this.item = item;
		System.out.println(item.getClass().getSimpleName());
		if(item.getClass().getSimpleName().equals("ShankWeapon")){
			image = new ImageIcon(shankImage);
			this.equipable = true;
			this.type = "Weapon";
		}
		else if (item.getClass().getSimpleName().equals("SpearWeapon")){
			image = new ImageIcon(katanaImage);
			this.equipable = true;
			this.type = "Weapon";
		}
		else if (item.getClass().getSimpleName().equals("Potion")){
			image = new ImageIcon(potionImage);
			this.equipable = false;
			this.type = "Consumable";
		}
		else if (item.getClass().getSimpleName().equals("PlateArmour")){
			image = new ImageIcon(helmet1Image);
			this.equipable = true;
			this.type = "Armour";
		}
		else if (item.getClass().getSimpleName().equals("ChainArmour")){
			image = new ImageIcon(helmet2Image);
			this.equipable = true;
			this.type = "Armour";
		}
		else if (item.getClass().getSimpleName().equals("RobeArmour")){
			image = new ImageIcon(helmet2Image);
			this.equipable = true;
			this.type = "Armour";
		}
		else if (item.getClass().getSimpleName().equals("LeatherArmour")){
			image = new ImageIcon(helmet2Image);
			this.equipable = true;
			this.type = "Armour";
		}
		else if (item.getClass().getSimpleName().equals("Key")){
			image = new ImageIcon(helmet2Image);
			this.equipable = true;
			this.type = "Armour";
		}
		else{
			image = null;
			this.equipable = false;
			this.type = "Empty";
			System.out.println("plz no");
		}
	}
	
	//Maybe redundant? :O
	public ItemIcon(String n, String d, int x, int y){
		this.Name = n;
		this.Description = d;
		this.x = x;
		this.y = y;
		if(n.equals("Shank")){
			image = new ImageIcon(shankImage);
		}
		else{
			image = new ImageIcon(potionImage);
		}
	}
	
	//Getters and Setters
	public String getName(){return this.Name;}
	public String getDesciption(){return this.Description;}
	public ImageIcon getImage(){return this.image;}
	public void setX(int x){this.x = x;}
	public void setY(int y){this.y = y;}
	public int getX(){return this.x;}
	public int getY(){return this.y;}
	public boolean getEquipable(){return this.equipable;}
	public String getType(){return this.type;}
	
	public boolean contains(int x, int y){
		if(x >= this.x && x <= this.x + 42){
			if(y >= this.y && y <= this.y + 52){
				return true;
			}
		}
		return false;
	}
}
