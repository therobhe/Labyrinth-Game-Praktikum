package labyrinth.contracts.communication.dtos.responses;

import labyrinth.contracts.communication.MessageType;

/**
 * Dto for a Response without content
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class EmptyResponseDto extends ResponseDtoBase 
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
	 * EmptyResponseDto constructor
	 * @param messageType MessageType of the request
	 */
	public EmptyResponseDto(MessageType messageType) { this.messageType = messageType; }
}
