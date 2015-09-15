package ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

import ui.panels.*;
import ui.panels.Item;


public class ApplicationWindow extends JFrame implements ActionListener, KeyListener{

	private JLayeredPane layeredPanel = new JLayeredPane();
	private InventoryPanel inventPanel;
	private RenderingWindow rw;
	private boolean inventOpen = false;
	
	public ApplicationWindow() {
		//Setup
		super("Adventure Game");
		setLayout(null);
		setResizable(false);
		addKeyListener(this);
		setFocusable(true);
		
		//Setup frame
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(450, 50, 1050, 950);

		//Setup layered pane
		layeredPanel.setBounds(0, 0, 1050, 950);
		getContentPane().add(layeredPanel);

		//TODO Change Image size to fit once confirmed
		//Setup Background Panel
		BackgroundPanel bgPanel = new BackgroundPanel();
		layeredPanel.add(bgPanel);

		//Setup rendering window
		rw = new RenderingWindow();
		layeredPanel.add(rw, 1,1);

		//Setup Inventory
		inventPanel = new InventoryPanel();
		layeredPanel.add(inventPanel,2,0);
		setInventory();
		
		//this.add(new InventoryBackground(),3,0);
				
		//Setup the menu bar
		setupMenu();
		
		//Setup buttons
		setupButtons();
	}

	private void setInventory(){
		if(inventOpen == false){
			inventPanel.setVisible(false);
			inventPanel.setFocusable(false);
		}
		else{
			inventPanel.setVisible(true);
			inventPanel.setFocusable(true);
		}
	}
	
	private void setupMenu(){
		ArrayList<JMenuItem> option1List = new ArrayList<>();
		ArrayList<JMenuItem> option2List = new ArrayList<>();

		//Horizontal menu
		JMenuBar menuBar = new JMenuBar();

		JMenu option1 = new JMenu("File");
		option1List.add(new JMenuItem("New Game"));

		for(JMenuItem jmItem : option1List){
			option1.add(jmItem);
			jmItem.addActionListener(this);
		}

		JMenu option2 = new JMenu("Edit");
		option2List.add(new JMenuItem("Shank all players"));
		
		for(JMenuItem jmItem : option2List){
			option2.add(jmItem);
			jmItem.addActionListener(this);
		}
		
		menuBar.add(option1);
		menuBar.add(option2);

		this.setJMenuBar(menuBar);		
	}
	
	private void setupButtons(){
		JButton addShankButton = new JButton("Add Shank");
		addShankButton.setBounds(500,800,100,30);
		addShankButton.addActionListener(this);
		addShankButton.setFocusable(false);
		this.add(addShankButton,0);
		
		JButton addPotionButton = new JButton("Add Potion");
		addPotionButton.setBounds(650,800,100,30);
		addPotionButton.addActionListener(this);
		addPotionButton.setFocusable(false);
		this.add(addPotionButton,0);
		
		//Temp label
		JLabel temp = new JLabel("Press I to open Invent");
		temp.setBounds(300,800,150,20);
		this.add(temp, 0);
	}

	private static void displaySplash(){
		JWindow window = new JWindow();
		window.setLayout(null);
		
		JLabel loadingTextImage = new JLabel(new ImageIcon("src/ui/images/splashTextImage.gif"));
		loadingTextImage.setBounds(0,25, 405, 200);
		window.getContentPane().add(loadingTextImage);
		
		JLabel loadingLabel = new JLabel(new ImageIcon("src/ui/images/loadingbar.gif"));
		loadingLabel.setBounds(50,325,305,15);
		loadingLabel.setOpaque(false);
		window.getContentPane().add(loadingLabel);
				
		
		JLabel backgroundLabel = new JLabel(new ImageIcon("src/ui/images/splashBackgroundImageTemp.jpg"));
		backgroundLabel.setBounds(0,0,400,400);
		window.getContentPane().add(backgroundLabel);
		
		window.setBounds(750, 300, 400, 400);
		window.setVisible(true);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		window.setVisible(false);
		window.dispose();
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		//displaySplash();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ApplicationWindow frame = new ApplicationWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "New Game":
			System.out.println("New Game optin clicked");
			break;
		case "Temp":
			System.out.println("Temp option clicked");
			break;
		case "Add Shank":
			System.out.println("Shank Added");
			inventPanel.addItem(new Item("Shank", "Tis a shank mate"));
			break;
		case "Add Potion":
			System.out.println("Potion Added");
			inventPanel.addItem(new Item("Potion", "Tis a mate mate"));
			break;
		default:
			break;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_I:
			if(inventOpen == true){
				System.out.println("Close Invent");
				inventOpen = false;
				setInventory();
			}
			else{
				System.out.println("Open Invent");
				inventOpen = true;
				setInventory();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//TODO
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//TODO
	}
}
