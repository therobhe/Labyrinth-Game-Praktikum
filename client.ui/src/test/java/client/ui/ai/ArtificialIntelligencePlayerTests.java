package client.ui.ai;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;
import labyrinth.client.ui.ClientConnectionManager;
import labyrinth.client.ui.ai.ArtificialIntelligencePlayer;
import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.SocketClientMock;
import labyrinth.contracts.communication.dtos.BoardDto;
import labyrinth.contracts.communication.dtos.PlayerBoniDto;
import labyrinth.contracts.communication.dtos.PlayerDto;
import labyrinth.contracts.communication.dtos.PlayerTreasuresDto;
import labyrinth.contracts.communication.dtos.TileDto;
import labyrinth.contracts.communication.dtos.requests.TurnMovePlayerDto;
import labyrinth.contracts.communication.dtos.requests.TurnShiftTilesDto;
import labyrinth.contracts.communication.dtos.responses.GameStateResponseDto;
import labyrinth.contracts.communication.dtos.responses.SimpleSuccessResponseDto;
import labyrinth.contracts.communication.listeners.ContinuousActionListener;
import labyrinth.contracts.entities.Coordinates;
import labyrinth.contracts.entities.game.Tile;
import labyrinth.contracts.entities.lobby.GameServerEntry;

/**
 * TestClass for @see ArtificialIntelligencePlayer
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ArtificialIntelligencePlayerTests 
{
	/**
	 * Tests that the AI class is initialized correctly from a GameState
	 */
	@Test
	public void doesInitializeTheStateCorrectly() 
		throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		ClientConnectionManager.initialize(SocketClientMock.class);
		ClientConnectionManager.startGameServerClient(new GameServerEntry("Testserver"));
		SocketClientMock mock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		GameStateResponseDto state = createGameState(new Coordinates(1, 2));
		ArtificialIntelligencePlayer ai = new ArtificialIntelligencePlayer(state, 3, "player1", "player2");
		
		assertEquals(1, mock.getListeners(MessageType.GameState).size());
		assertEquals(ContinuousActionListener.class, mock.getListeners(MessageType.GameState).get(0).getClass());
		assertEquals(1, mock.getListeners(MessageType.CurrentPlayer).size());
		assertEquals(ContinuousActionListener.class, mock.getListeners(MessageType.CurrentPlayer).get(0).getClass());
		
		Field field = ai.getClass().getDeclaredField("tileOutsideOfBoard");
		field.setAccessible(true);
		Tile tile = Tile.class.cast(field.get(ai));
		assertEquals(9, tile.getType());
		
		field = ai.getClass().getDeclaredField("currentPlayer");
		field.setAccessible(true);
		PlayerDto player = PlayerDto.class.cast(field.get(ai));
		assertEquals("player1", player.getName());
		assertEquals("green", player.getColor());
		assertEquals(5, (int)player.getTreasures().getCurrent());
		
		field = ai.getClass().getDeclaredField("board");
		field.setAccessible(true);
		Tile[][] tiles = Tile[][].class.cast(field.get(ai));
		assertEquals(0, tiles[0][0].getType());
		assertEquals(1, tiles[0][1].getType());
		assertEquals(2, tiles[0][2].getType());
		assertEquals(3, tiles[1][0].getType());
		assertEquals(4, tiles[1][1].getType());
		assertEquals(5, tiles[1][2].getType());
		assertEquals(6, tiles[2][0].getType());
		assertEquals(7, tiles[2][1].getType());
		assertEquals(8, tiles[2][2].getType());
	}
	
	/**
	 * Tests that the turn is executes correctly when the AI is initialized with current player as active player
	 */
	@Test
	public void doesExecuteTurnOnInitializationIfCurrentPlayerIsActivePlayerTest()
	{
		ClientConnectionManager.initialize(SocketClientMock.class);
		ClientConnectionManager.startGameServerClient(new GameServerEntry("Testserver"));
		SocketClientMock mock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		GameStateResponseDto state = createGameState(new Coordinates(0, 2));
		@SuppressWarnings("unused")
		ArtificialIntelligencePlayer ai = new ArtificialIntelligencePlayer(state, 3, "player1", "player1");
		
		assertEquals(TurnShiftTilesDto.class, mock.getLastRequest().getClass());
		TurnShiftTilesDto request = TurnShiftTilesDto.class.cast(mock.getLastRequest());
		assertEquals(8, request.getTileType());
		assertEquals(4, request.getSlot());
		
		mock.sendResponse(new SimpleSuccessResponseDto(MessageType.TurnShiftTiles, true));
		
		assertEquals(TurnMovePlayerDto.class, mock.getLastRequest().getClass());
		TurnMovePlayerDto request1 = TurnMovePlayerDto.class.cast(mock.getLastRequest());
		assertEquals(0, request1.getTargetX());
		assertEquals(2, request1.getTargetY());
	}
	
	/**
	 * Tests that the turn is executes correctly when the AI gets a CurrentPlayer response with the current player as active player
	 */
	@Test
	public void doesExecuteTurnOnCurrentPlayerPlayerResponseTest()
	{
		ClientConnectionManager.initialize(SocketClientMock.class);
		ClientConnectionManager.startGameServerClient(new GameServerEntry("Testserver"));
		SocketClientMock mock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		GameStateResponseDto state = createGameState(new Coordinates(2, 0));
		@SuppressWarnings("unused")
		ArtificialIntelligencePlayer ai = new ArtificialIntelligencePlayer(state, 3, "player1", "player1");
		
		assertEquals(TurnShiftTilesDto.class, mock.getLastRequest().getClass());
		TurnShiftTilesDto request = TurnShiftTilesDto.class.cast(mock.getLastRequest());
		assertEquals(8, request.getTileType());
		assertEquals(1, request.getSlot());
		
		mock.sendResponse(new SimpleSuccessResponseDto(MessageType.TurnShiftTiles, true));
		
		assertEquals(TurnMovePlayerDto.class, mock.getLastRequest().getClass());
		TurnMovePlayerDto request1 = TurnMovePlayerDto.class.cast(mock.getLastRequest());
		assertEquals(2, request1.getTargetX());
		assertEquals(0, request1.getTargetY());
	}
	
	private GameStateResponseDto createGameState(Coordinates treasureCoordinates)
	{
		BoardDto board = new BoardDto();
		TileDto tile1 = new TileDto(0, 0, 0);
		TileDto tile2 = new TileDto(0, 1, 1);
		TileDto tile3 = new TileDto(0, 2, 2);
		TileDto tile4 = new TileDto(1, 0, 3);
		TileDto tile5 = new TileDto(1, 1, 4);
		TileDto tile6 = new TileDto(1, 2, 5);
		TileDto tile7 = new TileDto(2, 0, 6);
		TileDto tile8 = new TileDto(2, 1, 7);
		TileDto tile9 = new TileDto(2, 2, 8);
		TileDto tile10 = new TileDto(-1, -1, 9);	
		board.getTiles().add(tile1);
		board.getTiles().add(tile2);
		board.getTiles().add(tile3);
		board.getTiles().add(tile4);
		board.getTiles().add(tile5);
		board.getTiles().add(tile6);
		board.getTiles().add(tile7);
		board.getTiles().add(tile8);
		board.getTiles().add(tile9);
		board.getTiles().add(tile10);
		board.getTiles().stream().filter(tile -> tile.getX() == treasureCoordinates.getX() 
			&& tile.getY() == treasureCoordinates.getY()).findFirst().get().setTreasure(5);
		GameStateResponseDto state = new GameStateResponseDto(board);
		PlayerDto player1 = new PlayerDto("player1", "green", new PlayerTreasuresDto(5, 1), new PlayerBoniDto(0, 0, 0, 0));
		PlayerDto player2 = new PlayerDto("player2", "red", new PlayerTreasuresDto(7, 1), new PlayerBoniDto(0, 0, 0, 0));
		tile1.setPlayer("player1");
		state.getPlayers().add(player1);
		state.getPlayers().add(player2);
		return state;
	}
}
