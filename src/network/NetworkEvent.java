package network;

import java.awt.event.KeyEvent;
import java.io.Serializable;

public class NetworkEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//The type of event that occurred.
	private final EventType type;
	
	//KeyCode of the pressed key, -1 if this is not a KEY_PRESS event.
	private final int keyCode;
	
	
	/**
	 * Creates a network event of the KEY_PRESS type.
	 * @param e - The KeyEvent associated with the event.
	 */
	public NetworkEvent(KeyEvent e){
		this.type = EventType.KEY_PRESS;
		this.keyCode = e.getKeyCode();
	}
	
	//All possible types of NetworkEvents.
	public enum EventType{
		KEY_PRESS;
	}
	
}
