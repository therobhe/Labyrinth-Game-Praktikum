package client.ui.viewModels;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import de.saxsys.mvvmfx.utils.commands.Command;
import labyrinth.client.ui.ClientConnectionManager;
import labyrinth.client.ui.viewModels.GameViewModel;
import labyrinth.client.ui.viewModels.PlayerBindingModel;
import labyrinth.client.ui.viewModels.TileBindingModel;
import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.SocketClientMock;
import labyrinth.contracts.communication.dtos.BoardDto;
import labyrinth.contracts.communication.dtos.PlayerBoniDto;
import labyrinth.contracts.communication.dtos.PlayerDto;
import labyrinth.contracts.communication.dtos.PlayerTreasuresDto;
import labyrinth.contracts.communication.dtos.TileDto;
import labyrinth.contracts.communication.dtos.requests.TurnMovePlayerDto;
import labyrinth.contracts.communication.dtos.requests.TurnShiftTilesDto;
import labyrinth.contracts.communication.dtos.requests.TurnUseBonusDto;
import labyrinth.contracts.communication.dtos.responses.GameStateResponseDto;
import labyrinth.contracts.communication.dtos.responses.SimpleSuccessResponseDto;
import labyrinth.contracts.communication.listeners.ContinuousUiThreadActionListener;
import labyrinth.contracts.communication.listeners.UiThreadActionListener;
import labyrinth.contracts.entities.game.BonusKind;
import labyrinth.contracts.entities.lobby.GameServerEntry;

