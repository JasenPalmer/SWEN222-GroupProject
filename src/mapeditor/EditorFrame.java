package mapeditor;

import gameworld.Game.Direction;
import gameworld.entity.BasicEntity;
import gameworld.entity.Chest;
import gameworld.entity.Entity;
import gameworld.location.InsideLocation;
import gameworld.location.Location;
import gameworld.location.OutsideLocation;
import gameworld.tile.BuildingTile;
import gameworld.tile.EntranceTile;
import gameworld.tile.FloorTile;
import gameworld.tile.Tile;
import gameworld.tile.EntranceTile.Type;

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

import ui.ImageStorage;


public class EditorFrame extends JFrame implements MouseListener, KeyListener{


	private Location map;
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

	/**
	 * Create the frame.
	 */
	public EditorFrame(Location map) {
		this.map = map;
		new ImageStorage();
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

		options = new OptionsMenu(this, map);
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
		String mapToSave = map.toString();
		String entitiesToSave = map.entitiesToString();
		int selected = save.showSaveDialog(null);

		if(selected == JFileChooser.APPROVE_OPTION) {
			try(FileWriter fw = new FileWriter(save.getSelectedFile()+".txt")) {
			    fw.write(mapToSave);
			}
			catch  (IOException e){
			}
			
			try(FileWriter fw = new FileWriter(save.getSelectedFile()+"-entites.txt")) {
			    fw.write(entitiesToSave);
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
		int x = Math.min(xClick1, xClick2);
		int y = Math.min(yClick1, yClick2);

		if(currentOption!=null && x >= 0 && x < map.getTiles().length && y >= 0 && y < map.getTiles().length){
				while(x<=Math.max(xClick1, xClick2)){
					while(y<=Math.max(yClick1, yClick2)){

						if(isBuilding(currentOption)){
							// making BuildingTile
							if(map instanceof OutsideLocation){
								OutsideLocation oMap = (OutsideLocation) map;
								if(isEntrance(currentOption)){
									oMap.setBuildingTile(x,y,getEntrance(currentOption, new Point(x,y)));
								} else {
									oMap.setBuildingTile(x,y,new BuildingTile(currentOption, new Point(x,y), false));
								}
							} else {
								map.setTile(x, y, getEntrance(currentOption, new Point(x,y)));
							}
						}
						else if(isEntity(currentOption)){
							map.getTiles()[y][x].setEntitiy(getEntity(currentOption, new Point(x,y)));
						}
						else if(isTerrain(currentOption)){
							// making FloorTile
							map.setTile(x, y, new FloorTile(currentOption, new Point(x,y), true));
						}
						else{
							map.setTile(x, y, null);
							if(map instanceof OutsideLocation){
								OutsideLocation oMap = (OutsideLocation) map;
								oMap.setBuildingTile(x, y, null);
							}
						}
						y++;
					}
					y = Math.min(yClick1, yClick2);
					x++;
				}
		}


		repaint();
	}


	private EntranceTile getEntrance(String name, Point p){
		EntranceTile toReturn = null;
		switch(name){
		case "Entrance - Building":
			return new EntranceTile(name, p, false, EntranceTile.Type.BUILDING);		
		case "Entrance - Invisible":
			return new EntranceTile(name, p, false, EntranceTile.Type.INVISIBLE);
		}
		return toReturn;
	}
	
	private Entity getEntity(String name, Point p) {
		switch(name){
		case "Tree":
			return new BasicEntity("Tree", "This is a tree. Just a tree.", p, map);
		case "Bush":
			return new BasicEntity("Bush", "Bush", p, map);
		case "Table":
			return new BasicEntity("Table", "This is a table.", p, map);
		case "Chest":
			return new Chest("Chest", "This is a chest", p, map);
		case "Chair":
			return new BasicEntity("Chair", "This is for sitting!", p, map);
		}
		return null;
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
		xClick1 = (e.getX()-(options.getWidth()+10))/TILESIZE + canvas.getCameraX()/TILESIZE;
		yClick1 = (e.getY()-54)/TILESIZE + canvas.getCameraY()/TILESIZE;
	}



	@Override
	public void mouseReleased(MouseEvent e) {
		xClick2 = (e.getX()-(options.getWidth()+10))/TILESIZE + canvas.getCameraX()/TILESIZE;
		yClick2 = (e.getY()-54)/TILESIZE + canvas.getCameraY()/TILESIZE;
		mouseClicked(e);

	}

	public void optionSelected(String s) {
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

	public void viewSelected(String string) {
		canvas.setView(string);
		requestFocus();
		repaint();
	}


	@Override
	public void keyPressed(KeyEvent e) {
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

	public boolean isBuilding(String name){
		switch(name){
			case "Building":
				return true;
			case "Entrance - Building":
				return true;	
			case "Entrance - Invisible":
				return true;
			case "Entrance - Tree":
				return true;
		}
		return false;
	}
	

	private boolean isEntrance(String name) {
		switch(name){
		case "Entrance - Building":
			return true;	
		case "Entrance - Invisible":
			return true;
	}
	return false;
	}

	public boolean isTerrain(String name){
		switch(name){
			case "Grass":
				return true;
			case "Rock":
				return true;
			case "Water":
				return true;
			case "Floor":
				return true;
		}
		return false;
	}

	public boolean isEntity(String name){
		switch(name){
			case "Tree":
				return true;
			case "Bush":
				return true;
			case "Boulder":
				return true;
			case "Table":
				return true;
			case "Chest":
				return true;
			case "Chair":
				return true;
		}
		return false;
	}


	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args){
		try {
			String outOrIn = "";

			while(!outOrIn.equals("outside") && !outOrIn.equalsIgnoreCase("inside")){
				outOrIn = (String)JOptionPane.showInputDialog(null, "Outside or Inside Location? ", "Location Select", JOptionPane.QUESTION_MESSAGE, null, null, null);
			}

			Location l = null;
			if(outOrIn.equalsIgnoreCase("inside")){
				l = new InsideLocation("Test Name", "test description", new Tile[MAPHEIGHT][MAPWIDTH]);
			} else{
				l = new OutsideLocation("Test Name", "test description", new Tile[MAPHEIGHT][MAPWIDTH], new Tile[MAPHEIGHT][MAPWIDTH]);
			}

			EditorFrame frame = new EditorFrame(l);
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
