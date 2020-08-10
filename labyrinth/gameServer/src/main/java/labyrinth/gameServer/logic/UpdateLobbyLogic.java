package labyrinth.gameServer.logic;

import labyrinth.contracts.communication.dtos.requests.EmptyRequestDto;
import labyrinth.contracts.communication.logic.LogicBase;
import labyrinth.gameServer.IGameServerConnection;
import labyrinth.gameServer.stateManagers.LobbyStateManager;

/**
 * Logic for a UpdateLobby-Request
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class UpdateLobbyLogic extends LogicBase<EmptyRequestDto>
{	
	//Constructor
	
	/**
	 * UpdateLobbyLogic constructor
	 * @param connection The Connection which called the logic
	 */
	public UpdateLobbyLogic(IGameServerConnection connection){ super(connection); }
	
	//Methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(EmptyRequestDto request) 
	{ getConnection().sendResponse(LobbyStateManager.createUpdateLobbyResponseDto()); }
}