/**
 * TestClass for @see GameViewModel
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class GameViewModelTests 
{
	/**
	 * Sets up the tests
	 */
	@BeforeEach
	public void setUp()
	{
		ClientConnectionManager.initialize(SocketClientMock.class);
		ClientConnectionManager.startGameServerClient(new GameServerEntry("Testserver"));
		PlayerBindingModel.setIsUnitTest(true);
		TileBindingModel.setIsUnitTest(true);
		UiThreadActionListener.setIsUnitTest(true);
		ContinuousUiThreadActionListener.setIsUnitTest(true);
	}
	
	/**
	 * Tests that the correct exception is thrown when navigating to the ViewModel without the GameState-Parameter
	 */
	@Test
	public void throwsCorrectExceptionWhenNavigatingWithoutGameStateParameterTest()
	{
		GameViewModel viewModel = new GameViewModel();
		assertThrows(IllegalArgumentException.class, () -> viewModel.handleNavigation(null), 
			"Expected exception of type IllegalArgumentException. has not been thrown.");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(GameViewModel.ACTIVE_PLAYER_PARAMETER, "Test");
		parameters.put(GameViewModel.BOARD_SIZE_PARAMETER, 7);
		parameters.put(GameViewModel.IS_AI_PARAMETER, false);
		assertThrows(IllegalArgumentException.class, () -> viewModel.handleNavigation(parameters), 
			"Expected exception of type IllegalArgumentException. has not been thrown.");
	}
	
	/**
	 * Tests that the correct exception is thrown when navigating to the ViewModel without the ActivePlayer-Parameter
	 */
	@Test
	public void throwsCorrectExceptionWhenNavigatingWithoutActivePlayerParameterTest()
	{
		GameViewModel viewModel = new GameViewModel();
		assertThrows(IllegalArgumentException.class, () -> viewModel.handleNavigation(null), 
			"Expected exception of type IllegalArgumentException. has not been thrown.");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(GameViewModel.GAME_STATE_PARAMETER, new GameStateResponseDto(new BoardDto()));
		parameters.put(GameViewModel.BOARD_SIZE_PARAMETER, 7);
		parameters.put(GameViewModel.IS_AI_PARAMETER, false);
		assertThrows(IllegalArgumentException.class, () -> viewModel.handleNavigation(parameters), 
			"Expected exception of type IllegalArgumentException. has not been thrown.");
	}
	
	/**
	 * Tests that the correct exception is thrown when navigating to the ViewModel without the BoardSize-Parameter
	 */
	@Test
	public void throwsCorrectExceptionWhenNavigatingWithoutBoardSizeParameterTest()
	{
		GameViewModel viewModel = new GameViewModel();
		assertThrows(IllegalArgumentException.class, () -> viewModel.handleNavigation(null), 
			"Expected exception of type IllegalArgumentException. has not been thrown.");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(GameViewModel.ACTIVE_PLAYER_PARAMETER, "Test");
		parameters.put(GameViewModel.GAME_STATE_PARAMETER, new GameStateResponseDto(new BoardDto()));
		parameters.put(GameViewModel.IS_AI_PARAMETER, false);
		assertThrows(IllegalArgumentException.class, () -> viewModel.handleNavigation(parameters), 
			"Expected exception of type IllegalArgumentException. has not been thrown.");
	}
	
	/**
	 * Tests that the correct exception is thrown when navigating to the ViewModel without the BoardSize-Parameter
	 */
	@Test
	public void throwsCorrectExceptionWhenNavigatingWithoutIsAIParameterTest()
	{
		GameViewModel viewModel = new GameViewModel();
		assertThrows(IllegalArgumentException.class, () -> viewModel.handleNavigation(null), 
			"Expected exception of type IllegalArgumentException. has not been thrown.");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(GameViewModel.ACTIVE_PLAYER_PARAMETER, "Test");
		parameters.put(GameViewModel.BOARD_SIZE_PARAMETER, 7);
		parameters.put(GameViewModel.GAME_STATE_PARAMETER, new GameStateResponseDto(new BoardDto()));
		assertThrows(IllegalArgumentException.class, () -> viewModel.handleNavigation(parameters), 
			"Expected exception of type IllegalArgumentException. has not been thrown.");
	}
	
	/**
	 * Tests that the Player and TileBindingModels are updated correctly when a GameStateResponse arrives
	 */
	@Test
	public void doesUpdateBindingModelsOnGameStateResponseDto()
	{
		GameViewModel viewModel = new GameViewModel();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(GameViewModel.ACTIVE_PLAYER_PARAMETER, "Test");
		BoardDto board = new BoardDto();
		TileDto tile1 = new TileDto(0, 0, 1);
		tile1.setBoni(BonusKind.BEAM);
		TileDto tile2 = new TileDto(0, 1, 2);
		tile2.setPlayer("player2");
		TileDto tile3 = new TileDto(1, 0, 3);
		tile3.setTreasure(1);
		TileDto tile4 = new TileDto(1, 1, 4);
		tile4.setPlayerBase("player3");
		board.getTiles().add(tile1);
		board.getTiles().add(tile2);
		board.getTiles().add(tile3);
		board.getTiles().add(tile4);
		GameStateResponseDto state = new GameStateResponseDto(board);
		PlayerDto player1 = new PlayerDto("player1", "blue", new PlayerTreasuresDto(1, 5), new PlayerBoniDto(0,0,0,1));
		PlayerDto player2 = new PlayerDto("player2", "green", new PlayerTreasuresDto(2, 5), new PlayerBoniDto(0,0,1,1));
		PlayerDto player3 = new PlayerDto("player3", "red", new PlayerTreasuresDto(null, 5), new PlayerBoniDto(1,0,1,1));
		state.getPlayers().add(player1);
		state.getPlayers().add(player2);
		state.getPlayers().add(player3);	
		parameters.put(GameViewModel.GAME_STATE_PARAMETER, state);
		parameters.put(GameViewModel.BOARD_SIZE_PARAMETER, 2);
		parameters.put(GameViewModel.IS_AI_PARAMETER, false);
		viewModel.handleNavigation(parameters);
		SocketClientMock mock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		tile1.setPlayer("player1");
		tile2.setTreasure(4);
		state.getPlayers().remove(2);
		player3 = new PlayerDto("player3", "red", new PlayerTreasuresDto(2, 7), new PlayerBoniDto(1,2,3,4));
		state.getPlayers().add(player3);
		mock.sendResponse(state);
		
		//Test PlayerModels
		testPlayer(player1, viewModel.getPlayer1());
		testPlayer(player2, viewModel.getPlayer2());
		testPlayer(player3, viewModel.getPlayer3());
		assertNull(viewModel.getPlayer4());
		
		//Test TileModels
		assertEquals(4, viewModel.getTiles().size());
		testTile(tile1, viewModel.getTiles().get(0), "blue", null);
		testTile(tile2, viewModel.getTiles().get(1), "green", null);
		testTile(tile3, viewModel.getTiles().get(2), null, null);
		testTile(tile4, viewModel.getTiles().get(3), null, "red");
	}
	
	/**
	 * Tests that the Player and TileBindingModels are initialized correctly from the GameState
	 */
	@Test
	public void doesInitializeGameStateOnNavigatingToTheViewModel()
	{
		PlayerBindingModel.setIsUnitTest(true);
		GameViewModel viewModel = new GameViewModel();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(GameViewModel.ACTIVE_PLAYER_PARAMETER, "Test");
		BoardDto board = new BoardDto();
		TileDto tile1 = new TileDto(0, 0, 1);
		tile1.setBoni(BonusKind.BEAM);
		TileDto tile2 = new TileDto(0, 1, 2);
		tile2.setPlayer("player2");
		TileDto tile3 = new TileDto(1, 0, 3);
		tile3.setTreasure(1);
		TileDto tile4 = new TileDto(1, 1, 4);
		tile4.setPlayerBase("player3");
		board.getTiles().add(tile1);
		board.getTiles().add(tile2);
		board.getTiles().add(tile3);
		board.getTiles().add(tile4);
		GameStateResponseDto state = new GameStateResponseDto(board);
		PlayerDto player1 = new PlayerDto("player1", "blue", new PlayerTreasuresDto(1, 5), new PlayerBoniDto(0,0,0,1));
		PlayerDto player2 = new PlayerDto("player2", "green", new PlayerTreasuresDto(2, 5), new PlayerBoniDto(0,0,1,1));
		PlayerDto player3 = new PlayerDto("player3", "red", new PlayerTreasuresDto(null, 5), new PlayerBoniDto(1,0,1,1));
		state.getPlayers().add(player1);
		state.getPlayers().add(player2);
		state.getPlayers().add(player3);	
		parameters.put(GameViewModel.GAME_STATE_PARAMETER, state);
		parameters.put(GameViewModel.BOARD_SIZE_PARAMETER, 2);
		parameters.put(GameViewModel.IS_AI_PARAMETER, false);
		viewModel.handleNavigation(parameters);	
		
		//Test PlayerModels
		testPlayer(player1, viewModel.getPlayer1());
		testPlayer(player2, viewModel.getPlayer2());
		testPlayer(player3, viewModel.getPlayer3());
		assertNull(viewModel.getPlayer4());
		
		//Test TileModels
		assertEquals(4, viewModel.getTiles().size());
		testTile(tile1, viewModel.getTiles().get(0), null, null);
		testTile(tile2, viewModel.getTiles().get(1), "green", null);
		testTile(tile3, viewModel.getTiles().get(2), null, null);
		testTile(tile4, viewModel.getTiles().get(3), null, "red");
	}
	
	/**
	 * Tests @see GameViewModel.useShiftTwiceBonusCommand
	 */
	@Test
	public void useShiftSolidBonusCommandTest() 
		throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{ useBonusCommandTestCore(BonusKind.SHIFT_SOLID); }
	
	/**
	 * Tests @see GameViewModel.useShiftSolidBonusCommand
	 */
	@Test
	public void useShiftTwiceBonusCommandTest() 
		throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{ useBonusCommandTestCore(BonusKind.SHIFT_TWICE); }
	
	/**
	 * Tests @see GameViewModel.useBeamBonusCommand
	 */
	@Test
	public void useBeamSolidBonusCommandTest() 
		throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{ useBonusCommandTestCore(BonusKind.BEAM); }
	
	/**
	 * Tests @see GameViewModel.useSwapBonusCommand
	 */
	@Test
	public void useSwapBonusCommandTest() 
		throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{ useBonusCommandTestCore(BonusKind.SWAP); }
	
	/**
	 * Tests shifting the tiles
	 */
	@Test
	public void shiftTilesTest() 
		throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	{
		SocketClientMock mock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		GameViewModel viewModel = new GameViewModel();
		initializeViewModel(viewModel);
		viewModel.getPlayer1().setActiveBonus(BonusKind.SHIFT_SOLID);
		viewModel.doTileAction(1, 0);
		
		assertEquals(1, mock.getListeners(MessageType.TurnShiftTiles).size());
		assertEquals(TurnShiftTilesDto.class, mock.getLastRequest().getClass());
		TurnShiftTilesDto request = TurnShiftTilesDto.class.cast(mock.getLastRequest());
		assertEquals(1, request.getSlot());
		assertEquals(0, request.getTileType());
		
		Field field = viewModel.getClass().getDeclaredField("shiftedCount");
		field.setAccessible(true);
		assertEquals(1, field.get(viewModel));
		
		//Active bonus must be reset if using the bonus was not valid
		mock.sendResponse(new SimpleSuccessResponseDto(MessageType.TurnShiftTiles, false));
		assertEquals(0, field.get(viewModel));
	}
	
	/**
	 * Tests that it's possible to shift tiles twice with the Bonus SHIFT_TWICE
	 */
	@Test
	public void canShiftTwiceWithBonusTest() 
		throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	{
		SocketClientMock mock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		GameViewModel viewModel = new GameViewModel();
		initializeViewModel(viewModel);
		viewModel.getPlayer1().setActiveBonus(BonusKind.SHIFT_TWICE);
		
		viewModel.doTileAction(1, 0);
		assertEquals(1, mock.getListeners(MessageType.TurnShiftTiles).size());
		assertEquals(TurnShiftTilesDto.class, mock.getLastRequest().getClass());
		TurnShiftTilesDto request = TurnShiftTilesDto.class.cast(mock.getLastRequest());
		assertEquals(1, request.getSlot());
		assertEquals(0, request.getTileType());
		
		mock.sendResponse(new SimpleSuccessResponseDto(MessageType.TurnShiftTiles, true));		
		Field field = viewModel.getClass().getDeclaredField("shiftedCount");
		field.setAccessible(true);
		assertEquals(1, field.get(viewModel));
		
		viewModel.doTileAction(1, 0);
		assertEquals(1, mock.getListeners(MessageType.TurnShiftTiles).size());
		assertEquals(TurnShiftTilesDto.class, mock.getLastRequest().getClass());
		request = TurnShiftTilesDto.class.cast(mock.getLastRequest());
		assertEquals(1, request.getSlot());
		assertEquals(0, request.getTileType());
		assertEquals(2, field.get(viewModel));
	}
	
	/**
	 * Tests moving the player
	 */
	@Test
	public void movePlayerTest() 
		throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	{
		SocketClientMock mock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		GameViewModel viewModel = new GameViewModel();
		initializeViewModel(viewModel);
		viewModel.getPlayer1().setActiveBonus(BonusKind.BEAM);
		Field field = viewModel.getClass().getDeclaredField("shiftedCount");
		field.setAccessible(true);
		field.set(viewModel, 1);
		
		viewModel.doTileAction(1, 0);
		assertEquals(1, mock.getListeners(MessageType.TurnMovePlayer).size());
		assertEquals(TurnMovePlayerDto.class, mock.getLastRequest().getClass());
		TurnMovePlayerDto request = TurnMovePlayerDto.class.cast(mock.getLastRequest());
		assertEquals(1, request.getTargetX());
		assertEquals(0, request.getTargetY());
		field = viewModel.getClass().getDeclaredField("hasMoved");
		field.setAccessible(true);
		assertTrue((boolean)field.get(viewModel));
		
		mock.sendResponse(new SimpleSuccessResponseDto(MessageType.TurnMovePlayer, false));
		assertFalse((boolean)field.get(viewModel));
	}
	
	/**
	 * Tests @see GameViewModel.switchTileTypeClockwiseCommand
	 */
	@Test
	public void switchTileTypeClockwiseCommandTest()
	{
		GameViewModel viewModel = new GameViewModel();
		initializeViewModel(viewModel);
		
		viewModel.getSwitchTileTypeClockwiseCommand().execute();
		assertEquals(1, viewModel.getTileOutsideOfBoard().getTileType());
		viewModel.getSwitchTileTypeClockwiseCommand().execute();
		assertEquals(2, viewModel.getTileOutsideOfBoard().getTileType());
		viewModel.getSwitchTileTypeClockwiseCommand().execute();
		assertEquals(3, viewModel.getTileOutsideOfBoard().getTileType());
		viewModel.getSwitchTileTypeClockwiseCommand().execute();
		assertEquals(0, viewModel.getTileOutsideOfBoard().getTileType());
	}
	
	/**
	 * Tests @see GameViewModel.switchTileTypeClockwiseCommand
	 */
	@Test
	public void switchTileTypeCounterClockwiseCommandTest()
	{
		GameViewModel viewModel = new GameViewModel();
		initializeViewModel(viewModel);
		
		viewModel.getSwitchTileTypeCounterClockwiseCommand().execute();
		assertEquals(3, viewModel.getTileOutsideOfBoard().getTileType());
		viewModel.getSwitchTileTypeCounterClockwiseCommand().execute();
		assertEquals(2, viewModel.getTileOutsideOfBoard().getTileType());
		viewModel.getSwitchTileTypeCounterClockwiseCommand().execute();
		assertEquals(1, viewModel.getTileOutsideOfBoard().getTileType());
		viewModel.getSwitchTileTypeCounterClockwiseCommand().execute();
		assertEquals(0, viewModel.getTileOutsideOfBoard().getTileType());
	}
	
	private void useBonusCommandTestCore(BonusKind bonusKind) 
	{
		SocketClientMock mock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		GameViewModel viewModel = new GameViewModel();
		initializeViewModel(viewModel);
		
		Command command = null;	
		switch(bonusKind)
		{
			case BEAM:
				command = viewModel.getUseBeamBonusCommand();
				break;
			case SWAP:
				command = viewModel.getUseSwapBonusCommand();
				break;
			case SHIFT_SOLID:
				command = viewModel.getUseShiftSolidBonusCommand();
				break;
			case SHIFT_TWICE:
				command = viewModel.getUseShiftTwiceBonusCommand();
				break;
		}
		command.execute();
					
		if(bonusKind == BonusKind.BEAM)
			viewModel.doTileAction(1, 0);
		if(bonusKind == BonusKind.SWAP)
			viewModel.doTileAction(0, 1);
		
		assertFalse(command.executableProperty().get());
		assertEquals(1, mock.getListeners(MessageType.TurnUseBonus).size());
		assertEquals(TurnUseBonusDto.class, mock.getLastRequest().getClass());
		TurnUseBonusDto request = TurnUseBonusDto.class.cast(mock.getLastRequest());
		assertEquals(bonusKind, request.getType());		
		assertEquals(bonusKind, viewModel.getPlayer1().getActiveBonus());
		
		//Active bonus must be reset if using the bonus was not valid
		mock.sendResponse(new SimpleSuccessResponseDto(MessageType.TurnUseBonus, false));
		assertNull(viewModel.getPlayer1().getActiveBonus());
		assertTrue(command.executableProperty().get());
	}
	
	private static void initializeViewModel(GameViewModel viewModel)
	{
		BoardDto board = new BoardDto();
		TileDto tile1 = new TileDto(0, 0, 1);
		tile1.setBoni(BonusKind.BEAM);
		TileDto tile2 = new TileDto(0, 1, 2);
		tile2.setPlayer("player2");
		TileDto tile3 = new TileDto(1, 0, 3);
		tile3.setTreasure(1);
		TileDto tile4 = new TileDto(0, 0, 4);
		tile4.setPlayerBase("player3");
		TileDto tile5 = new TileDto(0, 1, 5);
		TileDto tile6 = new TileDto(2, 0, 6);
		TileDto tile7 = new TileDto(2, 1, 7);
		TileDto tile8 = new TileDto(2, 2, 8);
		TileDto tile9 = new TileDto(1, 2, 9);
		TileDto tile10 = new TileDto(-1, -1, 0);
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
		GameStateResponseDto state = new GameStateResponseDto(board);
		PlayerDto player1 = new PlayerDto("player1", "blue", new PlayerTreasuresDto(1, 5), new PlayerBoniDto(2,2,2,2));
		PlayerDto player2 = new PlayerDto("player2", "green", new PlayerTreasuresDto(2, 5), new PlayerBoniDto(1,1,1,1));
		PlayerDto player3 = new PlayerDto("player3", "red", new PlayerTreasuresDto(null, 5), new PlayerBoniDto(1,1,1,1));
		state.getPlayers().add(player1);
		state.getPlayers().add(player2);
		state.getPlayers().add(player3);
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(GameViewModel.ACTIVE_PLAYER_PARAMETER, "player1");
		parameters.put(GameViewModel.CURRENT_PLAYER_PARAMETER, "player1");
		parameters.put(GameViewModel.GAME_STATE_PARAMETER, state);
		parameters.put(GameViewModel.BOARD_SIZE_PARAMETER, 3);
		parameters.put(GameViewModel.IS_AI_PARAMETER, false);
		viewModel.handleNavigation(parameters);	
	}
	
	private static void testTile(TileDto tile, TileBindingModel model, String expectedPlayerColor, 
		String expectedPlayerBaseColor)
	{
		assertEquals(tile.getX(), model.getCoordinates().getX());
		assertEquals(tile.getY(), model.getCoordinates().getY());
		assertEquals(tile.getType(), model.getTileType());
		assertEquals(tile.getTreasure(), model.getTreasureType());
		assertEquals(tile.getBoni(), model.getBonusKind());
		assertEquals(expectedPlayerColor, model.getPlayerColor());
		assertEquals(expectedPlayerBaseColor, model.getPlayerBaseColor());
	}
	
	private static void testPlayer(PlayerDto player, PlayerBindingModel model)
	{
		assertEquals(player.getName(), model.getPlayerName());
		assertEquals(player.getColor(), model.getColor());
		if(player.getTreasures().getCurrent() != null)
			assertEquals(player.getTreasures().getCurrent(), model.getNextTreasure());
		assertEquals(player.getTreasures().getRemaining(), model.getRemainingTreasures());
		assertEquals(player.getBoni().getBeam(), model.getBeam());
		assertEquals(player.getBoni().getSwap(), model.getSwap());
		assertEquals(player.getBoni().getShiftSolid(), model.getShiftSolid());
		assertEquals(player.getBoni().getShiftTwice(), model.getShiftTwice());
	}
}