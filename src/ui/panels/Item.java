package ui.panels;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Item {
	
	private String Name;
	private String Description;
	private ImageIcon image;
	private String potionImage = "src/ui/images/potionInventItem.png";
	private String shankImage = "src/ui/images/shankInventImageSize.png";
	private int x,y;
	
	public Item(String n, String d){
		this.Name = n;
		this.Description = d;
		if(n.equals("Shank")){
			image = new ImageIcon(shankImage);
		}
		else{
			image = new ImageIcon(potionImage);
		}
	}
	
	public Item(String n, String d, int x, int y){
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
	
	public boolean contains(int x, int y){
		if(x >= this.x && x <= this.x + 35){
			if(y >= this.y && y <= this.y + 35){
				return true;
			}
		}
		return false;
	}
}
