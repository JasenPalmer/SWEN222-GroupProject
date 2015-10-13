package ui.panels;

import gameworld.entity.Armour;
import gameworld.entity.Container;
import gameworld.entity.Gold;
import gameworld.entity.Item;
import gameworld.entity.Weapon;

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

public class LootInventoryPanel extends JLayeredPane implements MouseListener{

	private ItemIcon[][] itemList = new ItemIcon[6][3];
	private LootInventoryBackground lootInventBackground = new LootInventoryBackground();
	private InventoryPanel inventPanel;
	private LootInventoryPanel self = this;
	private ItemIcon movedItem;
	private int movedItemI;
	private int movedItemJ;
	private boolean inventOpen;
	private Client client;
	private Container container;
	private Clip effectClip;
	private int initEffectVolume = -30;

	//Sound paths
	private String buttonSound = "src/ui/sounds/buttonSound.wav";

	public LootInventoryPanel(InventoryPanel invent){
		//Setup
		this.inventPanel = invent;
		setLayout(null);
		setBounds(345, 100, 360, 240);

		//Add background
		this.add(lootInventBackground,0,0);

		populateSlots();
		addMouseListener(this);
		this.client = inventPanel.getPlayer();
	}

	public boolean addItem(ItemIcon item){
		for(int i = 0; i < itemList[0].length; i++){
			for(int j = 0; j < itemList.length; j++){
				if(itemList[j][i] == null){
					itemList[j][i] = item;
					System.out.println(itemList[j][i].getName() + " Added");
					populateSlots();
					return true;
				}
				if(i == itemList[0].length-1 && j == itemList.length-1){
					if(itemList[j][i] != null){
						System.out.println("Loot Inventory is full");
					}
					return false;
				}
			}
		}
		return false;
	}

	public void setLootContainer(Container container){
		this.container = container;
		
		Item[] containerList = this.container.getItems();
		System.out.println(containerList.length);
		for(int i = 0; i < containerList.length; i++){
			if(containerList[i]==null)continue;
			System.out.println(containerList[i].getName());
		}
		
		for(int i = 0; i < 6; i++){
			if(containerList[i] != null){
				itemList[i][0] = new ItemIcon(containerList[i].getName(), containerList[i].getDescription());
			}
			else{
				itemList[i][0] = null;
			}
		}
		
		for(int i = 0; i < 6; i++){
			if(containerList[i+6] != null){
				itemList[i][1] = new ItemIcon(containerList[i+6].getName(), containerList[i+6].getDescription());
			}
			else{
				itemList[i][1] = null;
			}
		}
		
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

	public void setInventVis(boolean change){
		this.inventOpen = change;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

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
	
	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println(e.getX() + " " + e.getY());
		if(movedItem != null){
			if(inventOpen){
				if(e.getX() > 473 && e.getX() < 691 && e.getY() > 573 && e.getY() < 709){
					if(!inventPanel.isInventFull()){
						client.addItem(makeItem(movedItem.getName(), movedItem.getDescription()));
						client.removeItemContainer(convertIndex(movedItemI, movedItemJ), this.container);
						playSound("Button");
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
			item = new Armour("Key", desc, null, null, Armour.ArmourType.Robe);
			break;
		case "Gold":
			String[] splitDesc = desc.split(" ");
			item = new Gold("Gold", desc, null, null, Integer.parseInt(splitDesc[1]));
			break;
		}

		return item;
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

}
