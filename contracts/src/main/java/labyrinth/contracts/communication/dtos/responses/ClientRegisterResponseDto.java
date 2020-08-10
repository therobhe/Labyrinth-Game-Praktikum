package labyrinth.contracts.communication.dtos.responses;

import labyrinth.contracts.communication.MessageType;

/**
 * Dto for @see MessageType.ClientRegister
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ClientRegisterResponseDto extends ResponseDtoBase
{
	//Properties
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageType getMessageType() { return MessageType.ClientRegister; }
	
	private int userID;
	/**
	 * Corresponding getter
	 * @return The user Id
	 */
	public int getUserID() { return userID; }
	
	//Constructors
	
	/**
	 * ClientRegisterDto constructor
	 * @param userID The user Id
	 */
	public ClientRegisterResponseDto(int userID) { this.userID = userID; }
}
