package labyrinth.contracts.communication;

import java.util.ArrayList;
import java.util.List;
import labyrinth.contracts.communication.dtos.responses.ResponseDtoBase;

/**
 * Mock for @see ISocketServer
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class SocketServerMock<ConnectionT extends IServerConnection> implements ISocketServer<ConnectionT>
{
	//Properties
	
	private ResponseDtoBase broadcastedResponse;
	/**
	 * Corresponding getter
	 * @return The last request broadcasted
	 */
	public ResponseDtoBase getBroadcastedResponse() { return broadcastedResponse; }
	
	private boolean wasClosed;
	/**
	 * Corresponding getter
	 * @return If the server was closed
	 */
	public boolean getWasClosed() { return wasClosed; }
	
	/**
	 * {@inheritDoc}
	 */
	public List<ConnectionT> getConnections() { return new ArrayList<ConnectionT>(); }

	//Methods
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() { wasClosed = true; }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start() { }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <ResponseT extends ResponseDtoBase> void broadcast(ResponseT response) { broadcastedResponse = response; }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <ResponseT extends ResponseDtoBase> void broadcast(ResponseT response, IServerConnection excluded) 
	{ broadcast(response); }
}
