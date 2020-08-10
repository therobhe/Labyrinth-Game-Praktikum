package labyrinth.contracts.communication.listeners;

import labyrinth.contracts.communication.ISocketClient;
import labyrinth.contracts.communication.MessageType;

/**
 * Base-class for all listeners for a response
 * @author Lukas Reinhardt
 * @version 1.0
 */
public abstract class ResponseListenerBase 
{	
	/**
	 * Returns whether the handler can handle a response of that type
	 * @param responseType The type of the response
	 * @return If the Listener can handle the response
	 */
	public abstract boolean canHandle(MessageType responseType);
	
	/**
	 * Handles the response
	 * @param response The response
	 */
	public abstract void handleResponse(Object response, ISocketClient client);
}
