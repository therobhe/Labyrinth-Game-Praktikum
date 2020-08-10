package labyrinth.contracts.communication.dtos.responses;

import labyrinth.contracts.communication.MessageType;

/**
 * Dto for a response with a success-flag and a message
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class SuccessMessageResponseDto extends SimpleSuccessResponseDto
{
	//Properties
	
	private String message;
	/**
	 * Corresponding getter
	 * @return The message of the response
	 */
	public String getMessage() { return message; }
	
	//Constructors

	/**
	 * SuccessMessageResultDto constructor
	 * @param success Success-Flag
	 * @param message The message of the response
	 * @param messageType The Type of the message
	 */
	public SuccessMessageResponseDto(MessageType messageType, boolean success, String message) 
	{
		super(messageType, success);
		this.message = message;
	}
}
