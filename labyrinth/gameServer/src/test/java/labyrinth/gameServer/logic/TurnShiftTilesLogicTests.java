package labyrinth.gameServer.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.SocketServerMock;
import labyrinth.contracts.communication.dtos.requests.TurnShiftTilesDto;
import labyrinth.contracts.communication.dtos.responses.*;
import labyrinth.contracts.entities.*;
import labyrinth.contracts.entities.game.BonusKind;
import labyrinth.contracts.entities.game.Player;
import labyrinth.contracts.entities.game.Tile;
import labyrinth.contracts.entities.lobby.*;
import labyrinth.contracts.utilities.FileAccessorMock;
import labyrinth.contracts.utilities.FileLogger;
import labyrinth.gameServer.*;
import labyrinth.gameServer.stateManagers.GameBoardStateManager;

/**
 * TestClass for @see TurnShiftTilesLogic
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class TurnShiftTilesLogicTests extends GameServerLogicTestBase
{
	/**
	 * Tests that it's not possible to shift the tiles if it's not your turn
	 */
	@Test
	public void cannotShiftTilesOutsideOfPlayerTurnTest()
	{
		FileAccessorMock mockAccessor = new FileAccessorMock();
		LoggingManager.initialize(new FileLogger("test", mockAccessor));
		GameServerConnectionMock mock = new GameServerConnectionMock();
		mock.setUser(new LobbyUser("Test1", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		initializeGameBoardManager(false);
		GameBoardStateManager.getTurnManagerMonitor().executeAction(turnManager -> turnManager.switchTurn("Test2", false));
		TurnShiftTilesLogic logic = new TurnShiftTilesLogic(mock);
		logic.execute(new TurnShiftTilesDto(3, 2));
		testNegativeResult(mock, mockAccessor);
	}
	
	/**
	 * Tests that it's not possible to shift the tiles if the player already has shifted the tiles once
	 */
	@Test
	public void cannotShiftTilesWhenTilesAreAlreadyShiftedOnceTest()
	{
		FileAccessorMock mockAccessor = new FileAccessorMock();
		LoggingManager.initialize(new FileLogger("test", mockAccessor));
		GameServerConnectionMock mock = new GameServerConnectionMock();
		mock.setUser(new LobbyUser("Test1", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		initializeGameBoardManager(false);
		GameBoardStateManager.getTurnManagerMonitor().executeAction(turnManager -> 
		{
			turnManager.switchTurn("Test1", false);
			turnManager.setShiftedCount(1);
		});
		
		TurnShiftTilesLogic logic = new TurnShiftTilesLogic(mock);
		logic.execute(new TurnShiftTilesDto(3, 2));		
		testNegativeResult(mock, mockAccessor);
	}
	
	/**
	 * Tests that it's possible to shift the tiles if the player already has shifted the tiles once and has used the specific bonus
	 */
	@Test
	public void canShiftTilesWithShiftTwiceBonusWhenTilesAreAlreadyShiftedOnceTest()
	{
		FileAccessorMock mockAccessor = new FileAccessorMock();
		LoggingManager.initialize(new FileLogger("test", mockAccessor));
		GameServerConnectionMock mock = new GameServerConnectionMock();
		mock.setUser(new LobbyUser("Test1", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		initializeGameBoardManager(true);
		GameBoardStateManager.getTurnManagerMonitor().executeAction(turnManager -> 
		{
			turnManager.switchTurn("Test1", false);
			turnManager.setShiftedCount(1);
		});
		
		TurnShiftTilesLogic logic = new TurnShiftTilesLogic(mock);
		TurnShiftTilesDto request = new TurnShiftTilesDto(3, 2);
		logic.execute(request);		
		testPositiveResult(mock, request, mockAccessor);
	}
	
	/**
	 * Tests that it's not possible to shift the tiles if the player already has shifted the tiles twice and has used the specific bonus
	 */
	@Test
	public void cannotShiftTilesWithShiftTwiceBonusWhenTilesAreAlreadyShiftedTiwceTest()
	{
		FileAccessorMock mockAccessor = new FileAccessorMock();
		LoggingManager.initialize(new FileLogger("test", mockAccessor));
		GameServerConnectionMock mock = new GameServerConnectionMock();
		mock.setUser(new LobbyUser("Test1", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		initializeGameBoardManager(true);
		GameBoardStateManager.getTurnManagerMonitor().executeAction(turnManager -> 
		{
			turnManager.switchTurn("Test1", false);
			turnManager.setShiftedCount(3);
		});
		
		TurnShiftTilesLogic logic = new TurnShiftTilesLogic(mock);
		logic.execute(new TurnShiftTilesDto(3, 2));		
		testNegativeResult(mock, mockAccessor);
	}
	
	/**
	 * Tests that it's not possible to shift the tiles if the sent type of tile is invalid
	 */
	@Test
	public void cannotShiftTilesWithInvalidTileTypeTest()
	{
		FileAccessorMock mockAccessor = new FileAccessorMock();
		LoggingManager.initialize(new FileLogger("test", mockAccessor));
		GameServerConnectionMock mock = new GameServerConnectionMock();
		mock.setUser(new LobbyUser("Test1", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		initializeGameBoardManager(false);
		GameBoardStateManager.getTurnManagerMonitor().executeAction(turnManager -> turnManager.switchTurn("Test1", false));
		
		TurnShiftTilesLogic logic = new TurnShiftTilesLogic(mock);
		logic.execute(new TurnShiftTilesDto(3, -1));		
		testNegativeResult(mock, mockAccessor);
		
		logic.execute(new TurnShiftTilesDto(3, -11));
		testNegativeResult(mock, mockAccessor);
	}
	
	/**
	 * Tests that it's not possible to shift the tiles if the sent slot is invalid
	 */
	@Test
	public void cannotShiftTilesWithInvalidSlotTypeTest()
	{
		FileAccessorMock mockAccessor = new FileAccessorMock();
		LoggingManager.initialize(new FileLogger("test", mockAccessor));
		GameServerConnectionMock mock = new GameServerConnectionMock();
		mock.setUser(new LobbyUser("Test1", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		initializeGameBoardManager(false);
		GameBoardStateManager.getTurnManagerMonitor().executeAction(turnManager -> turnManager.switchTurn("Test1", false));
		
		TurnShiftTilesLogic logic = new TurnShiftTilesLogic(mock);
		logic.execute(new TurnShiftTilesDto(-1, 2));		
		testNegativeResult(mock, mockAccessor);
		
		logic.execute(new TurnShiftTilesDto(28, 2));
		testNegativeResult(mock, mockAccessor);
	}
	
	/**
	 * Tests that the tiles are shifted correctly
	 */
	@Test
	public void shiftTilesTest()
	{
		FileAccessorMock mockAccessor = new FileAccessorMock();
		LoggingManager.initialize(new FileLogger("test", mockAccessor));
		GameServerConnectionMock mock = new GameServerConnectionMock();
		mock.setUser(new LobbyUser("Test1", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		initializeGameBoardManager(true);
		GameBoardStateManager.getTurnManagerMonitor().executeAction(turnManager -> turnManager.switchTurn("Test1", false));
		
		List<Tile> oldFields = new ArrayList<Tile>();
		GameBoardStateManager.getBoardMonitor().executeAction(board -> 
		{
			for(int i = 0;i < board.getBoardSize();i++)
				oldFields.add(board.getTile(new Coordinates(3, i)));
		});
		
		TurnShiftTilesLogic logic = new TurnShiftTilesLogic(mock);
		TurnShiftTilesDto request = new TurnShiftTilesDto(3, 2);
		logic.execute(request);		
		testPositiveResult(mock, request, mockAccessor);
		
		//Test that the actual logic of the GameBoard for shifting the tiles is called
		GameBoardStateManager.getBoardMonitor().executeAction(board -> 
		{
			assertEquals(2, board.getTile(new Coordinates(3, 0)).getType());
			for(int i = 0;i < board.getBoardSize() - 1;i++)
				assertSame(oldFields.get(i), board.getTile(new Coordinates(3, i + 1)));
			assertSame(oldFields.get(board.getBoardSize() - 1), board.getTileOutSideOfBoard());
		});
	}
	
	private void initializeGameBoardManager(boolean hasShiftTwiceBonus)
	{
		Player player1 = new Player("Test1", null);
		if(hasShiftTwiceBonus)
		{
			player1.getBoni().add(BonusKind.SHIFT_TWICE);
			player1.setActiveBonus(player1.getBoni().get(0));
		}
		Player player2 = new Player("Test2", null);
		List<Player> players = new ArrayList<Player>();
		players.add(player1);
		players.add(player2);
		GameConfiguration configuration = new GameConfiguration();
		configuration.setSize(new Coordinates(7,7));
		GameBoardStateManager.initialize(configuration, players);
	}
	
	private void testNegativeResult(GameServerConnectionMock mock, FileAccessorMock accessorMock)
	{
		assertEquals(SimpleSuccessResponseDto.class, mock.getLastResponseSent().getClass());
		SimpleSuccessResponseDto response = SimpleSuccessResponseDto.class.cast(mock.getLastResponseSent());
		assertEquals(MessageType.TurnShiftTiles, response.getMessageType());
		assertFalse(response.getSuccess());
		assertNotNull(accessorMock.getWrittenTexts().get("test"));
	}
	
	@SuppressWarnings("unchecked")
	private void testPositiveResult(GameServerConnectionMock mock, TurnShiftTilesDto request, FileAccessorMock accessorMock)
	{
		assertEquals(SimpleSuccessResponseDto.class, mock.getLastResponseSent().getClass());
		SimpleSuccessResponseDto response = SimpleSuccessResponseDto.class.cast(mock.getLastResponseSent());
		assertEquals(MessageType.TurnShiftTiles, response.getMessageType());
		assertTrue(response.getSuccess());
		SocketServerMock<GameServerConnection> mockServer 
			= SocketServerMock.class.cast(GameServerManager.getRunningServer().getServer());
		assertEquals(PlayerActionResponseDto.class, mockServer.getBroadcastedResponse().getClass());
		PlayerActionResponseDto broadcastResponse = PlayerActionResponseDto.class.cast(mockServer.getBroadcastedResponse());
		assertEquals("Test1", broadcastResponse.getPlayer());
		assertSame(request, broadcastResponse.getTurnShiftTiles());
		assertNull(broadcastResponse.getTurnMovePlayer());
		assertNull(broadcastResponse.getTurnUseBonus());
		assertNotNull(accessorMock.getWrittenTexts().get("test"));
	}
}
