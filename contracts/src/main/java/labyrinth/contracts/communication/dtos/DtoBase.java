package labyrinth.contracts.communication.dtos;

import labyrinth.contracts.communication.MessageType;

/**
 * Base-class for all Dtos
 * @author Lukas Reinhardt
 * @version 1.0
 */
public abstract class DtoBase 
{
	/**
	 * Returns the actual content which is supposed to be sent
	 * @return The actual content which is supposed to be sent
	 */
	public Object getContent() { return this; }
	
	/**
	 * Returns the MessageType of the message
	 * @return The MessageType of the message
	 */
	public abstract MessageType getMessageType(); 
}
