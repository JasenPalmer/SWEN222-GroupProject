package ui.panels;

import gameworld.entity.Armour;
import gameworld.entity.Container;
import gameworld.entity.Gold;
import gameworld.entity.Item;
import gameworld.entity.Key;
import gameworld.entity.Weapon;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

import network.Client;
import ui.ApplicationWindow;

/**
 * Panel that displays the contents of a container
 * @author ItsNotAGoodTime
 *
 */
public class LootInventoryPanel extends JLayeredPane implements MouseListener{
	
	//Serialize
	private static final long serialVersionUID = 1L;

	//Item icon array
	private ItemIcon[][] itemList = new ItemIcon[6][3];
	
	//Panels client
	private Client client;
	
	//Container
	private Container container;
	
	//States
	private boolean inventOpen;
	
	//Panels
	private LootInventoryBackground lootInventBackground = new LootInventoryBackground();
	private InventoryPanel inventPanel;
	private LootInventoryPanel self = this;
	private ApplicationWindow app;
	
	//Moved item
	private ItemIcon movedItem;
	private int movedItemI;
	private int movedItemJ;
	
	/**
	 * Sets dimensions of layout
	 * @param invent - the inventory panel of the player
	 */
	public LootInventoryPanel(ApplicationWindow app){
		//Setup
		this.app = app;
		this.inventPanel = app.getInventPanel();
		setLayout(null);
		setBounds(345, 100, 360, 240);

		//Add background
		this.add(lootInventBackground,0,0);

		populateSlots();
		addMouseListener(this);
		this.client = inventPanel.getPlayer();
	}

	/**
	 * Populates loot panel with the specified loot container
	 * @param container - Container to populate from
	 */
	public void setLootContainer(Container container){
		this.container = container;
		
		Item[] containerList = this.container.getItems();
		
		//First row of 6 items
		for(int i = 0; i < 6; i++){
			if(containerList[i] != null){
				itemList[i][0] = new ItemIcon(containerList[i].getName(), containerList[i].getDescription());
			}
			else{
				itemList[i][0] = null;
			}
		}
		
		//Second row of 6 items
		for(int i = 0; i < 6; i++){
			if(containerList[i+6] != null){
				itemList[i][1] = new ItemIcon(containerList[i+6].getName(), containerList[i+6].getDescription());
			}
			else{
				itemList[i][1] = null;
			}
		}
		
		//Third row of 6 items
		for(int i = 0; i < 6; i++){
			if(containerList[i+12] != null){
				itemList[i][2] = new ItemIcon(containerList[i+12].getName(), containerList[i+12].getDescription());
			}
			else{
				itemList[i][2] = null;
			}
		}
		populateSlots();
	}
	
	/**
	 * Sets locations of ItemIcon labels relevant to inventory image
	 */
	private void populateSlots(){
		this.removeAll();
		this.add(lootInventBackground);

		//Sets x and y coords for ItemIcon list
		for(int i = 0; i < itemList.length; i++){
			for(int j = 0; j < itemList[0].length; j++){
				if(itemList[i][j] != null){
					itemList[i][j].setX(25+(i*53));
					itemList[i][j].setY(28+(j*67));
				}
			}
		}

		//Creates label for each item in inventory and places it on items x and y coords
		for(int i = 0; i < itemList.length; i++){
			for(int j = 0; j < itemList[0].length; j++){
				if(itemList[i][j] != null){
					JLabel item = new JLabel(itemList[i][j].getImage());
					item.setBounds(itemList[i][j].getX(), itemList[i][j].getY(), 42,52);
					this.add(item,1,0);
					if(!itemList[i][j].getDescription().equals("Placeholder")){
						item.setToolTipText(itemList[i][j].getDescription());
						item.addMouseListener(new MouseAdapter(){
							public void mouseClicked(MouseEvent e){
								self.dispatchEvent(SwingUtilities.convertMouseEvent(e.getComponent(), e, self));
							}
							public void mousePressed(MouseEvent e){
								self.dispatchEvent(SwingUtilities.convertMouseEvent(e.getComponent(), e, self));
							}
							public void mouseReleased(MouseEvent e){
								self.dispatchEvent(SwingUtilities.convertMouseEvent(e.getComponent(), e, self));
							}
						});
					}
				}
			}
		}
	}

	/**
	 * Changes inventOpen state
	 * @param change
	 */
	public void setInventVis(boolean change){
		this.inventOpen = change;
	}

	/**
	 * Sets moved item to item clicked
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1){
			for(int i = 0; i < itemList.length; i++){
				for(int j = 0; j < itemList[0].length; j++){
					if(itemList[i][j]!= null && itemList[i][j].getName() != "Empty"){
						if(itemList[i][j].contains(e.getX(), e.getY())){
							movedItem = itemList[i][j];
							movedItemI = i;
							movedItemJ = j;
						}
					}
				}
			}
		}
	}

	/**
	 * Converts index's of a 2d array element to a 1d array index
	 * @param i - Index 1
	 * @param j - Index 2
	 * @return - Converted index for 1d array
	 */
	private int convertIndex(int i, int j){
		int index = 0;	
		if(j == 0){
			index = i;
		}
		else if(j == 1){
			index = i+6;
		}
		else{
			index = i+12;
		}	
		return index;
	}
	
	/**
	 * Moves item to players inventory from loot panel
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if(movedItem != null){
			if(inventOpen){
				if(e.getX() > 473 && e.getX() < 691 && e.getY() > 573 && e.getY() < 709){
					if(!inventPanel.isInventFull()){
						client.addItem(makeItem(movedItem.getName(), movedItem.getDescription()));
						client.removeItemContainer(convertIndex(movedItemI, movedItemJ), this.container);
						this.app.playSound("Button");
						movedItem = null;
					}
					else{
						System.out.println("Inventory full can't swap items");
					}
				}
				populateSlots();
			}
		}
	}

	/**
	 * Creates and item based on name and description provided
	 * @param name - Name of item
	 * @param desc - Description of item
	 * @return - New item with specified details
	 */
	private Item makeItem(String name, String desc){
		Item item = null;

		switch(name){
		case "Shank":
			item = new Weapon("Shank", desc, null, null, Weapon.WeaponType.Shank);
			break;
		case "Spear":
			item = new Weapon("Spear", desc, null, null, Weapon.WeaponType.Spear);
			break;
		case "Chain Armour":
			item = new Armour("Chain Armour", desc, null, null, Armour.ArmourType.Chain);
			break;
		case "Leather Armour":
			item = new Armour("Leather Armour", desc, null, null, Armour.ArmourType.Leather);
			break;
		case "Plate Armour":
			item = new Armour("Plate Armour", desc, null, null, Armour.ArmourType.Plate);
			break;
		case "Robe Armour":
			item = new Armour("Robe Armour", desc, null, null, Armour.ArmourType.Robe);
			break;
		case "Potion":
			item = new Armour("Potion", desc, null, null, Armour.ArmourType.Robe);
			break;
		case "Key":
			item = new Key("Key", desc, null, null);
			break;
		case "Gold":
			String[] splitDesc = desc.split(" ");
			item = new Gold("Gold", desc, null, null, Integer.parseInt(splitDesc[1]));
			break;
		}

		return item;
	}
	
	//Required but unused methods
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
}
