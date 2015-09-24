package mapeditor;

import gameworld.Game.Direction;
import gameworld.location.Location;
import gameworld.location.OutsideLocation;
import gameworld.tile.BuildingTile;
import gameworld.tile.FloorTile;
import gameworld.tile.Tile;

import java.awt.BorderLayout;
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

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ui.ApplicationWindow;
import main.Main;


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
	
	/**
	 * Create the frame.
	 */
	public EditorFrame(OutsideLocation map) {
		this.map = map;	
		
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
		JFileChooser load = new JFileChooser();
		
		load.setCurrentDirectory(new File("."));
		load.setDialogTitle("Select Map");

		// run the file chooser and check the user didn't hit cancel
		if (load.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			File f = load.getSelectedFile();
			try {			
				String line = "";	
				BufferedReader br = new BufferedReader(new FileReader(f));
				
				Tile[][]tiles = new Tile[MAPHEIGHT][MAPWIDTH]; 
				BuildingTile[][]rooms = new BuildingTile[MAPHEIGHT][MAPWIDTH]; 
				int i = 0;
				
				try {
					while ((line = br.readLine()) != null) {
						String[] split = line.split(" ");
						for(int j = 0; j < split.length; j++){
							if(!split[j].equals("0")){
								URL url =  Main.class.getResource("images/"+split[j]+".png");
								ImageIcon img = new ImageIcon(url);			
								tiles[i][j] = new FloorTile(new Point(j,i), img.getImage());
								if(split[j].equals("Room")){
									rooms[i][j] = new BuildingTile(new Point(j,i), img.getImage());
								}
							} else{
								tiles[i][j] = null;
							}
						}
						i++;
					}
					
					
				} catch (IOException e) {
					e.printStackTrace();
				}
					
				map.setTiles(tiles);
				map.setBuildingTiles(rooms);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		repaint();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		if(currentOption!=null){
			URL url =  Main.class.getResource("images/"+currentOption+".png");
			ImageIcon img = new ImageIcon(url);				
			
			
			
			if(xClick1!=xClick2 || yClick1!=yClick2){
				int x = Math.min(xClick1, xClick2);
				int y = Math.min(yClick1, yClick2);
				while(x<=Math.max(xClick1, xClick2)){
					while(y<=Math.max(yClick1, yClick2)){

						if(currentOption.equals("Room")){
							System.out.println("setting room");
							map.setBuildingTile(x,y,new BuildingTile(new Point(x,y), img.getImage()));
						} else {
							map.setTile(x, y, new FloorTile(new Point(x,y), img.getImage()));
							map.setBuildingTile(x, y, null);
							System.out.println(currentOption);
						}
						y++;
					}
					y = Math.min(yClick1, yClick2);
					x++;
				}



			} else {
				if(currentOption.equals("Room")){
					System.out.println("setting room");
					map.setBuildingTile(xClick1,yClick1,new BuildingTile(new Point(xClick1,yClick1), img.getImage()));
				}
				map.setTile(xClick1, yClick1, new FloorTile(new Point(xClick1,yClick1),img.getImage()));
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
			EditorFrame frame = new EditorFrame(new OutsideLocation("test", "test", new Tile[MAPHEIGHT][MAPHEIGHT], new Tile[MAPHEIGHT][MAPHEIGHT]));
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
