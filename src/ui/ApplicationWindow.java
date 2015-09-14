package ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import ui.panels.*;


public class ApplicationWindow extends JFrame implements ActionListener{

	private JLayeredPane layeredPane = new JLayeredPane();

	public ApplicationWindow() {
		super("Pretty Sick UI TBH");

		//Setup frame
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1050, 950);

		//Setup layered pane
		layeredPane.setBounds(0, 0, 750, 750);
		getContentPane().add(layeredPane);

		//TODO Change Image size to fit once confirmed
		//Setup Background Panel
		BackgroundPanel bgPanel = new BackgroundPanel();
		layeredPane.add(bgPanel, 0,0);

		//Setup the menu bar
		setupMenu();
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
			System.out.println("New Game");
			break;
		case "Temp":
			System.out.println("Temp");
			break;
		default:
			break;
		}
	}
}
