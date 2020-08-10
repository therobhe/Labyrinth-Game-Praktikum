package labyrinth.gameServer.stateManagers;

import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import labyrinth.contracts.communication.dtos.BoardDto;
import labyrinth.contracts.communication.dtos.PlayerBoniDto;
import labyrinth.contracts.communication.dtos.PlayerDto;
import labyrinth.contracts.communication.dtos.PlayerTreasuresDto;
import labyrinth.contracts.communication.dtos.TileDto;
import labyrinth.contracts.communication.dtos.responses.EndGameResponseDto;
import labyrinth.contracts.communication.dtos.responses.GameStateResponseDto;
import labyrinth.contracts.entities.Coordinates;
import labyrinth.contracts.entities.game.BonusKind;
import labyrinth.contracts.entities.game.GameBoard;
import labyrinth.contracts.entities.game.Player;
import labyrinth.contracts.entities.game.Tile;
import labyrinth.contracts.entities.lobby.GameConfiguration;
import labyrinth.gameServer.GameServerAchievementManager;
import labyrinth.gameServer.GameServerManager;
import labyrinth.gameServer.IGameServerConnection;
import labyrinth.gameServer.ObjectAccessMonitor;

/**
 * ManagerClass for managing the State of the GameBoard
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class GameBoardStateManager 
{
	//Properties
	
	private static Timer switchTurnTimer;
	
	private static int turnLength;
	
	private static GameStateResponseDto currentGameState;
	
	private static GameBoard board;
	
	private static ObjectAccessMonitor<GameBoard> boardMonitor;
	/**
	 * Corresponding getter
	 * @return The monitor containing the game board
	 */
	public static ObjectAccessMonitor<GameBoard> getBoardMonitor() { return boardMonitor; }
	
	private static TurnManager turnManager;
	
	private static ObjectAccessMonitor<TurnManager> turnManagerMonitor;
	/**
	 * Corresponding getter
	 * @return The monitor containing the turn manager
	 */
	public static ObjectAccessMonitor<TurnManager> getTurnManagerMonitor() { return turnManagerMonitor; }
	
	//Constructors
	
	private GameBoardStateManager() { }
	
	//Methods
	
	/**
	 * Sends a EndGame-Response with the results of all players to the clients
	 */
	public synchronized static void endGame()
	{
		EndGameResponseDto response = new EndGameResponseDto();
		for(PlayerDto player : currentGameState.getPlayers())
			response.getPlayers().add(player);
		GameServerManager.getRunningServer().broadcastResponse(response);
	}
	
	/**
	 * Handles a user leaving in the middle of a game
	 * @param userName The name of the user leaving
	 */
	public synchronized static void onUserLeaving(String userName)
	{
		if(currentGameState != null)
		{
			Optional<Player> leavingPlayer = board.getPlayers().stream().filter(
				player -> player.getPlayerName().equals(userName)).findFirst();
			if(leavingPlayer.isPresent()) //Is player?
			{
				leavingPlayer.get().setIsDisconnected(true);
				//Commit and send changes to clients
				GameServerManager.getRunningServer().commitCurrentGameState();
				
				//End game if there are less than 2 players remaining
				if(currentGameState.getPlayers().stream().filter(player -> !player.getDisconnected()).count() < 2)
					endGame();
				else if (turnManager.getIsActivePlayer(userName)) //switch turn if leaving player was active player
				{
					int nextPlayerIndex = getNextActivePlayerIndex(leavingPlayer.get());
					turnManager.switchTurn(board.getPlayers().get(nextPlayerIndex).getPlayerName(), true);
				}
			}
		}
	}
	
	/**
	 * Switches the turn to the next player
	 */
	public synchronized static void switchTurn()
	{
		if(switchTurnTimer != null)
		{
			switchTurnTimer.cancel();
			switchTurnTimer.purge();
		}		
		
		Optional<Player> player = board.getPlayers().stream().filter(
			player1 -> player1.getPlayerName().equals(turnManager.getActivePlayer())).findFirst();
		
		//Select next player
		Player nextPlayer;
		if(!player.isPresent())
			nextPlayer = board.getPlayers().get(0);
		else
			nextPlayer = board.getPlayers().get(getNextActivePlayerIndex(player.get()));
		turnManager.switchTurn(nextPlayer.getPlayerName(), true);
		
		//set timer for turn length
		switchTurnTimer = new Timer();
		switchTurnTimer.schedule(new TimerTask()
		{
			@Override
			public void run() { switchTurn(); }
		}, turnLength * 1000);
	}
	
	/**
	 * Initializes the GameBoardStateManager
	 * @param configuration The configuration of the game
	 * @param players The players of the Game
	 */
	public static void initialize(GameConfiguration configuration, List<Player> players) 
	{ 		
		board = new GameBoard(configuration, players);
		boardMonitor = new ObjectAccessMonitor<GameBoard>(board);
		turnManager = new TurnManager(configuration.getGameLengthLimit());
		turnManagerMonitor = new ObjectAccessMonitor<TurnManager>(turnManager);
		turnLength = configuration.getTurnLengthLimit();
		
		//Initialize server achievements for all players
		GameServerAchievementManager.initialize(
			players.stream().map(player -> player.getPlayerName()).collect(Collectors.toList()));
		
		//Switch turn to the first player
		switchTurn();
		
		//Update and send GameState
		updateCurrentGameState();
		GameServerManager.getRunningServer().broadcastResponse(currentGameState);
	}
	
	/**
	 * Commits the current GameState
	 * @param connections Alle the connected clients
	 */
	public synchronized static void commitCurrentGameState(List<IGameServerConnection> connections)
	{
		//Send changes to the specific clients
		for(IGameServerConnection connection : connections)
			sendGameBoardChangesResponseForPlayer(connection);
		
		//Update current GameState
		updateCurrentGameState();
		
		//End game if a player has collected all treasures
		if(currentGameState.getPlayers().stream().anyMatch(player -> player.getTreasures().getRemaining() == 0))
			endGame();
	}
	
	private static int getNextActivePlayerIndex(Player currentPlayer)
	{
		int nextPlayerIndex = board.getPlayers().indexOf(currentPlayer);
		return nextPlayerIndex == board.getPlayers().size() - 1 ? 0 :  nextPlayerIndex + 1;
	}
	
	private static void sendGameBoardChangesResponseForPlayer(IGameServerConnection connection)
	{
		String playerName = connection.getUser().getName();
		//Create BoardDto with changed tiles for the GameStateResponse
		BoardDto boardDto = new BoardDto();
		for(TileDto tile : currentGameState.getBoard().getTiles())
			if(hasTileChanges(tile))
			{
				Tile field;
				if(tile.getX() == -1 && tile.getY() == -1)
					field = board.getTileOutSideOfBoard();
				else
					field = board.getTile(new Coordinates(tile.getX(), tile.getY()));
				TileDto updatedTile = getTileDto(tile.getX(), tile.getY(), field);
				boardDto.getTiles().add(updatedTile);
			}
					
		
		GameStateResponseDto response = new GameStateResponseDto(boardDto);	
		
		//Add changed PlayerDtos to the GameStateResponse
		for(PlayerDto playerDto : currentGameState.getPlayers())
		{
			if(hasPlayerChanges(playerDto))
			{
				Player player = board.getPlayers().stream().filter(
					player1 -> player1.getPlayerName().equals(playerDto.getName())).findFirst().get();
				boolean shouldSendCurrentTreasure = player.getPlayerName().equals(playerName);
				addPlayerToState(response, player, shouldSendCurrentTreasure);
			}
		}				
		
		//Send the state to the client
		connection.sendResponse(response);
	}
	
	private static void updateCurrentGameState()
	{
		//Create BoardDto with all tiles
		BoardDto boardDto = createBoardDto();
						
		GameStateResponseDto state = new GameStateResponseDto(boardDto);
		
		//Add PlayerDtos to the GameStateResponse
		for(Player player : board.getPlayers())
			addPlayerToState(state, player, true);
		currentGameState = state;
	}
	
	private static void addPlayerToState(GameStateResponseDto state, Player player, boolean shouldSendCurrentTreasure)
	{
		Integer currentTreasure = null;
		if(shouldSendCurrentTreasure)
			currentTreasure = player.getNextTreasure();
		
		PlayerTreasuresDto treasureDto = new PlayerTreasuresDto(currentTreasure, player.getTargetTreasures().size());					
		int swapCount = (int)player.getBoni().stream().filter(bonus -> bonus == BonusKind.SWAP).count();
		int beamCount = (int)player.getBoni().stream().filter(bonus -> bonus == BonusKind.BEAM).count();
		int shiftSolidCount = (int)player.getBoni().stream().filter(bonus -> bonus == BonusKind.SHIFT_SOLID).count();
		int shiftTwiceCount = (int)player.getBoni().stream().filter(bonus -> bonus == BonusKind.SHIFT_TWICE).count();					
		PlayerBoniDto boniDto = new PlayerBoniDto(beamCount, swapCount, shiftSolidCount, shiftTwiceCount);			
		PlayerDto playerToSend = new PlayerDto(player.getPlayerName(), player.getPlayerColor(), player.getPoints(), 
			player.getIsDisconnected(), treasureDto, boniDto);
		state.getPlayers().add(playerToSend);
	}
	
 	private static BoardDto createBoardDto()
	{
		BoardDto boardDto = new BoardDto();
		for(int i = 0;i < board.getBoardSize();i++)
			for(int j = 0;j < board.getBoardSize();j++)
			{
				Tile field = board.getTile(new Coordinates(i, j));
				TileDto tile = getTileDto(i, j, field);
				boardDto.getTiles().add(tile);	
			}
		boardDto.getTiles().add(getTileDto(-1, -1, board.getTileOutSideOfBoard()));
		return boardDto;
	}
 	
 	private static TileDto getTileDto(int x, int y, Tile field)
 	{
 		TileDto tile = new TileDto(x, y, field.getType());
		
		if(field.getPlayer() != null)
			tile.setPlayer(field.getPlayer().getPlayerName());
		else
			tile.setPlayer(null);
		
		tile.setBoni(field.getBonus());
		tile.setTreasure(field.getTreasure());
		tile.setPlayerBase(field.getPlayerBase());
		return tile;
 	}

 	private static boolean hasPlayerChanges(PlayerDto playerDto)
	{
		Player player = board.getPlayers().stream().filter(
			player1 -> player1.getPlayerName().equals(playerDto.getName())).findFirst().get();
		
		return !hasSameBoni(playerDto, player) || !hasSameScore(playerDto, player) 
			|| !hasSameTreasures(playerDto, player) || !hasSameDisconnectedValue(playerDto, player);
	}
	
	private static boolean hasSameBoni(PlayerDto playerDto, Player player)
	{
		int boniCount = playerDto.getBoni().getShiftSolid() + playerDto.getBoni().getSwap() 
			+ playerDto.getBoni().getBeam() + playerDto.getBoni().getShiftTwice();
		return boniCount == player.getBoni().size();
	}
	
	private static boolean hasSameTreasures(PlayerDto playerDto, Player player)
	{ return playerDto.getTreasures().getRemaining() == player.getTargetTreasures().size(); }
	
	private static boolean hasSameScore(PlayerDto playerDto, Player player) 
	{ return playerDto.getScore() == player.getPoints(); }
	
	private static boolean hasSameDisconnectedValue(PlayerDto playerDto, Player player) 
	{ return playerDto.getDisconnected() == player.getIsDisconnected(); }
	
	private static boolean hasTileChanges(TileDto tile)
	{
		Tile field;
		
		if(tile.getX() == -1 && tile.getY() == -1)
			field = board.getTileOutSideOfBoard();
		else
			field = board.getTile(new Coordinates(tile.getX(), tile.getY()));
		
		return field.getType() != tile.getType() || !hasSamePlayer(field, tile) || !hasSamePlayerBase(field, tile) 
			|| !hasSameTreasure(field, tile) || !hasSameBonus(field, tile);
	}
	
	private static boolean hasSamePlayer(Tile field, TileDto tile)
	{
		return field.getPlayer() == null && tile.getPlayer() == null 
			|| tile.getPlayer() != null && field.getPlayer() != null 
				&& tile.getPlayer().equals(field.getPlayer().getPlayerName());
	}
	
	private static boolean hasSamePlayerBase(Tile field, TileDto tile)
	{
		return field.getPlayerBase() == null && tile.getPlayerBase() == null 
			|| tile.getPlayerBase() != null && field.getPlayerBase() != null 
				&& tile.getPlayerBase().equals(field.getPlayerBase());
	}
	
	private static boolean hasSameTreasure(Tile field, TileDto tile)
	{
		return field.getTreasure() == null && tile.getTreasure() == null 
			|| tile.getTreasure() != null && field.getTreasure() != null 
				&& tile.getTreasure() == field.getTreasure();
	}
	
	private static boolean hasSameBonus(Tile field, TileDto tile)
	{
		return field.getBonus() == null && tile.getBoni() == null 
			|| tile.getBoni() != null && field.getBonus() != null 
				&& tile.getBoni() == field.getBonus();
	}
}
