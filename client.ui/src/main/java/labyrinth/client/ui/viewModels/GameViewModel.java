package labyrinth.client.ui.viewModels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import javafx.application.Platform;
import javafx.beans.property.*;
import labyrinth.client.ui.ClientConnectionManager;
import labyrinth.client.ui.NavigationManager;
import labyrinth.client.ui.ai.ArtificialIntelligencePlayer;
import labyrinth.client.ui.resources.Errors;
import labyrinth.client.ui.views.GameEndView;
import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.*;
import labyrinth.contracts.communication.dtos.requests.*;
import labyrinth.contracts.communication.dtos.responses.*;
import labyrinth.contracts.communication.listeners.ContinuousUiThreadActionListener;
import labyrinth.contracts.communication.listeners.UiThreadActionListener;
import labyrinth.contracts.entities.game.BonusKind;

/**
 * ViewModel for @see GameView
 * @author Lukas Reinhardt
 * @version 1.3
 */
public final class GameViewModel extends ViewModelBase 
{
	//Navigation Parameters
	
	/**
	 * Navigation-Parameter for the configuration of the game
	 */
	public static final String IS_AI_PARAMETER = "IS_AI_PARAMETER";
	
	/**
	 * Navigation-Parameter for the configuration of the game
	 */
	public static final String BOARD_SIZE_PARAMETER = "BOARD_SIZE_PARAMETER";
	
	/**
	 * Navigation-Parameter for the state of the game
	 */
	public static final String GAME_STATE_PARAMETER = "GAME_STATE_PARAMETER";
	
	/**
	 * Navigation-Parameter for the current player
	 */
	public static final String ACTIVE_PLAYER_PARAMETER = "ACTIVE_PLAYER_PARAMETER";
	
	/**
	 * Navigation-Parameter for the current player
	 */
	public static final String CURRENT_PLAYER_PARAMETER = "CURRENT_PLAYER_PARAMETER";
	
	//Properties
	
	private boolean isAI = false;
	
	private String activePlayer;
	
	private String clientPlayerName;
	
	private int shiftedCount = 0;
	
	private boolean hasMoved;
	
	private int boardSize;
	/**
	 * Corresponding getter
	 * @return The size of the game board
	 */
	public int getBoardSize() { return boardSize; }
	
	/**
	 * Corresponding getter
	 * @return If the current user is spectator
	 */
	public boolean getIsSpectator() { return clientPlayerName == null; }
	
	//Binding Properties
	
	private StringProperty actionTextProperty = new SimpleStringProperty();
	/**
	 * Corresponding getter
	 * @return actionTextProperty
	 */
	public StringProperty actionTextProperty() { return actionTextProperty; }
	
	private StringProperty achievementTextProperty = new SimpleStringProperty();
	/**
	 * Corresponding getter
	 * @return achievementTextProperty
	 */
	public StringProperty achievementTextProperty() { return achievementTextProperty; }
	
	private BooleanProperty achievementTextVisibleProperty = new SimpleBooleanProperty();
	/**
	 * Corresponding getter
	 * @return achievementTextVisibleProperty
	 */
	public BooleanProperty achievementTextVisibleProperty() { return achievementTextVisibleProperty; }
	
	private BooleanProperty switchTileTypeClockwiseCommandEnabledProperty = new SimpleBooleanProperty();
	
	private BooleanProperty switchTileTypeCounterClockwiseCommandEnabledProperty = new SimpleBooleanProperty();
	
	private BooleanProperty useShiftSolidBonusCommandEnabledProperty = new SimpleBooleanProperty();
	
	private BooleanProperty useShiftTwiceBonusCommandEnabledProperty = new SimpleBooleanProperty();
	
	private BooleanProperty useSwapBonusCommandEnabledProperty = new SimpleBooleanProperty();
	
	private BooleanProperty useBeamBonusCommandEnabledProperty = new SimpleBooleanProperty();
	
	private PlayerBindingModel player1;
	/**
	 * Corresponding getter
	 * @return Player1
	 */
	public PlayerBindingModel getPlayer1() { return player1; }
	
	private PlayerBindingModel player2;
	/**
	 * Corresponding getter
	 * @return Player2
	 */
	public PlayerBindingModel getPlayer2() { return player2; }
	
