package ui.panels;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ui.ApplicationWindow;

/**
 * Inventory panel background
 * @author ItsNotAGoodTime
 *
 */
public class InventoryBackground extends JPanel{
	
	//Serialize
	private static final long serialVersionUID = 1L;
	//Background image
	private Image backgroundImage; 
	
	/**
	 * Gets image from file and sets panels layout
	 */
	public InventoryBackground(){
		setLayout(null);
		setOpaque(false);
		try{
			backgroundImage = ImageIO.read(ApplicationWindow.class.getResource("images/gui/inventImageMaster.png"));
		}catch(IOException e){
			System.out.println(e.getLocalizedMessage());
		}
		setBorder(new EmptyBorder(0, 5, 5, 5));
		setBounds(0, 0, 231, 262);	
	}
	
	/**
	 * Paints panel based on background image
	 */
	public void paintComponent(Graphics g) { 
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, null);
	}
}
