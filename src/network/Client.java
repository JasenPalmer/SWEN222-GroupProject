package network;

import java.awt.event.KeyEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
	
	//Socket connect to server
	private Socket socket;
	
	//Server input and output
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	private String host, user;
	private static final int port = 9954;
	
	public Client (String host, String user){
		this.host = host;
		this.user = user;
	}
	
	public void start(){
		
	}
	
	public void registerKeyPress(KeyEvent e){
		
	}

}
