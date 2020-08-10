package labyrinth.gameServer.stateManagers;

import labyrinth.contracts.communication.dtos.requests.GameServerStatusUpdateDto;
import labyrinth.contracts.entities.lobby.GameServerEntry;
import labyrinth.contracts.entities.lobby.User;
import labyrinth.gameServer.ObjectAccessMonitor;

/**
 * ManagerClass for managing the State of the GameServer
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class GameServerEntryStateManager  
{
	//Properties
	
	private static GameServerEntry server;
	
	private static ObjectAccessMonitor<GameServerEntry> serverMonitor;
	/**
	 * Corresponding getter
	 * @return The monitor containing the lobby
	 */
	public static ObjectAccessMonitor<GameServerEntry> getServerMonitor() { return serverMonitor; }	
	
	//Constructors
	
	private GameServerEntryStateManager() {}
	
	//Methods
	
	/**
	 * Initializes the Lobby
	 * @param serverEntry The Server
	 */
	public static void initialize(GameServerEntry serverEntry) 
	{ 
		server = serverEntry;
		serverMonitor = new ObjectAccessMonitor<GameServerEntry>(server); 
	}
	
	/**
	 * Constructs the GameServerStatusUpdateDto out of the server of the lobby
	 * @return The GameServerStatusUpdateDto
	 */
	public static synchronized GameServerStatusUpdateDto createServerStatusUpdateRequest()
	{
		GameServerStatusUpdateDto request 
			= new GameServerStatusUpdateDto(server.getServerName(), server.getState());
		for(User user : server.getMembers())
			request.getMembers().add(user);
		return request;
	}
}
