package labyrinth.contracts.communication.dtos.requests;

import labyrinth.contracts.communication.MessageType;

/**
 * Dto for @see MessageType.CheckUID
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class CheckUserIdDto extends RequestDtoBase
{
	//Properties
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageType getMessageType() { return MessageType.CheckUID; }
	
	
	private int userID;
	/**
	 * Corresponding getter
	 * @return The user Id
	 */
	public int getUserID() { return userID; }
	
	//Constructors
	
	/**
	 * CheckUserIdDto constructor
	 * @param userID The user Id
	 */
	public CheckUserIdDto(int userID) { this.userID = userID; }
}
