package ui.panels;

import gameworld.entity.Container;
import gameworld.entity.Item;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class LootInventoryPanel extends JLayeredPane implements MouseListener{

	private ItemIcon[][] itemList = new ItemIcon[6][3];
	private LootInventoryBackground lootInventBackground = new LootInventoryBackground();
	private InventoryPanel inventPanel;
	private LootInventoryPanel self = this;
	private ItemIcon movedItem;
	private int movedItemI;
	private int movedItemJ;
	private boolean inventOpen;

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
		Item[] containerList = container.getItems();
		
		for(int i = 0; i < 6; i++){
			itemList[i][0] = new ItemIcon(containerList[i].getName(), containerList[i].getDescription());
		}
		
		for(int i = 0; i < 6; i++){
			itemList[i][1] = new ItemIcon(containerList[i+6].getName(), containerList[i+6].getDescription());
		}
		
		for(int i = 0; i < 6; i++){
			itemList[i][2] = new ItemIcon(containerList[i+12].getName(), containerList[i+12].getDescription());
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
					if(!itemList[i][j].getDesciption().equals("Placeholder")){
						item.setToolTipText(itemList[i][j].getDesciption());
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

	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println(e.getX() + " " + e.getY());
		if(movedItem != null){
			if(inventOpen){
				if(e.getX() > 473 && e.getX() < 691 && e.getY() > 573 && e.getY() < 709){
					if(inventPanel.addItem(movedItem)){
						itemList[movedItemI][movedItemJ] = null;
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
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(file));
			clip.start();
		}catch(Exception e){
			System.out.println(e.getLocalizedMessage());
		}
	}

}
