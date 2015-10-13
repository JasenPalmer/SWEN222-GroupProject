package ui.panels;


import gameworld.Player;
import gameworld.entity.Armour;
import gameworld.entity.Item;
import gameworld.entity.Weapon;

import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

import network.Client;


public class InventoryPanel extends JLayeredPane implements MouseListener{

	Image backgroundImage;
	ItemIcon[][] inventArray = new ItemIcon[4][2];
	InventoryBackground inventBackground = new InventoryBackground();
	private ItemIcon movedItem;
	private int movedItemIndex;
	private boolean lootOpen;
	private LootInventoryPanel lootInvent;
	private InventoryPanel self = this;
	private Client client;
	private int initEffectVolume = -30;
	private Clip effectClip;

	//Sound paths
	private String buttonSound = "src/ui/sounds/buttonSound.wav";

	public InventoryPanel(Client client){
		setLayout(null);
		setBounds(814, 637, 231, 262);
		this.client = client;

		//Add invent background
		this.add(inventBackground,0,0);

		//Add invent items
		populateInventArray();
		fillEquipmentSlots();
		addMouseListener(this);
	}

	public boolean addItem(ItemIcon item){
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

	public void populateInventArray(){
		Item[] itemList = client.getState().getInventory();

		for(int i = 0; i < 4; i++){
			if(itemList[i] != null){
				inventArray[i][0] = new ItemIcon(itemList[i].getName(), itemList[i].getDescription());
				//System.out.println(itemList[i].getName());
			}
			else{
				inventArray[i][0] = null;
			}
		}

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

		for(int j = 0; j < inventArray[0].length; j++){
			for(int i = 0; i < inventArray.length; i++){
				if(inventArray[i][j] != null){
					inventArray[i][j].setX(11+(i*55));
					inventArray[i][j].setY(44+(j*70));
				}
			}
		}

		for(int j = 0; j < inventArray[0].length; j++){
			for(int i = 0; i < inventArray.length; i++){
				if(inventArray[i][j] != null){
					JLabel item = new JLabel(inventArray[i][j].getImage());
					item.setBounds(inventArray[i][j].getX(), inventArray[i][j].getY(), 42,52);
					this.add(item,1,0);
					if(!inventArray[i][j].getName().equals("Empty")){
						item.setToolTipText(inventArray[i][j].getDesciption());
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

	public void setLootVis(boolean change){
		this.lootOpen = change;
	}

	public void setLootInventPanel(LootInventoryPanel lootInvent){
		this.lootInvent = lootInvent;
	}

	private void fillEquipmentSlots(){
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

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}


	private int convertIndex(int i, int j){
		if(j == 0){
			return i;
		}
		else{
			return i+4;
		}
	}

	public boolean isInventFull(){
		Item[] itemList = client.getState().getInventory();

		for(int i = 0; i < itemList.length; i++){
			if(itemList[i] == null){
				return false;
			}
		}

		return true;
	}

	private Item makeItem(String name){
		Item weapon = null;

		switch(name){
		case "Shank":
			weapon = new Weapon("Shank", "Tis a shank mate", null, null, Weapon.WeaponType.Shank);
			break;
		case "Spear":
			weapon = new Weapon("Spear", "Tis a spear mate", null, null, Weapon.WeaponType.Spear);
			break;
		case "Chain Armour":
			weapon = new Armour("Chain Armour", "Tis sexy chain armour mate", null, null, Armour.ArmourType.Chain);
			break;
		case "Leather Armour":
			weapon = new Armour("Leather Armour", "Tis pretty shitty leather armour mate", null, null, Armour.ArmourType.Leather);
			break;
		case "Plate Armour":
			weapon = new Armour("Plate Armour", "Tis super sexy plate armour m9", null, null, Armour.ArmourType.Plate);
			break;
		case "Robe Armour":
			weapon = new Armour("Robe Armour", "Mate why even pick this shit up?", null, null, Armour.ArmourType.Robe);
			break;
		}

		return weapon;
	}

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
		else if(e.getButton() == MouseEvent.BUTTON3){
			Item temp = null;
			//If right click on weapon slot
			if(e.getX() >= 65 && e.getX() <= 107 && e.getY() >= 195 && e.getY() <= 247){
				if(client.getState().getWeapon() != null && !isInventFull()){
					client.addItem(client.getState().getWeapon());
					client.setWeapon(null);
					playSound("Button");
				}
				else{
					System.out.println("Inventory full can't dequip weapon");
				}
			}
			//If right click on armour slot
			else if(e.getX() >= 120 && e.getX() <= 162 && e.getY() >= 195 && e.getY() <= 247){
				if(client.getState().getArmour() != null && !isInventFull()){
					client.addItem(client.getState().getArmour());
					client.setArmour(null);
					playSound("Button");
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
									if(client.getState().getWeapon() != null){
										temp = client.getState().getWeapon();
									}
									Weapon newWeapon = (Weapon) makeItem(inventArray[i][j].getName());
									client.setWeapon(newWeapon);
									client.removeItem(convertIndex(i,j));

									if(temp != null){
										client.addItem(temp);
									}
									playSound("Button");
								}
								else if(inventArray[i][j].getType().equals("Armour")){
									if(client.getState().getArmour() != null){
										temp = client.getState().getArmour();
									}
									Armour newArmour = (Armour) makeItem(inventArray[i][j].getName());
									client.setArmour(newArmour);
									client.removeItem(convertIndex(i,j));

									if(temp != null){
										client.addItem(temp);
									}
									playSound("Button");
								}
							}
						}
					}
				}
			}
			populateInventArray();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1){
			if(movedItem != null){
				for(int i = 0; i < inventArray.length; i++){
					for(int j = 0; j < inventArray[0].length; j++){
						if(inventArray[i][j]!= null){
							if(inventArray[i][j].contains(e.getX(), e.getY()) && !inventArray[i][j].getName().equals(movedItem.getName())){
								client.swapItems(movedItemIndex, convertIndex(i,j));
								movedItem = null;
								playSound("Button");
							}
						}
					}
				}
				if(!lootOpen){
					if(e.getX() < -2 || e.getY() < -3){
						System.out.println("Dropped");
					}
				}
				else{
					if(e.getX() > -454 && e.getX() < -130 && e.getY() > -521 && e.getY() < -319){
						System.out.println("Inside");
						for(int i = 0; i < inventArray.length; i++){
							for(int j = 0; j < inventArray[0].length; j++){
								if(inventArray[i][j]!= null && inventArray[i][j].getName() != "Empty"){
									if(inventArray[i][j].contains(movedItem.getX(), movedItem.getY())){
										if(lootInvent.addItem(movedItem)){
											inventArray[i][j] = new ItemIcon("Empty", "Placeholder");
											playSound("Button");
										}
										else{
											System.out.println("Loot inventory is full can't swap item");
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

	public void changeEffectVolume(int change){
		//-60 to 6
		if(effectClip == null) return;
		FloatControl volume = (FloatControl) effectClip.getControl(FloatControl.Type.MASTER_GAIN);
		//System.out.println(volume);
		volume.setValue(change);
		initEffectVolume = change;
	}
	
	private void playSound(String sound){
		String soundPath = null;
		switch(sound){
		case "Button":
			soundPath = buttonSound;
			break;
		default:
			break;
		}
		try{
			File file = new File(soundPath);
			effectClip = AudioSystem.getClip();
			effectClip.open(AudioSystem.getAudioInputStream(file));
			changeEffectVolume(initEffectVolume);
			effectClip.start();
		}catch(Exception e){
			System.out.println(e.getLocalizedMessage());
		}
	}

	//Getters and Setters
	public Client getPlayer(){return this.client;}
}
