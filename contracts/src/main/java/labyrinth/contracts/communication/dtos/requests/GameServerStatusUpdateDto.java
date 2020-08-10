package labyrinth.contracts.communication.dtos.requests;

import java.util.ArrayList;
import java.util.List;
import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.entities.lobby.GameServerStatus;
import labyrinth.contracts.entities.lobby.User;

/**
 * Dto for @see MessageType.GameServerStatusUpdate
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class GameServerStatusUpdateDto extends RequestDtoBase
{
	//Properties
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageType getMessageType() { return MessageType.GameServerStatusUpdate; }

	//Properties
	
	private String serverName;
	/**
	 * Corresponding getter
	 * @return The name of the server
	 */
	public String getServerName() { return serverName; }
	
	private GameServerStatus state;
	/**
	 * Corresponding getter
	 * @return The state of the server
	 */
	public GameServerStatus getState() { return state; }
	
	List<User> members = new ArrayList<User>();
	/**
	 * Corresponding getter
	 * @return The members of the server
	 */
	public List<User> getMembers() { return members; }
	
	//Constructors
	
	/**
	 * GameServerRegisterDto constructor
	 * @param serverName Name of the GameServer
	 * @param state State of the GameServer
	 */
	public GameServerStatusUpdateDto(String serverName, GameServerStatus state)
	{
		this.serverName = serverName;
		this.state = state;
	}
}
