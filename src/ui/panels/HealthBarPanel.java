package ui.panels;

import gameworld.Player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import ui.ApplicationWindow;

/**
 * Health bar panel
 * @author ItsNotAGoodTime
 *
 */
public class HealthBarPanel extends JPanel{

	//Serialize
	private static final long serialVersionUID = 1L;

	//Background image
	private Image backgroundImage;
	
	//Health
	private int health = 100;
	
	/**
	 * Gets image from file and sets panels layout
	 */
	public HealthBarPanel(){
		setOpaque(false);
		setLayout(null);
		setBounds(0,0,200,64);
		
		try{
			backgroundImage = ImageIO.read(ApplicationWindow.class.getResource("images/gui/healthBar.png"));
		}catch(IOException e){
			System.out.println(e.getLocalizedMessage());
		}	
	}
	
	/**
	 * Gets and sets health from player
	 * @param player - Player to check health from
	 */
	public void setHealth(Player player){
		this.health = player.getHealth();
		if(this.health < 0){
			this.health = 0;	
		}
		this.repaint();
	}
	
	/**
	 * Paints background image and a rectangle which its size is determined by health
	 */
	public void paint(Graphics g){
		g.setColor(new Color(0,0,0));
		g.fillRect(55, 10, 140, 20);
		g.setColor(new Color(255,0,0));
		g.fillRect(55, 10, (int)(health * 1.4), 20);
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, null);	
		g.setFont(new Font("TimesRoman", Font.BOLD, 20));
		g.drawString(health + "/100", 65, 50);
	}
}
