package mapeditor;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SelectionPanel extends JPanel implements ActionListener{

	/**
	 * Create the panel.
	 */
	public SelectionPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JRadioButton rdbtnInsideLocation = new JRadioButton("Inside Location");
		GridBagConstraints gbc_rdbtnInsideLocation = new GridBagConstraints();
		gbc_rdbtnInsideLocation.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnInsideLocation.gridx = 0;
		gbc_rdbtnInsideLocation.gridy = 0;
		add(rdbtnInsideLocation, gbc_rdbtnInsideLocation);
		
		JRadioButton rdbtnOutsideLocation = new JRadioButton("Outside Location");
		GridBagConstraints gbc_rdbtnOutsideLocation = new GridBagConstraints();
		gbc_rdbtnOutsideLocation.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnOutsideLocation.gridx = 0;
		gbc_rdbtnOutsideLocation.gridy = 1;
		add(rdbtnOutsideLocation, gbc_rdbtnOutsideLocation);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

}
