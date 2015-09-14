package ui.panels;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class InventoryPanel extends JPanel{

	Image backgroundImage; 

	public InventoryPanel(){
		setLayout(null);
		setBounds(400,200,210,330);
		setOpaque(false);
		
		try{
			backgroundImage = ImageIO.read(new File("src/ui/images/inventImage.png"));
		}catch(IOException e){
			System.out.println(e.getLocalizedMessage());
		}
		
	}

	public void paint( Graphics g ) { 
		super.paint(g);
		g.drawImage(backgroundImage, 0, 0, null);
	}
}
