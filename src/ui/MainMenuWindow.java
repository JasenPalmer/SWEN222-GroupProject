package ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JWindow;

import network.Server;

public class MainMenuWindow extends JWindow implements ActionListener{
	
	public MainMenuWindow(){
		setLayout(null);
		setBounds(450, 10, 1050, 1020);
		addBackground();
	}

	private void addBackground(){
		//Add background
		JLabel background = new JLabel(new ImageIcon("src/ui/images/gui/backgroundImage.gif"));
		background.setBounds(0,0,1050,1020);
		this.add(background,0,0);

		//Add Title
		JLabel title = new JLabel(new ImageIcon("src/ui/images/gui/title.png"));
		title.setBounds(100,100,860,100);
		this.add(title,1,0);
		title.setOpaque(false);

		addButtons();
	}

	private void addButtons(){
		//Join
		JButton joinButton = new JButton(new ImageIcon("src/ui/images/gui/joinButtonImage.png"));
		joinButton.addActionListener(this);
		joinButton.setBounds(375,350,300,79);
		joinButton.setContentAreaFilled(false);
		joinButton.setActionCommand("Join");
		joinButton.setBorderPainted(false);
		this.add(joinButton, 1,0);

		//Host
		JButton hostButton = new JButton(new ImageIcon("src/ui/images/gui/hostButtonImage.png"));
		hostButton.addActionListener(this);
		hostButton.setBounds(375,450,300,79);
		hostButton.setContentAreaFilled(false);
		hostButton.setActionCommand("Host");
		hostButton.setBorderPainted(false);
		this.add(hostButton, 1,0);

		//Exit
		JButton exitButton = new JButton(new ImageIcon("src/ui/images/gui/exitButtonImage.png"));
		exitButton.addActionListener(this);
		exitButton.setBounds(375,550,300,79);
		exitButton.setContentAreaFilled(false);
		exitButton.setActionCommand("Exit");
		exitButton.setBorderPainted(false);
		this.add(exitButton, 1,0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "Exit":
			this.dispose();
			break;
		case "Host":	
			break;
		case "Join":
//			JComponent[] menuOptions = {new JTextField(), new  JTextField(), new JTextField()};
//			JOptionPane.showMessageDialog(this,  menuOptions, "IP and Username plz", JOptionPane.QUESTION_MESSAGE);			
			this.dispose();
			ApplicationWindow game = new ApplicationWindow("Player 1");
			game.setVisible(true);
			break;
		default:
			break;
		}
	}
	
	public static void main(String args[]){
//		SplashWindow splash = new SplashWindow();
//		splash.setVisible(true);
//		splash.dispose();
		MainMenuWindow window = new MainMenuWindow();
		window.setVisible(true);
	}
}
