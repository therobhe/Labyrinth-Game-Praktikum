package labyrinth.contracts.communication.logic;

import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.requests.RequestDtoBase;

/**
 * WrapperClass for logic for a specific Request
 * @param <RequestT> Type of the Request
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class LogicWrapper<RequestT extends RequestDtoBase>  
{
	//Properties
	
	private Class<RequestT> requestType;
	
	private MessageType messageType;
	/**
	 * Corresponding getter
	 * @return The type of the request
	 */
	public MessageType getMessageType() { return messageType; }
	
	private LogicBase<RequestT> logic;
	
	//Constructors
	
	/**
	 * LogicWrapper constructor
	 * @param messageType Type of the Request
	 * @param logic The logic handling the request
	 */
	public LogicWrapper(MessageType messageType, Class<RequestT> requestType, LogicBase<RequestT> logic) 
	{
		this.messageType = messageType;
		this.requestType = requestType;
		this.logic = logic;
	}
	
	//Methods
	
	/**
	 * Executes the contained Logic
	 * @param request The request for which the logic should be executed
	 */
	public void execute(RequestDtoBase request) { logic.execute(requestType.cast(request));}
}
