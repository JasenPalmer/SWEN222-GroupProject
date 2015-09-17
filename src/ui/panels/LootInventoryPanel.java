package ui.panels;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class LootInventoryPanel extends JPanel{
	
	private Image backgroundImage; 
	
	public LootInventoryPanel(){
		setLayout(null);
		setOpaque(false);
		try{
			backgroundImage = ImageIO.read(new File("src/ui/images/LootInventoryImage.png"));
		}catch(IOException e){
			System.out.println(e.getLocalizedMessage());
		}
		setBorder(new EmptyBorder(0, 5, 5, 5));
		//Set 330 for center
		setBounds(330, 100, 360, 240);	
	}
	
	public void paintComponent(Graphics g) { 
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, null);
	}
}
