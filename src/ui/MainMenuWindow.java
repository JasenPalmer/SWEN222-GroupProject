package ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import network.Server;

public class MainMenuWindow extends JFrame implements ActionListener{

	//Sound paths
	private static final String buttonSound = "src/ui/sounds/buttonSound.wav";
	private static final String music = "src/ui/sounds/mainMenuMusic.wav";
	
	//Button image paths
	private static String join = "src/ui/images/gui/joinButtonImage.png";
	private static String  host = "src/ui/images/gui/hostButtonImage.png";
	private static String exit = "src/ui/images/gui/exitButtonImage.png";
	private static String mute = "src/ui/images/gui/mute.png";
	private static String unmute = "src/ui/images/gui/unmute.png";

	//Image paths
	private static String backgroundImage = "src/ui/images/gui/backgroundImage.gif";
	private static String titleImage = "src/ui/images/gui/title.png";
	
	private Clip musicClip;
	private boolean muted = false;
	JButton muteButton;

	public MainMenuWindow(){
		super("");
		playMusic();
		this.setResizable(false);
		setLayout(null);
		setBounds(450, 10, 1050, 1020);
		addBackground();
	}

	private void addBackground(){
		//Add background
		JLabel background = new JLabel(new ImageIcon(backgroundImage));
		background.setBounds(0,0,1050,1020);
		this.add(background,0,0);

		//Add Title
		JLabel title = new JLabel(new ImageIcon(titleImage));
		title.setBounds(100,100,860,100);
		this.add(title,1,0);
		title.setOpaque(false);

		addButtons();
	}

	private void addButtons(){
		//Join
		JButton joinButton = new JButton(new ImageIcon(join));
		joinButton.addActionListener(this);
		joinButton.setBounds(375,350,300,79);
		joinButton.setContentAreaFilled(false);
		joinButton.setActionCommand("Join");
		joinButton.setBorderPainted(false);
		joinButton.setFocusPainted(false);
		this.add(joinButton, 1,0);

		//Host
		JButton hostButton = new JButton(new ImageIcon(host));
		hostButton.addActionListener(this);
		hostButton.setBounds(375,450,300,79);
		hostButton.setContentAreaFilled(false);
		hostButton.setActionCommand("Host");
		hostButton.setBorderPainted(false);
		hostButton.setFocusPainted(false);
		this.add(hostButton, 1,0);

		//Exit
		JButton exitButton = new JButton(new ImageIcon(exit));
		exitButton.addActionListener(this);
		exitButton.setBounds(375,550,300,79);
		exitButton.setContentAreaFilled(false);
		exitButton.setActionCommand("Exit");
		exitButton.setBorderPainted(false);
		exitButton.setFocusPainted(false);
		this.add(exitButton, 1,0);

		//Mute
		muteButton = new JButton(new ImageIcon(unmute));
		muteButton.addActionListener(this);
		muteButton.setBounds(850,900,300,79);
		muteButton.setContentAreaFilled(false);
		muteButton.setActionCommand("Mute");
		muteButton.setBorderPainted(false);
		muteButton.setFocusPainted(false);
		this.add(muteButton, 1,0);
	}

	private void setSoundButton(){
		if(muted == true){
			muteButton.setIcon(new ImageIcon(mute));
		}
		else{
			muteButton.setIcon(new ImageIcon(unmute));
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "Exit":
			playSound("Button");
			stopMusic();
			this.dispose();
			break;
		case "Host":
			playSound("Button");
			break;
		case "Join":
			playSound("Button");
			JLabel serverLabel = new JLabel("Server IP:");
			JTextField serverText = new JTextField();
			JLabel usernameLabel = new JLabel("Username:");
			JTextField usernameText = new JTextField();
			Component[] menuOptions = {serverLabel, serverText, usernameLabel, usernameText};
			JOptionPane.showMessageDialog(this,  menuOptions, "Join server", JOptionPane.QUESTION_MESSAGE);		

			if(!usernameText.getText().equals("") && !serverText.getText().equals("")){
				this.dispose();
				ApplicationWindow game = new ApplicationWindow(usernameText.getText());
				game.setVisible(true);
			}

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

	private void playMusic(){
		try{
			File file = new File(music);
			musicClip = AudioSystem.getClip();
			musicClip.open(AudioSystem.getAudioInputStream(file));
			musicClip.start();
		}catch(Exception e){
			System.out.println(e.getLocalizedMessage());
		}
	}

	private void stopMusic(){
		musicClip.stop();
	}

	public static void main(String args[]){
		//		SplashWindow splash = new SplashWindow();
		//		splash.setVisible(true);
		//		splash.dispose();
		MainMenuWindow window = new MainMenuWindow();
		window.setVisible(true);
	}
}
