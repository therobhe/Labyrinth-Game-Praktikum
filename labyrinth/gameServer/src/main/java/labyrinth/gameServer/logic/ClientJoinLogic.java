package labyrinth.gameServer.logic;

import labyrinth.contracts.communication.ISocketClient;
import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.requests.CheckUserIdDto;
import labyrinth.contracts.communication.dtos.requests.ClientJoinDto;
import labyrinth.contracts.communication.dtos.responses.SimpleSuccessResponseDto;
import labyrinth.contracts.communication.dtos.responses.SuccessMessageResponseDto;
import labyrinth.contracts.communication.listeners.ActionListener;
import labyrinth.contracts.communication.logic.LogicBase;
import labyrinth.contracts.entities.lobby.Lobby;
import labyrinth.contracts.entities.lobby.LobbyUser;
import labyrinth.contracts.entities.lobby.LobbyUserKind;
import labyrinth.contracts.entities.lobby.UserPermission;
import labyrinth.gameServer.GameServerManager;
import labyrinth.gameServer.IGameServerConnection;
import labyrinth.gameServer.ObjectAccessMonitor;
import labyrinth.gameServer.stateManagers.GameServerEntryStateManager;
import labyrinth.gameServer.stateManagers.LobbyStateManager;

/**
 * Logic for a ClientJoin-Request
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ClientJoinLogic extends LogicBase<ClientJoinDto>
{
	//Properties
	
	private ISocketClient registrationServerClient;
	
	private String userName;
	
	private ObjectAccessMonitor<Lobby> lobbyMonitor = LobbyStateManager.getLobbyMonitor();
	
	//Constructor
	
	/**
	 * ClientJoinLogic constructor
	 * @param connection The Connection which called the logic
	 */
	public ClientJoinLogic(IGameServerConnection connection) { super(connection); }
	
	//Methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(ClientJoinDto request) 
	{
		ActionListener listener = new ActionListener(MessageType.CheckUID, (response) -> onResponseArrived(response));
		registrationServerClient = GameServerManager.getRunningServer().getRegistrationServerClient();
		IGameServerConnection.class.cast(getConnection()).setUserId(request.getUserID());
		userName = request.getName();
		
		//Check the UserId of the user
		registrationServerClient.registerListener(listener);
		CheckUserIdDto checkUserIdRequest = new CheckUserIdDto(request.getUserID());
		registrationServerClient.sendRequest(checkUserIdRequest);	
	}
	
	private void onResponseArrived(Object response)
	{
		SimpleSuccessResponseDto content = SimpleSuccessResponseDto.class.cast(response);
		if(content.getSuccess()) //User with id does not exists
		{
			//Add user to Lobby and send status update of the GameServer
			if(lobbyMonitor.executeFunction((lobby -> tryAddNewUser(lobby))))
			{
				registrationServerClient.sendRequest(GameServerEntryStateManager.createServerStatusUpdateRequest());
				getConnection().sendResponse(new SuccessMessageResponseDto(MessageType.ClientJoin, true, null));
				GameServerManager.getRunningServer().broadcastResponse(LobbyStateManager.createUpdateLobbyResponseDto());
			}
		}			
		else //User with id does not exists
			onConnectionDenied("Invalide Nutzer-ID.");		
	}
		
	private boolean tryAddNewUser(Lobby lobby)
	{
		//Check if the user name already exists
		if(lobby.doesUserExists(userName))
		{
			onConnectionDenied("Ein Nutzer mit dem Nutzernamen existiert bereits.");
			return false;
		}
		
		//The first user is automatically the admin
		UserPermission permission = lobby.getPlayerList().size() == 0 
			&& lobby.getSpectatorList().size() == 0 ? UserPermission.ADMIN : UserPermission.DEFAULT;
		//If the player limit is reached the new user gets the role of a spectator
		LobbyUserKind userKind = lobby.getPlayerList().size() < 4 ? LobbyUserKind.PLAYER : LobbyUserKind.SPECTATOR;
		
		LobbyUser user = new LobbyUser(userName, null, permission, userKind);
		IGameServerConnection.class.cast(getConnection()).setUser(user);
			
		//Add user
		if(userKind == LobbyUserKind.PLAYER)
			lobby.getPlayerList().add(user);
		else
			lobby.getSpectatorList().add(user);
		GameServerEntryStateManager.getServerMonitor().executeAction(server -> server.getMembers().add(user));
		return true;
	}
	
	private void onConnectionDenied(String message)
	{
		getConnection().sendResponse(new SuccessMessageResponseDto(MessageType.ClientJoin, false, message));
		getConnection().cancel();
	}
}
