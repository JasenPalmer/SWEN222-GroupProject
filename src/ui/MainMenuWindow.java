package ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JTextField;

import network.Server;

/**
 * Main menu containing button options and a sound track
 * @author ItsNotAGoodTime
 *
 */
public class MainMenuWindow extends JFrame implements ActionListener{

	//Sound paths
	private static final String buttonSound = "sounds/buttonSound.wav";
	private static final String music = "sounds/mainMenuMusic.wav";

	//Button image paths
	private static String join = "images/gui/joinButtonImage.png";
	private static String  host = "images/gui/hostButtonImage.png";
	private static String exit = "images/gui/exitButtonImage.png";
	private static String mute = "images/gui/mute.png";
	private static String unmute = "images/gui/unmute.png";

	//Image paths
	private static String backgroundImage = "images/gui/backgroundImage.gif";
	private static String titleImage = "images/gui/title.png";

	private Clip musicClip;
	private boolean muted = false;
	JButton muteButton;

	/**
	 * Sets dimensions to main menu and starts the sound track
	 */
	public MainMenuWindow(){
		super("");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		playMusic();
		this.setResizable(false);
		setLayout(null);
		setBounds(450, 10, 1050, 950);
		addBackground();
	}

	/**
	 * Adds the background label and aligns its contents
	 */
	private void addBackground(){
		//Add background
		JLabel background = new JLabel(new ImageIcon(getClass().getResource(backgroundImage)));
		background.setBounds(0,0,1050,950);
		this.add(background,0,0);

		//Add Title
		JLabel title = new JLabel(new ImageIcon(getClass().getResource(titleImage)));
		title.setBounds(100,100,860,100);
		this.add(title,1,0);
		title.setOpaque(false);

		addButtons();
	}

	/**
	 * Adds all required main menu buttons
	 */
	private void addButtons(){
		//Join
		JButton joinButton = new JButton(new ImageIcon(getClass().getResource(join)));
		joinButton.addActionListener(this);
		joinButton.setBounds(375,350,300,79);
		joinButton.setContentAreaFilled(false);
		joinButton.setActionCommand("Join");
		joinButton.setBorderPainted(false);
		joinButton.setFocusPainted(false);
		this.add(joinButton, 1,0);

		//Host
		JButton hostButton = new JButton(new ImageIcon(getClass().getResource(host)));
		hostButton.addActionListener(this);
		hostButton.setBounds(375,450,300,79);
		hostButton.setContentAreaFilled(false);
		hostButton.setActionCommand("Host");
		hostButton.setBorderPainted(false);
		hostButton.setFocusPainted(false);
		this.add(hostButton, 1,0);

		//Exit
		JButton exitButton = new JButton(new ImageIcon(getClass().getResource(exit)));
		exitButton.addActionListener(this);
		exitButton.setBounds(375,550,300,79);
		exitButton.setContentAreaFilled(false);
		exitButton.setActionCommand("Exit");
		exitButton.setBorderPainted(false);
		exitButton.setFocusPainted(false);
		this.add(exitButton, 1,0);

		//Mute
		muteButton = new JButton(new ImageIcon(getClass().getResource(unmute)));
		muteButton.addActionListener(this);
		muteButton.setBounds(850,840,300,79);
		muteButton.setContentAreaFilled(false);
		muteButton.setActionCommand("Mute");
		muteButton.setBorderPainted(false);
		muteButton.setFocusPainted(false);
		this.add(muteButton, 1,0);
	}

	/**
	 * Changes the image of the mute/unmute button depending on its previous state
	 */
	private void setSoundButton(){
		if(muted == true){
			muteButton.setIcon(new ImageIcon(getClass().getResource(mute)));
		}
		else{
			muteButton.setIcon(new ImageIcon(getClass().getResource(unmute)));
		}
	}

	/**
	 * Dictates what action to perform depending on what button was clicked
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "Join":
			playSound("Button");
			JLabel serverLabel = new JLabel("Server IP:");
			JTextField serverText = new JTextField();
			JLabel usernameLabel = new JLabel("Username:");
			JTextField usernameText = new JTextField();
			Component[] menuOptions = {serverLabel, serverText, usernameLabel, usernameText};
			JOptionPane.showMessageDialog(this,  menuOptions, "Join server", JOptionPane.QUESTION_MESSAGE);
			String defaultServer = "localhost";
			//Sets username and joins local host
			if(!usernameText.getText().equals("")  && serverText.getText().equals("")){
				stopMusic();
				this.dispose();
				ApplicationWindow game = new ApplicationWindow(defaultServer,usernameText.getText());
				game.setVisible(true);
			}
			//Sets username and join remote host
			else if(!usernameText.getText().equals("") && !serverText.getText().equals("")){
				stopMusic();
				this.dispose();
				ApplicationWindow game = new ApplicationWindow(serverText.getText(),usernameText.getText());
				game.setVisible(true);
			}
			break;
		case "Host":
			playSound("Button");
			new Thread(new Runnable(){public void run(){new Server();}}).start();
			break;
		case "Exit":
			playSound("Button");
			stopMusic();
			this.dispose();
			break;
		case "Mute":
			if(muted == true){
				playMusic();
				muted = false;
				setSoundButton();
			}
			else{
				stopMusic();
				muted = true;
				setSoundButton();
			}
		default:
			break;
		}
	}

	/**
	 * Plays a sound based on the string provided
	 * @param sound - String associated with a sound
	 */
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
			InputStream file = new BufferedInputStream(getClass().getResourceAsStream(soundPath));
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(file));
			clip.start();
		}catch(Exception e){
			System.out.println(e.getLocalizedMessage());
		}
	}

	/**
	 * Starts the main menus music sound track
	 */
	private void playMusic(){
		try{
			InputStream file = new BufferedInputStream(getClass().getResourceAsStream(music));
			musicClip = AudioSystem.getClip();
			musicClip.open(AudioSystem.getAudioInputStream(file));
			FloatControl volume = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(-15);
			musicClip.start();
			musicClip.loop(Clip.LOOP_CONTINUOUSLY);
		}catch(Exception e){
			System.out.println(e.getLocalizedMessage());
		}
	}

	/**
	 * Stops the main menus music sounds track
	 */
	private void stopMusic(){
		musicClip.stop();
	}

	/**
	 * Creates and starts a main menu screen
	 * @param args - Possible terminal start option
	 */
	public static void main(String args[]){
		MainMenuWindow window = new MainMenuWindow();
		window.setVisible(true);
	}
}
