package mapeditor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OptionsMenu extends JPanel {

	EditorFrame frame;
	
	/**
	 * Create the panel.
	 */
	public OptionsMenu(final EditorFrame frame) {
		this.frame = frame;
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		// Terrain Menu
		
		String[] terrainOptions = {"Grass", "Rock", "Water"};
		
		JLabel lblTerrain = new JLabel("Terrain");
		GridBagConstraints gbc_lblTerrain = new GridBagConstraints();
		gbc_lblTerrain.insets = new Insets(0, 0, 5, 0);
		gbc_lblTerrain.gridx = 0;
		gbc_lblTerrain.gridy = 0;
		add(lblTerrain, gbc_lblTerrain);
		final JComboBox terrain = new JComboBox(terrainOptions);
		GridBagConstraints gbc_terrain = new GridBagConstraints();
		gbc_terrain.insets = new Insets(0, 0, 5, 0);
		gbc_terrain.fill = GridBagConstraints.HORIZONTAL;
		gbc_terrain.gridx = 0;
		gbc_terrain.gridy = 1;
		
		terrain.addActionListener(new ActionListener()
	    {
		      public void actionPerformed(ActionEvent e)
		      {
		    	  frame.optionSelected(terrain.getSelectedItem().toString());
		      }
		    });
		
		add(terrain, gbc_terrain);
		
		// Building Menu
		
		String[] buildingOptions = {"Building", "Entrance"};
		
		JLabel lblBuildings = new JLabel("Buildings");
		GridBagConstraints gbc_lblBuildings = new GridBagConstraints();
		gbc_lblBuildings.insets = new Insets(0, 0, 5, 0);
		gbc_lblBuildings.gridx = 0;
		gbc_lblBuildings.gridy = 2;
		add(lblBuildings, gbc_lblBuildings);
		
		final JComboBox buildings = new JComboBox(buildingOptions);
		GridBagConstraints gbc_buildings = new GridBagConstraints();
		gbc_buildings.insets = new Insets(0, 0, 5, 0);
		gbc_buildings.fill = GridBagConstraints.HORIZONTAL;
		gbc_buildings.gridx = 0;
		gbc_buildings.gridy = 3;
		
		buildings.addActionListener(new ActionListener()
	    {
		      public void actionPerformed(ActionEvent e)
		      {
		    	  frame.optionSelected(buildings.getSelectedItem().toString());
		      }
		    });
		
		add(buildings, gbc_buildings);
		
		// Items Menu
		
		JLabel lblItems = new JLabel("Items");
		GridBagConstraints gbc_lblItems = new GridBagConstraints();
		gbc_lblItems.insets = new Insets(0, 0, 5, 0);
		gbc_lblItems.gridx = 0;
		gbc_lblItems.gridy = 4;
		add(lblItems, gbc_lblItems);
		
		String[] itemOptions = {"Drawers", "Barrel", "Chest"};
		
		final JComboBox items = new JComboBox(itemOptions);
		GridBagConstraints gbc_items = new GridBagConstraints();
		gbc_items.insets = new Insets(0, 0, 5, 0);
		gbc_items.fill = GridBagConstraints.HORIZONTAL;
		gbc_items.gridx = 0;
		gbc_items.gridy = 5;
		add(items, gbc_items);
		
		items.addActionListener(new ActionListener()
	    {
		      public void actionPerformed(ActionEvent e)
		      {
		    	  frame.optionSelected(items.getSelectedItem().toString());
		      }
		    });
		
		
		// Direction Menu
		
		JLabel lblDirection = new JLabel("Direction");
		GridBagConstraints gbc_lblDirection = new GridBagConstraints();
		gbc_lblDirection.insets = new Insets(0, 0, 5, 0);
		gbc_lblDirection.gridx = 0;
		gbc_lblDirection.gridy = 6;
		add(lblDirection, gbc_lblDirection);
		
		String[] directionOptions = {"North", "East", "South", "West"};
		
		final JComboBox direction = new JComboBox(directionOptions);
		GridBagConstraints gbc_direction = new GridBagConstraints();
		gbc_direction.fill = GridBagConstraints.HORIZONTAL;
		gbc_direction.gridx = 0;
		gbc_direction.gridy = 7;
		
		direction.addActionListener(new ActionListener()
	    {
		      public void actionPerformed(ActionEvent e)
		      {
		    	  frame.directionSelected(direction.getSelectedItem().toString());
		      }
		    });
		
		add(direction, gbc_direction);
		
		// View Menu
		
		JLabel lblView = new JLabel("View");
		GridBagConstraints gbc_lblView = new GridBagConstraints();
		gbc_lblView.insets = new Insets(0, 0, 5, 0);
		gbc_lblView.gridx = 0;
		gbc_lblView.gridy = 8;
		add(lblView,gbc_lblView);
		
		String[] viewOptions = {"Shit", "Render"};
		
		final JComboBox view = new JComboBox(viewOptions);
		GridBagConstraints gbc_view = new GridBagConstraints();
		gbc_view.fill = GridBagConstraints.HORIZONTAL;
		gbc_view.gridx = 0;
		gbc_view.gridy = 9;
		
		view.addActionListener(new ActionListener()
	    {
		      public void actionPerformed(ActionEvent e)
		      {
		    	  frame.viewSelected(view.getSelectedItem().toString());
		      }
		    });
		
		add(view, gbc_view);

	}

}
