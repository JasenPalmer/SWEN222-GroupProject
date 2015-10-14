package network;

import gameworld.Game;
import gameworld.Game.Direction;
import gameworld.Player;
import gameworld.entity.Armour;
import gameworld.entity.Container;
import gameworld.entity.Weapon;

import java.awt.event.KeyEvent;
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
 * The main server for our multiplayer Arena Game: "Shank the World". 
 * This server holds the main instance of our Game World and all clients in the game send and receive updates
 * from this Server Instances Game World.
 *
 */
public class Server {

	// Game uses a constant port of 9954
	private static final int PORT = 9954;

	// Server to accept socket connections
	private ServerSocket serverSocket;

	// Server console
	private ServerWindow console;

	// All connected clients
	private ArrayList<ClientThread> connections;

	// Queue of events to be processed
	private Queue<NetworkEvent> eventQueue;

	private EventThread eventHandler;

	// Running status of server
	private boolean finished = false;

	// Main instance of game, that will be sent to clients
	private Game gameState;

	public Server() {
		console = new ServerWindow(this);

		connections = new ArrayList<ClientThread>();
		eventQueue = new LinkedList<NetworkEvent>();

		gameState = new Game();

		eventHandler = new EventThread();
		eventHandler.start();

		start();
	}

	public void start() {
		try {
			serverSocket = new ServerSocket(PORT);
			serverSocket.setReceiveBufferSize(128000);
			console.displayEvent("Server started successfully on port number: "
					+ PORT);

			while (!finished) {

				if (finished)
					break;

				Socket client = serverSocket.accept();
				console.displayEvent("Server accepting a new client");
				ClientThread clientThread = new ClientThread(client);

				synchronized (connections) {
					
					//Will not open the client if their username is already in use.
					for(ClientThread ct : connections){
						if(ct.getUser().equals(clientThread.getUser()))
							clientThread.close();
					}
					
					console.displayEvent(clientThread.getUser() + " connected.");
					connections.add(clientThread);
				}
				
				clientThread.start();
			}
		} catch (IOException e) {
			console.displayError("Server failed to start");
		}
	}

	/**
	 * Kicks the user with the specified username, if user is not found no
	 * errors will be thrown, nor will anything happen.
	 * 
	 * @param user
	 *            - That will be kicked, if no name is specified, the method
	 *            will return out.
	 */
	public synchronized void kickPlayer(String user) {
		if (user.equals(""))
			return;
		ClientThread toKick = getClient(user);
		if (toKick != null) {
			toKick.close();
			toKick.sendMessage("You have been kicked", "Server");
			console.displayEvent(user + " has been kicked from the server.");
		}
	}

	/**
	 * Finds and returns the client with the specified username
	 * 
	 * @param user
	 *            - To be found, can be empty.
	 * @return - The client with the username or null if not found.
	 */
	public synchronized ClientThread getClient(String user) {
		synchronized (connections) {
			for (ClientThread client : connections) {
				if (client.getUser().equals(user))
					return client;
			}
		}
		return null;
	}

	/**
	 * Sends a message to the specified receiver if they can be found, else
	 * nothing.
	 * 
	 * @param receiverUser
	 *            - The user to receiver the message.
	 * @param message
	 *            - The String message to be displayed on the clients screen.
	 * @param senderUser
	 *            - The sender of the message, to show the receiver who sent it.
	 */
	public synchronized void sendMessage(String receiverUser, String message,
			String senderUser) {
		ClientThread receiver = getClient(receiverUser);
		if (receiver != null)
			receiver.sendMessage(message, senderUser);
	}

	/**
	 * Sends a message to all connected clients at the time of sending.
	 * 
	 * @param message
	 *            - The message to be displayed to the clients.
	 * @param senderUser
	 *            - The sender to be shown to the clients.
	 */
	public synchronized void broadcastMessage(String message, String senderUser) {
		synchronized (connections) {
			for (ClientThread client : connections) {
				client.sendMessage(message, senderUser);
			}
		}
	}

