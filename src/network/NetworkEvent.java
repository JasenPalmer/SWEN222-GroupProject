package network;

import java.awt.event.KeyEvent;
import java.io.Serializable;

public class NetworkEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//The type of event that occurred.
	private final EventType type;
	
	//KeyCode of the pressed key, -1 if this is not a KEY_PRESS event.
	private final int keyCode;
	
	//Username of the client that created the NetworkEvent
	private final String user;
	
	
	/**
	 * Creates a network event of the KEY_PRESS type.
	 * @param e - The KeyEvent associated with the event.
	 */
	public NetworkEvent(KeyEvent e, String user){
		this.type = EventType.KEY_PRESS;
		this.keyCode = e.getKeyCode();
		this.user = user;
	}
	
	//Getters
	public String getUser() { return user; }
	public EventType getType() { return type; }
	public int getKeyCode() { return keyCode; }
	
	//All possible types of NetworkEvents.
	public enum EventType{
		KEY_PRESS;
	}
	
}
