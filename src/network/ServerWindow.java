package network;

import java.awt.BorderLayout;
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
public class ServerWindow extends JFrame implements WindowListener {

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

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		server.stopServer();
		System.exit(0);
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}
