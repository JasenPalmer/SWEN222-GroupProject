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
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.Timer;

import network.Client;
import ui.panels.BackgroundPanel;
import ui.panels.ChatBoxPanel;
import ui.panels.HealthBarPanel;
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
	private HealthBarPanel hpBar;
	private String track = null;
	private int initVolume = -30;
	Timer timer;
	KeyEvent keyEve;
	private String direction = "north";
	JLabel compass;

	//Sound paths
	private String buttonSound = "src/ui/sounds/buttonSound.wav";
	private String track1 = "src/ui/sounds/track1.wav";
	private String track2 = "src/ui/sounds/track2.wav";
	private String fighting = "src/ui/sounds/fighting.wav";

	//Images
	private String northCompass = "src/ui/images/gui/compassNorth.png";
	private String eastCompass = "src/ui/images/gui/compassEast.png";
	private String southCompass = "src/ui/images/gui/compassSouth.png";
	private String westCompass = "src/ui/images/gui/compassWest.png";

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
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(450, 10, 1050, 950);

		//Setup layered pane
		layeredPanel.setBounds(0, 0, 1050, 950);
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

		//Setup HP Bar
		hpBar = new HealthBarPanel();
		overlayPanel.add(hpBar,2,0);

		//Setup compass		
		compass = new JLabel(new ImageIcon(northCompass));
		compass.setBounds(870, 10, 170,149);
		overlayPanel.add(compass,2,0);

		//Setup timer
		timer = new Timer(50, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(keyEve != null){
					client.registerKeyPress(keyEve);
				}
			}});
		timer.setRepeats(true);

		RepaintThread rt = new RepaintThread();
		rt.start();
	}

	public class RepaintThread extends Thread{
		public void run(){
			while(true){
				System.out.println("repaint");
				try{
					Thread.sleep(50);
				}catch(Exception e){
					e.printStackTrace();
				}
				if(game != null && username != null && hpBar != null){
					hpBar.setHealth(game.parsePlayer(username).getHealth());
				}
				if(rw != null && game != null){
					rw.repaint();
				}
			}
		}
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
		option2List.add(new JMenuItem("Add Shank"));
		option2List.add(new JMenuItem("Add Potion"));
		option2List.add(new JMenuItem("Add Shank loot"));
		option2List.add(new JMenuItem("Add Potion loot"));
		option2List.add(new JMenuItem("Add Katana"));
		option2List.add(new JMenuItem("Add Helmet1"));
		option2List.add(new JMenuItem("Add Helmet2"));

		for(JMenuItem jmItem : option2List){
			option2.add(jmItem);
			jmItem.addActionListener(this);
		}

		menuBar.add(option1);
		menuBar.add(option2);

		this.setJMenuBar(menuBar);
	}

	public void repaintRenderingWindow(){
		if(game != null && username != null && hpBar != null){
			hpBar.setHealth(game.parsePlayer(username).getHealth());
		}
		if(rw != null){
			rw.repaint();
		}
	}

	private void updateCompass(){
		switch(direction){
		case "north":
			compass.setIcon(new ImageIcon(northCompass));
			break;
		case "east":
			compass.setIcon(new ImageIcon(eastCompass));
			break;
		case "south":
			compass.setIcon(new ImageIcon(southCompass));
			break;
		case "west":
			compass.setIcon(new ImageIcon(westCompass));
			break;
			default:
				break;
		}
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
			hpBar.setHealth(-100);
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
			client.registerKeyPress(e);
			switch(direction){
			case "north":
				direction = "east";
				break;
			case "east":
				direction = "south";
				break;
			case "south":
				direction = "west";
				break;
			case "west":
				direction = "north";
				break;
			default:
				break;
			}
			updateCompass();
			rw.repaint();
			break;
		case KeyEvent.VK_E:
			rw.setDirection(directionSetter("E"));
			client.registerKeyPress(e);
			switch(direction){
			case "north":
				direction = "west";
				break;
			case "east":
				direction = "north";
				break;
			case "south":
				direction = "east";
				break;
			case "west":
				direction = "south";
				break;
			default:
				break;
			}
			updateCompass();
			rw.repaint();
			break;
		case KeyEvent.VK_W:
			keyEve = e;
			timer.start();
			break;
		case KeyEvent.VK_A:
			keyEve = e;
			timer.start();
			break;
		case KeyEvent.VK_S:
			keyEve = e;
			timer.start();
			break;
		case KeyEvent.VK_D:
			keyEve = e;
			timer.start();
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
		case KeyEvent.VK_ENTER:
			chatBoxPanel.getTextField().requestFocus();
			break;
		case KeyEvent.VK_SPACE:
			client.registerKeyPress(e);
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
		timer.stop();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//TODO
	}

	//Getters
	public ChatBoxPanel getChatBox(){return this.chatBoxPanel;}
	public Player getPlayer(){return this.game.parsePlayer(username);}
	public Game getGame(){ return this.game;}
	public RenderingWindow getRenderingWindow(){ return this.rw;}

	//Setters
	public void setGame(Game game){
		this.game = game;
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
