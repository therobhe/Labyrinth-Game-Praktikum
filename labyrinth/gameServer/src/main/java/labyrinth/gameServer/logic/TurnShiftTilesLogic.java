package labyrinth.gameServer.logic;

import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.requests.TurnShiftTilesDto;
import labyrinth.contracts.communication.dtos.responses.PlayerActionResponseDto;
import labyrinth.contracts.communication.dtos.responses.SimpleSuccessResponseDto;
import labyrinth.contracts.entities.game.BonusKind;
import labyrinth.contracts.entities.game.GameBoard;
import labyrinth.contracts.entities.game.Player;
import labyrinth.gameServer.GameServerManager;
import labyrinth.gameServer.IGameServerConnection;
import labyrinth.gameServer.LoggingManager;
import labyrinth.gameServer.stateManagers.GameBoardStateManager;
import labyrinth.gameServer.stateManagers.TurnManager;

/**
 * Logic for a TurnShiftTiles-Request
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class TurnShiftTilesLogic extends TurnActionLogicBase<TurnShiftTilesDto>
{
	//Constructors

	/**
	 * TurnShiftTilesLogic constructor
	 * @param connection The Connection which called the logic
	 */
	public TurnShiftTilesLogic(IGameServerConnection connection) { super(connection); }
	
	//Methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(TurnShiftTilesDto request) 
	{
		//Test if the action is valid
		if(!validateAction(request))
		{
			GameBoardStateManager.getBoardMonitor().executeAction(
				board -> onFailedShiftTiles(getCurrentPlayer(board).getPlayerName(), request));
			return;
		}
		
		//Shift the Tiles
		GameBoardStateManager.getBoardMonitor().executeAction(board -> shiftTiles(board, request));
	}
	
	private boolean validateAction(TurnShiftTilesDto request)
	{
		//Test if the player is allowed to shift tiles at the moment
		boolean canShiftTiles = GameBoardStateManager.getTurnManagerMonitor()
			.executeFunction(turnManager -> canShiftTiles(turnManager));
		
		//Test if valid tile type
		boolean isValidTileType = request.getTileType() >= 0 && request.getTileType() < 10;
		
		//Test if the sent slot is valid
		boolean isValidSlot = GameBoardStateManager.getBoardMonitor()
			.executeFunction(board -> isValidSlot(board, request.getSlot()));
		
		return canShiftTiles && isValidTileType && isValidSlot;
	}
	
	private boolean canShiftTiles(TurnManager turnManager)
	{
		Player player = GameBoardStateManager.getBoardMonitor().executeFunction(board -> getCurrentPlayer(board));
		return turnManager.getIsActivePlayer(player.getPlayerName()) && isShiftTilesStep(turnManager, player);
	}
	
	private boolean isShiftTilesStep(TurnManager turnManager, Player player)
	{
		return turnManager.getShiftedCount() == 0  || player.getActiveBonus() != null 
			&& player.getActiveBonus() == BonusKind.SHIFT_TWICE  && turnManager.getShiftedCount() == 1;
	}
	
	private boolean isValidSlot(GameBoard board, int slot) { return slot > 0 && slot < board.getBoardSize() * 4; }
	
	private void shiftTiles(GameBoard board, TurnShiftTilesDto request)
	{
		Player player = getCurrentPlayer(board);
		boolean hasShiftSolidActivated = player.getActiveBonus() != null 
			&& player.getActiveBonus() == BonusKind.SHIFT_SOLID;
		
		//Try to shift the tiles
		if(!board.tryShiftTile(hasShiftSolidActivated, request.getSlot(), request.getTileType()))
			onFailedShiftTiles(player.getPlayerName(), request);
		else
		{
			//Log result
			LoggingManager.LogPlayerAction(player.getPlayerName(), "ShiftTiles Success - Slot: " + request.getSlot() 
				+ " Type: " + request.getTileType());
			
			//Increment the shifted count of the turn manager
			GameBoardStateManager.getTurnManagerMonitor()
				.executeAction(turnManager -> turnManager.setShiftedCount(turnManager.getShiftedCount() + 1));
			
			//Send success response
			getConnection().sendResponse(new SimpleSuccessResponseDto(MessageType.TurnShiftTiles, true));
			
			//Broadcast the players action
			GameServerManager.getRunningServer()
				.broadcastResponse(new PlayerActionResponseDto(player.getPlayerName(), request));
			
			//Commit current state
			GameServerManager.getRunningServer().commitCurrentGameState();
		}		
	}
	
	private void onFailedShiftTiles(String playerName, TurnShiftTilesDto request)
	{
		//Log result
		LoggingManager.LogPlayerAction(playerName, "ShiftTiles Failed - Slot: " + request.getSlot() + " Type: " 
			+ request.getTileType());
		//Send response
		getConnection().sendResponse(new SimpleSuccessResponseDto(MessageType.TurnShiftTiles, false));
	}
}
