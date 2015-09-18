package ui.panels;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;


public class InventoryPanel extends JLayeredPane implements MouseListener{

	Image backgroundImage; 
	Item[][] inventArray = new Item[4][2];
	InventoryBackground inventBackground = new InventoryBackground();
	private int pressedX, pressedY;
	private Item movedItem;


	public InventoryPanel(){
		setLayout(null);
		setBounds(765, 517, 231, 262);

		//Add invent background
		this.add(inventBackground,0,0);

		//Add invent items
		inventArray[0][0] = null;
		populateInvent();
		addMouseListener(this);
	}

	public void addItem(Item item){
		for(int i = 0; i < inventArray[0].length; i++){
			for(int j = 0; j < inventArray.length; j++){
				if(inventArray[j][i] == null){
					inventArray[j][i] = item;
					System.out.println(inventArray[j][i].getName() + " Added");
					populateInvent();
					return;
				}
				if(i == inventArray[0].length-1 && j == inventArray.length-1){
					if(inventArray[j][i] != null){
						System.out.println("Inventory is full");
					}
				}
			}
		}
	}

	public void addItemTo(int x1, int y1, int x2, int y2){
		if(inventArray[x2][y2] == null){
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
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
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
		this.pressedX = e.getX();
		this.pressedY = e.getY();
		System.out.println(pressedX);
		System.out.println(pressedY);
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
		// TODO Auto-generated method stub

	}
}
