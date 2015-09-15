package network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;

public class Server {
	
	//Game uses a constant port of 9954
	private static final int PORT = 9954;
	
	
	//Server to accept socket connections
	private ServerSocket serverSocket;
	
	//All connected clients
	private ArrayList<ClientThread> connections;
	
	//Running status of server
	private boolean finished = false;
	
	//Queue of events to be processed
	private Queue<NetworkEvent> eventQueue;
	
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
		
		private NetworkEvent currentEvent;
		
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