	private PlayerBindingModel player3;
	/**
	 * Corresponding getter
	 * @return Player3
	 */
	public PlayerBindingModel getPlayer3() { return player3; }
	
	private PlayerBindingModel player4;
	/**
	 * Corresponding getter
	 * @return Player4
	 */
	public PlayerBindingModel getPlayer4() { return player4; }
	
	private TileBindingModel tileOutsideOfBoard;
	/**
	 * Corresponding getter
	 * @return The tile outside of the board
	 */
	public TileBindingModel getTileOutsideOfBoard() { return tileOutsideOfBoard; }
	
	private List<TileBindingModel> tiles = new ArrayList<TileBindingModel>();
	/**
	 * Corresponding getter
	 * @return The tiles of the board
	 */
	public List<TileBindingModel> getTiles() { return tiles; }
	
	//Commands
	
	private Command switchTileTypeClockwiseCommand = new DelegateCommand(() -> new Action() 
	{
		@Override
		protected void action() { switchTileTypeClockwise(); }
	}, switchTileTypeClockwiseCommandEnabledProperty);
	/**
	 * Corresponding getter
	 * @return switchTileTypeClockwiseCommand
	 */
	public Command getSwitchTileTypeClockwiseCommand() { return switchTileTypeClockwiseCommand; } 
		
	private Command switchTileTypeCounterClockwiseCommand = new DelegateCommand(() -> new Action() 
	{
		@Override
		protected void action() { switchTileTypeCounterClockwise(); }
	}, switchTileTypeCounterClockwiseCommandEnabledProperty);
	/**
	 * Corresponding getter
	 * @return switchTileTypeCounterClockwiseCommand
	 */
	public Command getSwitchTileTypeCounterClockwiseCommand() { return switchTileTypeCounterClockwiseCommand; } 
		
	private Command useSwapBonusCommand = new DelegateCommand(() -> new Action() 
	{
		@Override
		protected void action() { useBonus(BonusKind.SWAP); }
	}, useSwapBonusCommandEnabledProperty);
	/**
	 * Corresponding getter
	 * @return useSwapBonusCommand
	 */
	public Command getUseSwapBonusCommand() { return useSwapBonusCommand; } 
		
	private Command useBeamBonusCommand = new DelegateCommand(() -> new Action() 
	{
		@Override
		protected void action() { useBonus(BonusKind.BEAM); }
	}, useBeamBonusCommandEnabledProperty);
	/**
	 * Corresponding getter
	 * @return useBeamBonusCommand
	 */
	public Command getUseBeamBonusCommand() { return useBeamBonusCommand; } 
		
	private Command useShiftTwiceBonusCommand = new DelegateCommand(() -> new Action() 
	{
		@Override
		protected void action() { useBonus(BonusKind.SHIFT_TWICE); }
	}, useShiftTwiceBonusCommandEnabledProperty);
	/**
	 * Corresponding getter
	 * @return useShiftTwiceBonusCommand
	 */
	public Command getUseShiftTwiceBonusCommand() { return useShiftTwiceBonusCommand; } 
		
	private Command useShiftSolidBonusCommand = new DelegateCommand(() -> new Action() 
	{
		@Override
		protected void action() { useBonus(BonusKind.SHIFT_SOLID); }
	}, useShiftSolidBonusCommandEnabledProperty);
	/**
	 * Corresponding getter
	 * @return useShiftSolidBonusCommand
	 */
	public Command getUseShiftSolidBonusCommand() { return useShiftSolidBonusCommand; } 
	
	//Constructor
	
