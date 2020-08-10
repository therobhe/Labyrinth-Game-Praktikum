package labyrinth.contracts.communication.dtos.requests;

import labyrinth.contracts.communication.MessageType;

/**
 * Dto for @see MessageType.TurnShiftTilesDto
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class TurnShiftTilesDto extends RequestDtoBase
{
	//Properties
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageType getMessageType() { return MessageType.TurnShiftTiles; } 
	
	private int slot;
	/**
	 * Corresponding getter
	 * @return The position in which the Fields should be shifted
	 */
	public int getSlot() { return slot; }
	
	private int tileType;
	/**
	 * Corresponding getter
	 * @return The type of the field
	 */
	public int getTileType() { return tileType; }
	
	//Constructors
	
	/**
	 * TurnShiftTilesDto constructor
	 * @param slot The position in which the Fields should be shifted
	 * @param tileType The type of the field
	 */
	public TurnShiftTilesDto(int slot, int tileType)
	{
		this.slot = slot;
		this.tileType = tileType;
	}
}
