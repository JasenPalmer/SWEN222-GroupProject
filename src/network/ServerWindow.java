package network;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * JFrame to display the status of the server to the server creator.
 * @author Matt Byers
 *
 */
public class ServerWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	//Text area to display Server events
	private JTextArea console;
	
	//Input for sending messages to clients
	private JTextField input;
	
	/**
	 * Creates server window, that will display messages and other server events,
	 * plus allow server wide messages to be broadcasted.
	 * @param server - The Server instance.
	 */
	public ServerWindow(){
		
		super("Adventure Game Server");
		
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(10, 10, 500, 500);
		
		console = new JTextArea();
		console.setBounds(5, 5, 290, 480);
		console.setEditable(false);
		
		JScrollPane consoleScroll = new JScrollPane(console);
		consoleScroll.setBounds(5, 5, 290, 480);
		
		getContentPane().add(consoleScroll, BorderLayout.CENTER);
		
		input = new JTextField();
		input.setBounds(5, 485, 290, 10);
		getContentPane().add(input, BorderLayout.SOUTH);
		
		setVisible(true);
	}
	
	/**
	 * Displays a Message on the server console
	 * @param user - The user that sent the message.
	 * @param message - The message sent.
	 */
	public void displayMessage(String message, String user){
		console.append("\n[MESSAGE] - " + user + ": " + message);
	}
	
	/**
	 * Displays any important events on the server
	 * @param event
	 */
	public void displayEvent(String event){
		console.append("\n[EVENT] - " + event);
	}
	
	/**
	 * Displays any major errors on the server
	 * @param error
	 */
	public void displayError(String error){
		console.append("\n[ERROR] - " + error);
	}

}
