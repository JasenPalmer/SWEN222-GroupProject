package ui.panels;

import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import network.Client;
import ui.ApplicationWindow;

public class ChatBoxPanel extends JPanel implements KeyListener{

	private Image backgroundImage;
	private JTextArea textArea = new JTextArea();
	private JTextField textBox = new JTextField();
	private JScrollPane scrollPane = new JScrollPane();
	private Client client;
	private ApplicationWindow window;

	public ChatBoxPanel(Client client, ApplicationWindow window){
		this.client = client;
		this.window = window;
		setLayout(null);

		setBounds(10, 760, 450, 190);

		//Set text area
		textArea.setEditable(false);
		textArea.append("Welcome to the chat!");

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
			if(!textBox.getText().equals("")){
				sendMessage();
			}
		}
	}
	public void keyPressed(KeyEvent arg0) {}
	public void keyReleased(KeyEvent arg0) {}

	private void sendMessage(){
		client.registerMessage(textBox.getText());
		textBox.setText("");
		window.requestFocus();
	}

	public void displayMessage(String user, String message){
		System.out.println(user + ": " + message);
		textArea.append("\n" + user + ": " + message);
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}
	
	//Getters
	public JTextField getTextField(){return this.textBox;}
}
