package labyrinth.gameServer.stateManagers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import labyrinth.contracts.communication.SocketClientMock;
import labyrinth.contracts.communication.SocketServerMock;
import labyrinth.contracts.communication.dtos.responses.CurrentPlayerResponseDto;
import labyrinth.contracts.communication.dtos.responses.EndGameResponseDto;
import labyrinth.contracts.entities.game.Player;
import labyrinth.contracts.entities.lobby.GameConfiguration;
import labyrinth.gameServer.GameServerConnectionMock;
import labyrinth.gameServer.GameServerManager;

/**
 * TestClass for @see TurnManager
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class TurnManagerTests 
{	
	/**
	 * Tests @see TurnManager.switchTurn
	 */
	@Test
	public void switchTurnTest()
	{
		SocketServerMock<GameServerConnectionMock> mockServer = new SocketServerMock<GameServerConnectionMock>();
		GameServerManager.initialize(new SocketClientMock(), mockServer);
		GameServerManager.startServer("test");
		
		TurnManager turnManager = new TurnManager(5);
		turnManager.setHasMoved(true);
		turnManager.setShiftedCount(2);
		turnManager.setHasUsedBonus(true);
		turnManager.switchTurn("test", false);
		
		assertEquals(0, turnManager.getShiftedCount());
		assertFalse(turnManager.getHasMoved());
		assertFalse(turnManager.getHasUsedBonus());
		assertTrue(turnManager.getIsActivePlayer("test"));
		assertFalse(turnManager.getIsActivePlayer("test123"));
		
		assertEquals(CurrentPlayerResponseDto.class, mockServer.getBroadcastedResponse().getClass());
		CurrentPlayerResponseDto response = CurrentPlayerResponseDto.class.cast(mockServer.getBroadcastedResponse());
		assertEquals("test", response.getPlayer());
	}
	
	/**
	 * Tests that the game is ended if the game length limit is reached
	 */
	@SuppressWarnings("serial")
	@Test
	public void doesSendEndGameIfGameLengthLimitReached()
	{
		SocketServerMock<GameServerConnectionMock> mockServer = new SocketServerMock<GameServerConnectionMock>();
		GameServerManager.initialize(new SocketClientMock(), mockServer);
		GameServerManager.startServer("test");

		GameConfiguration configuration = new GameConfiguration();
		configuration.setGameLengthLimit(2);
		Player player1 = new Player("player1", "green");
		Player player2 = new Player("player2", "red");
		List<Player> players = new ArrayList<Player>() {{ add(player1); add(player2); }};
		GameBoardStateManager.initialize(configuration, players);
		
		TurnManager turnManager = new TurnManager(2);
		turnManager.switchTurn("test", false);	
		assertFalse(EndGameResponseDto.class == mockServer.getBroadcastedResponse().getClass());	
		turnManager.switchTurn("test", false);
		assertFalse(EndGameResponseDto.class == mockServer.getBroadcastedResponse().getClass());
		turnManager.switchTurn("test", false);	
		assertEquals(EndGameResponseDto.class, mockServer.getBroadcastedResponse().getClass());
		EndGameResponseDto response = EndGameResponseDto.class.cast(mockServer.getBroadcastedResponse());
		assertEquals(2, response.getPlayers().size());
		assertEquals("player1", response.getPlayers().get(0).getName());
		assertEquals(0, response.getPlayers().get(0).getScore());
		assertEquals("player2", response.getPlayers().get(1).getName());
		assertEquals(0, response.getPlayers().get(1).getScore());
	}
}
