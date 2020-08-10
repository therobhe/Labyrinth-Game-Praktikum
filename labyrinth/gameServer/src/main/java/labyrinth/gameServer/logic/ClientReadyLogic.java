package labyrinth.gameServer.logic;

import java.util.ArrayList;
import java.util.List;

import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.requests.EmptyRequestDto;
import labyrinth.contracts.communication.dtos.responses.EmptyResponseDto;
import labyrinth.contracts.communication.logic.LogicBase;
import labyrinth.contracts.entities.game.Player;
import labyrinth.contracts.entities.lobby.Lobby;
import labyrinth.contracts.entities.lobby.LobbyUser;
import labyrinth.gameServer.GameServerManager;
import labyrinth.gameServer.IGameServerConnection;
import labyrinth.gameServer.stateManagers.GameBoardStateManager;
import labyrinth.gameServer.stateManagers.GameConfigurationStateManager;
import labyrinth.gameServer.stateManagers.LobbyStateManager;

/**
 * Logic for a ClientReady-Request
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ClientReadyLogic extends LogicBase<EmptyRequestDto>
{	
	//Constructor
	
	/**
	 * ClientLeaveLogic constructor
	 * @param connection The Connection which called the logic
	 */
	public ClientReadyLogic(IGameServerConnection connection){ super(connection); }
	
	//Methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(EmptyRequestDto request) 
	{ 
		IGameServerConnection.class.cast(getConnection()).getUser().setIsReady(true);
		boolean allReady = LobbyStateManager.getLobbyMonitor().executeFunction(lobby -> areAllPlayersReady(lobby));
		if(allReady)
		{
			GameServerManager.getRunningServer().broadcastResponse(new EmptyResponseDto(MessageType.AllClientsReady));
			//Start game
			LobbyStateManager.getLobbyMonitor().executeAction(lobby -> startGame(lobby));
		}	
	}
	
	private boolean areAllPlayersReady(Lobby lobby)
	{ return lobby.getPlayerList().stream().allMatch(player -> player.getIsReady()); }
	
	private void startGame(Lobby lobby)
	{
		List<Player> players = new ArrayList<Player>();
		for(LobbyUser player : lobby.getPlayerList())
			players.add(new Player(player.getName(), player.getColor()));
		
		//Initialize the game board
		GameConfigurationStateManager.getConfigurationMonitor()
			.executeAction(configuration -> GameBoardStateManager.initialize(configuration, players));
	}
}
