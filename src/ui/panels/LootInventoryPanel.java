package ui.panels;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class LootInventoryPanel extends JLayeredPane{
	 
	private Item[][] itemList = new Item[6][3];
	private LootInventoryBackground lootInventBackground = new LootInventoryBackground();
	
	public LootInventoryPanel(){
		//Setup
		setLayout(null);
		setBounds(330, 100, 360, 240);
		
		//Add background
		this.add(lootInventBackground,0,0);
		
		populateSlots();
	}
	
	public void addItem(Item item){
		for(int i = 0; i < itemList[0].length; i++){
			for(int j = 0; j < itemList.length; j++){
				if(itemList[j][i] == null){
					itemList[j][i] = item;
					System.out.println(itemList[j][i].getName() + " Added");
					populateSlots();
					return;
				}
				if(i == itemList[0].length-1 && j == itemList.length-1){
					if(itemList[j][i] != null){
						System.out.println("Loot Inventory is full");
					}
				}
			}
		}
	}
	
	private void populateSlots(){
		this.removeAll();
		this.add(lootInventBackground);
		
		for(int i = 0; i < itemList.length; i++){
			for(int j = 0; j < itemList[0].length; j++){
				if(itemList[i][j] != null){
					itemList[i][j].setX(25+(i*53));
					itemList[i][j].setY(28+(j*67));
				}
			}
		}
		
		for(int i = 0; i < itemList.length; i++){
			for(int j = 0; j < itemList[0].length; j++){
				if(itemList[i][j] != null){
					JLabel item = new JLabel(itemList[i][j].getImage());
					item.setBounds(itemList[i][j].getX(), itemList[i][j].getY(), 42,52);
					this.add(item,1,0);
				}
			}
		}
	}
}
