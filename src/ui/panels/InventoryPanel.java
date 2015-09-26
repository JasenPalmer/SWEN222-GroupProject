package ui.panels;


import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;


public class InventoryPanel extends JLayeredPane implements MouseListener{

	Image backgroundImage; 
	Item[][] inventArray = new Item[4][2];
	InventoryBackground inventBackground = new InventoryBackground();
	private Item movedItem;
	private Item weapon, armour;
	private boolean lootOpen;
	private LootInventoryPanel lootInvent;


	public InventoryPanel(){
		setLayout(null);
		setBounds(810, 487, 231, 262);

		//Add invent background
		this.add(inventBackground,0,0);

		//Add invent items
		inventArray[0][0] = null;
		fillAllSlots();
		populateInvent();
		fillEquipmentSlots();
		addMouseListener(this);
	}

	public boolean addItem(Item item){
		for(int i = 0; i < inventArray[0].length; i++){
			for(int j = 0; j < inventArray.length; j++){
				if(inventArray[j][i].getName().equals("Empty")){
					inventArray[j][i] = item;
					System.out.println(inventArray[j][i].getName() + " Added");
					populateInvent();
					return true;
				}
				if(i == inventArray[0].length-1 && j == inventArray.length-1){
					if(inventArray[j][i] != null){
						System.out.println("Inventory is full");
						return false;
					}
				}
			}
		}
		return false;
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
			Item temp = inventArray[x2][y2];
			inventArray[x2][y2] = inventArray[x1][y1];
			inventArray[x1][y1] = temp;
		}
		populateInvent();
	}

	/**
	 * Clears the inventory then populates it with elements of the inventory array
	 */
	private void populateInvent(){
		this.removeAll();
		this.add(inventBackground,0,0);

		for(int i = 0; i < inventArray.length; i++){
			for(int j = 0; j < inventArray[0].length; j++){
				if(inventArray[i][j] != null){
					inventArray[i][j].setX(11+(i*55));
					inventArray[i][j].setY(44+(j*70));
				}
			}
		}

		for(int i = 0; i < inventArray.length; i++){
			for(int j = 0; j < inventArray[0].length; j++){
				if(inventArray[i][j] != null){
					JLabel item = new JLabel(inventArray[i][j].getImage());
					item.setBounds(inventArray[i][j].getX(), inventArray[i][j].getY(), 42,52);
					this.add(item,1,0);
					if(!inventArray[i][j].getDesciption().equals("Placeholder")){
						item.setToolTipText(inventArray[i][j].getDesciption());
					}
				}
			}
		}

		fillEquipmentSlots();
	}

	public void setLootVis(boolean change){
		this.lootOpen = change;
	}

	public void setLootInventPanel(LootInventoryPanel lootInvent){
		this.lootInvent = lootInvent;
	}

	private void fillEquipmentSlots(){
		if(weapon != null){
			JLabel weaponLabel = new JLabel(weapon.getImage());
			weaponLabel.setBounds(65,195,42,52);
			this.add(weaponLabel,1,0);
		}
		if(armour != null){
			JLabel armourLabel = new JLabel(armour.getImage());
			armourLabel.setBounds(120,195,42,52);
			this.add(armourLabel,1,0);
		}
	}

	/**
	 * Fills all slots with an empty item slot
	 */
	private void fillAllSlots(){
		for(int i = 0; i < inventArray.length; i++){
			for(int j = 0; j < inventArray[0].length; j++){
				if(inventArray[i][j] == null){
					inventArray[i][j] = new Item("Empty", "Placeholder");
				}
			}
		}
	}

	private void setToolTips(){
		for(int i = 0; i < inventArray.length; i++){
			for(int j = 0; j < inventArray[0].length; j++){
				
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON3){
			if(lootOpen == false){
				Item temp = null;
				System.out.println("Registered Right-Click");
				if(e.getX() >= 65 && e.getX() <= 107 && e.getY() >= 195 && e.getY() <= 247){
					if(weapon != null && addItem(weapon)){
						weapon = null;
					}
					else{
						System.out.println("Inventory full can't dequip weapon");
					}
				}
				else if(e.getX() >= 120 && e.getX() <= 162 && e.getY() >= 195 && e.getY() <= 247){
					if(armour != null && addItem(armour)){
						armour = null;
					}
					else{
						System.out.println("Inventory full can't dequip armour");
					}
				}
				else{
					//Check/Change items in invent
					for(int i = 0; i < inventArray.length; i++){
						for(int j = 0; j < inventArray[0].length; j++){
							if(inventArray[i][j]!= null && inventArray[i][j].getName() != "Empty"){
								if(inventArray[i][j].contains(e.getX(), e.getY())){
									if(inventArray[i][j].getType().equals("Weapon")){
										if(weapon != null){
											temp = new Item(weapon.getName(), weapon.getDesciption());
										}
										weapon = inventArray[i][j];
										inventArray[i][j] = new Item("Empty", "PlaceHolder");
										if(temp != null){
											addItem(temp);
										}
									}
									else if(inventArray[i][j].getType().equals("Armour")){
										if(armour != null){
											temp = new Item(armour.getName(), armour.getDesciption());
										}
										armour = inventArray[i][j];
										inventArray[i][j] = new Item("Empty", "PlaceHolder");
										if(temp != null){
											addItem(temp);
										}
									}
								}
							}
						}
					}
				}
			}
			else{
				for(int i = 0; i < inventArray.length; i++){
					for(int j = 0; j < inventArray[0].length; j++){
						if(inventArray[i][j]!= null && inventArray[i][j].getName() != "Empty"){
							if(inventArray[i][j].contains(e.getX(), e.getY())){
								if(lootInvent.addItem(inventArray[i][j])){		//Null pointer
									inventArray[i][j] = new Item("Empty", "PlaceHolder");
								}
								else{
									System.out.println("Loot inventory is full can't swap item");
								}	
							}
						}
					}
				}
			}
			populateInvent();

		}		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		for(int i = 0; i < inventArray.length; i++){
			for(int j = 0; j < inventArray[0].length; j++){
				if(inventArray[i][j] != null){
					if(inventArray[i][j].contains(e.getX(), e.getY())){
						movedItem = inventArray[i][j];
					}
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(movedItem != null){
			for(int i = 0; i < inventArray.length; i++){
				for(int j = 0; j < inventArray[0].length; j++){
					if(inventArray[i][j]!= null){
						if(inventArray[i][j].contains(e.getX(), e.getY())){
							addItemTo(i,j,(int)getIndices(movedItem).getX(),(int)getIndices(movedItem).getY());
						}
					}
				}
			}
		}
	}

	private Point getIndices(Item item){
		for(int i = 0; i < inventArray.length; i++){
			for(int j = 0; j < inventArray[0].length; j++){
				if(inventArray[i][j] != null){
					if(inventArray[i][j].equals(item)){
						Point indexs = new Point(i, j);
						return indexs;
					}
				}
			}
		}
		return null;		
	}
}
