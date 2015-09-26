package ui.panels;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class LootInventoryBackground extends JPanel{
	
	private Image backgroundImage; 
	
	public LootInventoryBackground(){
		setLayout(null);
		setOpaque(false);
		try{
			backgroundImage = ImageIO.read(new File("src/ui/images/LootInventoryImage.png"));
		}catch(IOException e){
			System.out.println(e.getLocalizedMessage());
		}
		setBounds(0, 0, 360, 240);	
	}
	
	public void paintComponent(Graphics g) { 
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, null);
	}
}
