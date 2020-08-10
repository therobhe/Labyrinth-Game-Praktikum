package labyrinth.gameServer.logic;

import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.requests.EmptyRequestDto;
import labyrinth.contracts.communication.dtos.responses.SimpleSuccessResponseDto;
import labyrinth.contracts.communication.logic.LogicBase;
import labyrinth.contracts.entities.lobby.Lobby;
import labyrinth.contracts.entities.lobby.UserPermission;
import labyrinth.gameServer.GameServerManager;
import labyrinth.gameServer.IGameServerConnection;
import labyrinth.gameServer.stateManagers.LobbyStateManager;

/**
 * Logic for a StartGameLogic-Request
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class StartGameLogic extends LogicBase<EmptyRequestDto>
{	
	//Constructor
	
	/**
	 * StartGameLogic constructor
	 * @param connection The Connection which called the logic
	 */
	public StartGameLogic(IGameServerConnection connection){ super(connection); }
	
	//Methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(EmptyRequestDto request) 
	{
		//Only allow starting the game if the user is the admin
		if(IGameServerConnection.class.cast(getConnection()).getUser().getPermission() == UserPermission.DEFAULT)
		{
			getConnection().sendResponse(new SimpleSuccessResponseDto(MessageType.StartGame, false));
			return;
		}
					
		boolean hasEnoughPlayers = LobbyStateManager.getLobbyMonitor().executeFunction(lobby -> hasEnoughPlayers(lobby));
		if(!hasEnoughPlayers) //enough players for starting the game
			getConnection().sendResponse(new SimpleSuccessResponseDto(MessageType.StartGame, false));
		else //not enough players for starting the game
		{
			IGameServerConnection.class.cast(getConnection()).getUser().setIsReady(true);
			GameServerManager.getRunningServer().broadcastResponse(new SimpleSuccessResponseDto(MessageType.StartGame, true));
		}
	}
	
	private boolean hasEnoughPlayers(Lobby lobby) { return lobby.getPlayerList().size() > 1; }
}
