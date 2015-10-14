package network;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * JFrame to display the status of the server to the server creator.
 * @author Matt Byers
 *
 */
public class ServerWindow extends JFrame implements WindowListener, KeyListener {

	private static final long serialVersionUID = 1L;

	//Text area to display Server events
	private JTextArea console;
	
	//Input for sending messages to clients
	private JTextField input;
	
	//The server instance this window corresponds to
	private Server server;
	
	/**
	 * Creates server window, that will display messages and other server events,
	 * plus allow server wide messages to be broadcasted.
	 * @param server - The Server instance.
	 */
	public ServerWindow(Server server){
		
		super("Adventure Game Server");
		
		this.server = server;
		
		getContentPane().setLayout(new BorderLayout());
		setResizable(false);
		setBounds(10, 10, 500, 500);
		
		addWindowListener(this);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		console = new JTextArea();
		console.setBounds(5, 5, 290, 480);
		console.setEditable(false);
		
		JScrollPane consoleScroll = new JScrollPane(console);
		consoleScroll.setBounds(5, 5, 290, 480);
		
		getContentPane().add(consoleScroll, BorderLayout.CENTER);
		
		input = new JTextField();
		input.setBounds(5, 485, 290, 10);
		input.setText("");
		input.addKeyListener(this);
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
		console.setCaretPosition(console.getDocument().getLength());
	}
	
	/**
	 * Displays any important events on the server
	 * @param event
	 */
	public void displayEvent(String event){
		console.append("\n[EVENT] - " + event);
		console.setCaretPosition(console.getDocument().getLength());
	}
	
	/**
	 * Displays any major errors on the server
	 * @param error
	 */
	public void displayError(String error){
		console.append("\n[ERROR] - " + error);
		console.setCaretPosition(console.getDocument().getLength());
	}
	
	private void sendServerMessage(String message){
		displayMessage(message, "Server");
		server.broadcastMessage(message, "Server");
	}



	@Override
	public void windowClosing(WindowEvent e) {
		server.stopServer();
		System.exit(0);
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyChar() == '\n'){
			if(input.getText() != "") {
				if(input.getText().split(" ", 2)[0].equals("KICK")){
					server.kickPlayer(input.getText().split(" ", 2)[1]);
				}
				else {
					sendServerMessage(input.getText());
				}
				input.setText("");
			}
			
		}
	}
	
	//Unused Window and Key Listener events
	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}

}
