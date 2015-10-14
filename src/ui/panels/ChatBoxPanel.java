package ui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import network.Client;
import ui.ApplicationWindow;

/**
 * Chatbox component
 * @author ItsNotAGoodTime
 *
 */
public class ChatBoxPanel extends JPanel implements KeyListener{

	//Serialize
	private static final long serialVersionUID = 1L;
	//Text items
	private JTextArea textArea = new JTextArea();
	private JTextField textBox = new JTextField();
	private JScrollPane scrollPane = new JScrollPane();
	
	//Chatbox client
	private Client client;
	
	//Chatbox application window
	private ApplicationWindow window;

	/**
	 * Adds and aligns elements
	 * @param window - Application window associated with chatbox
	 */
	public ChatBoxPanel(ApplicationWindow window){
		this.window = window;
		this.client = window.getClient();
		setOpaque(false);
		setLayout(null);

		setBounds(0, 685, 470, 215);

		//Setup background
		ChatBoxBackground background = new ChatBoxBackground();
		this.add(background,0,0);

		//Set text area
		textArea.setEditable(false);
		textArea.append("\n\n\n\n\n\n\n\n\n\n\n\n\nWelcome to the chat!");
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
		textBox.setBounds(10, 190, 450, 20);
		textBox.addKeyListener(this);
		textBox.setOpaque(false);
		textBox.setBorder(border);
		textBox.setFont(font);
		textBox.setForeground(Color.YELLOW);
		this.add(textBox,2,0);
	}

	/**
	 * Checks if user pressed enter
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		if(e.getKeyChar() == '\n'){
			if(!textBox.getText().equals("")){
				sendMessage();
			}
			else{
				window.requestFocus();
			}
		}
	}

	/**
	 * Sends textbox message to server
	 */
	private void sendMessage(){
		client.registerMessage(textBox.getText());
		textBox.setText("");
		window.requestFocus();
	}

	/**
	 * Displays in text area a message from server
	 * @param user - Message owners username
	 * @param message - Message to be displayed
	 */
	public void displayMessage(String user, String message){
		textArea.append("\n" + user + ": " + message);
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}

	//Getters
	public JTextField getTextField(){return this.textBox;}
	
	//Required but unused methods
	public void keyPressed(KeyEvent arg0) {}
	public void keyReleased(KeyEvent arg0) {}
}
