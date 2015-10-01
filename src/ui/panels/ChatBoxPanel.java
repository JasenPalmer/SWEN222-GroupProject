package ui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.border.Border;

import network.Client;
import ui.ApplicationWindow;

public class ChatBoxPanel extends JPanel implements KeyListener{

	private Image backgroundImage;
	private JTextArea textArea = new JTextArea();
	private JTextField textBox = new JTextField();
	private JScrollPane scrollPane = new JScrollPane();
	private JViewport vp = new JViewport();
	private Client client;
	private ApplicationWindow window;

	public ChatBoxPanel(Client client, ApplicationWindow window){
		this.client = client;
		this.window = window;
		setOpaque(false);
		setLayout(null);

		setBounds(0, 530, 470, 215);

		//Setup background
		ChatBoxBackground background = new ChatBoxBackground();
		this.add(background,0,0);
		
		//Set text area
		textArea.setEditable(false);
		textArea.append("Welcome to the chat!");
		Font font = new Font("Verdana", Font.BOLD, 12);
		textArea.setFont(font);
		textArea.setForeground(Color.YELLOW);
		textArea.setOpaque(false);
		textArea.setLineWrap(true);
		
		//Setup scroll pane
		scrollPane = new JScrollPane(textArea);
		scrollPane.getViewport().setOpaque(false);
		Border border = BorderFactory.createEmptyBorder(0,0,0,0);
		scrollPane.setViewportBorder(border);
		scrollPane.setBorder(border);
		scrollPane.setBounds(10,15,450,175);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0,0));
		scrollPane.setOpaque(false);
		this.add(scrollPane,2,0);	

		//Setup text field
		textBox = new JTextField();
		textBox.setBounds(10, 185, 450, 20);
		textBox.addKeyListener(this);
		textBox.setOpaque(false);
		textBox.setBorder(border);
		textBox.setFont(font);
		textBox.setForeground(Color.YELLOW);
		this.add(textBox,2,0);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if(e.getKeyChar() == '\n'){
			System.out.println("enter");
			if(!textBox.getText().equals("")){
				sendMessage();
			}
			else{
				window.requestFocus();
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
