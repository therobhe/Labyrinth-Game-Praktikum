package labyrinth.gameServer.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.requests.TurnMovePlayerDto;
import labyrinth.contracts.communication.dtos.responses.SimpleSuccessResponseDto;
import labyrinth.contracts.entities.game.Player;
import labyrinth.contracts.entities.Coordinates;
import labyrinth.contracts.entities.lobby.GameConfiguration;
import labyrinth.contracts.entities.lobby.LobbyUser;
import labyrinth.contracts.entities.lobby.LobbyUserKind;
import labyrinth.contracts.entities.lobby.UserPermission;
import labyrinth.contracts.utilities.FileAccessorMock;
import labyrinth.contracts.utilities.FileLogger;
import labyrinth.gameServer.GameServerConnectionMock;
import labyrinth.gameServer.LoggingManager;
import labyrinth.gameServer.stateManagers.GameBoardStateManager;

/**
 * TestClass for @see TurnMovePlayerLogic
 * @author Eugen Cravtov-Grandl
 * @version 1.0
 */
public final class TurnMovePlayerLogicTests extends GameServerLogicTestBase 
{
	/**
	 * tests that it is not possible to move the Player out of Bounds
	 */
	@Test
	public void cannotMovePlayerOutOfBounds()
	{
		FileAccessorMock mockAccessor = new FileAccessorMock();
		LoggingManager.initialize(new FileLogger("test", mockAccessor));
		GameServerConnectionMock mock = new GameServerConnectionMock();
		mock.setUser(new LobbyUser("Hans", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		initializeGameBoardManager(false);
		GameBoardStateManager.getTurnManagerMonitor().executeAction(turnManager -> turnManager.switchTurn("Hans", false));
		TurnMovePlayerLogic movePlayer = new TurnMovePlayerLogic(mock);
		movePlayer.execute(new TurnMovePlayerDto(6, 2));
		testNegativeResult(mock, mockAccessor);
	}
	
	/**
	 * tests that it is not possible to move the Player twice in a Turn
	 */
	@Test
	public void cannotMoveTwiceInATurn()
	{
		FileAccessorMock mockAccessor = new FileAccessorMock();
		LoggingManager.initialize(new FileLogger("test", mockAccessor));
		GameServerConnectionMock mock = new GameServerConnectionMock();
		mock.setUser(new LobbyUser("Hans", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		initializeGameBoardManager(false);
		GameBoardStateManager.getTurnManagerMonitor().executeAction(turnManager -> turnManager.switchTurn("Hans", false));
		TurnMovePlayerLogic movePlayer = new TurnMovePlayerLogic(mock);
		movePlayer.execute(new TurnMovePlayerDto(1, 2));
		movePlayer.execute(new TurnMovePlayerDto(3, 2));
		testNegativeResult(mock, mockAccessor);
	}
	
	/**
	 * tests that it is not possible to move the Player to a Field with another Player
	 */
	@Test
	public void cannotMoveToAUsedField()
	{
		FileAccessorMock mockAccessor = new FileAccessorMock();
		LoggingManager.initialize(new FileLogger("test", mockAccessor));
		GameServerConnectionMock mock = new GameServerConnectionMock();
		mock.setUser(new LobbyUser("Hans", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		initializeGameBoardManager(false);
		GameBoardStateManager.getTurnManagerMonitor().executeAction(turnManager -> turnManager.switchTurn("Hans", false));
		TurnMovePlayerLogic movePlayer = new TurnMovePlayerLogic(mock);
		movePlayer.execute(new TurnMovePlayerDto(4, 2));
		testNegativeResult(mock, mockAccessor);
	}

	private void initializeGameBoardManager(boolean hasShiftTwiceBonus)
	{
		Player player1 = new Player("Hans", null);
		Player player2 = new Player("Peter", null);
		List<Player> players = new ArrayList<Player>();
		players.add(player1);
		players.add(player2);
		GameConfiguration configuration = new GameConfiguration();
		configuration.setSize(new Coordinates(7, 7));
		GameBoardStateManager.initialize(configuration, players);
	}
	
	private void testNegativeResult(GameServerConnectionMock mock, FileAccessorMock accessorMock)
	{
		assertEquals(SimpleSuccessResponseDto.class, mock.getLastResponseSent().getClass());
		SimpleSuccessResponseDto response = SimpleSuccessResponseDto.class.cast(mock.getLastResponseSent());
		assertEquals(MessageType.TurnMovePlayer, response.getMessageType());
		assertFalse(response.getSuccess());
		assertNotNull(accessorMock.getWrittenTexts().get("test"));
	}
}
