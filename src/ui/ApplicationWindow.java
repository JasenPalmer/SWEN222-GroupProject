package ui;

import gameworld.Game.Direction;
import gameworld.Player;
import gameworld.entity.Armour;
import gameworld.entity.Container;
import gameworld.entity.Key;
import gameworld.entity.Potion;
import gameworld.entity.Weapon;

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
import ui.panels.ItemIcon;
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
	private String username;
	private String host;
	private Clip musicClip;
	private Clip effectClip;
	private boolean showSettings = false;
	private HealthBarPanel hpBar;
	private String track = null;
	private int initVolume = -30;
	private int initEffectVolume = -30;
	private Timer timer;
	private KeyEvent keyEve;
	private String direction = "north";
	private JLabel compass;
	private boolean wasOpen = false;

	//Sound paths
	private String buttonSound = "src/ui/sounds/buttonSound.wav";
	private String deathSound = "src/ui/sounds/deathSound.wav";
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
		inventPanel = new InventoryPanel(client);
		overlayPanel.add(inventPanel,2,0);

		//Setup loot inventory
		lootInventPanel = new LootInventoryPanel(inventPanel);
		overlayPanel.add(lootInventPanel,2,0);
		setInventory();
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
		timer = new Timer(100, new ActionListener(){
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
				try{
					Thread.sleep(50);
				}catch(Exception e){
					e.printStackTrace();
				}
				if(rw != null && client.getState() != null){
					rw.repaint();
					hpBar.setHealth(client.getState());
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
			lootInventPanel.setInventVis(false);
		}
		else{
			inventPanel.setVisible(true);
			inventPanel.setFocusable(true);
			lootInventPanel.setInventVis(true);
		}
		playSound("Death");
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
		option2List.add(new JMenuItem("Add Spear"));
		option2List.add(new JMenuItem("Add Plate Armour"));
		option2List.add(new JMenuItem("Add Chain Armour"));
		option2List.add(new JMenuItem("Add Leather Armour"));
		option2List.add(new JMenuItem("Add Robe Armour"));
		option2List.add(new JMenuItem("Add Potion"));
		option2List.add(new JMenuItem("Add Shank loot"));
		option2List.add(new JMenuItem("Add Potion loot"));

		for(JMenuItem jmItem : option2List){
			option2.add(jmItem);
			jmItem.addActionListener(this);
		}

		menuBar.add(option1);
		menuBar.add(option2);

		this.setJMenuBar(menuBar);
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
			client.addItem(new Weapon("Shank", "Fuckn shank u mate", null, null, Weapon.WeaponType.Shank));
			break;
		case "Add Spear":
			client.addItem(new Weapon("Spear", "I'll fukn spear you bro", null, null, Weapon.WeaponType.Spear));
			break;
		case "Add Potion":
			client.addItem(new Potion("Potion", "Drink this shit", null, null));
			break;
		case "Test":
			inventPanel.addItemTo(0,0,1,0);
			break;
		case "Add Plate Armour":
			client.addItem(new Armour("Plate Armour", "some plate amour", null, null,Armour.ArmourType.Plate));
			break;
		case "Add Chain Armour":
			client.addItem(new Armour("Chain Armour", "some leather amour", null, null,Armour.ArmourType.Chain));
			break;
		case "Add Leather Armour":
			client.addItem(new Armour("Leather Armour", "some leather amour", null, null,Armour.ArmourType.Leather));
			break;
		case "Add Robe Armour":
			client.addItem(new Armour("Robe Armour", "some leather amour", null, null, Armour.ArmourType.Robe));
			break;
		case "Add Potion loot":
			client.addItem(new Key("Key", "WTF", null, null));
			break;
		case "Add Shank loot":
			lootInventPanel.addItem(new ItemIcon("Shank", "Tis a shank mate"));
			break;
		default:
			break;
		}

		inventPanel.populateInventArray();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_TAB:
			if(inventOpen == true){
				inventOpen = false;
				setInventory();
			}
			else{
				inventOpen = true;
				setInventory();
			}
			break;
		case KeyEvent.VK_I:
			if(lootInventOpen == true){
				lootInventOpen = false;
				setLootInventory();
			}
			else{
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
			if(lootInventOpen) break;
			keyEve = e;
			timer.start();
			break;
		case KeyEvent.VK_A:
			if(lootInventOpen) break;
			keyEve = e;
			timer.start();
			break;
		case KeyEvent.VK_S:
			if(lootInventOpen) break;
			keyEve = e;
			timer.start();
			break;
		case KeyEvent.VK_D:
			if(lootInventOpen) break;
			keyEve = e;
			timer.start();
			break;
		case KeyEvent.VK_ESCAPE:
			if(lootInventOpen){
				lootInventPanel.setVisible(false);
				lootInventPanel.setFocusable(false);
				lootInventOpen = false;
				inventPanel.setLootVis(false);
				if(!wasOpen){
					inventOpen = false;
					setInventory();
				}
			}
			else{
				if(showSettings){
					showSettings = false;
				}
				else{
					showSettings = true;
				}
				setSettings();
			}
			break;
		case KeyEvent.VK_ENTER:
			chatBoxPanel.getTextField().requestFocus();
			break;
		case KeyEvent.VK_SPACE:
			client.registerKeyPress(e);
			break;
		case KeyEvent.VK_F:
			client.registerKeyPress(e);
			break;
		default:
			break;
		}
	}

	public void openContainer(Container container){
		lootInventPanel.setVisible(true);
		lootInventPanel.setLootContainer(container);
		lootInventOpen = true;
		setLootInventory();
		
		if(inventOpen){
			wasOpen = true;
		}else{
			inventOpen = true;
			setInventory();
		}
	}

	public void cycleAnimations() { 
		this.client.cycleAnimations(); 
	}

	public void changeVolume(int change){
		//-60 to 6
		if(musicClip == null) return;
		FloatControl volume = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
		volume.setValue(change);
		initVolume = change;
	}

	public void changeEffectVolume(int change){
		//-60 to 6
		if(effectClip == null) return;
		FloatControl volume = (FloatControl) effectClip.getControl(FloatControl.Type.MASTER_GAIN);
		//System.out.println(volume);
		volume.setValue(change);
		initEffectVolume = change;
	}

	private void playSound(String sound){
		String soundPath = null;
		switch(sound){
		case "Button":
			soundPath = buttonSound;
			break;
		case "Death":
			soundPath = deathSound;
			break;
		default:
			break;
		}
		try{
			File file = new File(soundPath);
			effectClip = AudioSystem.getClip();
			effectClip.open(AudioSystem.getAudioInputStream(file));
			changeEffectVolume(initEffectVolume);
			effectClip.start();
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
	}

	//Getters
	public ChatBoxPanel getChatBox(){return this.chatBoxPanel;}
	public Player getPlayer(){return this.client.getState();}
	public RenderingWindow getRenderingWindow(){ return this.rw;}
	public InventoryPanel getInventPanel(){ return this.inventPanel;}
	public LootInventoryPanel getLootInvent(){ return this.lootInventPanel;}


	public void closeAppWindow(){
		System.exit(0);	
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		client.close();
		System.exit(0);
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}
