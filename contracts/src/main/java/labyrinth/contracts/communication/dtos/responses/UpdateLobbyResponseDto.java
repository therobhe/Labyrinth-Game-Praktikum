package labyrinth.contracts.communication.dtos.responses;

import java.util.ArrayList;
import java.util.List;
import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.entities.lobby.LobbyUser;

/**
 * Dto for @see MessageType.UpdateLobby
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class UpdateLobbyResponseDto extends ResponseDtoBase
{	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageType getMessageType() { return MessageType.UpdateLobby; }
	
	private List<LobbyUser> members = new ArrayList<LobbyUser>();
	/**
	 * Corresponding getter
	 * @return The members of the lobby
	 */
	public List<LobbyUser> getMembers() { return members;}
}
