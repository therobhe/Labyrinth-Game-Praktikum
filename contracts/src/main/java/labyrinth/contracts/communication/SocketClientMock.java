package labyrinth.contracts.communication;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import labyrinth.contracts.communication.dtos.requests.RequestDtoBase;
import labyrinth.contracts.communication.dtos.responses.ResponseDtoBase;
import labyrinth.contracts.communication.listeners.ResponseListenerBase;

/**
 * Mock for @see ISocketClient
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class SocketClientMock implements ISocketClient
{
	//Properties
	
	private boolean wasClosed;
	/** 
	 * Corresponding getter
	 * @return If the client was closed
	 */
	public boolean getWasClosed() { return wasClosed; }
	
	private RequestDtoBase lastRequest;
	/** 
	 * Corresponding getter
	 * @return The last Request sent
	 */
	public RequestDtoBase getLastRequest() { return lastRequest; }
	
	private List<ResponseListenerBase> listeners = new ArrayList<ResponseListenerBase>(); 
	/**
	 * Gets the list of listeners for a specific MessageType
	 * @param messageType The MessagType of the listeners
	 * @return The list of listeners
	 */
	public List<ResponseListenerBase> getListeners(MessageType messageType) 
	{ return listeners.stream().filter(listener -> listener.canHandle(messageType)).collect(Collectors.toList()); }
	
	//Constructors
	
	/**
	 * SocketClientMock constructor
	 */
	public SocketClientMock() { }
	
	/**
	 * SocketClientMock constructor
	 * @param ipAdress IP-Address of the target server
	 * @param port Port number of the target server
	 */
	public SocketClientMock(String ipAdress, int port) { }
	
	//Methods
	
	/** 
	 * Cleans up the mock
	 */
	public void cleanup() { listeners.clear(); }
	
	/** 
	 * Mock method for simulating the receiving of a response
	 * @param response The response
	 */
	public void sendResponse(ResponseDtoBase response)
	{
		List<ResponseListenerBase> listenersCopy = new ArrayList<ResponseListenerBase>(listeners);
		ResponseWrapper wrapper = new ResponseWrapper(response);
		for(ResponseListenerBase listener : listenersCopy)
			if(listener.canHandle(response.getMessageType()))
				listener.handleResponse(wrapper.getContent(), this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <ContentT, RequestT extends RequestDtoBase> void sendRequest(RequestT request) 
	{ lastRequest = request; }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerListener(ResponseListenerBase listener) { listeners.add(listener); }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unregisterListener(ResponseListenerBase listener) { listeners.remove(listener); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() { wasClosed = true; }
}
