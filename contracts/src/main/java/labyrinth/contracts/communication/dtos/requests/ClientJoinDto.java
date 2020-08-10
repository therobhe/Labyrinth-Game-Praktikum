package labyrinth.contracts.communication.dtos.requests;

import labyrinth.contracts.communication.MessageType;

/**
 * Dto for @see MessageType.ClientJoin
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ClientJoinDto extends RequestDtoBase
{
	//Properties
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageType getMessageType() { return MessageType.ClientJoin; }
	
	
	private String name;
	/**
	 * Corresponding getter
	 * @return The user name
	 */
	public String getName() { return name; }
	
	private int userID;
	/**
	 * Corresponding getter
	 * @return The user Id
	 */
	public int getUserID() { return userID; }
	
	//Constructors
	
	/**
	 * CheckUserIdDto constructor
	 * @param name The name of the user
	 * @param userID The user Id
	 */
	public ClientJoinDto(String name, int userID)
	{ 
		this.userID = userID; 
		this.name = name;
	}
}
