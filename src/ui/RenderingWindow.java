package ui;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class RenderingWindow extends JPanel{
	
	private Image backgroundImage;
	
	public RenderingWindow(){
		setLayout(null);
		setBounds(50,30,950,750);
		
		try{
		backgroundImage = ImageIO.read(new File("src/ui/images/renderingWindowTemp.jpg"));
		}catch(IOException e){
			System.out.println(e.getLocalizedMessage());
		}
	}
	
	public void paint( Graphics g ) { 
		super.paint(g);
		g.drawImage(backgroundImage, 0, 0, null);
	}
}
