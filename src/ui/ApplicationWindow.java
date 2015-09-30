package ui;

import gameworld.Game;
import gameworld.Game.Direction;
import gameworld.Player;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import network.Client;
import ui.panels.BackgroundPanel;
import ui.panels.ChatBoxPanel;
import ui.panels.InventoryPanel;
import ui.panels.Item;
import ui.panels.LootInventoryPanel;
import ui.panels.SettingsMenu;


public class ApplicationWindow extends JFrame implements ActionListener, KeyListener, WindowListener{

	private JLayeredPane layeredPanel = new JLayeredPane();
	private InventoryPanel inventPanel;
	private JLayeredPane overlayPanel;
	private LootInventoryPanel lootInventPanel;
	private ChatBoxPanel chatBoxPanel;
	private SettingsMenu settings;
	private RenderingWindow rw;
	private boolean inventOpen = false;
	private boolean lootInventOpen = false;
	private Client client;
	private Game game;
	private String username;
	private String host;
	private Clip musicClip;
	private boolean showSettings = false;
	private String track = null;
	private int initVolume = -30;

	//Sound paths
	private String buttonSound = "src/ui/sounds/buttonSound.wav";
	private String track1 = "src/ui/sounds/track1.wav";
	private String track2 = "src/ui/sounds/track2.wav";
	private String fighting = "src/ui/sounds/fighting.wav";

	public ApplicationWindow(String host, String username) {
		//Setup
		super("Shank the world");
		this.host = host;
		this.username = username;
		client = new Client(this.host, this.username, this);
		setLayout(null);
		setResizable(false);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		addWindowListener(this);
		playMusic("Track 1");
		track = "Track 1";

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
		rw = new RenderingWindow(this);
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
		chatBoxPanel = new ChatBoxPanel(client, this);
		layeredPanel.add(chatBoxPanel,1,0);

		//Setup the menu bar
		setupMenu();

		//Setup settings menu
		settings = new SettingsMenu(this);
		layeredPanel.add(settings,3,0);
		setSettings();

		//Setup buttons
		setupButtons();
	}

	private void setSettings(){
		if(showSettings == true){
			settings.setVisible(true);
			settings.setFocusable(true);
		}
		else{
			settings.setVisible(false);
			settings.setFocusable(false);
		}
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

	public void repaintRenderingWindow(){
		if(rw != null) rw.repaint();
	}

	private void playSound(String sound){
		String soundPath = null;
		switch(sound){
		case "Button":
			soundPath = buttonSound;
			break;
		default:
			break;
		}
		try{
			File file = new File(soundPath);
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(file));
			clip.start();
		}catch(Exception e){
			System.out.println(e.getLocalizedMessage());
		}
	}

	private void playMusic(String music){
		switch(music){
		case "Track 1":
			track = track1;
			break;
		case "Track 2":
			track = track2;
			break;
		case "Fighting":
			track = fighting;
			break;
		}
		try{
			File file = new File(track);
			musicClip = AudioSystem.getClip();
			musicClip.open(AudioSystem.getAudioInputStream(file));
			changeVolume(initVolume);
			musicClip.start();
			musicClip.loop(Clip.LOOP_CONTINUOUSLY);
		}catch(Exception e){
			System.out.println(e.getLocalizedMessage());
		}
	}

	private void stopMusic(){
		musicClip.stop();
	}

	public void toggleMusic(){
		stopMusic();
		if(track.equals("Track 1")){
			playMusic("Track 2");
			track = "Track 2";
		}
		else if(track.equals("Track 2")){
			playMusic("Fighting");
			track = "Fighting";
		}
		else{
			playMusic("Track 1");
			track = "Track 1";
		}
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
		case KeyEvent.VK_W:
			client.registerKeyPress(e);
			break;
		case KeyEvent.VK_A:
			client.registerKeyPress(e);
			break;
		case KeyEvent.VK_S:
			client.registerKeyPress(e);
			break;
		case KeyEvent.VK_D:
			client.registerKeyPress(e);
			break;
		case KeyEvent.VK_ESCAPE:
			if(showSettings == true){
				showSettings = false;
			}
			else{
				showSettings = true;
			}
			setSettings();
			break;
		default:
			break;
		}
	}

	public void changeVolume(int change){
		//-60 to 6
		FloatControl volume = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
		volume.setValue(change);
		initVolume = change;
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

	//Getters
	public ChatBoxPanel getChatBox(){return this.chatBoxPanel;}

	public Player getPlayer(){
		return this.game.parsePlayer(username);
	}

	//Setters
	public void setGame(Game game){
		this.game = game;
		System.out.println("Gamestate has been updated");
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		client.close();
		System.exit(0);

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}
}
