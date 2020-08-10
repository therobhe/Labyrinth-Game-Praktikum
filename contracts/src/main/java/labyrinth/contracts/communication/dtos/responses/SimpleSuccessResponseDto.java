package labyrinth.contracts.communication.dtos.responses;

import labyrinth.contracts.communication.MessageType;

/**
 * Dto for a simple response with a success-flag
 * @author Lukas Reinhardt
 * @version 1.0
 */
public class SimpleSuccessResponseDto extends ResponseDtoBase
{
	//Properties
	
	private transient MessageType messageType;
	/**
	 * Corresponding getter
	 * @return The Type of the message
	 */
	public MessageType getMessageType() { return messageType; }
	
	private boolean success;
	/**
	 * Corresponding getter
	 * @return If the execution of the request was successful
	 */
	public boolean getSuccess() { return success; }
	
	//Constructors
	
	/**
	 * SimpleSuccessResultDto constructor
	 * @param success Success-Flag
	 * @param messageType The Type of the message
	 */
	public SimpleSuccessResponseDto(MessageType messageType, boolean success) 
	{ 
		this.success = success; 
		this.messageType = messageType;
	}
}
