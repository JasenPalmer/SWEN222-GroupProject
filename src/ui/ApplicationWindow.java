package ui;

import gameworld.Game.Direction;

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
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

import ui.panels.*;


public class ApplicationWindow extends JFrame implements ActionListener, KeyListener{

	private JLayeredPane layeredPanel = new JLayeredPane();
	private InventoryPanel inventPanel;
	private JLayeredPane overlayPanel;
	private LootInventoryPanel lootInventPanel;
	private ChatBoxPanel chatBoxPanel;
	private RenderingWindow rw;
	private boolean inventOpen = false;
	private boolean lootInventOpen = false;

	public ApplicationWindow() {
		//Setup
		super("Adventure Game");
		setLayout(null);
		setResizable(false);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);

		//Setup frame
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(450, 10, 1050, 1020);

		//Setup layered pane
		layeredPanel.setBounds(0, 0, 1050, 1020);
		getContentPane().add(layeredPanel);

		//Setup Background Panel
		BackgroundPanel bgPanel = new BackgroundPanel();
		layeredPanel.add(bgPanel);

		//Setup rendering window
		rw = new RenderingWindow();
		layeredPanel.add(rw, 1,1);

		//Setup Overlay Panel
		overlayPanel = new JLayeredPane();
		overlayPanel.setBounds(0,0,1050,1020);
		layeredPanel.add(overlayPanel,2,0);

		//Setup Inventory
		inventPanel = new InventoryPanel();
		overlayPanel.add(inventPanel,2,0);
		setInventory();

		//Setup loot inventory
		lootInventPanel = new LootInventoryPanel(inventPanel);
		overlayPanel.add(lootInventPanel,2,0);
		setLootInventory();
		inventPanel.setLootInventPanel(lootInventPanel);
		
		//Setup chat box
		chatBoxPanel = new ChatBoxPanel();
		layeredPanel.add(chatBoxPanel,1,0);

		//Setup the menu bar
		setupMenu();

		//Setup buttons
		setupButtons();		
	}

	/**
	 * Changes visibility of the inventory panel
	 */
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

	/**
	 * Changes visibility of loot inventory panel
	 */
	private void setLootInventory(){
		if(lootInventOpen == false){
			lootInventPanel.setVisible(false);
			lootInventPanel.setFocusable(false);
			inventPanel.setLootVis(false);
		}
		else{
			lootInventPanel.setVisible(true);
			lootInventPanel.setFocusable(true);
			inventPanel.setLootVis(true);
		}
	}

	/**
	 * Creates and populates the UI Menubar
	 */
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

	/**
	 * Creates and positions the UI's buttons
	 */
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

		//Test Button
		JButton testButton = new JButton("Test");
		testButton.setBounds(800,800,100,30);
		testButton.addActionListener(this);
		testButton.setFocusable(false);
		this.add(testButton,0);
		
		//add shank loot
		JButton addShankLoot = new JButton("Add Shank loot");
		addShankLoot.setBounds(500,850,100,30);
		addShankLoot.addActionListener(this);
		addShankLoot.setFocusable(false);
		this.add(addShankLoot,0);
		
		//Add potion loot
		JButton addPotionLoot = new JButton("Add Potion loot");
		addPotionLoot.setBounds(650,850,100,30);
		addPotionLoot.addActionListener(this);
		addPotionLoot.setFocusable(false);
		this.add(addPotionLoot,0);
		
		//More buttons
		JButton addKatana = new JButton("Add Katana");
		addKatana.setBounds(500,890,100,30);
		addKatana.addActionListener(this);
		addKatana.setFocusable(false);
		this.add(addKatana,0);
		
		JButton addHelmet1 = new JButton("Add Helmet1");
		addHelmet1.setBounds(650,890,100,30);
		addHelmet1.addActionListener(this);
		addHelmet1.setFocusable(false);
		this.add(addHelmet1,0);
		
		JButton addHelmet2 = new JButton("Add Helmet2");
		addHelmet2.setBounds(800,890,100,30);
		addHelmet2.addActionListener(this);
		addHelmet2.setFocusable(false);
		this.add(addHelmet2,0);
	}

	/**
	 * Creates splash loading screen then updates according to loading speed
	 */
	private static void displaySplash(){
		JWindow window = new JWindow();
		window.setLayout(null);

		JLabel loadingTextImage = new JLabel(new ImageIcon("src/ui/images/splash/splashTextImage.gif"));
		loadingTextImage.setBounds(0,25, 405, 200);
		window.getContentPane().add(loadingTextImage);

		JLabel loadingLabel = new JLabel(new ImageIcon("src/ui/images/splash/loadingbar.gif"));
		loadingLabel.setBounds(50,325,305,15);
		loadingLabel.setOpaque(false);
		window.getContentPane().add(loadingLabel);


		JLabel backgroundLabel = new JLabel(new ImageIcon("src/ui/images/splash/splashBackgroundImageTemp.jpg"));
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

	public void repaintRenderingWindow(){
		rw.repaint();
	}

	/**
	 * Launches the application.
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
			System.out.println("New Game option clicked");
			break;
		case "Shank all players":
			System.out.println("All players were ruthlessly shanked, y u do dis?");
			break;
		case "Add Shank":
			inventPanel.addItem(new Item("Shank", "Tis a shank mate"));
			break;
		case "Add Potion":
			inventPanel.addItem(new Item("Potion", "Tis a potion mate"));
			break;
		case "Test":
			inventPanel.addItemTo(0,0,1,0);
			break;
		case "Add Potion loot":
			lootInventPanel.addItem(new Item("Potion", "Tis a potion mate"));
			break;
		case "Add Shank loot":
			lootInventPanel.addItem(new Item("Shank", "Tis a shank mate"));
			break;
		case "Add Katana":
			lootInventPanel.addItem(new Item("Katana", "Tis a katana mate"));
			break;
		case "Add Helmet1":
			lootInventPanel.addItem(new Item("Helmet1", "Tis a helmet1 mate"));
			break;
		case "Add Helmet2":
			lootInventPanel.addItem(new Item("Helmet2", "Tis a helmet2 mate"));
			break;
		default:
			break;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_TAB:
			if(inventOpen == true){
				System.out.println("Close Inventory");
				inventOpen = false;
				setInventory();
			}
			else{
				System.out.println("Open Inventory");
				inventOpen = true;
				setInventory();
			}
			break;
		case KeyEvent.VK_I:
			if(lootInventOpen == true){
				System.out.println("Close Loot Inventory");
				lootInventOpen = false;
				setLootInventory();
			}
			else{
				System.out.println("Open Loot Inventory");
				lootInventOpen = true;
				setLootInventory();
			}
			break;
		case KeyEvent.VK_Q:
			rw.setDirection(directionSetter("Q"));
			rw.repaint();
			break;
		case KeyEvent.VK_E:
			rw.setDirection(directionSetter("E"));
			rw.repaint();
			break;
		default:
			break;
		}
	}

	private Direction directionSetter(String key){
		if(key.equals("Q")){
			switch(rw.direction){
			case NORTH:
				return Direction.EAST;
			case EAST:
				return Direction.SOUTH;
			case SOUTH:
				return Direction.WEST;
			case WEST:
				return Direction.NORTH;
			}
		}
		else{
			switch(rw.direction){
			case NORTH:
				return Direction.WEST;
			case EAST:
				return Direction.NORTH;
			case SOUTH:
				return Direction.EAST;
			case WEST:
				return Direction.SOUTH;
			}
		}
		return null;
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
