package labyrinth.contracts.communication.dtos.requests;

import labyrinth.contracts.communication.MessageType;

/**
 * Dto for @see MessageType.TurnMovePlayer
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class TurnMovePlayerDto extends RequestDtoBase
{
	//Properties
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageType getMessageType() { return MessageType.TurnMovePlayer; } 
	
	private int targetX;
	/**
	 * Corresponding getter
	 * @return x-coordinate
	 */
	public int getTargetX() { return targetX; }
	
	private int targetY;
	/**
	 * Corresponding getter
	 * @return y-coordinate
	 */
	public int getTargetY() { return targetY; }
	
	//Constructors
	
	/**
	 * TurnMovePlayerDto constructor
	 * @param targetX y-coordinate
	 * @param targetY y-coordinate
	 */
	public TurnMovePlayerDto(int targetX, int targetY)
	{
		this.targetX = targetX;
		this.targetY = targetY;
	}
}
