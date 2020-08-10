package labyrinth.contracts.communication.dtos.requests;

import labyrinth.contracts.communication.MessageType;

/**
 * Dto for @see MessageType.ChangeColor
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ChangeColorDto extends RequestDtoBase
{
	//Properties
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageType getMessageType() { return MessageType.ChangeColor; }
	
	private String color;
	/**
	 * Corresponding getter
	 * @return The new color
	 */
	public String getColor() { return color; }
	
	//Constructors
	
	/**
	 * ChangeColorDto constructor
	 * @param color The new color
	 */
	public ChangeColorDto(String color)  { this.color = color; }
}