	/**
	 * GameViewModel Constructor
	 */
	public GameViewModel()
	{
		//Listen for GameState-Changes
		ContinuousUiThreadActionListener listener 
			= new ContinuousUiThreadActionListener(MessageType.GameState, content -> onGameStateUpdate(content));
		ClientConnectionManager.getGameServerClient().registerListener(listener);
		
		//Listen for turn switches
		listener = new ContinuousUiThreadActionListener(MessageType.CurrentPlayer, content -> onActivePlayerUpdate(content));
		ClientConnectionManager.getGameServerClient().registerListener(listener);
		
		//Listen for actions of other players
		listener = new ContinuousUiThreadActionListener(MessageType.PlayerAction, content -> onPlayerAction(content));
		ClientConnectionManager.getGameServerClient().registerListener(listener);
		
		//Listen for achievements
		listener = new ContinuousUiThreadActionListener(MessageType.PropagateAchievement, 
			content -> onPropagateAchievement(content));
		ClientConnectionManager.getGameServerClient().registerListener(listener);
		
		//Listen for EndGame-Notification
		UiThreadActionListener listener1 = new UiThreadActionListener(MessageType.EndGame, content -> onEndGame(content));
		ClientConnectionManager.getGameServerClient().registerListener(listener1);
	}
	
	//Methods
	
	/**
	 * {@inheritDoc}
	 * @throws IllegalArgumentException when a parameter is missing
	 */
	@Override
	public void handleNavigation(Map<String, Object> parameters) 
	{ 
		if(parameters == null || parameters.get(BOARD_SIZE_PARAMETER) == null 
			|| parameters.get(GAME_STATE_PARAMETER) == null || parameters.get(ACTIVE_PLAYER_PARAMETER) == null
			|| parameters.get(IS_AI_PARAMETER) == null)
			throw new IllegalArgumentException(Errors.getGameViewModel_MissingParameter());	
		
		isAI = Boolean.class.cast(parameters.get(IS_AI_PARAMETER));
		boardSize = Integer.class.cast(parameters.get(BOARD_SIZE_PARAMETER));
		clientPlayerName = String.class.cast(parameters.get(CURRENT_PLAYER_PARAMETER));
		String activePlayer = String.class.cast(parameters.get(ACTIVE_PLAYER_PARAMETER));	
		GameStateResponseDto state = GameStateResponseDto.class.cast(parameters.get(GAME_STATE_PARAMETER));
		
		//Initialize the state and the active player
		initializeGameState(state);
		onActivePlayerUpdateCore(activePlayer);	
		
		//Start AI
		if(isAI)
			new ArtificialIntelligencePlayer(state, boardSize, clientPlayerName, activePlayer);
	}
	
	/**
	 * Signals the use of a bonus to the server
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 */
	public void doTileAction(int x, int y)
	{
		//Actions are only valid if it's the players turn
		if(!activePlayer.equals(clientPlayerName))
			return;
		
		if(player1.getActiveBonus() == BonusKind.BEAM && shiftedCount == 0) //player has selected tile for beam
			useBeam(x, y);			
		else if(player1.getActiveBonus() == BonusKind.SWAP && shiftedCount == 0) //player has selected other player for swap
			swapWithOtherPlayer(x, y);
		else if(shiftedCount == 0 || shiftedCount == 1 && player1.getActiveBonus() == BonusKind.SHIFT_TWICE) //shift tiles
			shiftTiles(x, y);
		else if(!hasMoved) //Move player
			movePlayer(x, y);		
	}
	
	private void onPropagateAchievement(Object result)
	{
		//Show achievement
		PropagateAchievementResponseDto response = PropagateAchievementResponseDto.class.cast(result);
		String message = "Der Spieler " + response.getPlayer() + " hat das Achievement \"" + response.getAchievement() 
			+ "\" freigeschaltet.";
		achievementTextProperty.set(message);
		achievementTextVisibleProperty.set(true);
		
		//Hide achievement notification after 3 seconds
		Timer timer = new Timer();
		timer.schedule(new TimerTask()
		{
			@Override
			public void run() 
			{
				Platform.runLater(() -> 
				{
					achievementTextProperty.set(null);
					achievementTextVisibleProperty.set(false);
				});
			}
		}, 4*1000);
	}
	
	private void onEndGame(Object content)
	{
		//Navigate to GameEndView
		EndGameResponseDto response = EndGameResponseDto.class.cast(content);
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(GameEndViewModel.END_GAME_RESPONSE_PARAMETER, response);
		parameters.put(GameEndViewModel.CURRENT_PLAYER_PARAMETER, clientPlayerName);
		NavigationManager.navigate(GameEndView.class, parameters);
	}
	
