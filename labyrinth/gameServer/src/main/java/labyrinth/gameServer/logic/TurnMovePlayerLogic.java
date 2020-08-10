package labyrinth.gameServer.logic;

import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.requests.TurnMovePlayerDto;
import labyrinth.contracts.communication.dtos.responses.PlayerActionResponseDto;
import labyrinth.contracts.communication.dtos.responses.SimpleSuccessResponseDto;
import labyrinth.contracts.entities.Coordinates;
import labyrinth.contracts.entities.game.Player;
import labyrinth.gameServer.GameServerAchievementManager;
import labyrinth.gameServer.GameServerManager;
import labyrinth.gameServer.IGameServerConnection;
import labyrinth.gameServer.LoggingManager;
import labyrinth.gameServer.stateManagers.GameBoardStateManager;
import labyrinth.gameServer.stateManagers.TurnManager;

/**
 * Logic for a TurnMovePlayer-Request
 * @author Eugen Cravtov-Grandl
 * @version 1.0
 */
public final class TurnMovePlayerLogic extends TurnActionLogicBase<TurnMovePlayerDto>
{
	//Constructor
	
	/**
	 * Constructor for TurnMovePlayerLogic
	 * @param connection The Connection which called the logic
	 */
	public TurnMovePlayerLogic(IGameServerConnection connection) { super(connection); }
	
	//Methods
	
	/**
	 * {@inheritDoc}
	 */
	public void execute(TurnMovePlayerDto request)
	{
		Player player = GameBoardStateManager.getBoardMonitor().executeFunction(board -> getCurrentPlayer(board));
		
		//Test if it's possible to move the player
		boolean canMovePlayer = GameBoardStateManager.getTurnManagerMonitor().executeFunction(
			turnManager -> canMovePlayer(turnManager, player));		
		if(!canMovePlayer)
			onFailedMovePlayer(player.getPlayerName(), request);
		
		//Try to move the player
		tryMovePlayer(request, player);	
	}
	
	private static boolean canMovePlayer(TurnManager turnManager, Player player)
	{ return turnManager.getIsActivePlayer(player.getPlayerName()) && !turnManager.getHasMoved(); }
	
	private void tryMovePlayer(TurnMovePlayerDto request, Player player)
	{
		Coordinates playerCoordinates 
			= GameBoardStateManager.getBoardMonitor().executeFunction(board -> board.getPlayerPosition(player));
		boolean success = GameBoardStateManager.getBoardMonitor().executeFunction(
			board -> board.tryMovePlayer(player, new Coordinates(request.getTargetX(), request.getTargetY())));
		if(!success)
			onFailedMovePlayer(player.getPlayerName(), request);
		else
			onSuccessfullMovePlayer(request, player, playerCoordinates);
	}
	
	private void onFailedMovePlayer(String playerName, TurnMovePlayerDto request)
	{
		//Log result
		LoggingManager.LogPlayerAction(playerName, "MovePlayer Failed - Target coordinates: " + request.getTargetX()  
			+ " Type: " + request.getTargetY());
		//Send response
		getConnection().sendResponse(new SimpleSuccessResponseDto(MessageType.TurnMovePlayer, false));
	}
	
	private void onSuccessfullMovePlayer(TurnMovePlayerDto request, Player player, Coordinates playerCoordinates)
	{
		//Log result
		LoggingManager.LogPlayerAction(player.getPlayerName(), "MovePlayer Success - Target coordinates: " 
			+ request.getTargetX()  + " Type: " + request.getTargetY());	
		
		//Set the flag indicating that the player has already moved
		GameBoardStateManager.getTurnManagerMonitor().executeAction(turnManager -> turnManager.setHasMoved(true));
		
		//Send success response
		getConnection().sendResponse(new SimpleSuccessResponseDto(MessageType.TurnMovePlayer, true));	
		//Broadcast the players action
		GameServerManager.getRunningServer()
			.broadcastResponse(new PlayerActionResponseDto(player.getPlayerName(), request), getConnection());
		
		//Commit current state
		GameServerManager.getRunningServer().commitCurrentGameState();
		
		//Add moved tiles for achievements
		int movedTiles = getMovedTiles(playerCoordinates, request.getTargetX(), request.getTargetY());
		GameServerAchievementManager.addMovedTiles(player.getPlayerName(),  movedTiles);
		
		//Switch turn
		GameBoardStateManager.switchTurn();
	}
	
	private int getMovedTiles(Coordinates playerCoordintes, int targetX, int targetY)
	{ return Math.abs(playerCoordintes.getX() - targetX) + Math.abs(playerCoordintes.getY() - targetY); }
}
