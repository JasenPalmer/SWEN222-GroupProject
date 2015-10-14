package ui.panels;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import ui.ApplicationWindow;

/**
 * Background to chatbox
 * @author ItsNotAGoodTime
 *
 */
public class ChatBoxBackground extends JPanel{
	
	//Background image
	private Image backgroundImage; 
	
	/**
	 * Sets dimensions and loads image for chatbox background
	 */
	public ChatBoxBackground(){
		setLayout(null);
		setOpaque(false);
		try{
			backgroundImage = ImageIO.read(ApplicationWindow.class.getResource("images/gui/chatBoxBackground.png"));
		}catch(IOException e){
			System.out.println(e.getLocalizedMessage());
		}
		setBounds(0, 0, 470, 250);	
	}
	
	/**
	 * Paints the chatbox panel using the background image
	 */
	public void paintComponent(Graphics g) { 
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, null);
	}
}
