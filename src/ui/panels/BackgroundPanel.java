package ui.panels;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;



public class BackgroundPanel extends JPanel {	
	private Image backgroundImage; 
	public BackgroundPanel(){
		setLayout(null);
		try{
			backgroundImage = ImageIO.read(new File("src/ui/images/adventureBackgroundImageSize.jpg"));
		}catch(IOException e){
			System.out.println(e.getLocalizedMessage());
		}
		setBorder(new EmptyBorder(0, 5, 5, 5));
		setBounds(0, 0, 1050, 1020);	
	}
	
	public void paintComponent(Graphics g) { 
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, null);
	}
}