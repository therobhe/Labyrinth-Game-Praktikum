package labyrinth.gameServer.logic;

import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.requests.TurnUseBonusDto;
import labyrinth.contracts.communication.dtos.responses.PlayerActionResponseDto;
import labyrinth.contracts.communication.dtos.responses.SimpleSuccessResponseDto;
import labyrinth.contracts.entities.Coordinates;
import labyrinth.contracts.entities.game.BonusKind;
import labyrinth.contracts.entities.game.GameBoard;
import labyrinth.contracts.entities.game.Player;
import labyrinth.contracts.entities.game.Tile;
import labyrinth.gameServer.GameServerAchievementManager;
import labyrinth.gameServer.GameServerManager;
import labyrinth.gameServer.IGameServerConnection;
import labyrinth.gameServer.LoggingManager;
import labyrinth.gameServer.stateManagers.GameBoardStateManager;
import labyrinth.gameServer.stateManagers.TurnManager;

/**
 * Logic for a TurnUseBonus-Request
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class TurnUseBonusLogic extends TurnActionLogicBase<TurnUseBonusDto>
{
	//Constructors
	
	/**
	 * TurnUseBonusLogic constructor
	 * @param connection The Connection which called the logic
	 */
	public TurnUseBonusLogic(IGameServerConnection connection) { super(connection); }
	
	//Methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(TurnUseBonusDto request) 
	{
		Player player = GameBoardStateManager.getBoardMonitor().executeFunction(board -> getCurrentPlayer(board));
		boolean canUseBonus = GameBoardStateManager.getTurnManagerMonitor()
			.executeFunction(turnManager -> canUseBonus(turnManager, player, request.getType()));
		
		if(canUseBonus)
			useBonus(request, player);
		else
			onFailedBonusUse(player.getPlayerName(), request);
	}
	
	private void useBonus(TurnUseBonusDto request, Player player)
	{
		switch(request.getType())
		{
			case SHIFT_SOLID:
				setActiveBonus(player, BonusKind.SHIFT_SOLID);
				onSuccessfulBonusUse(request, player.getPlayerName());
				break;
			case SHIFT_TWICE:
				setActiveBonus(player, BonusKind.SHIFT_TWICE);
				onSuccessfulBonusUse(request, player.getPlayerName());
				break;
			case BEAM:
				GameBoardStateManager.getBoardMonitor().executeAction(board -> beam(board, player, request));
				break;
			case SWAP:
				GameBoardStateManager.getBoardMonitor().executeAction(board -> swap(board, player, request));
				break;
		}
	}
	
	private void beam(GameBoard board, Player player, TurnUseBonusDto request)
	{
		Tile field = board.getTile(new Coordinates(request.getBeam().getTargetX(), request.getBeam().getTargetY()));
		if(field.getPlayer() == null)
		{
			Coordinates coordinates = board.getPlayerPosition(player);
			board.getTile(coordinates).setPlayer(null);
			field.setPlayer(player);
			onSuccessfulBonusUse(request, player.getPlayerName());
		}
		else
			onFailedBonusUse(player.getPlayerName(), request);
	}
	
	private void swap(GameBoard board, Player player, TurnUseBonusDto request)
	{
		Player targetPlayer = board.getPlayers().stream().filter(
			player1 -> player1.getPlayerName() == request.getSwap().getTargetPlayer()).findFirst().get();
		Coordinates targetCoordinates = board.getPlayerPosition(targetPlayer);
		Coordinates sourceCoordinates = board.getPlayerPosition(player);
		Tile targetField = board.getTile(targetCoordinates);
		targetField.setPlayer(player);
		Tile sourceField = board.getTile(sourceCoordinates);
		sourceField.setPlayer(targetPlayer);
		onSuccessfulBonusUse(request, player.getPlayerName());
	}
	
	private static void setActiveBonus(Player player, BonusKind bonusKind)
	{ player.setActiveBonus(player.getBoni().stream().filter(bonus -> bonus == bonusKind).findFirst().get()); }
	
	private static boolean canUseBonus(TurnManager turnManager, Player player, BonusKind bonusKind)
	{ 
		return turnManager.getIsActivePlayer(player.getPlayerName()) && !turnManager.getHasUsedBonus() 
			&& player.getBoni().stream().anyMatch(bonus -> bonus == bonusKind); 
	}
	
	private void onSuccessfulBonusUse(TurnUseBonusDto request, String playerName)
	{
		//log the action
		LoggingManager.LogPlayerAction(playerName, "UseBonus Success - " + getActionText(request));
		
		//Send success response
		getConnection().sendResponse(new SimpleSuccessResponseDto(MessageType.TurnUseBonus, true));
		
		//Broadcast the action
		GameServerManager.getRunningServer().broadcastResponse(new PlayerActionResponseDto(playerName, request));
		
		//Commit current state
		GameServerManager.getRunningServer().commitCurrentGameState();
		
		//Add used bonus for achievements
		GameServerAchievementManager.addUsedBonus(playerName);
	}
	
	private void onFailedBonusUse(String playerName, TurnUseBonusDto request)
	{
		LoggingManager.LogPlayerAction(playerName, "UseBonus Failed - " + getActionText(request));
		getConnection().sendResponse(new SimpleSuccessResponseDto(MessageType.TurnUseBonus, false));
	}
	
	private static String getActionText(TurnUseBonusDto request)
	{
		String text = request.getMessageType().toString();
		if(request.getSwap() != null)
			text += " Target player: " + request.getSwap().getTargetPlayer();
		else if(request.getBeam() != null)
			text += " Target coordinates: " + request.getBeam().getTargetX() + ", " + request.getBeam().getTargetY();
		return text;
	}
}
