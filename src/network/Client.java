package network;

import gameworld.Player;
import gameworld.entity.Container;
import gameworld.entity.Item;

import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import ui.ApplicationWindow;
import ui.panels.ChatBoxPanel;

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

	private ServerThread serverConnection;

	private Player state;

	/**
	 * Constructs a new client that will in turn create and connect a socket to the server socket at the specified host address.
	 * The Client thread will also be started to wait for updates from the server.
	 * @param host - The String address of the host to connect to, if empty the socket wont connect.
	 * @param user - The user name of the client, this is checked by ApplicationWindow
	 * @param gui - The ApplicationWindow that will display the clients gui.
	 */
	public Client (String host, String user, ApplicationWindow gui){
		this.host = host;
		this.user = user;
		this.gui = gui;

		start();

		serverConnection = new ServerThread();
		serverConnection.start();
	}

	/**
	 * Starts the client socket.
	 * Creates the input/output streams
	 * Writes the first object to the output(the username of this client).
	 */
	public void start(){

		System.out.println("Start new client: " + user);

		//Create a new Socket to the specified host's server
		try {
			socket = new Socket(host, PORT);
			socket.setReceiveBufferSize(128000);
			socket.setSendBufferSize(128000);
			socket.setTcpNoDelay(true);
			
		} catch (IOException e){
			System.err.println("Error creating new client: " + user + ". Cloisng game...");
			gui.closeAppWindow();
			return;
		}

		//Open object input and output streams for the newly created socket
		try {
			output = new ObjectOutputStream(this.socket.getOutputStream());
			//input = new ObjectInputStream(this.socket.getInputStream());
			input = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			//output = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			

			output.flush();
		} catch (IOException e){
			System.err.println("Error opening object streams for client: " + user+ ". Closing game...");
			gui.closeAppWindow();
			return;
		}

		//Write the username to the ouput
		try {
			output.writeObject(user);
			output.flush();
		} catch (IOException e) {
			System.err.println("Error writing username to output stream " + user + ". Closing game...");
			gui.closeAppWindow();
			return;
		}
	}

	/**
	 * Creates a NetworkEvent storing a KeyEvent and writes it to the output.
	 * @param event- The KeyEvent of the key pressed.
	 */
	public void registerKeyPress(KeyEvent event){
		NetworkEvent toWrite = new NetworkEvent(event, user);
		try {
			output.writeObject(toWrite);
			output.reset();
			output.flush();
		} catch (IOException e) {
			System.err.println("Failed to write key press to server");
		}
	}

	/**
	 * Creates a NetworkEvent storing a message string and writes it to the output.
	 * @param message - The message written.
	 */
	public void registerMessage(String message){
		NetworkEvent toWrite = new NetworkEvent(message, user);
		try {
			output.writeObject(toWrite);
			output.flush();
		} catch (IOException e) {
			System.err.println("Failed to write message to server");
		}
	}

	/**
	 * Creates a NetworkEvent storing
	 */
	public void cycleAnimations(){
		NetworkEvent toWrite = new NetworkEvent(this.user, NetworkEvent.EventType.CYCLE_ANIMATIONS);
		try {
			output.writeObject(toWrite);
			output.reset();
			output.flush();
		} catch (IOException e) {
			System.err.println("Failed to write animation cycle to server");
		}
	}

	public void swapItems(int index1, int index2){
		NetworkEvent toWrite = new NetworkEvent(this.user, NetworkEvent.EventType.SWAP_ITEM, index1, index2);
		try {
			output.writeObject(toWrite);
			output.reset();
			output.flush();
		} catch (IOException e) {
			System.err.println("Failed to write swap items in inventory event to server");
		}
	}

	public void removeItem(int index){
		NetworkEvent toWrite = new NetworkEvent(this.user, NetworkEvent.EventType.REMOVE_ITEM, index, -1);
		try {
			output.writeObject(toWrite);
			output.reset();
			output.flush();
		} catch (IOException e) {
			System.err.println("Failed to write remove item from inventory event to server");
		}
	}

	public void addItem(Item item){
		NetworkEvent toWrite = new NetworkEvent(this.user, NetworkEvent.EventType.ADD_ITEM, item);
		try {
			output.writeObject(toWrite);
			output.reset();
			output.flush();
		} catch (IOException e) {
			System.err.println("Failed to write add item to inventory event to server");
		}
	}

	public void setWeapon(Item item){
		NetworkEvent toWrite = new NetworkEvent(this.user, NetworkEvent.EventType.SET_WEAPON, item);
		try {
			output.writeObject(toWrite);
			output.reset();
			output.flush();
		} catch (IOException e) {
			System.err.println("Failed to write set weapon event to server");
		}
	}

	public void setArmour(Item item){
		NetworkEvent toWrite = new NetworkEvent(this.user, NetworkEvent.EventType.SET_ARMOUR, item);
		try {
			output.writeObject(toWrite);
			output.reset();
			output.flush();
		} catch (IOException e) {
			System.err.println("Failed to write set armour to server");
		}
	}

	public void removeItemContainer(int index, Container container){
		NetworkEvent toWrite = new NetworkEvent(this.user, NetworkEvent.EventType.REMOVE_ITEM_CONTAINER, index, container);
		try {
			output.writeObject(toWrite);
			output.reset();
			output.flush();
		} catch (IOException e) {
			System.err.println("Failed to write remove item from container event to server");
		}
	}
	
	public void useItem(Item item){
		NetworkEvent toWrite = new NetworkEvent(this.user, NetworkEvent.EventType.USE_ITEM, item);
		try {
			output.writeObject(toWrite);
			output.reset();
			output.flush();
		} catch (IOException e) {
			System.err.println("Failed to write use item event to server");
		}
	}
	
	public void dropItem(int index){
		NetworkEvent toWrite = new NetworkEvent(this.user, NetworkEvent.EventType.DROP_ITEM, index, -1);
		try {
			output.writeObject(toWrite);
			output.reset();
			output.flush();
		} catch (IOException e) {
			System.err.println("Failed to write drop item event to server");
		}
	}
	
	public void addItemContainer(Item item, Container container){
		NetworkEvent toWrite = new NetworkEvent(this.user, NetworkEvent.EventType.ADD_ITEM_CONTAINER, item, container);
		try {
			output.writeObject(toWrite);
			output.reset();
			output.flush();
		} catch (IOException e) {
			System.err.println("Failed to write add item container event to server");
		}
	}
	

	public void close(){
		serverConnection.finish();

		try{
			output.writeObject(new NetworkEvent(user, NetworkEvent.EventType.CLOSE));
			output.flush();
			output.close();
			input.close();
			socket.close();
		}
		catch(IOException e){
			System.err.println("Error closing the client socket: " + e);
		}
	}

	//Getters
	public Player getState(){ return state; }

	public class ServerThread extends Thread {
		private boolean finished = false;

		public void run() {
			while(!finished) {
				NetworkEvent event = null;

				try {
					event = (NetworkEvent)input.readObject();
				}
				catch (IOException e){
					System.err.println("Connection to the server has been interrupted, closing game...");
					gui.closeAppWindow();
					close();
				}
				catch(NullPointerException e) {
					System.err.println("Connection to the server could not be established, closing game...");
					gui.closeAppWindow();
					close();
				}
				catch (ClassNotFoundException e) {}
				

				if(event == null) return;
				
				Set<Player> tempPlayers;
				
				switch(event.getType()){
				case UPDATE_GAME:
					if(event.getState() != null) state = event.getState();
					break;
				case UPDATE_INVENT:
					System.out.println("Received update invent event");
					if(event.getState() != null) state = event.getState();
					if(gui.getInventPanel() != null) gui.getInventPanel().populateInventArray();
					break;
				case MOVE_PLAYER:
					tempPlayers = new HashSet<Player>();
					tempPlayers.addAll(state.getLocation().getPlayers());
					synchronized(tempPlayers){
						for(Player p : tempPlayers){
							if(p.getName().equals(event.getUser())){
								p.move(event.getDir(), true);
							}
						}
					}
					break;
				case CYCLE_ANIMATIONS:
					tempPlayers = state.getLocation().getPlayers();
					synchronized(tempPlayers){
						for(Player p : tempPlayers){
							if(p.getName().equals(event.getUser())){
								p.getAnimation().cycleAttack();
							}
						}
					}
					break;
				case DISPLAY_CONTAINER:
					gui.openContainer(event.getContainer());
					break;
				case MESSAGE:
					ChatBoxPanel chatBox = gui.getChatBox();
					chatBox.displayMessage(event.getUser(), event.getMessage());
					break;
				case CLOSE:
					if(event.getUser().equals("Server")) close();
					break;
				case KEY_PRESS:
					break;
				default:
					break;
				}
			}
		}
		public void finish(){
			finished = true;
		}
	}

}
