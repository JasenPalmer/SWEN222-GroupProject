package ui.panels;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Item {
	
	private String Name;
	private String Description;
	private ImageIcon image;
	private String potionImage = "src/ui/images/potionInventItem.png";
	private String shankImage = "src/ui/images/shankInventImageSize.png";
	
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
	
	//Getters and Setters
	public String getName(){return this.Name;}
	public String getDesciption(){return this.Description;}
	public ImageIcon getImage(){return this.image;}
}
