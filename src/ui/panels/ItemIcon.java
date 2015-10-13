package ui.panels;

import javax.swing.ImageIcon;

public class ItemIcon {

	private String Name;
	private String Description;
	private ImageIcon image;
	private String potionImage = "src/ui/images/items/potionInventItem.png";
	private String shankImage = "src/ui/images/items/shankImage.png";
	private String spearImage = "src/ui/images/items/spearImage.png";
	private String helmet1Image = "src/ui/images/items/helmet-1.png";
	private String helmet2Image = "src/ui/images/items/helmet-2.png";
	private String keyImage = "src/ui/images/items/keyImage.png";
	private String plateImage = "src/ui/images/items/plateImage.png";
	private String chainImage = "src/ui/images/items/chainImage.png";
	private String leatherImage = "src/ui/images/items/leatherImage.png";
	private String robeImage = "src/ui/images/items/robeImage.png";

	//Gold images
	private String goldImage50 = "src/ui/images/items/50goldImage.png";
	private String goldImage40 = "src/ui/images/items/40goldImage.png";
	private String goldImage30 = "src/ui/images/items/30goldImage.png";
	private String goldImage20 = "src/ui/images/items/20goldImage.png";
	private String goldImage10 = "src/ui/images/items/10goldImage.png";
	private String goldImage5 = "src/ui/images/items/5goldImage.png";
	private String goldImage4 = "src/ui/images/items/4goldImage.png";
	private String goldImage3 = "src/ui/images/items/3goldImage.png";
	private String goldImage2 = "src/ui/images/items/2goldImage.png";
	private String goldImage1 = "src/ui/images/items/1goldImage.png";
	private int x,y;
	private boolean equipable;
	private String type;

	public ItemIcon(String n, String d){
		this.Name = n;
		this.Description = d;
		if(n.equals("Shank")){
			image = new ImageIcon(shankImage);
			this.equipable = true;
			this.type = "Weapon";
		}
		else if (n.equals("Spear")){
			image = new ImageIcon(spearImage);
			this.equipable = true;
			this.type = "Weapon";
		}
		else if (n.equals("Potion")){
			image = new ImageIcon(potionImage);
			this.equipable = false;
			this.type = "Consumable";
		}
		else if (n.equals("Plate Armour")){
			image = new ImageIcon(plateImage);
			this.equipable = true;
			this.type = "Armour";
		}
		else if (n.equals("Chain Armour")){
			image = new ImageIcon(chainImage);
			this.equipable = true;
			this.type = "Armour";
		}
		else if (n.equals("Robe Armour")){
			image = new ImageIcon(robeImage);
			this.equipable = true;
			this.type = "Armour";
		}
		else if (n.equals("Leather Armour")){
			image = new ImageIcon(leatherImage);
			this.equipable = true;
			this.type = "Armour";
		}
		else if (n.equals("Key")){
			image = new ImageIcon(keyImage);
			this.equipable = true;
			this.type = "Key";
		}
		else if (n.equals("Gold")){
			String[] splitDesc = d.split(" ");
			int goldAmount = Integer.parseInt(splitDesc[1]);
			if(goldAmount <= 4){
				switch(goldAmount){
				case 1:
					image = new ImageIcon(goldImage1);
					break;
				case 2:
					image = new ImageIcon(goldImage2);
					break;
				case 3:
					image = new ImageIcon(goldImage3);
					break;
				case 4:
					image = new ImageIcon(goldImage4);
					break;
				}
			}
			else if(goldAmount >= 5 && goldAmount < 10){
				image = new ImageIcon(goldImage5);
			}
			else if(goldAmount >= 10 && goldAmount < 20){
				image = new ImageIcon(goldImage10);
			}
			else if(goldAmount >= 20 && goldAmount < 30){
				image = new ImageIcon(goldImage20);
			}
			else if(goldAmount >= 30 && goldAmount < 40){
				image = new ImageIcon(goldImage30);
			}
			else if(goldAmount >= 40 && goldAmount < 50){
				image = new ImageIcon(goldImage40);
			}
			else if(goldAmount >= 50){
				image = new ImageIcon(goldImage50);
			}
			this.equipable = false;
			this.type = "Gold";
		}
		else{
			image = null;
			this.equipable = false;
			this.type = "Empty";
		}
	}

	//Getters and Setters
	public String getName(){return this.Name;}
	public String getDescription(){return this.Description;}
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