	private void onPlayerAction(Object content)
	{
		//Change the turn title
		PlayerActionResponseDto response = PlayerActionResponseDto.class.cast(content);
		PlayerBindingModel player = getPlayerFromName(response.getPlayer());
		if(response.getTurnUseBonus() != null)
			player.setActiveBonus(response.getTurnUseBonus().getType());
		else if(response.getTurnShiftTiles() != null)
		{
			if(shiftedCount == 0 || shiftedCount == 1 && player.getActiveBonus() == BonusKind.SHIFT_TWICE)
				actionTextProperty.set(activePlayer + " - Schritt 2: Spielfigur bewegen");
			shiftedCount++;
		}
	}
	
	private void useBonus(BonusKind bonusKind)
	{	
		//If shift solid or shift twice send request immediatly otherwise wait for further input
		if(bonusKind == BonusKind.SHIFT_SOLID || bonusKind == BonusKind.SHIFT_TWICE)
		{
			UiThreadActionListener responseListener = new UiThreadActionListener(MessageType.TurnUseBonus, 
				content -> onUseBonusSuccessResponse(content));
			ClientConnectionManager.getGameServerClient().registerListener(responseListener);
			ClientConnectionManager.getGameServerClient().sendRequest(new TurnUseBonusDto(bonusKind));
		}
		
		//Set Bonus and disable commands
		player1.setActiveBonus(bonusKind);
		useSwapBonusCommandEnabledProperty.set(false);
		useBeamBonusCommandEnabledProperty.set(false);
		useShiftTwiceBonusCommandEnabledProperty.set(false);
		useShiftSolidBonusCommandEnabledProperty.set(false);
	}
	
	private void onUseBonusSuccessResponse(Object content)
	{
		SimpleSuccessResponseDto response = SimpleSuccessResponseDto.class.cast(content);
		if(!response.getSuccess())
		{
			player1.setActiveBonus(null);
			useSwapBonusCommandEnabledProperty.set(true);
			useBeamBonusCommandEnabledProperty.set(true);
			useShiftTwiceBonusCommandEnabledProperty.set(true);
			useShiftSolidBonusCommandEnabledProperty.set(true);
		}
	}
	
	private void onMovePlayerSuccessResponse(Object content)
	{
		SimpleSuccessResponseDto response = SimpleSuccessResponseDto.class.cast(content);
		if(!response.getSuccess())
			hasMoved = false;
	}
	
	private void switchTileTypeClockwise()
	{
		int type = tileOutsideOfBoard.getTileType();
		if(type < 4)
		{
			type++;
			if(type == 4)
				type = 0;
		}
		else if(type < 8)
		{
			type++;
			if(type == 8)
				type = 4;
		}
		else
		{
			type++;
			if(type == 10)
				type = 8;
		}
		tileOutsideOfBoard.setTileType(type);
	}
	
	private void switchTileTypeCounterClockwise()
	{
		int type = tileOutsideOfBoard.getTileType();
		if(type < 4)
		{
			type--;
			if(type == -1)
				type = 3;		
		}
		else if(type < 8)
		{
			type--;
			if(type == 3)
				type = 7;
		}
		else
		{
			type--;
			if(type == 7)
				type = 9;
		}
		tileOutsideOfBoard.setTileType(type);
	}
		
	private void swapWithOtherPlayer(int x, int y)
	{
		TileBindingModel tile = tiles.stream().filter(
			model -> model.getCoordinates().getX() == x && model.getCoordinates().getY() == y).findFirst().get();
		if(tile.getPlayer() != null) //If player selected send request
		{
			TargetPlayerDto player = new TargetPlayerDto(tile.getPlayer());
			UiThreadActionListener responseListener = new UiThreadActionListener(MessageType.TurnUseBonus, 
					content -> onUseBonusSuccessResponse(content));
			ClientConnectionManager.getGameServerClient().registerListener(responseListener);
			ClientConnectionManager.getGameServerClient().sendRequest(new TurnUseBonusDto(player));
		}
	}
	
	private void useBeam(int x, int y)
	{
		UiThreadActionListener responseListener = new UiThreadActionListener(MessageType.TurnUseBonus, 
				content -> onUseBonusSuccessResponse(content));
		ClientConnectionManager.getGameServerClient().registerListener(responseListener);
		ClientConnectionManager.getGameServerClient().sendRequest(new TurnUseBonusDto(new TargetCoordinatesDto(x, y)));
	}
	
