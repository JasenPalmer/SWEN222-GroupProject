package network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	
	//Server to accept socket connections
	private ServerSocket serverSocket;
	
	//All connected clients
	private ArrayList<ClientThread> connections;
	
	private boolean finished = false;
	
	private static final int port = 9954;
	
	public Server(){
		connections = new ArrayList<ClientThread>();
	}
	
	public void start(){
		
	}
	
	public class ClientThread extends Thread {
		
		//Socket asscoiated with this client
		private Socket socket;
		
		//Username of the client
		private String user;
		
		//Socket input and output
		private ObjectInputStream input;
		private ObjectOutputStream output;
		
		private String action;
		
		public ClientThread(Socket socket){
			this.socket = socket;
		}
		
		public void run(){
			System.out.println("Client thread for " + user + " is running...");
		}
		
		public void close(){
			
			try {
				output.close();
				input.close();
				socket.close();
			} catch (Exception e){}
		}
	}
}