	/**
	 * Calls update GUI on all connected client threads.
	 */
	public synchronized void updateGUIAll() {
		if (!(connections.size() > 0))
			return;

		synchronized (connections) {
			for (ClientThread client : connections) {
				client.updateGUI();
			}
		}
	}

	/**
	 * Updates all clients within the same location as the specified player.
	 * 
	 * @param madeUpdate
	 *            - The player who's location needs an update, can be null.
	 */
	public synchronized void updateGUI(Player madeUpdate) {
		if (madeUpdate == null)
			return;

		synchronized (connections) {
			for (ClientThread client : connections) {
				if (madeUpdate.getLocation().getPlayers()
						.contains(gameState.parsePlayer(client.getUser())))
					client.updateGUI();
			}
		}
	}

	/**
	 * Calls update invent on all clients with the same location as the
	 * specified player
	 * 
	 * @param madeUpdate
	 *            - Player who's location needs an invent update, can be null.
	 */
	private void updateInvent(Player madeUpdate) {
		if (madeUpdate == null)
			return;

		synchronized (connections) {
			for (ClientThread client : connections) {
				if (madeUpdate.getLocation().getPlayers()
						.contains(gameState.parsePlayer(client.getUser())))
					client.updateInvent();
			}
		}
	}

	/**
	 * Moves the specified users player on all clients who are in the same
	 * location as said player.
	 * 
	 * @param movingUser - The username of the player who needs to be moved, if client
	 *            does not exist this will return.
	 */
	public synchronized void movePlayer(String movingUser) {
		Player p = gameState.parsePlayer(movingUser);
		if (p == null)
			return;

		synchronized (connections) {
			for (ClientThread client : connections) {
				if (p.getLocation().getPlayers()
						.contains(gameState.parsePlayer(client.getUser())))
					client.movePlayer(movingUser);
			}

		}
	}

	/**
	 * Cycles the animation of the specified users player on all clients in that
	 * same location.
	 * 
	 * @param cycleUser
	 *            - The user who needs an animation cycle, if client does not
	 *            exist this will return.
	 */
	public synchronized void animationCycle(String cycleUser) {
		Player p = gameState.parsePlayer(cycleUser);
		if (p == null)
			return;

		synchronized (connections) {
			for (ClientThread client : connections) {
				if (p.getLocation().getPlayers().contains(gameState.parsePlayer(client.getUser())))
					client.animationCycle(cycleUser);
			}

		}
	}

