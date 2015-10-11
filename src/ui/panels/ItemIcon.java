package ui.panels;

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
	
	public ItemIcon(String n, String d){
		this.Name = n;
		this.Description = d;
		if(n.equals("ShankWeapon")){
			image = new ImageIcon(shankImage);
			this.equipable = true;
			this.type = "Weapon";
		}
		else if (n.equals("SpearWeapon")){
			image = new ImageIcon(katanaImage);
			this.equipable = true;
			this.type = "Weapon";
		}
		else if (n.equals("Potion")){
			image = new ImageIcon(potionImage);
			this.equipable = false;
			this.type = "Consumable";
		}
		else if (n.equals("PlateArmour")){
			image = new ImageIcon(helmet1Image);
			this.equipable = true;
			this.type = "Armour";
		}
		else if (n.equals("ChainArmour")){
			image = new ImageIcon(helmet2Image);
			this.equipable = true;
			this.type = "Armour";
		}
		else if (n.equals("RobeArmour")){
			image = new ImageIcon(helmet2Image);
			this.equipable = true;
			this.type = "Armour";
		}
		else if (n.equals("LeatherArmour")){
			image = new ImageIcon(helmet2Image);
			this.equipable = true;
			this.type = "Armour";
		}
		else if (n.equals("Key")){
			image = new ImageIcon(helmet2Image);
			this.equipable = true;
			this.type = "Key";
		}
		else{
			image = null;
			this.equipable = false;
			this.type = "Empty";
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
