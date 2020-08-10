package labyrinth.contracts.communication.listeners;

import java.util.function.Consumer;
import labyrinth.contracts.communication.ISocketClient;
import labyrinth.contracts.communication.MessageType;

/**
 * A listener that handles a response one time with a certain action
 * @author Lukas Reinhardt
 * @version 1.0
 */
public class ActionListener extends SingleExecutionListenerBase
{
	//Properties
	
	private Consumer<Object> action;
	
	private MessageType messageType;
	
	//Constructors
	
	/**
	 * ActionListener constructor
	 * @param messageType The type of the response the listener should handle
	 * @param action The Action the listener should perform when a response arrives
	 */
	public ActionListener(MessageType messageType, Consumer<Object> action)
	{
		this.action = action;
		this.messageType = messageType;
	}
	
	//Methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canHandle(MessageType responseType) { return messageType == responseType; }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleResponse(Object response, ISocketClient client) 
	{ 
		super.handleResponse(response, client);
		action.accept(response); 
	}
}
