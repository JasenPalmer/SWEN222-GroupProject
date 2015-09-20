package ui.panels;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class ChatBoxPanel extends JPanel implements KeyListener{

	private Image backgroundImage;
	private JTextArea textArea = new JTextArea();
	private JTextField textBox = new JTextField();
	private JScrollPane scrollPane = new JScrollPane();

	public ChatBoxPanel(){
		setLayout(null);

		setBounds(10, 760, 450, 190);

		//Set text area
		textArea.setEditable(false);
		 
		//Setup scroll pane
		scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(0,0,450,170);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
		this.add(scrollPane);	

		//Setup text field
		textBox = new JTextField();
		textBox.setBounds(0, 170, 450, 20);
		textBox.addKeyListener(this);
		this.add(textBox);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if(e.getKeyChar() == '\n'){
			System.out.println("enter");
			appendTextBox();
		}
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private void appendTextBox(){
		textArea.append(textBox.getText());
		textBox.setText("");
		textArea.append("\n");
	}
}
