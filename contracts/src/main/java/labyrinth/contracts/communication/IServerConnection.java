package labyrinth.contracts.communication;

import labyrinth.contracts.communication.dtos.responses.ResponseDtoBase;

/**
 * Interface for a Connection of a client to a server
 * @author Lukas Reinhardt
 * @version 1.0
 */
public interface IServerConnection 
{
	/**
	 * Sends a response to the connected client
	 * @param <ResponseT> Type of the response
	 * @param response The response to send
	 */
	public <ResponseT extends ResponseDtoBase> void sendResponse(ResponseT response);
	
	/**
	 * Starts the connection
	 */
	public void start();
	
	/**
	 * Cancels the Connection with the client
	 */
	public void cancel();
	
    /**
     * Corresponding getter
     * @return The IP-Address of the client
     */
    public String getIp();
}
