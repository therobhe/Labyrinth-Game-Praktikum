package labyrinth.contracts.communication.listeners;

import labyrinth.contracts.communication.ISocketClient;

/**
 * Base-class for all listeners that only listen for one response
 * @author Lukas Reinhardt
 * @version 1.0
 */
public abstract class SingleExecutionListenerBase extends ResponseListenerBase
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleResponse(Object response, ISocketClient client) { client.unregisterListener(this); }
}
