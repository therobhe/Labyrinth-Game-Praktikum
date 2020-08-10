package labyrinth.gameServer.stateManagers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import labyrinth.contracts.communication.*;
import labyrinth.contracts.communication.dtos.*;
import labyrinth.contracts.communication.dtos.responses.EndGameResponseDto;
import labyrinth.contracts.communication.dtos.responses.GameStateResponseDto;
import labyrinth.contracts.entities.Coordinates;
import labyrinth.contracts.entities.game.BonusKind;
import labyrinth.contracts.entities.game.GameBoard;
import labyrinth.contracts.entities.game.Player;
import labyrinth.contracts.entities.game.Tile;
import labyrinth.contracts.entities.lobby.*;
import labyrinth.gameServer.*;

/**
 * TestClass for @see GameBoardStateManager
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class GameBoardStateManagerTests 
{
	/**
	 * Sets up the tests
	 */
	@BeforeAll
	public static void setUp() 
	{
		SocketServerMock<GameServerConnectionMock> mockServer = new SocketServerMock<GameServerConnectionMock>();
		GameServerManager.initialize(new SocketClientMock(), mockServer);
		GameServerManager.startServer("test");
	}
	
	/**
	 * Tests that on a game state update a EndGame-Response is sent when a player has no remaining treasures
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void doesSendEndGameOnStateUpdateIfPlayerHasNoRemainingTreasures()
	{
		SocketServerMock<GameServerConnection> mockServer 
			= SocketServerMock.class.cast(GameServerManager.getRunningServer().getServer());
		List<Player> players = initializePlayers();
		players.get(0).getTargetTreasures().add(1);
		players.get(1).getTargetTreasures().clear();
		players.get(2).getTargetTreasures().add(1);
		GameConfiguration configuration = new GameConfiguration();
		configuration.setTreasureCount(0);
		GameBoardStateManager.initialize(configuration, players);
		List<IGameServerConnection> connections = new ArrayList<IGameServerConnection>();
		GameServerConnectionMock connection1 = new GameServerConnectionMock();
		GameServerConnectionMock connection2 = new GameServerConnectionMock();
		connection1.setUser(new LobbyUser("player1", "green", UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		connection2.setUser(new LobbyUser("player2", "blue", UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		connections.add(connection1);
		connections.add(connection2);
		
		GameBoardStateManager.commitCurrentGameState(connections);
		assertEquals(EndGameResponseDto.class, mockServer.getBroadcastedResponse().getClass());
		EndGameResponseDto response = EndGameResponseDto.class.cast(mockServer.getBroadcastedResponse());
		assertEquals(3, response.getPlayers().size());
		assertEquals("player1", response.getPlayers().get(0).getName());
		assertEquals("player2", response.getPlayers().get(1).getName());
		assertEquals("player3", response.getPlayers().get(2).getName());
		assertEquals(0, response.getPlayers().get(0).getScore());
		assertEquals(0, response.getPlayers().get(1).getScore());
		assertEquals(0, response.getPlayers().get(2).getScore());
		assertFalse(response.getPlayers().get(0).getDisconnected());
		assertFalse(response.getPlayers().get(1).getDisconnected());
		assertFalse(response.getPlayers().get(2).getDisconnected());
		
	}
	
	/**
	 * Tests that the first player is selected as current player when initializing the GameBoardStateManager
	 */
	@Test
	public void doesSwitchPlayerTurnOnInitializingTest()
	{
		List<Player> players = initializePlayers();
		GameBoardStateManager.initialize(new GameConfiguration(), players);
		
		assertTrue(GameBoardStateManager.getTurnManagerMonitor()
			.executeFunction(stateManager -> stateManager.getIsActivePlayer("player1")));
	}
	
	/**
	 * Tests that the GameServerAchievementsManager is also initialized when initializing the GameBoardStateManager
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void doesInitializeGameServerAchievementsManagerOnInitializingTest() 
		throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		List<Player> players = initializePlayers();
		GameBoardStateManager.initialize(new GameConfiguration(), players);
		
		Field field = GameServerAchievementManager.class.getDeclaredField("playerAchievements");
		field.setAccessible(true);
		Map<String, ServerAchievementsWrapper> playerAchievements = Map.class.cast(field.get(null));
		assertNotNull(playerAchievements.get("player1"));
		assertNotNull(playerAchievements.get("player2"));
		assertNotNull(playerAchievements.get("player3"));
	}
	
	/**
	 * Tests that the current GameState is also initialized when initializing the GameBoardStateManager
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void doesAlsoInitializeTheGameStateOnInitializingTest()
	{
		SocketServerMock<GameServerConnection> mockServer 
			= SocketServerMock.class.cast(GameServerManager.getRunningServer().getServer());
		List<Player> players = initializePlayers();
		GameBoardStateManager.initialize(new GameConfiguration(), players);
		
		assertEquals(GameStateResponseDto.class, mockServer.getBroadcastedResponse().getClass());
		GameStateResponseDto response = GameStateResponseDto.class.cast(mockServer.getBroadcastedResponse());
		assertEquals(3, response.getPlayers().size());
		
		//Test player1
		PlayerDto playerDto1 = response.getPlayers().get(0);
		testPlayerDtoCore(playerDto1, players.get(0), true);
		
		//Test player2
		PlayerDto playerDto2 = response.getPlayers().get(1);
		testPlayerDtoCore(playerDto2, players.get(1), true);
		
		//Test player23
		PlayerDto playerDto3 = response.getPlayers().get(2);
		testPlayerDtoCore(playerDto3, players.get(2), true);
		
		//Test tiles
		testTiles(response);
	}
	
	/**
	 * Tests that on committing the current GameState the changed players are sent to all clients correctly
	 */
	@Test
	public void commitCurrentGameStateDoesSentUpdatedPlayersTest()
	{
		List<Player> players = initializePlayers();
		GameBoardStateManager.initialize(new GameConfiguration(), players);
		List<IGameServerConnection> connections = new ArrayList<IGameServerConnection>();
		GameServerConnectionMock connection1 = new GameServerConnectionMock();
		GameServerConnectionMock connection2 = new GameServerConnectionMock();
		connection1.setUser(new LobbyUser("player1", "green", UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		connection2.setUser(new LobbyUser("player2", "blue", UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		connections.add(connection1);
		connections.add(connection2);
			
		//Change the game state and commit the changes
		applyAndCommitChanges(players.get(0), players.get(1), connections);
		
		//Test connection1 player information
		assertEquals(GameStateResponseDto.class, connection1.getLastResponseSent().getClass());
		GameStateResponseDto response1 = GameStateResponseDto.class.cast(connection1.getLastResponseSent());
		assertEquals(2, response1.getPlayers().size());		
		PlayerDto playerDto1 = response1.getPlayers().get(0);
		testPlayerDtoCore(playerDto1, players.get(0), true);
		PlayerDto playerDto2 = response1.getPlayers().get(1);
		testPlayerDtoCore(playerDto2, players.get(1), false);
		
		//Test connection2 player information
		assertEquals(GameStateResponseDto.class, connection2.getLastResponseSent().getClass());
		GameStateResponseDto response2 = GameStateResponseDto.class.cast(connection2.getLastResponseSent());
		assertEquals(2, response2.getPlayers().size());	
		playerDto1 = response2.getPlayers().get(0);
		testPlayerDtoCore(playerDto1, players.get(0), false);	
		playerDto2 = response2.getPlayers().get(1);
		testPlayerDtoCore(playerDto2, players.get(1), true);
	}
	
	/**
	 * Tests that on committing the current GameState the changed tiles are sent to all clients correctly
	 */
	@Test
	public void commitCurrentGameStateDoesSentUpdatedTilesTest()
	{
		List<Player> players = initializePlayers();
		GameBoardStateManager.initialize(new GameConfiguration(), players);
		List<IGameServerConnection> connections = new ArrayList<IGameServerConnection>();
		GameServerConnectionMock connection1 = new GameServerConnectionMock();
		GameServerConnectionMock connection2 = new GameServerConnectionMock();
		connection1.setUser(new LobbyUser("player1", "green", UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		connection2.setUser(new LobbyUser("player2", "blue", UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		connections.add(connection1);
		connections.add(connection2);
			
		//Change the game state and commit the changes
		applyAndCommitChanges(players.get(0), players.get(1), connections);
		
		//Test the responses for the changed tiles (Sometimes fails due to the random generation of the board)
		assertEquals(GameStateResponseDto.class, connection1.getLastResponseSent().getClass());
		GameStateResponseDto response1 = GameStateResponseDto.class.cast(connection2.getLastResponseSent());
		testGameStateForChangedTiles(response1);	
		assertEquals(GameStateResponseDto.class, connection2.getLastResponseSent().getClass());
		GameStateResponseDto response2 = GameStateResponseDto.class.cast(connection2.getLastResponseSent());
		testGameStateForChangedTiles(response2);
	}
	
	/**
	 * Tests @see GameBoardStateManager.onUserLeaving for a player
	 */
	@Test
	public void handlePlayerLeaveTest()
	{
		List<Player> players = initializePlayers();
		GameBoardStateManager.initialize(new GameConfiguration(), players);
		GameBoardStateManager.getTurnManagerMonitor().executeAction(turnManager -> turnManager.switchTurn("player1", false));
		GameBoardStateManager.onUserLeaving("player1");
		
		assertTrue(players.get(0).getIsDisconnected());
		assertTrue(GameBoardStateManager.getTurnManagerMonitor().executeFunction(
			turnManager -> turnManager.getIsActivePlayer("player2")));
	}
	
	/**
	 * Tests @see GameBoardStateManager.onUserLeaving for a player
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void doesEndGameOnPlayerLeavingWhenRemainingPlayerCountBelowTwoTest()
	{
		SocketServerMock<GameServerConnection> mockServer 
			= SocketServerMock.class.cast(GameServerManager.getRunningServer().getServer());
		List<Player> players = initializePlayers();
		GameBoardStateManager.initialize(new GameConfiguration(), players);
		GameBoardStateManager.getTurnManagerMonitor().executeAction(turnManager -> turnManager.switchTurn("player1", false));
		GameBoardStateManager.onUserLeaving("player1");
		GameBoardStateManager.onUserLeaving("player2");
		
		assertEquals(EndGameResponseDto.class, mockServer.getBroadcastedResponse().getClass());
		EndGameResponseDto response = EndGameResponseDto.class.cast(mockServer.getBroadcastedResponse());
		assertEquals(3, response.getPlayers().size());
		assertEquals("player1", response.getPlayers().get(0).getName());
		assertEquals("player2", response.getPlayers().get(1).getName());
		assertEquals("player3", response.getPlayers().get(2).getName());
		assertEquals(0, response.getPlayers().get(0).getScore());
		assertEquals(0, response.getPlayers().get(1).getScore());
		assertEquals(0, response.getPlayers().get(2).getScore());
		assertTrue(response.getPlayers().get(0).getDisconnected());
		assertTrue(response.getPlayers().get(1).getDisconnected());
		assertFalse(response.getPlayers().get(2).getDisconnected());
	}
	
	/**
	 * Tests @see GameBoardStateManager.onUserLeaving for a spectator
	 */
	@Test
	public void handleSpectatorLeaveTest()
	{
		List<Player> players = initializePlayers();
		GameBoardStateManager.initialize(new GameConfiguration(), players);
		List<IGameServerConnection> connections = new ArrayList<IGameServerConnection>();
		GameServerConnectionMock connection1 = new GameServerConnectionMock();
		GameServerConnectionMock connection2 = new GameServerConnectionMock();
		connection1.setUser(new LobbyUser("player1", "green", UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		connection2.setUser(new LobbyUser("player2", "blue", UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		connections.add(connection1);
		connections.add(connection2);
		GameBoardStateManager.onUserLeaving("user3");
		
		//Test the responses for the changed tiles
		assertNull(connection1.getLastResponseSent());
		assertNull(connection2.getLastResponseSent());
	}
	
	private static List<Player> initializePlayers()
	{
		List<Player> players = new ArrayList<Player>(); 
		Player player1 = new Player("player1", "green");
		Player player2 = new Player("player2", "blue");
		Player player3 = new Player("player3", "red");
		player2.getBoni().add(BonusKind.SHIFT_SOLID);
		players.add(player1);
		players.add(player2);
		players.add(player3);
		return players;
	}
	
	private static void testGameStateForChangedTiles(GameStateResponseDto response)
	{
		assertTrue(response.getBoard().getTiles().stream().anyMatch(tile -> tile.getX() == 1 && tile.getY() == 0));
		assertTrue(response.getBoard().getTiles().stream().anyMatch(tile -> tile.getX() == 1 && tile.getY() == 1));
		assertTrue(response.getBoard().getTiles().stream().anyMatch(tile -> tile.getX() == 1 && tile.getY() == 2));
		assertTrue(response.getBoard().getTiles().stream().anyMatch(tile -> tile.getX() == 1 && tile.getY() == 3));
		assertTrue(response.getBoard().getTiles().stream().anyMatch(tile -> tile.getX() == 1 && tile.getY() == 4));
		assertTrue(response.getBoard().getTiles().stream().anyMatch(tile -> tile.getX() == 1 && tile.getY() == 5));
		assertTrue(response.getBoard().getTiles().stream().anyMatch(tile -> tile.getX() == 1 && tile.getY() == 6));
		assertTrue(response.getBoard().getTiles().stream().anyMatch(tile -> tile.getX() == 3 && tile.getY() == 0));
		assertTrue(response.getBoard().getTiles().stream().anyMatch(tile -> tile.getX() == 3 && tile.getY() == 1));
		assertTrue(response.getBoard().getTiles().stream().anyMatch(tile -> tile.getX() == 3 && tile.getY() == 2));
		assertTrue(response.getBoard().getTiles().stream().anyMatch(tile -> tile.getX() == 3 && tile.getY() == 3));
	}
	
	private static void applyAndCommitChanges(Player player1, Player player2, List<IGameServerConnection> connections)
	{
		player1.setIsDisconnected(true);
		player2.setPoints(12);
		GameBoard board = GameBoardStateManager.getBoardMonitor().executeFunction(gameBoard -> gameBoard);
		board.tryShiftTile(false, 1, 5);		
		board.getTile(new Coordinates(3, 0)).setBonus(BonusKind.SWAP);
		board.getTile(new Coordinates(3, 1)).setPlayer(player1);
		board.getTile(new Coordinates(3, 2)).setPlayerBase(player1.getPlayerName());
		board.getTile(new Coordinates(3, 3)).setTreasure(1);
		
		//Commit the game state
		GameBoardStateManager.commitCurrentGameState(connections);
	}
	
	private static void testPlayerDtoCore(PlayerDto playerDto, Player player, boolean shouldSentCurrentTreasure)
	{
		assertEquals(player.getPlayerName(), playerDto.getName());
		assertEquals(player.getPlayerColor(), playerDto.getColor());
		assertEquals(player.getPoints(), playerDto.getScore());
		assertEquals(player.getIsDisconnected(), playerDto.getDisconnected());
		assertEquals(player.getTargetTreasures().size(), playerDto.getTreasures().getRemaining());
		if(shouldSentCurrentTreasure)
			assertEquals(player.getNextTreasure(), playerDto.getTreasures().getCurrent());
		else
			assertNull(playerDto.getTreasures().getCurrent());
		
		int swapCount = (int)player.getBoni().stream().filter(bonus -> bonus == BonusKind.SWAP).count();
		int beamCount = (int)player.getBoni().stream().filter(bonus -> bonus == BonusKind.BEAM).count();
		int shiftSolidCount = (int)player.getBoni().stream().filter(bonus -> bonus == BonusKind.SHIFT_SOLID).count();
		int shiftTwiceCount = (int)player.getBoni().stream().filter(bonus -> bonus == BonusKind.SHIFT_TWICE).count();
		
		assertEquals(beamCount, playerDto.getBoni().getBeam());
		assertEquals(shiftSolidCount, playerDto.getBoni().getShiftSolid());
		assertEquals(shiftTwiceCount, playerDto.getBoni().getShiftTwice());
		assertEquals(swapCount, playerDto.getBoni().getSwap());
	}
	
	private static void testTiles(GameStateResponseDto response)
	{
		GameBoard board = GameBoardStateManager.getBoardMonitor().executeFunction(gameBoard -> gameBoard);
	
		for(int i = 0;i < board.getBoardSize();i++)
		{
			for(int j = 0;j < board.getBoardSize();j++)
			{
				Tile field = board.getTile(new Coordinates(i, j));
				TileDto tile = getTile(response.getBoard(), i, j);
				assertNotNull(tile);
				assertEquals(field.getType(), tile.getType());
				
				if(field.getBonus() != null)
					assertEquals(field.getBonus(), tile.getBoni());
				else
					assertNull(field.getBonus());
				
				if(field.getPlayer() != null)
					assertEquals(field.getPlayer().getPlayerName(), tile.getPlayer());
				else
					assertNull(field.getPlayer());
				
				if(field.getPlayerBase() != null)
					assertEquals(field.getPlayerBase(), tile.getPlayerBase());
				else
					assertNull(field.getPlayerBase());
				
				if(field.getTreasure() != null)
					assertEquals(field.getTreasure(), tile.getTreasure());
				else
					assertNull(field.getTreasure());
			}
		}
	}
	
	private static TileDto getTile(BoardDto board, int x, int y)
	{
		for(TileDto tile : board.getTiles())
			if(tile.getX() == x && tile.getY() == y)
				return tile;
		return null;
	}
}
