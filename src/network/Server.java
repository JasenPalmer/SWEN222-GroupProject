package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 
 * @author Matt Byers
 *
 */
public class Server {
	
	//Game uses a constant port of 9954
	private static final int PORT = 9954;
	
	
	//Server to accept socket connections
	private ServerSocket serverSocket;
	
	//All connected clients
	private ArrayList<ClientThread> connections;
	
	//Queue of events to be processed
	private Queue<NetworkEvent> eventQueue;
	
	//Running status of server
	private boolean finished = false;
	
	
	public Server(){
		connections = new ArrayList<ClientThread>();
		eventQueue = new LinkedList<NetworkEvent>();
	}
	
	public void start(){
		try {
			serverSocket= new ServerSocket(PORT);
			System.out.println("Server started successfully on port number: " + PORT);
			
			while(!finished) {
				
				if(finished) break;
				
				Socket client = serverSocket.accept();
				
				ClientThread clientThread = new ClientThread(client);
				connections.add(clientThread);
				clientThread.start();
			}
		} catch (IOException e){
			System.err.println("Error in main server.");
		}
	}
	
	public ClientThread getClient(String user){
		for(ClientThread client : connections){
			if(client.getUser().equals(user)) return client;
		}
		System.err.println("User: " + user + " not found.");
		return null;
	}
	
	public void sendMessage(String receiverUser, String message, String senderUser){
		ClientThread receiver = getClient(receiverUser);
		if(receiver != null) receiver.sendMessage(message, senderUser);
	}
	
	public void broadcastMessage(String message, String senderUser){
		for(ClientThread client: connections){
			client.sendMessage(message, senderUser);
		}
	}
	
	public void updateGUI(){
		for(ClientThread client : connections){
			client.updateGUI();
		}
	}
	
	public void stopServer(){
		for(ClientThread t : connections){
			t.close();
		}
		finished = true;
	}
	
	public class ClientThread extends Thread {
		
		//Socket of the associated client
		private Socket socket;
		
		//Username of the associated client
		private String user;
		
		//Socket input and output
		private ObjectInputStream input;
		private ObjectOutputStream output;
		
		//The last event read from the sockets input stream
		private NetworkEvent currentEvent;
		
		private boolean finished = false;
		
		public ClientThread(Socket socket){
			this.socket = socket;
			
			//Create input/output streams to the client's socket, then read the username.
			try {
				output = new ObjectOutputStream(this.socket.getOutputStream());
				input = new ObjectInputStream(this.socket.getInputStream());
				
				this.user = (String)input.readObject();
				
			} catch (Exception e) {
				System.err.println("Error getting input/output streams for the client: " + user);
			}
			
			
			System.out.println(user + " connected.");
		}
		
		public void run(){
			System.out.println("Client thread for " + user + " is running...");
			
			while(!finished){
			
				try {
					currentEvent = (NetworkEvent)input.readObject();
				} catch (Exception e){
					System.err.println("Error reading input stream of client: " + user);
				}
				
				if(currentEvent  != null) {
					switch(currentEvent.getType()){
					case KEY_PRESS:
						eventQueue.add(currentEvent);
						break;
					case MESSAGE:
						broadcastMessage(currentEvent.getMessage(), user);
						break;
					case UPDATE_GUI:
						System.err.println("Unexpected EventType on server-side: UPDATE_GUI");
						break;
					}
				}
			}
		}
		
		public void updateGUI(){
			try {
				output.writeObject(new NetworkEvent());
			} catch (IOException e) {
				System.err.println("Error writing update to client: " + user);
			}
		}
		
		public void sendMessage(String message, String senderUser){
			try {
				output.writeObject(new NetworkEvent(message, senderUser));
			} catch (IOException e) {
				System.err.println("Error writing update to client: " + user);
			}
		}
		
		//Getters
		public String getUser(){ return user; }
		
		public void close(){
			
			try {
				output.close();
				input.close();
				socket.close();
			} catch (Exception e){}
		}
	}
}
