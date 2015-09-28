package network;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import ui.ApplicationWindow;
import ui.panels.ChatBoxPanel;
import network.NetworkEvent.EventType;

/**
 * 
 * @author Matt Byers
 *
 */
public class Client {
	
	//Socket connect to server
	private Socket socket;
	
	//Server input and output
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	//Host's address to connect, User name of the client
	private final String host, user;
	
	private final ApplicationWindow gui;
	
	//Game uses a constant port of 9954
	private static final int PORT = 9954;
	
	public Client (String host, String user, ApplicationWindow gui){
		this.host = host;
		this.user = user;
		this.gui = gui;
		
		start();
	}
	
	public void start(){
		
		System.out.println("Start new client: " + user);
		
		//Create a new Socket to the specified host's server
		try {
			socket = new Socket(host, PORT);
		} catch (IOException e){
			System.err.println("Error creating new client: " + user);
			return;
		}
		
		//Open object input and output streams for the newly created socket
		try {
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e){
			System.err.println("Error opening object streams for client: " + user);
			return;
		}
		
		//Write the username to the ouput
		try {
			output.writeObject(user);
		} catch (IOException e) {
			System.err.println("Error writing to output stream for client: " + user);
		}
	}
	
	public void registerKeyPress(KeyEvent event){
		NetworkEvent toWrite = new NetworkEvent(event, user);
		try {
			output.writeObject(toWrite);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void registerMessage(String message){
		NetworkEvent toWrite = new NetworkEvent(message, user);
		try {
			output.writeObject(toWrite);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public class ServerThread extends Thread {
		public void run() {
			while(true) {
				try {
					NetworkEvent event = (NetworkEvent)input.readObject();
					if(event.getType() == EventType.UPDATE_GUI){
						//TODO Repaint the clients Rendering window
					}
					else if(event.getType() == EventType.MESSAGE){
						ChatBoxPanel chatBox = gui.getChatBox();
						chatBox.displayMessage(user, event.getMessage());
					}
				}
				catch (IOException e){
					System.err.println("Connection to the server has been interrupted..."); } 
				catch (ClassNotFoundException e) {}
			}
		}
	}

}
