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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


public class EditorFrame extends JFrame implements MouseListener{


	private OutsideLocation map;
	private EditorCanvas canvas;
	private OptionsMenu options;
	private String currentOption;
	private JMenuBar bar;
	
	private static int MAPHEIGHT = 15;
	private static int MAPWIDTH = 15;
	private static int TILESIZE = 64;
	
	int xClick1;
	int yClick1;
	int xClick2;
	int yClick2;
	
	private Image image;

	private Image grass;
	private Image building;
	private Image water;
	private Image rock;
	private Image room;
	private Image door;

	
	/**
	 * Create the frame.
	 */
	public EditorFrame(OutsideLocation map) {
		this.map = map;	
		setImages();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
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
		
		if(image!=null){		
			if(xClick1!=xClick2 || yClick1!=yClick2){
				// MOUSE WAS DRAGGED
				int x = Math.min(xClick1, xClick2);
				int y = Math.min(yClick1, yClick2);
				while(x<=Math.max(xClick1, xClick2)){
					while(y<=Math.max(yClick1, yClick2)){
						
						if(currentOption.equals("Building") || currentOption.equals("Entrance")){
							// making FloorTiles
							map.setBuildingTile(x,y,new BuildingTile(currentOption, new Point(x,y)));
						} else{
							// making BuildingTiles
							map.setTile(x, y, new FloorTile(currentOption, new Point(x,y)));
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
					map.setBuildingTile(xClick1,yClick1,new BuildingTile(currentOption, new Point(xClick1,yClick1)));
				} else{
					// Making FloorTile
					map.setTile(xClick1, yClick1, new FloorTile(currentOption, new Point(xClick1,yClick1)));
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
		xClick1 = (e.getX()-options.getWidth()-13)/TILESIZE;
		yClick1 = (e.getY()-bar.getHeight()-35)/TILESIZE;
	}
	
	

	@Override
	public void mouseReleased(MouseEvent e) {
		xClick2 = (e.getX()-options.getWidth()-13)/TILESIZE;
		yClick2 = (e.getY()-bar.getHeight()-35)/TILESIZE;
		mouseClicked(e);
		
	}

	public void optionSelected(String s) {
		System.out.println(s);
		currentOption = s;	
		
		switch(currentOption){
		case "Grass":
			image = grass;
			break;
		case "Rock":
			image = rock;
			break;
		case "Water":
			image = water;
			break;
		case "Building":
			image = building;
			break;
		case "Entrance":
			image = door;
			break;
		}
		
	}
	
	/**
	 * Setting images to files in images folder. Will be changed when location parser is working for some tiles. (grass, water, rock will be gone).
	 */
	private void setImages() {
		try{
			grass = ImageIO.read(new File("src/ui/images/terrain/Grass.png"));
			building = ImageIO.read(new File("src/ui/images/buildings/Room.png"));
			water = ImageIO.read(new File("src/ui/images/terrain/Water.png"));
			rock = ImageIO.read(new File("src/ui/images/terrain/Rock.png"));
			room = ImageIO.read(new File("src/ui/images/buildings/Room.png"));
			door = ImageIO.read(new File("src/ui/images/buildings/DoorUD.png"));
			
		}catch(IOException e){
			System.out.println(e.getLocalizedMessage());
		}
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
		repaint();
	}

}
