package labyrinth.contracts.communication.dtos.requests;

import labyrinth.contracts.communication.MessageType;

/**
 * Dto for a Request without content
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class EmptyRequestDto extends RequestDtoBase
{
	//Properties
	
	private MessageType messageType;
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageType getMessageType() { return messageType; }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getContent() { return null; }
	
	//Constructors
	
	/**
	 * EmptyRequestDto constructor
	 * @param messageType MessageType of the request
	 */
	public EmptyRequestDto(MessageType messageType) { this.messageType = messageType; }
}
