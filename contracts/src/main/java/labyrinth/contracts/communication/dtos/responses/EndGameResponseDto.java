package labyrinth.contracts.communication.dtos.responses;

import java.util.ArrayList;
import java.util.List;
import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.PlayerDtoBase;

/**
 * Dto for a response to @see MessageType.EndGame
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class EndGameResponseDto extends ResponseDtoBase
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageType getMessageType() { return MessageType.EndGame; }
	
	private List<PlayerDtoBase> players = new ArrayList<PlayerDtoBase>();
	/**
	 * Corresponding getter
	 * @return The player of the game
	 */
	public List<PlayerDtoBase> getPlayers() { return players; }
}
