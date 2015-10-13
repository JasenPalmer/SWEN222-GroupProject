package network;

import gameworld.Game;
import gameworld.Game.Direction;
import gameworld.Player;
import gameworld.entity.Armour;
import gameworld.entity.Container;
import gameworld.entity.Weapon;

import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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
			serverSocket.setReceiveBufferSize(64000);
			console.displayEvent("Server started successfully on port number: " + PORT);

			while(!finished) {

				if(finished) break;

				Socket client = serverSocket.accept();
				console.displayEvent("Server accepting a new client");
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
		synchronized(connections){
			for(ClientThread client : connections){
				if(client.getUser().equals(user)) return client;
			}
		}
		//console.displayError("User: " + user + " not found.");
		return null;
	}

	public synchronized void sendMessage(String receiverUser, String message, String senderUser){
		ClientThread receiver = getClient(receiverUser);
		if(receiver != null) receiver.sendMessage(message, senderUser);
	}

	public synchronized void broadcastMessage(String message, String senderUser){
		synchronized(connections){
			for(ClientThread client: connections){
				client.sendMessage(message, senderUser);
			}
		}
	}
	
	public synchronized void updateGUIAll(){
		if(!(connections.size() > 0)) return;
		console.displayEvent("Updating all clients");
		 synchronized(connections){
			 for(ClientThread client : connections){
				 client.updateGUI();
			 }
		 }
	}

	public synchronized void updateGUI(Player madeUpdate){
		//console.displayEvent("Updating all clients");
		synchronized(connections){
			 for(ClientThread client : connections){
				 if(madeUpdate.getLocation().getPlayers().contains(gameState.parsePlayer(client.getUser()))) client.updateGUI();
			 }
		}
	}
	

	private void updateInvent(Player madeUpdate) {
		console.displayEvent("Updating client invents");
		synchronized(connections){
			 for(ClientThread client : connections){
				 if(madeUpdate.getLocation().getPlayers().contains(gameState.parsePlayer(client.getUser()))) client.updateInvent();
			 }
		}
	}

	public synchronized void movePlayer(String movingUser){
		synchronized(connections){
			for(ClientThread client : connections){
				if(gameState.parsePlayer(movingUser).getLocation().getPlayers().contains(gameState.parsePlayer(client.getUser()))) client.movePlayer(movingUser);
			}

		}
	}

	public synchronized void animationCycle(String cycleUser){
		synchronized(connections){
			for(ClientThread client : connections){
				if(gameState.parsePlayer(cycleUser).getLocation().getPlayers().contains(gameState.parsePlayer(client.getUser()))) client.animationCycle(cycleUser);
			}

		}
	}

	public synchronized void processEvents(){
		NetworkEvent toProcess =  eventQueue.poll();
		//System.out.println("Processing");
		if(toProcess == null) return;

		boolean gameNeedsUpdate = false;
		boolean inventNeedsUpdate = false;
		Player p = null;

		switch(toProcess.getType()){
		case KEY_PRESS:
			int hasMoved = 0;
			switch(toProcess.getKeyCode()) {
			case KeyEvent.VK_W:
				hasMoved = gameState.movePlayer(toProcess.getUser(), Direction.NORTH);
				break;
			case KeyEvent.VK_D:
				hasMoved = gameState.movePlayer(toProcess.getUser(), Direction.EAST);
				break;
			case KeyEvent.VK_S:
				hasMoved = gameState.movePlayer(toProcess.getUser(), Direction.SOUTH);
				break;
			case KeyEvent.VK_A:
				hasMoved = gameState.movePlayer(toProcess.getUser(), Direction.WEST);
				break;
			case KeyEvent.VK_Q:
				gameState.parsePlayer(toProcess.getUser()).changeDirection(toProcess.getKeyCode());
				gameNeedsUpdate = true;
				break;
			case KeyEvent.VK_E:
				gameState.parsePlayer(toProcess.getUser()).changeDirection(toProcess.getKeyCode());
				gameNeedsUpdate = true;
				break;
			case KeyEvent.VK_SPACE:
				gameNeedsUpdate = gameState.attackPlayer(toProcess.getUser());
				break;
			case KeyEvent.VK_F:
				Container c = gameState.performAction(toProcess.getUser());
				if(c != null) getClient(toProcess.getUser()).displayContainer(c);
				gameNeedsUpdate = true;
				break;
			default:
				break;
			}
			
			if(hasMoved > 0) movePlayer(toProcess.getUser());
			else if(hasMoved > 1) gameNeedsUpdate = true;
			
//			if(hasMoved > 0) gameNeedsUpdate = true;
			break;
			
		case CYCLE_ANIMATIONS:
			if(this.gameState.parsePlayer(toProcess.getUser()).isAttacking()){
				this.gameState.parsePlayer(toProcess.getUser()).getAnimation().cycleAttack();
				animationCycle(toProcess.getUser());
			}
			break;
		case ADD_ITEM:
			p = this.gameState.parsePlayer(toProcess.getUser());
			inventNeedsUpdate = p.addItem(toProcess.getItem());
			break;
		case REMOVE_ITEM:
			p = this.gameState.parsePlayer(toProcess.getUser());
			p.removeItem(toProcess.getIndex1());
			inventNeedsUpdate = true;
			break;
		case SWAP_ITEM:
			p = this.gameState.parsePlayer(toProcess.getUser());
			p.swapItems(toProcess.getIndex1(), toProcess.getIndex2());
			inventNeedsUpdate = true;
			break;
		case SET_WEAPON:
			p = this.gameState.parsePlayer(toProcess.getUser());
			p.setWeapon((Weapon)toProcess.getItem());
			inventNeedsUpdate = true;
			break;
		case SET_ARMOUR:
			p = this.gameState.parsePlayer(toProcess.getUser());
			p.setArmour((Armour)toProcess.getItem());
			inventNeedsUpdate = true;
			break;
		case REMOVE_ITEM_CONTAINER:
			p = this.gameState.parsePlayer(toProcess.getUser());
			Container c = gameState.removeItemContainer(p,toProcess.getIndex1(), toProcess.getContainer());
			getClient(toProcess.getUser()).displayContainer(c);
			inventNeedsUpdate = true;
			break;
		case UPDATE_GAME:
			break;
		case CLOSE:
			break;
		default:
			break;
		}
		if(gameNeedsUpdate) updateGUI(gameState.parsePlayer(toProcess.getUser()));
		if(inventNeedsUpdate) updateInvent(gameState.parsePlayer(toProcess.getUser()));
	}

	public synchronized void stopServer(){
		for(ClientThread t : connections){
			t.close();
		}
		synchronized(connections){
			connections.clear();
		}
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
			console.displayEvent("Creating client thread");
			this.socket = socket;

			//Create input/output streams to the client's socket, then read the username.
			try {
				//output = new ObjectOutputStream(this.socket.getOutputStream());
				input = new ObjectInputStream(this.socket.getInputStream());
				//console.displayEvent("Opening data streams for client");
				output = new ObjectOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
				//input = new ObjectInputStream(new BufferedInputStream(this.socket.getInputStream()));

				
				output.flush();
				
				this.user = (String)input.readObject();

			} catch (Exception e) {
				console.displayError("Failed to get input/output streams for the client: " + user);
			}

			gameState.addPlayer(new Player(user, gameState));
			console.displayEvent(user + " connected.");
			this.updateGUI();
		}

		public void movePlayer(String movingUser) {
			try {
				//output.reset();
				output.writeObject(new NetworkEvent(movingUser, gameState.parsePlayer(movingUser).getFacing(), gameState.parsePlayer(movingUser).getPosition()));
				output.reset();
				output.flush();
			} catch (IOException e) {
				console.displayError("Failed to write update to client: " + user + " - " + e);
			}
		}

		public void animationCycle(String cycleUser){
			try {
				//output.reset();
				output.writeObject(new NetworkEvent(this.user, NetworkEvent.EventType.CYCLE_ANIMATIONS));
				output.reset();
				output.flush();
			} catch (IOException e) {
				console.displayError("Failed to write update to client: " + user + " - " + e);
			}
		}


		public void displayContainer(Container c){
			try {
				
				output.writeObject(new NetworkEvent(this.user, c));
				output.reset();
				output.flush();
			} catch (IOException e) {
				console.displayError("Failed to write update to client: " + user + " - " + e);
			}
		}

		public void run(){
			console.displayEvent("Client thread for " + user + " is running...");

			while(!finished){

				try {
					currentEvent = (NetworkEvent)input.readObject();
				} catch (Exception e){
					console.displayError("Failed to read input stream of client: " + user);
					close();
				}

				if(currentEvent  != null) {
					console.displayEvent("Event Received from " + user + " : " + currentEvent.getType());

					//System.out.println("Size of Queue: " + eventQueue.size());
					if(eventQueue.size() > 35) eventQueue.poll();
					switch(currentEvent.getType()){
					case KEY_PRESS:
						eventQueue.add(currentEvent);
						break;
					case CYCLE_ANIMATIONS:
						eventQueue.add(currentEvent);
						break;
					case ADD_ITEM:
						eventQueue.add(currentEvent);
						break;
					case REMOVE_ITEM:
						eventQueue.add(currentEvent);
						break;
					case SWAP_ITEM:
						eventQueue.add(currentEvent);
						break;
					case SET_WEAPON:
						eventQueue.add(currentEvent);
						break;
					case SET_ARMOUR:
						eventQueue.add(currentEvent);
						break;
					case REMOVE_ITEM_CONTAINER:
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
				//output.reset();
				output.writeObject(new NetworkEvent(gameState.parsePlayer(user), NetworkEvent.EventType.UPDATE_GAME));
				output.reset();
				output.flush();
			} catch (IOException e) {
				console.displayError("Failed to write update to client: " + user + " - " + e);
			}
		}
		
		public synchronized void updateInvent(){
			//console.displayEvent("Updating INVENT for client: " + user + " with position at - " + gameState.parsePlayer(user).getPosition());
			try {
				
				output.writeObject(new NetworkEvent(gameState.parsePlayer(user), NetworkEvent.EventType.UPDATE_INVENT));
				output.reset();
				output.flush();
			} catch (IOException e) {
				console.displayError("Failed to write update to client: " + user + " - " + e);
			}
		}

		public synchronized void sendMessage(String message, String senderUser){
			try {
				//output.reset();
				output.writeObject(new NetworkEvent(message, senderUser));
				output.reset();
				output.flush();
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
