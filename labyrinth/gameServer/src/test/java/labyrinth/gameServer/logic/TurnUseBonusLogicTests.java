package labyrinth.gameServer.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.SocketServerMock;
import labyrinth.contracts.communication.dtos.TargetCoordinatesDto;
import labyrinth.contracts.communication.dtos.TargetPlayerDto;
import labyrinth.contracts.communication.dtos.requests.TurnUseBonusDto;
import labyrinth.contracts.communication.dtos.responses.PlayerActionResponseDto;
import labyrinth.contracts.communication.dtos.responses.SimpleSuccessResponseDto;
import labyrinth.contracts.entities.game.Achievement;
import labyrinth.contracts.entities.game.BonusKind;
import labyrinth.contracts.entities.game.GameBoard;
import labyrinth.contracts.entities.game.Player;
import labyrinth.contracts.entities.game.Tile;
import labyrinth.contracts.entities.Coordinates;
import labyrinth.contracts.entities.lobby.GameConfiguration;
import labyrinth.contracts.entities.lobby.LobbyUser;
import labyrinth.contracts.entities.lobby.LobbyUserKind;
import labyrinth.contracts.entities.lobby.UserPermission;
import labyrinth.contracts.utilities.FileAccessorMock;
import labyrinth.contracts.utilities.FileLogger;
import labyrinth.gameServer.GameServerAchievementManager;
import labyrinth.gameServer.GameServerConnection;
import labyrinth.gameServer.GameServerConnectionMock;
import labyrinth.gameServer.GameServerManager;
import labyrinth.gameServer.LoggingManager;
import labyrinth.gameServer.ServerAchievementsWrapper;
import labyrinth.gameServer.stateManagers.GameBoardStateManager;

