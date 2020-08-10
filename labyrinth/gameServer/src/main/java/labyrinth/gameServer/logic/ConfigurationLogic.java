package labyrinth.gameServer.logic;

import java.util.stream.Stream;
import labyrinth.contracts.communication.dtos.requests.ConfigurationDto;
import labyrinth.contracts.communication.dtos.requests.GameServerStatusUpdateDto;
import labyrinth.contracts.communication.dtos.responses.ConfigurationResponseDto;
import labyrinth.contracts.communication.dtos.responses.UpdateLobbyResponseDto;
import labyrinth.contracts.communication.logic.LogicBase;
import labyrinth.contracts.entities.lobby.GameConfiguration;
import labyrinth.contracts.entities.lobby.GameServerEntry;
import labyrinth.contracts.entities.lobby.Lobby;
import labyrinth.contracts.entities.lobby.LobbyUser;
import labyrinth.contracts.entities.lobby.UserPermission;
import labyrinth.gameServer.GameServerManager;
import labyrinth.gameServer.IGameServerConnection;
import labyrinth.gameServer.stateManagers.GameConfigurationStateManager;
import labyrinth.gameServer.stateManagers.GameServerEntryStateManager;
import labyrinth.gameServer.stateManagers.LobbyStateManager;

/**
 * Logic for a ConfigurationLogic-Request
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ConfigurationLogic extends LogicBase<ConfigurationDto>
{	
	//Constructor
	
	/**
	 * ConfigurationLogic constructor
	 * @param connection The Connection which called the logic
	 */
	public ConfigurationLogic(IGameServerConnection connection) { super(connection); }
	
	//Methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(ConfigurationDto request) 
	{		
		//Only allow configuration changes if the user is the admin
		if(IGameServerConnection.class.cast(getConnection()).getUser().getPermission() == UserPermission.DEFAULT)
			return;
		
		//Try to update the name of the server
		if(request.getServerName() != null)
			updateServerName(request.getServerName());
		
		//Try to update the admin of the server
		if(request.getAdmin() != null)
			updateAdmin(request.getAdmin());
		
		//Update Configuration
		if(request.getBoni() != null|| request.getBoniProbability() != null || request.getTreasureCount() != null 
			|| request.getGameLengthLimit() != null || request.getSize() != null || request.getTurnLengthLimit() != null)
		GameConfigurationStateManager.getConfigurationMonitor()
			.executeAction(configuration1 -> updateConfiguration(configuration1, request));
	}
	
	private void updateServerName(String serverName)
	{
		boolean wasNameChanged = GameServerEntryStateManager.getServerMonitor()
			.executeFunction(server -> tryUpdateServerName(server, serverName));
		if(wasNameChanged)
		{
			GameServerStatusUpdateDto statusUpdateRequest = GameServerEntryStateManager.createServerStatusUpdateRequest();
			GameServerManager.getRunningServer().getRegistrationServerClient().sendRequest(statusUpdateRequest);
		}
	}
	
	private boolean tryUpdateServerName(GameServerEntry server, String newName)
	{
		if(server.getServerName() == newName)
			return false;
		server.setServerName(newName);
		return true;
	}
	
	private void updateAdmin(String userName)
	{
		boolean wasAdminChanged = LobbyStateManager.getLobbyMonitor()
			.executeFunction(lobby -> tryUpdateAdmin(lobby, userName));
		if(wasAdminChanged)
		{
			UpdateLobbyResponseDto response = LobbyStateManager.createUpdateLobbyResponseDto();
			GameServerManager.getRunningServer().broadcastResponse(response);
		}
	}
	
	private boolean tryUpdateAdmin(Lobby lobby, String userName)
	{
		Stream<LobbyUser> allUsers = Stream.concat(lobby.getPlayerList().stream(), lobby.getSpectatorList().stream());
		LobbyUser oldAdmin = allUsers.filter(user -> user.getPermission() == UserPermission.ADMIN).findFirst().get();
		//Java stream does not support multiple iterations like IEnumerable(C#)
		allUsers = Stream.concat(lobby.getPlayerList().stream(), lobby.getSpectatorList().stream()); 
		LobbyUser newAdmin = allUsers.filter(user -> user.getName() == userName).findFirst().get();
		
		if(oldAdmin == newAdmin)
			return false;
		oldAdmin.setPermission(UserPermission.DEFAULT);
		newAdmin.setPermission(UserPermission.ADMIN);
		return true;
	}
	
	private void updateConfiguration(GameConfiguration configuration, ConfigurationDto request)
	{
		configuration.setBoni(request.getBoni());
		configuration.setBoniProbability(request.getBoniProbability());
		configuration.setGameLengthLimit(request.getGameLengthLimit());
		configuration.setTurnLengthLimit(request.getTurnLengthLimit());
		configuration.setSize(request.getSize());
		configuration.setTreasureCount(request.getTreasureCount());
		GameServerManager.getRunningServer().broadcastResponse(new ConfigurationResponseDto(configuration), getConnection());
	}
}
