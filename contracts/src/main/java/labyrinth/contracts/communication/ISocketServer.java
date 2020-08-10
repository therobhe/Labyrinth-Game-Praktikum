package labyrinth.contracts.communication;

import java.util.List;
import labyrinth.contracts.communication.dtos.responses.ResponseDtoBase;

/**
 * Interface for a SocketServer
 * @author Lukas Reinhardt
 * @version 1.0
 */
public interface ISocketServer<ServerConnectionT extends IServerConnection> extends AutoCloseable
{
	//Properties
	
	/**
	 * Corresponding getter
	 * @return The connected clients
	 */
	public List<ServerConnectionT> getConnections();
	
	//Methods
	
	/**
	 * Starts the actual server
	 */
	public void start();
	
	/**
	 * Sends a response to all clients
	 * @param <ResponseT> Type of the response
	 * @param response The Response to send to all clients
	 */
	public <ResponseT extends ResponseDtoBase> void broadcast(ResponseT response);
	
	/**
	 * Sends a response to all clients
	 * @param <ResponseT> Type of the response
	 * @param response The Response to send to all clients
	 * @param excluded The client which should be excluded from the broadcast
	 */
	public <ResponseT extends ResponseDtoBase> void broadcast(ResponseT response, IServerConnection excluded);
}
