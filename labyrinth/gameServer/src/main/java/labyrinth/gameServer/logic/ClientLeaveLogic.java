package labyrinth.gameServer.logic;

import labyrinth.contracts.communication.dtos.requests.EmptyRequestDto;
import labyrinth.contracts.communication.logic.LogicBase;
import labyrinth.contracts.entities.lobby.Lobby;
import labyrinth.contracts.entities.lobby.LobbyUser;
import labyrinth.contracts.entities.lobby.UserPermission;
import labyrinth.gameServer.GameServerManager;
import labyrinth.gameServer.IGameServerConnection;
import labyrinth.gameServer.stateManagers.GameBoardStateManager;
import labyrinth.gameServer.stateManagers.GameServerEntryStateManager;
import labyrinth.gameServer.stateManagers.LobbyStateManager;

/**
 * Logic for a ClientLeave-Request
 * @author Lukas Reinhardt
 * @version 1.1
 */
public final class ClientLeaveLogic extends LogicBase<EmptyRequestDto>
{	
	//Constructor
	
	/**
	 * ClientLeaveLogic constructor
	 * @param connection The Connection which called the logic
	 */
	public ClientLeaveLogic(IGameServerConnection connection){ super(connection); }
	
	//Methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(EmptyRequestDto request) 
	{ 
		LobbyUser user = IGameServerConnection.class.cast(getConnection()).getUser();
		
		//Remove user from Lobby
		LobbyStateManager.getLobbyMonitor().executeAction(lobby -> removeUser(lobby, user));
		GameServerManager.getRunningServer().broadcastResponse(LobbyStateManager.createUpdateLobbyResponseDto());
		
		//Remove user from the GameServer
		GameServerEntryStateManager.getServerMonitor().executeAction(server -> server.getMembers().remove(user));
		
		//Handle leaving in the middle of a game
		GameBoardStateManager.onUserLeaving(user.getName());
	}
	
	private void removeUser(Lobby lobby, LobbyUser user)
	{
		if(lobby.getPlayerList().contains(user))
			lobby.getPlayerList().remove(user);
		else
			lobby.getSpectatorList().remove(user);
		
		//Select new admin if the user was the admin
		if(user.getPermission() == UserPermission.ADMIN)
		{
			user.setPermission(UserPermission.DEFAULT);
			selectNewAdmin(lobby);
		}
	}
	
	private void selectNewAdmin(Lobby lobby)
	{
		if(lobby.getPlayerList().size() > 0)
			lobby.getPlayerList().get(0).setPermission(UserPermission.ADMIN);
		else if(lobby.getSpectatorList().size() > 0)
			lobby.getSpectatorList().get(0).setPermission(UserPermission.ADMIN);
		else
			GameServerManager.getRunningServer().close(); //Close server if the last user left
	}
}
