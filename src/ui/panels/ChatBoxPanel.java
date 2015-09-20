package ui.panels;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class ChatBoxPanel extends JPanel{

	private Image backgroundImage;
	private JTextArea textArea = new JTextArea();
	private JTextField textBox = new JTextField();
	private JScrollPane scrollPane = new JScrollPane();

	public ChatBoxPanel(){
		setLayout(null);

		setBounds(10, 760, 450, 190);

		//Set text area
		textArea.setEditable(false);
		textArea.append("Please work\n\n\n\n\n\n\n\n\n\n\n");
		 
		//Setup scroll pane
		scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(0,0,450,170);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
		this.add(scrollPane);
		

		//Setup text field
		textBox = new JTextField();
		textBox.setBounds(0, 170, 450, 20);
		this.add(textBox);
	}
}
