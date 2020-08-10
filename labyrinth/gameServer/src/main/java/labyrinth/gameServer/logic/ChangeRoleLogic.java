package labyrinth.gameServer.logic;

import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.requests.ChangeRoleDto;
import labyrinth.contracts.communication.dtos.responses.SimpleSuccessResponseDto;
import labyrinth.contracts.communication.logic.LogicBase;
import labyrinth.contracts.entities.lobby.LobbyUser;
import labyrinth.gameServer.GameServerManager;
import labyrinth.gameServer.IGameServerConnection;
import labyrinth.gameServer.stateManagers.LobbyStateManager;

/**
 * Logic for a ChangeRole-Request
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ChangeRoleLogic extends LogicBase<ChangeRoleDto>
{	
	//Constructor
	
	/**
	 * ChangeRoleLogic constructor
	 * @param connection The Connection which called the logic
	 */
	public ChangeRoleLogic(IGameServerConnection connection){ super(connection); }
	
	//Methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(ChangeRoleDto request) 
	{ 
		LobbyUser user = IGameServerConnection.class.cast(getConnection()).getUser();
		if(user.getRole() != request.getRole())
		{
			boolean success = LobbyStateManager.getLobbyMonitor().executeFunction(lobby -> lobby.tryChangeUserKind(user));
			if(!success)
				getConnection().sendResponse(new SimpleSuccessResponseDto(MessageType.ChangeRole, false));
		}
		getConnection().sendResponse(new SimpleSuccessResponseDto(MessageType.ChangeRole, true));
		GameServerManager.getRunningServer().broadcastResponse(LobbyStateManager.createUpdateLobbyResponseDto());
	}
}
