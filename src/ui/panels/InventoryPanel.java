package ui.panels;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class InventoryPanel extends JLayeredPane{

	Image backgroundImage; 
	ArrayList<Item> inventList = new ArrayList<Item>();

	public InventoryPanel(){
		setLayout(null);
		setBounds(790, 450, 210, 330);
			
		//Add invent background
		InventoryBackground inventBackground = new InventoryBackground();
		this.add(inventBackground,0,0);
		
		//Add invent items
		populateInvent();
	}
	
	public void addItem(Item item){
		inventList.add(item);
		populateInvent();
	}
	
	public void removeItem(Item item){
		inventList.remove(item);
	}
	
	private void populateInvent(){
		int x = 13,  y = 37, xcount = 0, ycount = 0;
		for(Item i : inventList){
			if(ycount > 6){
				System.out.println("Inventory Full");
				return;
			}
			JLabel item = new JLabel(i.getImage());
			item.setBounds(x,y,35,35);
			this.add(item, 1,1);
			xcount++;
			if(xcount < 4){
				x += 49;
			}
			else{
				xcount = 0;
				x = 13;
				y += 36;
			}
		}
	}
}
