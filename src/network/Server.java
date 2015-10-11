package network;

import gameworld.Game;
import gameworld.Game.Direction;
import gameworld.Player;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
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

	//Server console
	private ServerWindow console;

	//All connected clients
	private ArrayList<ClientThread> connections;

	//Queue of events to be processed
	private Queue<NetworkEvent> eventQueue;

	private EventThread eventHandler;

	//Running status of server
	private boolean finished = false;

	//Main instance of game, that will be sent to clients
	private Game gameState;


	public Server(){
		console = new ServerWindow(this);

		connections = new ArrayList<ClientThread>();
		eventQueue = new LinkedList<NetworkEvent>();

		gameState = new Game();

		eventHandler = new EventThread();
		eventHandler.start();

		start();
	}

	public void start(){
		try {
			serverSocket= new ServerSocket(PORT);
			console.displayEvent("Server started successfully on port number: " + PORT);

			while(!finished) {

				if(finished) break;

				Socket client = serverSocket.accept();
				ClientThread clientThread = new ClientThread(client);
				synchronized(connections){
					connections.add(clientThread);
				}
				clientThread.start();
			}
		} catch (IOException e){
			console.displayError("Server failed to start");
		}
	}
	
	public synchronized void kickPlayer(String user){
		ClientThread toKick = getClient(user);
		toKick.sendMessage("You have been kicked", "Server");
		if(toKick != null) toKick.close();
	}

	public synchronized ClientThread getClient(String user){
		for(ClientThread client : connections){
			if(client.getUser().equals(user)) return client;
		}
		//console.displayError("User: " + user + " not found.");
		return null;
	}

	public synchronized void sendMessage(String receiverUser, String message, String senderUser){
		ClientThread receiver = getClient(receiverUser);
		if(receiver != null) receiver.sendMessage(message, senderUser);
	}

	public synchronized void broadcastMessage(String message, String senderUser){
		for(ClientThread client: connections){
			client.sendMessage(message, senderUser);
		}
	}

	public synchronized void updateGUI(Player madeUpdate){
		//console.displayEvent("Updating all clients");
		for(ClientThread client : connections){
			if(madeUpdate.getLocation().getPlayers().contains(gameState.parsePlayer(client.getUser()))) client.updateGUI();
		}
	}

	public synchronized void processEvents(){
		NetworkEvent toProcess =  eventQueue.poll();
		//System.out.println("Processing");
		if(toProcess == null) return;
		switch(toProcess.getType()){
		case KEY_PRESS:
			boolean hasChanged = false;
			switch(toProcess.getKeyCode()) {
			case KeyEvent.VK_W:
				hasChanged = gameState.movePlayer(toProcess.getUser(), Direction.NORTH);
				break;
			case KeyEvent.VK_D:
				hasChanged = gameState.movePlayer(toProcess.getUser(), Direction.EAST);
				break;
			case KeyEvent.VK_S:
				hasChanged = gameState.movePlayer(toProcess.getUser(), Direction.SOUTH);
				break;
			case KeyEvent.VK_A:
				hasChanged = gameState.movePlayer(toProcess.getUser(), Direction.WEST);
				break;
			case KeyEvent.VK_Q:
				gameState.parsePlayer(toProcess.getUser()).changeDirection(toProcess.getKeyCode());
				break;
			case KeyEvent.VK_E:
				gameState.parsePlayer(toProcess.getUser()).changeDirection(toProcess.getKeyCode());
				break;
			case KeyEvent.VK_SPACE:
				hasChanged = gameState.attackPlayer(toProcess.getUser());
				break;
			case KeyEvent.VK_F:
				hasChanged = gameState.performAction(toProcess.getUser());
			default:
				break;
			}
			if(hasChanged) updateGUI(gameState.parsePlayer(toProcess.getUser()));
			break;
		case CYCLE_ANIMATIONS:
			if(this.gameState.parsePlayer(toProcess.getUser()).isAttacking()){
				this.gameState.parsePlayer(toProcess.getUser()).getAnimation().cycleAttack();
				updateGUI(gameState.parsePlayer(toProcess.getUser()));
			}
			break;
		case MESSAGE:
			break;
		case UPDATE_GUI:
			break;
		case CLOSE:
			break;
		default:
			break;
		}
	}


	public synchronized void stopServer(){
		for(ClientThread t : connections){
			t.close();
		}
		connections.clear();
		finished = true;
		eventHandler.finish();
	}

	public class EventThread extends Thread {
		private boolean finished = false;

		public void run(){
			while(!finished){
				processEvents();
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {}
			}
		}

		public void finish(){
			this.finished = true;
		}
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
				console.displayError("Failed to get input/output streams for the client: " + user);
			}

			gameState.addPlayer(new Player(user, gameState));
			console.displayEvent(user + " connected.");
			this.updateGUI();
		}

		public void run(){
			console.displayEvent("Client thread for " + user + " is running...");

			while(!finished){

				try {
					currentEvent = (NetworkEvent)input.readObject();
				} catch (Exception e){
					console.displayError("Failed to read input stream of client: " + user);
				}

				if(currentEvent  != null) {
					console.displayEvent("Event Received from " + user + " : " + currentEvent.getType());
					
					switch(currentEvent.getType()){
					case KEY_PRESS:
						eventQueue.add(currentEvent);
						break;
					case CYCLE_ANIMATIONS:
						eventQueue.add(currentEvent);
						break;
					case MESSAGE:
						console.displayMessage(currentEvent.getMessage(), user);
						broadcastMessage(currentEvent.getMessage(), user);
						break;
					case CLOSE:
						if(!currentEvent.getUser().equals("Server")) close();
						break;
					default:
						console.displayError("Unexpected EventType from " + user + " : " + currentEvent.getType());
						break;
					}
				}
			}
		}

		public synchronized void updateGUI(){
			//console.displayEvent("Updating GUI for client: " + user + " with position at - " + gameState.parsePlayer(user).getPosition());
			try {
				output.reset();
				output.writeObject(new NetworkEvent(gameState.parsePlayer(user)));
			} catch (IOException e) {
				console.displayError("Failed to write update to client: " + user + " - " + e);
			}
		}

		public synchronized void sendMessage(String message, String senderUser){
			try {
				output.reset();
				output.writeObject(new NetworkEvent(message, senderUser));
			} catch (IOException e) {
				console.displayError("Failed to write message to client: " + user);
			}
		}

		//Getters
		public String getUser(){ return user; }

		public void close(){

			finished = true;

			try {
				output.close();
				input.close();
				socket.close();
			} catch (Exception e){}

		}
	}
	
	public static void main(String [] args){
		new Server();
	}
}
