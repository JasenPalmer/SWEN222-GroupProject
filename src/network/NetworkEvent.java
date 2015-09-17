package network;

import java.awt.event.KeyEvent;
import java.io.Serializable;

/**
 * 
 * @author Matt Byers
 *
 */
public class NetworkEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//The type of event that occurred.
	private final EventType type;
	
	//KeyCode of the pressed key, -1 if this is not a KEY_PRESS event.
	private final int keyCode;
	
	//Message, null if not a MESSAGE event.
	private final String message;
	
	//Username of the client that created the NetworkEvent
	private final String user;
	
	
	/**
	 * Creates a network event of the KEY_PRESS type.
	 * @param e - The KeyEvent associated with the event.
	 * @param user - The user that created the event.
	 */
	public NetworkEvent(KeyEvent e, String user){
		this.type = EventType.KEY_PRESS;
		this.keyCode = e.getKeyCode();
		this.user = user;
		
		this.message = null;
	}
	
	/**
	 * Creates a network event of the type MESSAGE.
	 * @param msg - The message.
	 * @param user - The user that created the event.
	 */
	public NetworkEvent(String msg, String user){
		this.type = EventType.MESSAGE;
		this.message = msg;
		this.user = user;
		
		this.keyCode = -1;
	}
	
	/**
	 * Network event with no parameters signals a server event,
	 * tells the clients GUI to update.
	 */
	public NetworkEvent(){
		this.type = EventType.UPDATE_GUI;
		this.user = "Server";
		
		this.keyCode = -1;
		this.message = null;
	}
	
	//Getters
	public String getUser() { return user; }
	public EventType getType() { return type; }
	public int getKeyCode() { return keyCode; }
	public String getMessage() { return message; }
	
	//All possible types of NetworkEvents.
	public enum EventType{
		KEY_PRESS,
		MESSAGE,
		UPDATE_GUI;
	}
	
}