	private void movePlayer(int x, int y)
	{
		UiThreadActionListener responseListener = new UiThreadActionListener(MessageType.TurnMovePlayer, 
				content -> onMovePlayerSuccessResponse(content));
		ClientConnectionManager.getGameServerClient().registerListener(responseListener);
		ClientConnectionManager.getGameServerClient().sendRequest(new TurnMovePlayerDto(x, y));
		hasMoved = true;
	}
	
	private void shiftTiles(int x, int y)
	{
		if(isValidTileForShifting(x, y)) //send request if the selected tile is valid
		{
			int slot = getSlotFromCoordinates(x, y);
			int type = tileOutsideOfBoard.getTileType();
			UiThreadActionListener responseListener = new UiThreadActionListener(MessageType.TurnShiftTiles, 
				content -> onShiftTilesSuccessResponse(content));
			ClientConnectionManager.getGameServerClient().registerListener(responseListener);
			ClientConnectionManager.getGameServerClient().sendRequest(new TurnShiftTilesDto(slot, type));
			shiftedCount++;
		}
	}
	
	private void onShiftTilesSuccessResponse(Object content)
	{
		SimpleSuccessResponseDto response = SimpleSuccessResponseDto.class.cast(content);
		if(!response.getSuccess() && shiftedCount != 0)
			shiftedCount--;
		else if(shiftedCount == 2 || shiftedCount == 1 && player1.getActiveBonus() != BonusKind.SHIFT_TWICE)
			actionTextProperty.set(activePlayer + " - Schritt 2: Spielfigur bewegen");
	}
	
	private boolean isValidTileForShifting(int x, int y) 
	{ 	
		boolean test = x == 0 || y == 0 ||  y == boardSize - 1 || x == boardSize - 1; 
		System.out.println("isValidTileForShifting: " + test);
		return test;
	}	
	
	private int getSlotFromCoordinates(int x, int y)
	{
		if(y == 0)
			return x;
		else if(x == boardSize - 1)
			return boardSize + y;
		else if(y == boardSize - 1)
			return 2 * boardSize - 1 + boardSize - x;
		else
			return 3 * boardSize - 1 + boardSize - y;
	}
	
	private void initializeGameState(GameStateResponseDto state)
	{
		//Create PlayerBindingModels from response
		initializePlayers(state);
		
		//Create TileBindingModels from response
		initializeTiles(state);
	}
	
	private void initializePlayers(GameStateResponseDto state)
	{
		if(clientPlayerName == null) //User of client is spectator
			initializePlayersForSpectator(state);
		else //User of client is player
			initializePlayersForPlayer(state);
	}
	
	private void initializeTiles(GameStateResponseDto state)
	{
		for(TileDto tile : state.getBoard().getTiles())
		{
			TileBindingModel model = new TileBindingModel(tile);
			if(tile.getX() == -1 && tile.getY() == -1)
				tileOutsideOfBoard = model;
			else
				tiles.add(model);
			if(tile.getPlayer() != null)
				model.setPlayerColor(getPlayerFromName(tile.getPlayer()).getColor());
			if(tile.getPlayerBase() != null)
				model.setPlayerBaseColor(getPlayerFromName(tile.getPlayerBase()).getColor());
		}
	}
	
	private void initializePlayersForSpectator(GameStateResponseDto state)
	{
		player1 = new PlayerBindingModel(state.getPlayers().get(0));
		player2 = new PlayerBindingModel(state.getPlayers().get(1));
		if(state.getPlayers().size() > 2)
			player3 = new PlayerBindingModel(state.getPlayers().get(2));
		if(state.getPlayers().size() > 3)
			player3 = new PlayerBindingModel(state.getPlayers().get(3));
	}
	
	private void initializePlayersForPlayer(GameStateResponseDto state)
	{
		PlayerDto player = state.getPlayers().stream().filter(
				playerDto -> playerDto.getName().equals(clientPlayerName)).findFirst().get();
		int index = state.getPlayers().indexOf(player);
			
		player1 = new PlayerBindingModel(state.getPlayers().get(index));
		for(int i = 0;i < state.getPlayers().size();i++)
		{
			if(i == index)
				continue;
			if(player2 == null)
				player2 = new PlayerBindingModel(state.getPlayers().get(i));
			else if(player3 == null)
				player3 = new PlayerBindingModel(state.getPlayers().get(i));
			else
				player4 = new PlayerBindingModel(state.getPlayers().get(i));
		}
	}
	
