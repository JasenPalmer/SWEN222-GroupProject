package network;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
	
	//Socket connect to server
	private Socket socket;
	
	//Server input and output
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	//Host's address to connect, User name of the client
	private final String host, user;
	
	//Game uses a constant port of 9954
	private static final int PORT = 9954;
	
	public Client (String host, String user){
		this.host = host;
		this.user = user;
	}
	
	public void start(){
		
	}
	
	public void registerKeyPress(KeyEvent event){
		NetworkEvent toWrite = new NetworkEvent(event);
		try {
			output.writeObject(toWrite);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
