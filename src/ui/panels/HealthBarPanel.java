package ui.panels;

import gameworld.Player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class HealthBarPanel extends JPanel{

	private Image backgroundImage;
	private int health = 100;
	
	public HealthBarPanel(){
		setOpaque(false);
		setLayout(null);
		setBounds(0,0,200,64);
		
		try{
			backgroundImage = ImageIO.read(new File("src/ui/images/gui/healthBar.png"));
		}catch(IOException e){
			System.out.println(e.getLocalizedMessage());
		}	
	}
	
	public void setHealth(Player player){
		this.health = player.getHealth();
		this.repaint();
	}
	
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
