package ui.panels;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import ui.ApplicationWindow;

/**
 * Background image panel for containers
 * @author ItsNotAGoodTime
 *
 */
public class LootInventoryBackground extends JPanel{
	
	//Background image
	private Image backgroundImage; 
	
	public LootInventoryBackground(){
		setLayout(null);
		setOpaque(false);
		try{
			backgroundImage = ImageIO.read(ApplicationWindow.class.getResource("images/gui/LootInventoryImage.png"));
		}catch(IOException e){
			System.out.println(e.getLocalizedMessage());
		}
		setBounds(0, 0, 360, 240);	
	}
	
	//Paints this panel using the background image
	public void paintComponent(Graphics g) { 
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, null);
	}
}