	private void onGameStateUpdate(Object content)
	{
		GameStateResponseDto state = GameStateResponseDto.class.cast(content);		
		updatePlayers(state);
		updateTiles(state);
		
		if(activePlayer != null)
			calculateCommandExecutable();
	}	
	
	private void updatePlayers(GameStateResponseDto state)
	{
		for(PlayerDto player : state.getPlayers())
		{
			PlayerBindingModel model = getPlayerFromName(player.getName());		
			model.setScore(player.getScore());
			if(player.getTreasures().getCurrent() != null)
				model.setNextTreasure(player.getTreasures().getCurrent());
			model.setRemainingTreasures(player.getTreasures().getRemaining());
			model.setShiftSolid(player.getBoni().getShiftSolid());
			model.setShiftTwice(player.getBoni().getShiftTwice());
			model.setBeam(player.getBoni().getBeam());
			model.setSwap(player.getBoni().getSwap());
		}
	}
	
	private void updateTiles(GameStateResponseDto state)
	{
		for(TileDto tile : state.getBoard().getTiles())
		{
			TileBindingModel model;
			if(tile.getX() == -1 && tile.getY() == -1)
				model = tileOutsideOfBoard;
			else
				model = tiles.stream().filter(tileModel -> tileModel.getCoordinates().getX() == tile.getX() 
					&& tileModel.getCoordinates().getY() == tile.getY()).findFirst().get();
			
			if(tile.getPlayer() != null)
				model.setPlayerColor(getPlayerFromName(tile.getPlayer()).getColor());
			else
				model.setPlayerColor(null);
			
			if(tile.getPlayerBase() != null)
				model.setPlayerBaseColor(getPlayerFromName(tile.getPlayerBase()).getColor());
			else
				model.setPlayerBaseColor(null);
			
			model.setBonusKind(tile.getBoni());		
			model.setTreasureType(tile.getTreasure());
			model.setTileType(tile.getType());
		}
	}	
	
	private PlayerBindingModel getPlayerFromName(String playerName)
	{
		PlayerBindingModel model;
		if(playerName.equals(player1.getPlayerName()))
			model = player1;
		else if(playerName.equals(player2.getPlayerName()))
			model = player2;
		else if(playerName.equals(player3.getPlayerName()))
			model = player3;
		else
			model = player4;
		return model;
	}
		
	private void onActivePlayerUpdate(Object content)
	{
		String player = String.class.cast(content);
		onActivePlayerUpdateCore(player);
	}	
	
	private void onActivePlayerUpdateCore(String activePlayer)
	{
		this.activePlayer = activePlayer;
		if(player1 != null)
			calculateCommandExecutable();
		player1.setActiveBonus(null);
		shiftedCount = 0;
		hasMoved = false;
		actionTextProperty.set(activePlayer + " - Schritt 1: Felder verschieben");
	}
	
	private void calculateCommandExecutable()
	{
		useSwapBonusCommandEnabledProperty.set(!getIsSpectator() && activePlayer.equals(clientPlayerName) 
			&& player1.getSwap() > 0);
		useBeamBonusCommandEnabledProperty.set(!getIsSpectator() && activePlayer.equals(clientPlayerName) 
			&& player1.getBeam() > 0);
		useShiftTwiceBonusCommandEnabledProperty.set(!getIsSpectator() && activePlayer.equals(clientPlayerName) 
			&& player1.getShiftTwice() > 0);
		useShiftSolidBonusCommandEnabledProperty.set(!getIsSpectator() && activePlayer.equals(clientPlayerName) 
			&& player1.getShiftSolid() > 0);
		switchTileTypeClockwiseCommandEnabledProperty.set(!getIsSpectator() && activePlayer.equals(clientPlayerName));
		switchTileTypeCounterClockwiseCommandEnabledProperty.set(!getIsSpectator() && activePlayer.equals(clientPlayerName));
	}
}
