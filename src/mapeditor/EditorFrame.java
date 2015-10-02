package mapeditor;

import gameworld.Game.Direction;
import gameworld.location.OutsideLocation;
import gameworld.tile.BuildingTile;
import gameworld.tile.FloorTile;
import gameworld.tile.Tile;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


public class EditorFrame extends JFrame implements MouseListener, KeyListener{


	private OutsideLocation map;
	private EditorCanvas canvas;
	private OptionsMenu options;
	private String currentOption;
	private JMenuBar bar;

	private static int MAPHEIGHT = 30;
	private static int MAPWIDTH = 30;
	private static int TILESIZE = 64;

	int xClick1;
	int yClick1;
	int xClick2;
	int yClick2;

	private Image image;


	/**
	 * Create the frame.
	 */
	public EditorFrame(OutsideLocation map) {
		this.map = map;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1280, 720);
		JPanel outerPanel = new JPanel();
		setContentPane(outerPanel);
		outerPanel.setLayout(new BorderLayout(0, 0));

		bar = new JMenuBar();
		JMenu file = new JMenu("File");
		bar.add(file);

		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new ActionListener()
	    {
		      public void actionPerformed(ActionEvent e)
		      {
		    	  save();
		      }

		    });

		JMenuItem load = new JMenuItem("Load");
		load.addActionListener(new ActionListener()
	    {
		      public void actionPerformed(ActionEvent e)
		      {
		    	  load();
		      }


		    });

		file.add(save);
		file.add(load);
		add(bar, BorderLayout.NORTH);



		canvas = new EditorCanvas(map);
		canvas.setBorder(new EmptyBorder(5, 5, 5, 5));
		outerPanel.add(canvas, BorderLayout.CENTER);

		options = new OptionsMenu(this);
		options.setBorder(new EmptyBorder(5, 5, 5, 5));
		outerPanel.add(options, BorderLayout.WEST);

		addMouseListener(this);
		addKeyListener(this);
		setFocusable(true);
		requestFocus();
	}


	private void save() {
		String name = null;
		String description = null;

		name = JOptionPane.showInputDialog("Map Name: ");

		description = JOptionPane.showInputDialog("Map Description: ");

		// name or description not entered so stop saving. this is required.
		if(name.equals("") || description.equals("")){
			return;
		}

		map.setName(name);
		map.setDescription(description);

		JFileChooser save = new JFileChooser();
		save.setCurrentDirectory(new File("."));
		String textToSave = map.toString();
		int selected = save.showSaveDialog(null);

		if(selected == JFileChooser.APPROVE_OPTION) {
			try(FileWriter fw = new FileWriter(save.getSelectedFile()+".txt")) {
			    fw.write(textToSave);
			}
			catch  (IOException e){
			}
		}
	}

	private void load() {
		// doesnt load yet
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		if(currentOption!=null){
			if(xClick1!=xClick2 || yClick1!=yClick2){
				// MOUSE WAS DRAGGED
				int x = Math.min(xClick1, xClick2);
				int y = Math.min(yClick1, yClick2);
				while(x<=Math.max(xClick1, xClick2)){
					while(y<=Math.max(yClick1, yClick2)){

						if(currentOption.equals("Building") || currentOption.equals("Entrance")){
							// making FloorTiles
							map.setBuildingTile(x,y,new BuildingTile(currentOption, new Point(x,y), false));
						} else{
							// making BuildingTiles
							System.out.println("adding floortile");
							map.setTile(x, y, new FloorTile(currentOption, new Point(x,y), true));
						}
						y++;
					}
					y = Math.min(yClick1, yClick2);
					x++;
				}



			} else {
				// MOUSE CLICKED. NOT DRAGGED
				if(currentOption.equals("Building")  || currentOption.equals("Entrance")){
					// Making BuildingTile
					map.setBuildingTile(xClick1,yClick1,new BuildingTile(currentOption, new Point(xClick1,yClick1), false));
				} else{
					// Making FloorTile
					System.out.println("adding floortile");
					map.setTile(xClick1, yClick1, new FloorTile(currentOption, new Point(xClick1,yClick1), true));
				}
			}
		}
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("X: "+e.getX()+",  "+"Y: "+e.getY());
		xClick1 = (e.getX()-97)/TILESIZE + canvas.getCameraX()/TILESIZE;
		yClick1 = (e.getY()-54)/TILESIZE + canvas.getCameraY()/TILESIZE;
	}



	@Override
	public void mouseReleased(MouseEvent e) {
		xClick2 = (e.getX()-options.getWidth()-13)/TILESIZE + canvas.getCameraX()/TILESIZE;
		yClick2 = (e.getY()-bar.getHeight()-35)/TILESIZE + canvas.getCameraY()/TILESIZE;
		mouseClicked(e);

	}

	public void optionSelected(String s) {
		System.out.println(s);
		currentOption = s;
		requestFocus();
	}


	public void directionSelected(String string) {
		switch(string){
			case "North":
				canvas.setDirection(Direction.NORTH);
				repaint();
				break;
			case "East":
				canvas.setDirection(Direction.EAST);
				repaint();
				break;
			case "South":
				canvas.setDirection(Direction.SOUTH);
				repaint();
				break;
			case "West":
				canvas.setDirection(Direction.WEST);
				repaint();
				break;
		}

	}

	public static void main(String[] args){
		try {
			EditorFrame frame = new EditorFrame(new OutsideLocation("Test Name", "test description", new Tile[MAPHEIGHT][MAPHEIGHT], new Tile[MAPHEIGHT][MAPHEIGHT]));
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void viewSelected(String string) {
		canvas.setView(string);
		requestFocus();
		repaint();
	}


	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("hello?");
		switch(e.getKeyCode()){
			case KeyEvent.VK_W:
				canvas.setCameraY(canvas.getCameraY()-20);
				repaint();
				break;
			case KeyEvent.VK_A:
				canvas.setCameraX(canvas.getCameraX()-20);
				repaint();
				break;
			case KeyEvent.VK_S:
				canvas.setCameraY(canvas.getCameraY()+20);
				repaint();
				break;
			case KeyEvent.VK_D:
				canvas.setCameraX(canvas.getCameraX()+20);
				repaint();
				break;
		}
		
	}


	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
