package labyrinth.gameServer.stateManagers;

import labyrinth.contracts.communication.dtos.responses.UpdateLobbyResponseDto;
import labyrinth.contracts.entities.lobby.Lobby;
import labyrinth.contracts.entities.lobby.LobbyUser;
import labyrinth.gameServer.ObjectAccessMonitor;

/**
 * ManagerClass for managing the State of the Lobby of the GameServer
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class LobbyStateManager 
{
	//Properties
	
	private static Lobby lobby;
	
	private static ObjectAccessMonitor<Lobby> lobbyMonitor;
	/**
	 * Corresponding getter
	 * @return The monitor containing the lobby
	 */
	public static ObjectAccessMonitor<Lobby> getLobbyMonitor() { return lobbyMonitor; }	
	
	//Constructors
	
	private LobbyStateManager() {}
	
	//Methods
	
	/**
	 * Initializes the state manager 
	 */
	public static void initialize()
	{
		lobby = new Lobby();
		lobbyMonitor = new ObjectAccessMonitor<Lobby>(lobby);
	}
	
	/**
	 * Constructs theUpdateLobbyResponseDto out of the server of the lobby
	 * @return The UpdateLobbyResponseDto
	 */
	public static synchronized UpdateLobbyResponseDto createUpdateLobbyResponseDto()
	{
		UpdateLobbyResponseDto response = new UpdateLobbyResponseDto();
		for(LobbyUser player : lobby.getPlayerList())
			response.getMembers().add(player);
		for(LobbyUser spectator : lobby.getSpectatorList())
			response.getMembers().add(spectator);
		return response;
	}
}