/**
 * TestClass for @see TurnUseBonusLogic
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class TurnUseBonusLogicTests extends GameServerLogicTestBase
{
	/**
	 * Tests that it's not possible to a use a bonus if it's not the players turn
	 */
	@Test
	public void cannotUseBonusOutsideOfPlayerTurnTest()
	{
		FileAccessorMock mockAccessor = new FileAccessorMock();
		LoggingManager.initialize(new FileLogger("test", mockAccessor));
		GameServerConnectionMock mock = new GameServerConnectionMock();
		mock.setUser(new LobbyUser("Test1", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		initializeGameBoardManager(true);
		GameBoardStateManager.getTurnManagerMonitor().executeAction(turnManager -> turnManager.switchTurn("Test2", false));
		TurnUseBonusLogic logic = new TurnUseBonusLogic(mock);
		logic.execute(new TurnUseBonusDto(BonusKind.SHIFT_SOLID));
		testNegativeResult(mock, mockAccessor);
	}
	
	/**
	 * Tests that it's not possible to use a bonus if the player has already used a bonus this turn
	 */
	@Test
	public void cannotUseBonusIfBonusAlreadyUsedThisTurnTest()
	{
		FileAccessorMock mockAccessor = new FileAccessorMock();
		LoggingManager.initialize(new FileLogger("test", mockAccessor));
		GameServerConnectionMock mock = new GameServerConnectionMock();
		mock.setUser(new LobbyUser("Test1", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		initializeGameBoardManager(true);
		GameBoardStateManager.getTurnManagerMonitor().executeAction(turnManager -> turnManager.setHasUsedBonus(true));
		TurnUseBonusLogic logic = new TurnUseBonusLogic(mock);
		logic.execute(new TurnUseBonusDto(BonusKind.SHIFT_SOLID));
		testNegativeResult(mock, mockAccessor);
	}
	
	/**
	 * Tests that it's not possible to use a bonus if the player has no such bonus
	 */
	@Test
	public void cannotUseBonusIfPlayerHasNoBonusOfThisKindTest()
	{
		FileAccessorMock mockAccessor = new FileAccessorMock();
		LoggingManager.initialize(new FileLogger("test", mockAccessor));
		GameServerConnectionMock mock = new GameServerConnectionMock();
		GameBoardStateManager.getTurnManagerMonitor().executeAction(turnManager -> turnManager.setHasUsedBonus(true));
		mock.setUser(new LobbyUser("Test1", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		initializeGameBoardManager(false);
		TurnUseBonusLogic logic = new TurnUseBonusLogic(mock);
		logic.execute(new TurnUseBonusDto(BonusKind.SHIFT_SOLID));
		testNegativeResult(mock, mockAccessor);
	}
	
	/**
	 * Tests the execution of the logic for BonusKind.SHIFT_SOLID
	 */
	@Test
	public void useShiftTwiceBonusTest() { simpleUseBonusTestCore(BonusKind.SHIFT_SOLID); }
	
	/**
	 * Tests the execution of the logic for BonusKind.SHIFT_SOLID
	 */
	@Test
	public void useShiftSolidBonusTest() { simpleUseBonusTestCore(BonusKind.SHIFT_SOLID); }
	
	/**
	 * Tests the execution of the logic for BonusKind.SWAP
	 */
	@Test
	public void useSwapBonusTest() 
	{  
		FileAccessorMock mockAccessor = new FileAccessorMock();
		LoggingManager.initialize(new FileLogger("test", mockAccessor));
		GameServerConnectionMock mock = new GameServerConnectionMock();
		mock.setUser(new LobbyUser("Test1", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		Player player = initializeGameBoardManager(true);	
		GameBoard board = GameBoardStateManager.getBoardMonitor().executeFunction(gameBoard -> gameBoard);
		Coordinates playerPosition = board.getPlayerPosition(player);
		Player target = board.getPlayers().get(1);
		Coordinates targetPlayerPosition = board.getPlayerPosition(target);
		Tile playerField = board.getTile(playerPosition);
		Tile targetPlayerField = board.getTile(targetPlayerPosition);
		
		TurnUseBonusLogic logic = new TurnUseBonusLogic(mock);
		TurnUseBonusDto request = new TurnUseBonusDto(new TargetPlayerDto("Test2"));
		logic.execute(request);
		
		assertSame(player, targetPlayerField.getPlayer());
		assertSame(target, playerField.getPlayer());
		testPositiveResult(mock, request, mockAccessor);
	}
	
	/**
	 * Tests the execution of the logic for BonusKind.BEAM
	 */
	@Test
	public void useBeamBonusTest() 
	{  
		FileAccessorMock mockAccessor = new FileAccessorMock();
		LoggingManager.initialize(new FileLogger("test", mockAccessor));
		GameServerConnectionMock mock = new GameServerConnectionMock();
		mock.setUser(new LobbyUser("Test1", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		Player player = initializeGameBoardManager(true);	
		GameBoard board = GameBoardStateManager.getBoardMonitor().executeFunction(gameBoard -> gameBoard);	
		
		int x = 0, y = 0;
		Coordinates coordinates = board.getPlayerPosition(player);
		Tile sourceField = board.getTile(coordinates);
		Tile targetField = null;
		outerloop:
		for(int i = 0;i < board.getBoardSize();i++)
			for(int j = 0;j < board.getBoardSize();j++)
				if(board.getTile(new Coordinates(i, j)).getPlayer() == null)
				{
					x = i;
					y = j;
					targetField = board.getTile(new Coordinates(i, j));
					break outerloop;
				}
		
		TurnUseBonusLogic logic = new TurnUseBonusLogic(mock);
		TurnUseBonusDto request = new TurnUseBonusDto(new TargetCoordinatesDto(x, y));
		logic.execute(request);
		
		assertSame(player, targetField.getPlayer());
		assertNull(sourceField.getPlayer());
		testPositiveResult(mock, request, mockAccessor);
	}
	
	/**
	 * Tests the execution of the logic for BonusKind.SWAP
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void doesAddBonusUseToAchievementsTest() 
		throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException 
	{  
		FileAccessorMock mockAccessor = new FileAccessorMock();
		LoggingManager.initialize(new FileLogger("test", mockAccessor));
		GameServerConnectionMock mock = new GameServerConnectionMock();
		mock.setUser(new LobbyUser("Test1", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		initializeGameBoardManager(true);	
		TurnUseBonusLogic logic = new TurnUseBonusLogic(mock);
		logic.execute(new TurnUseBonusDto(BonusKind.SHIFT_TWICE));
		
		Field field = GameServerAchievementManager.class.getDeclaredField("playerAchievements");
		field.setAccessible(true);
		Map<String, ServerAchievementsWrapper> playerAchievements = Map.class.cast(field.get(null));
		
		ServerAchievementsWrapper wrapper1 = playerAchievements.get("Test1");
		testAchievementProgressCore(wrapper1, "usedBoniAchievement1", 1);
		testAchievementProgressCore(wrapper1, "usedBoniAchievement2", 1);
		testAchievementProgressCore(wrapper1, "usedBoniAchievement3", 1);
		testAchievementProgressCore(wrapper1, "usedBoniAchievement4", 1);
		testAchievementProgressCore(wrapper1, "usedBoniAchievement5", 1);
	}
	
	/**
	 * Tests that the Beam failes if the traget tile has a player on it
	 */
	@Test
	public void cannotUseBeamBonusOnFieldWithPlayerTest() 
	{
		FileAccessorMock mockAccessor = new FileAccessorMock();
		LoggingManager.initialize(new FileLogger("test", mockAccessor));
		GameServerConnectionMock mock = new GameServerConnectionMock();
		mock.setUser(new LobbyUser("Test1", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		Player player = initializeGameBoardManager(true);	
		GameBoard board = GameBoardStateManager.getBoardMonitor().executeFunction(gameBoard -> gameBoard);	
		
		int x = 0, y = 0;
		Coordinates coordinates = board.getPlayerPosition(player);
		Tile sourceField = board.getTile(coordinates);
		Tile targetField = null;
		outerloop:
		for(int i = 0;i < board.getBoardSize();i++)
			for(int j = 0;j < board.getBoardSize();j++)
			{
				if(board.getTile(new Coordinates(i, j)).getPlayer() != null 
					&& board.getTile(new Coordinates(i, j)).getPlayer() != player)
				{
					x = i;
					y = j;
					targetField = board.getTile(new Coordinates(i, j));
					break outerloop;
				}
			}

		TurnUseBonusLogic logic = new TurnUseBonusLogic(mock);
		TurnUseBonusDto request = new TurnUseBonusDto(new TargetCoordinatesDto(x, y));
		logic.execute(request);
		
		assertSame(player, sourceField.getPlayer());
		assertFalse(player == targetField.getPlayer());
		testNegativeResult(mock, mockAccessor);
	}
	
	private void simpleUseBonusTestCore(BonusKind bonusKind)
	{
		FileAccessorMock mockAccessor = new FileAccessorMock();
		LoggingManager.initialize(new FileLogger("test", mockAccessor));
		GameServerConnectionMock mock = new GameServerConnectionMock();
		mock.setUser(new LobbyUser("Test1", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		Player player = initializeGameBoardManager(true);
		TurnUseBonusLogic logic = new TurnUseBonusLogic(mock);
		TurnUseBonusDto request = new TurnUseBonusDto(bonusKind);
		logic.execute(request);
		
		assertEquals(bonusKind, player.getActiveBonus());
		assertEquals(3, player.getBoni().size());
		assertFalse(player.getBoni().stream().anyMatch(bonus -> bonus == bonusKind));
		
		testPositiveResult(mock, request, mockAccessor);
	}
	
	private void testNegativeResult(GameServerConnectionMock mock, FileAccessorMock accessorMock)
	{
		assertEquals(SimpleSuccessResponseDto.class, mock.getLastResponseSent().getClass());
		SimpleSuccessResponseDto response = SimpleSuccessResponseDto.class.cast(mock.getLastResponseSent());
		assertEquals(MessageType.TurnUseBonus, response.getMessageType());
		assertFalse(response.getSuccess());
		assertNotNull(accessorMock.getWrittenTexts().get("test"));
	}
	
	private Player initializeGameBoardManager(boolean hasBoni)
	{
		Player player1 = new Player("Test1", null);
		Player player2 = new Player("Test2", null);
		if(hasBoni)
		{
			player1.getBoni().add(BonusKind.BEAM);
			player1.getBoni().add(BonusKind.SHIFT_SOLID);
			player1.getBoni().add(BonusKind.SHIFT_TWICE);
			player1.getBoni().add(BonusKind.SWAP);
		}
		List<Player> players = new ArrayList<Player>();
		players.add(player1);
		players.add(player2);
		GameConfiguration configuration = new GameConfiguration();
		configuration.setSize(new Coordinates(7,7));
		GameBoardStateManager.initialize(configuration, players);
		return player1;
	}
	
	@SuppressWarnings("unchecked")
	private void testPositiveResult(GameServerConnectionMock mock, TurnUseBonusDto request, FileAccessorMock accessorMock)
	{
		assertEquals(SimpleSuccessResponseDto.class, mock.getLastResponseSent().getClass());
		SimpleSuccessResponseDto response = SimpleSuccessResponseDto.class.cast(mock.getLastResponseSent());
		assertEquals(MessageType.TurnUseBonus, response.getMessageType());
		assertTrue(response.getSuccess());
		SocketServerMock<GameServerConnection> mockServer 
			= SocketServerMock.class.cast(GameServerManager.getRunningServer().getServer());
		assertEquals(PlayerActionResponseDto.class, mockServer.getBroadcastedResponse().getClass());
		PlayerActionResponseDto broadcastResponse = PlayerActionResponseDto.class.cast(mockServer.getBroadcastedResponse());
		assertEquals("Test1", broadcastResponse.getPlayer());
		assertSame(request, broadcastResponse.getTurnUseBonus());
		assertNull(broadcastResponse.getTurnMovePlayer());
		assertNull(broadcastResponse.getTurnShiftTiles());
		assertNotNull(accessorMock.getWrittenTexts().get("test"));
	}
	
	private static void testAchievementProgressCore(ServerAchievementsWrapper wrapper, String achievementName, 
		int expectedProgress) 
		throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		Field field = wrapper.getClass().getDeclaredField(achievementName);
		field.setAccessible(true);
		Achievement achievement = Achievement.class.cast(field.get(wrapper));
		assertEquals(expectedProgress, achievement.getProgress());
	}
}
