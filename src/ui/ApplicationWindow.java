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
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import ui.panels.*;


public class ApplicationWindow extends JFrame implements ActionListener, KeyListener{

	private JLayeredPane layeredPane = new JLayeredPane();
	private RenderingWindow rw;
	private boolean inventOpen = false;
	
	public ApplicationWindow() {
		//Setup
		super("Pretty Sick UI TBH");
		setLayout(null);
		setResizable(false);
		addKeyListener(this);
		setFocusable(true);
		
		//Setup frame
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(450, 50, 1050, 950);

		//Setup layered pane
		layeredPane.setBounds(0, 0, 1050, 950);
		getContentPane().add(layeredPane);

		//TODO Change Image size to fit once confirmed
		//Setup Background Panel
		BackgroundPanel bgPanel = new BackgroundPanel();
		layeredPane.add(bgPanel, 0,0);

		//Setup rendering window
		rw = new RenderingWindow();
		layeredPane.add(rw, 1,1);
		
		//Setup the menu bar
		setupMenu();
		
		//Setup buttons
		setupButtons();
	}

	private void setInventory(){
		if(inventOpen == false){
			
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
		option2List.add(new JMenuItem("Temp"));
		
		for(JMenuItem jmItem : option2List){
			option2.add(jmItem);
			jmItem.addActionListener(this);
		}
		
		menuBar.add(option1);
		menuBar.add(option2);

		this.setJMenuBar(menuBar);		
	}
	
	private void setupButtons(){
		JButton cameraLeftButton = new JButton("Left");
		cameraLeftButton.setBounds(500,800,70,30);
		cameraLeftButton.addActionListener(this);
		this.add(cameraLeftButton,0);
		
		JButton cameraRightButton = new JButton("Right");
		cameraRightButton.setBounds(600,800,70,30);
		cameraRightButton.addActionListener(this);
		this.add(cameraRightButton,0);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
		case "Left":
			System.out.println("Left button clicked");
			break;
		case "Right":
			System.out.println("Right button clicked");
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
			}
			else{
				System.out.println("Open Invent");
				inventOpen = true;
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
