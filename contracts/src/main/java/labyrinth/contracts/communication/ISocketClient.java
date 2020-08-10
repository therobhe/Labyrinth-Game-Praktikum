package labyrinth.contracts.communication;

import labyrinth.contracts.communication.dtos.requests.RequestDtoBase;
import labyrinth.contracts.communication.listeners.ResponseListenerBase;

/**
 * Interface for a SocketClient
 * @author Lukas Reinhardt
 * @version 1.0
 */
public interface ISocketClient extends AutoCloseable
{	
	/**
	 * Sends a request to the target
	 * @param request The request to send
	 */
	public <ContentT, RequestT extends RequestDtoBase> void sendRequest(RequestT request);
	
	/**
	 * Registers a listener for a specific message type
	 * @param listener The listener tom listen for a message from the Server
	 */
	public void registerListener(ResponseListenerBase listener);
	
	/**
	 * Unregisters a listener for a specific message type
	 * @param listener The listener tom listen for a message from the Server
	 */
	public void unregisterListener(ResponseListenerBase listener);
}
