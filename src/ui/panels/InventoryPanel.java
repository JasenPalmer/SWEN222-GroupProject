package ui.panels;


import gameworld.entity.Armour;
import gameworld.entity.Container;
import gameworld.entity.Gold;
import gameworld.entity.Item;
import gameworld.entity.Potion;
import gameworld.entity.Weapon;

import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

import network.Client;
import ui.ApplicationWindow;

/**
 * Inventory component which displays players items
 * @author ItsNotAGoodTime
 *
 */
public class InventoryPanel extends JLayeredPane implements MouseListener{
	//ItemIcon array
	ItemIcon[][] inventArray = new ItemIcon[4][2];
	
	//Panels
	private InventoryBackground inventBackground = new InventoryBackground();
	private InventoryPanel self = this;
	private ApplicationWindow app;
	
	//Moved item
	private ItemIcon movedItem;
	private int movedItemIndex;
	
	//States
	private boolean lootOpen;

	//Inventory client
	private Client client;
	
	//Container player opened
	private Container container;

	/**
	 * Adds required components and sets layout
	 * @param app - Application window associated with invent
	 */
	public InventoryPanel(ApplicationWindow app){
		setLayout(null);
		setBounds(814, 637, 231, 262);
		this.app = app;
		this.client = app.getClient();

		//Add invent background
		this.add(inventBackground,0,0);

		//Add invent items
		populateInventArray();
		fillEquipmentSlots();
		addMouseListener(this);
	}

	/**
	 * Sets container opened by player
	 * @param container - Container opened
	 */
	public void setContainer(Container container){
		this.container = container;
	}

	/**
	 * Populates ItemIcon array with new ItemIcons based on players inventory item array
	 */
	public void populateInventArray(){
		Item[] itemList = client.getState().getInventory();

		//First 6 slots
		for(int i = 0; i < 4; i++){
			if(itemList[i] != null){
				inventArray[i][0] = new ItemIcon(itemList[i].getName(), itemList[i].getDescription());
			}
			else{
				inventArray[i][0] = null;
			}
		}

		//Second 6 slots
		for(int i = 0; i < 4; i++){
			if(itemList[i+4] != null){
				inventArray[i][1] = new ItemIcon(itemList[i+4].getName(), itemList[i+4].getDescription());
			}
			else{
				inventArray[i][1] = null;
			}
		}

		fillAllSlots();
	}

	/**
	 * Fills all slots with an empty item slot
	 */
	private void fillAllSlots(){

		for(int j = 0; j < inventArray[0].length; j++){
			for(int i = 0; i < inventArray.length; i++){
				if(inventArray[i][j] == null){
					inventArray[i][j] = new ItemIcon("Empty", "Placeholder");
				}
			}
		}

		populateInvent();
	}

