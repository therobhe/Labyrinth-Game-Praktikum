package labyrinth.gameServer.logic;

import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.requests.ChangeColorDto;
import labyrinth.contracts.communication.dtos.responses.SimpleSuccessResponseDto;
import labyrinth.contracts.communication.logic.LogicBase;
import labyrinth.contracts.entities.lobby.LobbyUser;
import labyrinth.gameServer.GameServerManager;
import labyrinth.gameServer.IGameServerConnection;
import labyrinth.gameServer.stateManagers.LobbyStateManager;

/**
 * Logic for a ChangeColor-Request
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ChangeColorLogic extends LogicBase<ChangeColorDto>
{	
	//Constructor
	
	/**
	 * ChangeColorLogic constructor
	 * @param connection The Connection which called the logic
	 */
	public ChangeColorLogic(IGameServerConnection connection){ super(connection); }
	
	//Methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(ChangeColorDto request) 
	{ 
		LobbyUser user = IGameServerConnection.class.cast(getConnection()).getUser();
		user.setColor(request.getColor());
		getConnection().sendResponse(new SimpleSuccessResponseDto(MessageType.ChangeColor, true));	
		GameServerManager.getRunningServer().broadcastResponse(LobbyStateManager.createUpdateLobbyResponseDto());
	}
}
