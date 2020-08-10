package labyrinth.contracts.communication.dtos.responses;

import labyrinth.contracts.communication.MessageType;

/**
 * Dto for a response to @see MessageType.CurrentPlayer
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class CurrentPlayerResponseDto extends ResponseDtoBase
{
	//Properties
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageType getMessageType() { return MessageType.CurrentPlayer; }
	
	private String player;
	/**
	 * Corresponding getter
	 * @return Name of the player which turn it is
	 */
	public String getPlayer() { return player; }
		
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getContent() { return player; }
	
	//Constructors
	
	/**
	 * CurrentPlayerResponseDto constructor
	 * @param player Name of the player which turn it is
	 */
	public CurrentPlayerResponseDto(String player) { this.player = player; }
}