	/**
	 * Clears the inventory then populates it with elements of the inventory array
	 */
	private void populateInvent(){
		this.removeAll();

		this.add(inventBackground,0,0);

		//Sets ItemIcon x and y coords
		for(int j = 0; j < inventArray[0].length; j++){
			for(int i = 0; i < inventArray.length; i++){
				if(inventArray[i][j] != null){
					inventArray[i][j].setX(11+(i*55));
					inventArray[i][j].setY(44+(j*70));
				}
			}
		}

		//Creates and places label relative to ItemIcons x and  y coords
		for(int j = 0; j < inventArray[0].length; j++){
			for(int i = 0; i < inventArray.length; i++){
				if(inventArray[i][j] != null){
					JLabel item = new JLabel(inventArray[i][j].getImage());
					item.setBounds(inventArray[i][j].getX(), inventArray[i][j].getY(), 42,52);
					this.add(item,1,0);
					if(!inventArray[i][j].getName().equals("Empty")){
						item.setToolTipText(inventArray[i][j].getDescription());
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

		fillEquipmentSlots();
	}

	/**
	 * Swaps 2 elements of the array
	 * @param x1 - Item 1 array x coordinate
	 * @param y1 - Item 1 array y coordinate
	 * @param x2 - Item 2 array x coordinate
	 * @param y2 - Item 2 array y coordinate
	 */
	public void addItemTo(int x1, int y1, int x2, int y2){
		if(inventArray[x2][y2].equals(null)){
			inventArray[x2][y2] = inventArray[x1][y1];
			inventArray[x1][y1] = null;
		}
		else{
			ItemIcon temp = inventArray[x2][y2];
			inventArray[x2][y2] = inventArray[x1][y1];
			inventArray[x1][y1] = temp;
		}
		populateInventArray();
	}

	/**
	 * Change state of lootOpen
	 * @param change
	 */
	public void setLootVis(boolean change){
		this.lootOpen = change;
	}

	/**
	 * Populates equipment slots from players equipment
	 */
	private void fillEquipmentSlots(){
		//Creates and places ItemIcons in Equipment slots relative to players current weapon
		if(client.getState().getWeapon() != null){
			String name = client.getState().getWeapon().getName();
			String desc = client.getState().getWeapon().getDescription();
			ItemIcon weapon = new ItemIcon(name, desc);
			JLabel weaponLabel = new JLabel(weapon.getImage());
			weaponLabel.setBounds(65,195,42,52);
			this.add(weaponLabel,1,0);
			if(client.getState().getWeapon() != null){
				weaponLabel.setToolTipText(desc);
				weaponLabel.addMouseListener(new MouseAdapter(){
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
		//Creates and places ItemIcons in Equipment slots relative to players current armour
		if(client.getState().getArmour() != null){
			String name = client.getState().getArmour().getName();
			String desc = client.getState().getArmour().getDescription();
			ItemIcon armour = new ItemIcon(name, desc);
			JLabel armourLabel = new JLabel(armour.getImage());
			armourLabel.setBounds(120,195,42,52);
			this.add(armourLabel,1,0);
			if(client.getState().getArmour() != null){
				armourLabel.setToolTipText(desc);
				armourLabel.addMouseListener(new MouseAdapter(){
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

	/**
	 * Converts 2d array index's to 1d array index
	 * @param i - Index 1
	 * @param j - Index 2
	 * @return - Converted 1d array index
	 */
	private int convertIndex(int i, int j){
		if(j == 0){
			return i;
		}
		else{
			return i+4;
		}
	}

	/**
	 * Checks if inventory is full
	 * @return - Whether inventory is full or not
	 */
	public boolean isInventFull(){
		Item[] itemList = client.getState().getInventory();
		for(int i = 0; i < itemList.length; i++){
			if(itemList[i] == null){
				return false;
			}
		}
		return true;
	}

	/**
	 * Creates new item based on name and description provided
	 * @param name - Name of item
	 * @param desc - Description of item
	 * @return - New item based on name and description
	 */
	private Item makeItem(String name, String desc){
		Item item = null;

		switch(name){
		case "Shank":
			item = new Weapon(name, desc, null, null, Weapon.WeaponType.Shank);
			break;
		case "Spear":
			item = new Weapon(name, desc, null, null, Weapon.WeaponType.Spear);
			break;
		case "Chain Armour":
			item = new Armour(name, desc, null, null, Armour.ArmourType.Chain);
			break;
		case "Leather Armour":
			item = new Armour(name, desc, null, null, Armour.ArmourType.Leather);
			break;
		case "Plate Armour":
			item = new Armour(name, desc, null, null, Armour.ArmourType.Plate);
			break;
		case "Robe Armour":
			item = new Armour(name, desc, null, null, Armour.ArmourType.Robe);
			break;
		case "Potion":
			item = new Potion(name, desc, null, null);
			break;
		case "Gold":
			String[] splitDesc = desc.split(" ");
			item = new Gold("Gold", desc, null, null, Integer.parseInt(splitDesc[1]));
			break;
		}
		return item;
	}

	/**
	 * Allows player to drag and drop items as well as use items
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		//Sets item to be moved in inventory
		if(e.getButton() == MouseEvent.BUTTON1){
			for(int i = 0; i < inventArray.length; i++){
				for(int j = 0; j < inventArray[0].length; j++){
					if(inventArray[i][j] != null && !inventArray[i][j].getName().equals("Empty")){
						if(inventArray[i][j].contains(e.getX(), e.getY())){
							movedItem = inventArray[i][j];
							movedItemIndex = convertIndex(i,j);
							return;
						}
					}
					else{
						movedItem = null;
					}
				}
			}
		}
		//Right click events
		else if(e.getButton() == MouseEvent.BUTTON3){
			Item temp = null;
			//If right click on weapon slot unequip weapon
			if(e.getX() >= 65 && e.getX() <= 107 && e.getY() >= 195 && e.getY() <= 247){
				if(client.getState().getWeapon() != null && !isInventFull()){
					client.addItem(client.getState().getWeapon());
					client.setWeapon(null);
					this.app.playSound("Button");
				}
				else{
					System.out.println("Inventory full can't dequip weapon");
				}
			}
			//If right click on armour slot unequip armour
			else if(e.getX() >= 120 && e.getX() <= 162 && e.getY() >= 195 && e.getY() <= 247){
				if(client.getState().getArmour() != null && !isInventFull()){
					client.addItem(client.getState().getArmour());
					client.setArmour(null);
					this.app.playSound("Button");
				}
				else{
					System.out.println("Inventory full can't dequip armour");
				}
			}
			else{
				//Swap weapon or armour or use item
				for(int i = 0; i < inventArray.length; i++){
					for(int j = 0; j < inventArray[0].length; j++){
						if(inventArray[i][j]!= null && inventArray[i][j].getName() != "Empty"){
							if(inventArray[i][j].contains(e.getX(), e.getY())){
								//Swap weapon
								if(inventArray[i][j].getType().equals("Weapon")){
									if(client.getState().getWeapon() != null){
										temp = client.getState().getWeapon();
									}
									Weapon newWeapon = (Weapon) makeItem(inventArray[i][j].getName(), inventArray[i][j].getDescription());
									client.setWeapon(newWeapon);
									client.removeItem(convertIndex(i,j));

									if(temp != null){
										client.addItem(temp);
									}
									this.app.playSound("Button");
								}
								//Swap armour
								else if(inventArray[i][j].getType().equals("Armour")){
									if(client.getState().getArmour() != null){
										temp = client.getState().getArmour();
									}
									Armour newArmour = (Armour) makeItem(inventArray[i][j].getName(), inventArray[i][j].getDescription());
									client.setArmour(newArmour);
									client.removeItem(convertIndex(i,j));

									if(temp != null){
										client.addItem(temp);
									}
									this.app.playSound("Button");
								}
								//Use item
								else if(inventArray[i][j].getType().equals("Consumable")){
									client.useItem((Potion)makeItem(inventArray[i][j].getName(), inventArray[i][j].getDescription()));
									client.removeItem(convertIndex(i,j));
								}
							}
						}
					}
				}
			}
			populateInventArray();
		}
	}

	/**
	 * Swaps moved item with new location or item
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1){
			if(movedItem != null){
				//Swap items in inventory
				for(int i = 0; i < inventArray.length; i++){
					for(int j = 0; j < inventArray[0].length; j++){
						if(inventArray[i][j]!= null){
							if(inventArray[i][j].contains(e.getX(), e.getY()) && !inventArray[i][j].getName().equals(movedItem.getName())){
								client.swapItems(movedItemIndex, convertIndex(i,j));
								movedItem = null;
								this.app.playSound("Button");
							}
						}
					}
				}
				//Drop item if released out of invent
				if(!lootOpen){
					if(e.getX() < -2 || e.getY() < -3){
						client.dropItem(movedItemIndex);
					}
				}
				//Move items to container
				else{
					if(e.getX() > -454 && e.getX() < -130 && e.getY() > -521 && e.getY() < -319){
						System.out.println("Inside");
						for(int i = 0; i < inventArray.length; i++){
							for(int j = 0; j < inventArray[0].length; j++){
								if(inventArray[i][j]!= null && inventArray[i][j].getName() != "Empty"){
									if(inventArray[i][j].contains(movedItem.getX(), movedItem.getY())){
										if(this.container != null){
											if(!isContainerFull()){	
												this.app.playSound("Button");
												client.addItemContainer(makeItem(inventArray[i][j].getName(), inventArray[i][j].getDescription()), this.container);
												client.removeItem(convertIndex(i,j));
											}
											else{
												System.out.println("Loot inventory is full can't swap item");
											}
										}else{
											System.out.println("is null");
										}
									}
								}
							}
						}
					}
				}
				populateInventArray();
			}

		}
	}

	/**
	 * Check is container is full
	 * @return - Whether container is full
	 */
	private boolean isContainerFull() {
		if(this.container == null) return true;
		for(int i = 0; i < this.container.getItems().length; i++){
			if(this.container.getItems()[i] == null){
				return false;
			}
		}
		return true;
	}

	//Getters and Setters
	public Client getPlayer(){return this.client;}
	
	//Required but unused methods
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {	}
	@Override
	public void mouseExited(MouseEvent arg0) {}
}
