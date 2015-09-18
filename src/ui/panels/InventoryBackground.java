package ui.panels;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class InventoryBackground extends JPanel{
	
	private Image backgroundImage; 
	
	public InventoryBackground(){
		setLayout(null);
		setOpaque(false);
		try{
			backgroundImage = ImageIO.read(new File("src/ui/images/inventImageMaster.png"));
		}catch(IOException e){
			System.out.println(e.getLocalizedMessage());
		}
		setBorder(new EmptyBorder(0, 5, 5, 5));
		setBounds(0, 0, 231, 262);	
	}
	
	public void paintComponent(Graphics g) { 
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, null);
	}
}