	/**
	 * Takes the head of the queue and performs the necessary action depending on what type of action
	 * was removed from the queue. i.e. if a key press, game.move is called, then movePlayer() is called.
	 */
	public synchronized void processEvents() {
		NetworkEvent toProcess = eventQueue.poll();
		if (toProcess == null)
			return;

		boolean gameNeedsUpdate = false;
		boolean inventNeedsUpdate = false;
		Player p = null;
		

		switch (toProcess.getType()) {
		case KEY_PRESS:
			int hasMoved = 0;
			switch (toProcess.getKeyCode()) {
			
			case KeyEvent.VK_W:
				hasMoved = gameState.movePlayer(toProcess.getUser(),
						Direction.NORTH);
				break;
			case KeyEvent.VK_D:
				hasMoved = gameState.movePlayer(toProcess.getUser(),
						Direction.EAST);
				break;
			case KeyEvent.VK_S:
				hasMoved = gameState.movePlayer(toProcess.getUser(),
						Direction.SOUTH);
				break;
			case KeyEvent.VK_A:
				hasMoved = gameState.movePlayer(toProcess.getUser(),
						Direction.WEST);
				break;
			case KeyEvent.VK_Q:
				gameState.parsePlayer(toProcess.getUser()).changeDirection(
						toProcess.getKeyCode());
				gameNeedsUpdate = true;
				break;
			case KeyEvent.VK_E:
				gameState.parsePlayer(toProcess.getUser()).changeDirection(
						toProcess.getKeyCode());
				gameNeedsUpdate = true;
				break;
			case KeyEvent.VK_SPACE:
				gameNeedsUpdate = gameState.attackPlayer(toProcess.getUser());
				gameNeedsUpdate = true;
				break;
			case KeyEvent.VK_F:
				Container c = gameState.performAction(toProcess.getUser());
				if (c != null)
					getClient(toProcess.getUser()).displayContainer(c);
				inventNeedsUpdate = true;
				break;
			default:
				break;
			}
			
			// Player has moved but hasn't changed locations
			if (hasMoved == 1){
				movePlayer(toProcess.getUser());
			}
			// Player has moved and has changed locations
			else if (hasMoved == 2){
				gameNeedsUpdate = true;
			}
			
			break;
		case CYCLE_ANIMATIONS:
			if (this.gameState.parsePlayer(toProcess.getUser()).isAttacking()) {
				this.gameState.parsePlayer(toProcess.getUser()).getAnimation()
						.cycleAttack();
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
			p.setWeapon((Weapon) toProcess.getItem());
			inventNeedsUpdate = true;
			break;
		case SET_ARMOUR:
			p = this.gameState.parsePlayer(toProcess.getUser());
			p.setArmour((Armour) toProcess.getItem());
			inventNeedsUpdate = true;
			break;
		case REMOVE_ITEM_CONTAINER:
			p = this.gameState.parsePlayer(toProcess.getUser());
			Container c = gameState.removeItemContainer(p,
					toProcess.getIndex1(), toProcess.getContainer());
			getClient(toProcess.getUser()).displayContainer(c);
			inventNeedsUpdate = true;
			break;
		case ADD_ITEM_CONTAINER:
			p = this.gameState.parsePlayer(toProcess.getUser());
			gameState.addItemContainer(p, toProcess.getItem(), toProcess.getContainer());
			Container cont = gameState.performAction(toProcess.getUser());
			if (cont != null)
				getClient(toProcess.getUser()).displayContainer(cont);
			inventNeedsUpdate = true;
			break;
		case USE_ITEM:
			p = this.gameState.parsePlayer(toProcess.getUser());
			gameState.useItem(p, toProcess.getItem());
			inventNeedsUpdate = true;
			break;
		case DROP_ITEM:
			gameState.playerDropItem(toProcess.getUser(), toProcess.getIndex1());
			inventNeedsUpdate = true;
			break;
		case UPDATE_GAME:
			break;
		case CLOSE:
			break;
		default:
			break;
		}
		
		
		//Updates the necessary part of the client.
		if (gameNeedsUpdate)
			//updateGUIAll();
			updateGUI(gameState.parsePlayer(toProcess.getUser()));
		if (inventNeedsUpdate)
			updateInvent(gameState.parsePlayer(toProcess.getUser()));
	}

	/**
	 * Closes all connections to the server, clears the connection list and finishes all threads.
	 */
	public synchronized void stopServer() {
		for (ClientThread t : connections) {
			t.close();
		}
		synchronized (connections) {
			connections.clear();
		}
		finished = true;
		eventHandler.finish();
	}

	/**
	 * Thread to continuously call processEvents while the game is running
	 * @author Matt Byers
	 *
	 */
	public class EventThread extends Thread {
		private boolean finished = false;

		public void run() {
			while (!finished) {
				processEvents();
				try {
					//Sleeps to avoid processing glitches
					Thread.sleep(5);
				} catch (InterruptedException e) {
				}
			}
		}

		public void finish() {
			this.finished = true;
		}
	}

	/**
	 * Thread that is created for each client that connects, this deals with any updates from the clients,
	 * as well as writes updates to the client when the server requires it to.
	 * @author Matt Byers
	 *
	 */
	public class ClientThread extends Thread {

		// Socket of the associated client
		private Socket socket;

		// Username of the associated client
		private String user;

		// Socket input and output
		private ObjectInputStream input;
		private ObjectOutputStream output;

		// The last event read from the sockets input stream
		private NetworkEvent currentEvent;

		private boolean finished = false;

		public ClientThread(Socket socket) {
			console.displayEvent("Creating client thread");
			this.socket = socket;

			// Create input/output streams to the client's socket, then read the username.
			try {
				input = new ObjectInputStream(this.socket.getInputStream());
				output = new ObjectOutputStream(new BufferedOutputStream(
						this.socket.getOutputStream()));

				output.flush();

				this.user = (String) input.readObject();

			} catch (Exception e) {
				console.displayError("Failed to get input/output streams for the client: "
						+ user);
			}

			gameState.addPlayer(new Player(user, gameState));
			this.updateGUI();
			updateGUIAll();
		}

		public void movePlayer(String movingUser) {
			try {
				output.writeObject(new NetworkEvent(movingUser, gameState
						.parsePlayer(movingUser).getFacing()));
				output.reset();
				output.flush();
			} catch (IOException e) {
				console.displayError("Failed to write update to client: "
						+ user + " - " + e);
			}
		}

		public void animationCycle(String cycleUser) {
			try {
				output.writeObject(new NetworkEvent(this.user,
						NetworkEvent.EventType.CYCLE_ANIMATIONS));
				output.reset();
				output.flush();
			} catch (IOException e) {
				console.displayError("Failed to write update to client: "
						+ user + " - " + e);
			}
		}

		public void displayContainer(Container c) {
			try {

				output.writeObject(new NetworkEvent(this.user, c));
				output.reset();
				output.flush();
			} catch (IOException e) {
				console.displayError("Failed to write update to client: "
						+ user + " - " + e);
			}
		}

		public void run() {
			console.displayEvent("Client thread for " + user + " is running...");

			while (!finished) {

				try {
					currentEvent = (NetworkEvent) input.readObject();
				} catch (Exception e) {
					console.displayError("Failed to read input stream of client: "
							+ user);
					close();
				}

				if (currentEvent != null) {
					console.displayEvent("Event Received from " + user + " : "
							+ currentEvent.getType());

					if (eventQueue.size() > 50)
						eventQueue.poll();
					
					switch (currentEvent.getType()) {
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
					case USE_ITEM:
						eventQueue.add(currentEvent);
						break;
					case DROP_ITEM:
						eventQueue.add(currentEvent);
						break;
					case ADD_ITEM_CONTAINER:
						eventQueue.add(currentEvent);
						break;
					case MESSAGE:
						console.displayMessage(currentEvent.getMessage(), user);
						broadcastMessage(currentEvent.getMessage(), user);
						break;
					case CLOSE:
						if (!currentEvent.getUser().equals("Server"))
							close();
						break;
					default:
						console.displayError("Unexpected EventType from "
								+ user + " : " + currentEvent.getType());
						break;
					}
				}
			}
		}

		public synchronized void updateGUI() {
			try {
				output.writeObject(new NetworkEvent(
						gameState.parsePlayer(user),
						NetworkEvent.EventType.UPDATE_GAME));
				output.reset();
				output.flush();
			} catch (IOException e) {
				console.displayError("Failed to write update to client: "
						+ user + " - " + e);
			}
		}

		public synchronized void updateInvent() {
			try {

				output.writeObject(new NetworkEvent(
						gameState.parsePlayer(user),
						NetworkEvent.EventType.UPDATE_INVENT));
				output.reset();
				output.flush();
			} catch (IOException e) {
				console.displayError("Failed to write update to client: "
						+ user + " - " + e);
			}
		}

		public synchronized void sendMessage(String message, String senderUser) {
			try {
				output.writeObject(new NetworkEvent(message, senderUser));
				output.reset();
				output.flush();
			} catch (IOException e) {
				console.displayError("Failed to write message to client: "
						+ user);
			}
		}

		// Getters
		public String getUser() { return user; }

		public void close() {

			finished = true;

			try {
				gameState.removePlayer(gameState.parsePlayer(user));
				output.close();
				input.close();
				socket.close();
				updateGUIAll();
			} catch (Exception e) {}

		}
	}

	public static void main(String[] args) {
		new Server();
	}
}
