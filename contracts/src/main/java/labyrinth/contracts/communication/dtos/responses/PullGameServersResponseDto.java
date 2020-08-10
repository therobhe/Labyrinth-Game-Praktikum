package labyrinth.contracts.communication.dtos.responses;

import java.util.ArrayList;
import java.util.List;
import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.entities.lobby.GameServerEntry;

/**
 * Dto for @see MessageType.PullGameServers
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class PullGameServersResponseDto extends ResponseDtoBase
{	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageType getMessageType() { return MessageType.PullGameServers; } 
	
	private List<GameServerEntry> items = new ArrayList<GameServerEntry>();
	/**
	 * Corresponding getter
	 * @return The list of GameServers
	 */
	public List<GameServerEntry> getItems() { return items; }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getContent() { return items; }
}
