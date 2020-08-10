package labyrinth.client.ui.viewModels;

import java.util.HashMap;
import java.util.Map;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import labyrinth.client.ui.ClientConnectionManager;
import labyrinth.client.ui.NavigationManager;
import labyrinth.client.ui.resources.Errors;
import labyrinth.client.ui.resources.Messages;
import labyrinth.client.ui.views.GameConfigurationView;
import labyrinth.client.ui.views.GameView;
import labyrinth.client.ui.views.MainMenuView;
import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.requests.*;
import labyrinth.contracts.communication.dtos.responses.GameStateResponseDto;
import labyrinth.contracts.communication.dtos.responses.UpdateLobbyResponseDto;
import labyrinth.contracts.communication.listeners.ContinuousUiThreadActionListener;
import labyrinth.contracts.communication.listeners.UiThreadActionListener;
import labyrinth.contracts.entities.lobby.*;

/**
 * ViewModel for @see LobbyView
 * @author Lukas Reinhardt
 * @version 1.4
 */
public final class LobbyViewModel extends ViewModelBase
{
	//Properties
	
	/**
	 * Navigation-Parameter for the user name of the current User
	 */
	public static final String USER_PARAMETER = "USER_PARAMETER";
	
	/**
	 * Navigation-Parameter for the name of the server
	 */
	public static final String SERVER_PARAMETER = "SERVER_PARAMETER";
	
	private Lobby lobby = new Lobby();
	
	private GameConfiguration configuration = new GameConfiguration();
	
	private String username;
	
	private GameStateResponseDto gameState;
	
	private String currentPlayer;
	
	private LobbyUser user;
	/**
	 * Corresponding getter
	 * @return The current user
	 */
	public LobbyUser getUser() { return user; }
	
	//Commands
	
	private Command leaveLobbyCommand;
	/**
	 * Getter for leaveLobbyCommand
	 * @return leaveLobbyCommand
	 */
	public Command getLeaveLobbyCommand() { return leaveLobbyCommand; }
	
	private Command gameConfigurationCommand;
	/**
	 * Getter for gameConfigurationCommand
	 * @return gameConfigurationCommand
	 */
	public Command getGameConfigurationCommand() { return gameConfigurationCommand; }
	
	private Command startGameCommand;
	/**
	 * Getter for navigateToConfigurationViewCommand
	 * @return navigateToConfigurationViewCommand
	 */
	public Command getStartGameCommand() { return startGameCommand; }
	
	private Command changeRoleCommand;
	/**
	 * Getter for changeRoleCommand
	 * @return changeRoleCommand
	 */
	public Command getChangeRoleCommand() { return changeRoleCommand; }
	
	//Bindable Properties
	
	private ObservableList<LobbyUser> bindablePlayerList = FXCollections.observableArrayList();
	/**
	 * Getter for the list of available GameServers
	 * @return List of available GameServers
	 */
	public ObservableList<LobbyUser> getBindablePlayerList() { return bindablePlayerList; }
	
	private ObservableList<LobbyUser> bindableSpectatorList = FXCollections.observableArrayList();
	/**
	 * Getter for the list of available GameServers
	 * @return List of available GameServers
	 */
	public ObservableList<LobbyUser> getBindableSpectatorList() { return bindableSpectatorList; }
	
	private BooleanProperty isAIProperty = new SimpleBooleanProperty();
	/**
	 * Getter for isAIProperty
	 * @return isAIProperty
	 */
	public BooleanProperty getIsAIProperty() { return isAIProperty; }
	
	private StringProperty startGameCommandCaptionProperty = new SimpleStringProperty();
	/**
	 * Getter for startGameCommandCaptionProperty
	 * @return startGameCommandCaptionProperty
	 */
	public StringProperty startGameCommandCaptionProperty() { return startGameCommandCaptionProperty; }
	/**
	 * Getter for the value of startGameCommandCaptionProperty
	 * @return The value of startGameCommandCaptionProperty
	 */
	public String getStartGameCommandCaptionProperty() { return startGameCommandCaptionProperty.get(); }
	
	private BooleanProperty configurationCommandEnabledProperty = new SimpleBooleanProperty(false);
	/**
	 * Getter for configurationCommandEnabledProperty
	 * @return configurationCommandEnabledProperty 
	 */
	public BooleanProperty configurationCommandEnabledProperty() { return configurationCommandEnabledProperty; }
	
	private BooleanProperty startGameCommandEnabledProperty = new SimpleBooleanProperty(false);
	/**
	 * Getter for startGameCommandEnabledProperty 
	 * @return startGameCommandEnabledProperty 
	 */
	public BooleanProperty startGameCommandEnabledProperty() { return startGameCommandEnabledProperty; }
	
	//Constructors
	
	/**
	 * LobbyViewModel constructor
	 */
	public LobbyViewModel() 
	{ 
		initializeCommands(); 
		
		//Enable the ready command when the admin wants to start the game
		UiThreadActionListener listener 
			= new UiThreadActionListener(MessageType.StartGame, response ->   onStartGameResponse());
		ClientConnectionManager.getGameServerClient().registerListener(listener);
		
		//listener for configuration updates
		ContinuousUiThreadActionListener listener2 
			= new ContinuousUiThreadActionListener(MessageType.Configuration, response -> updateConfiguration(response));
		ClientConnectionManager.getGameServerClient().registerListener(listener2);
		
		//Wait for CurrentPlayer and GameState when AllClientsReady received
		listener = new UiThreadActionListener(MessageType.AllClientsReady, 
			response -> onAllClientsReadyResponse());		
		ClientConnectionManager.getGameServerClient().registerListener(listener);
	}
	
	//Methods
	
	/**
	 * {@inheritDoc}
	 * @throws IllegalArgumentException when no WITH_AI_PARAMETER has been passed with navigation
	 */
	@Override
	public void handleNavigation(Map<String, Object> parameters) 
	{ 
		if(parameters == null || parameters.get(USER_PARAMETER) == null)
			throw new IllegalArgumentException(Errors.getLobbyViewModel_MissingParameter());	
		
		username = String.class.cast(parameters.get(USER_PARAMETER));
		if(parameters.containsKey(SERVER_PARAMETER))
			configuration.setServerName(String.class.cast(parameters.get(SERVER_PARAMETER)));
		lobby = new Lobby();
		
		//Retrieve the status of the lobby
		getLobbyStatus();		
	}
	
	/**
	 * Changes the Role of the current Player
	 */
	public void changeRole()
	{
		LobbyUserKind role = user.getRole() == LobbyUserKind.PLAYER ? LobbyUserKind.SPECTATOR : LobbyUserKind.PLAYER;
		ClientConnectionManager.getGameServerClient().sendRequest(new ChangeRoleDto(role));
	}
	
	/**
	 * Sets the user as the new admin
	 */
	public void setAdmin(LobbyUser user)
	{
		configuration.setAdmin(user.getName());
		ClientConnectionManager.getGameServerClient().sendRequest(new ConfigurationDto(configuration));
	}
	
	/**
	 * Changes the color of the current Player
	 */
	public void changeColor(String color) 
	{ ClientConnectionManager.getGameServerClient().sendRequest(new ChangeColorDto(color)); }
	
	private void onStartGameResponse()
	{
		if(user.getPermission() != UserPermission.ADMIN)
			startGameCommandEnabledProperty.set(true);
	}
	
	private void initializeCommands()
	{
		gameConfigurationCommand = new DelegateCommand(() -> new Action() 
		{
			@Override
			protected void action() { navigateToConfigurationView(); }
		}, configurationCommandEnabledProperty);
		leaveLobbyCommand = new DelegateCommand(() -> new Action() 
		{
			@Override
			protected void action() { leaveLobby(); }
		});
		startGameCommand = new DelegateCommand(() -> new Action() 
		{
			@Override
			protected void action() 
			{ 
				if(user.getPermission() == UserPermission.ADMIN)
					startGame();
				else
					signalReady();
			}
		}, startGameCommandEnabledProperty);	
	}
	
	private void navigateToConfigurationView()
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(GameConfigurationViewModel.CONFIGURATION_PARAMETER, configuration);
		NavigationManager.navigate(GameConfigurationView.class, parameters);
	}
	
	private void leaveLobby()
	{
		ClientConnectionManager.getGameServerClient().sendRequest(new EmptyRequestDto(MessageType.ClientLeave));
		NavigationManager.navigate(MainMenuView.class);
	}
	
	private void startGame()
	{ 
		ClientConnectionManager.getGameServerClient().sendRequest(new EmptyRequestDto(MessageType.StartGame)); 
		startGameCommandEnabledProperty.set(false);
	}
	
	private void signalReady()
	{ 
		ClientConnectionManager.getGameServerClient().sendRequest(new EmptyRequestDto(MessageType.ClientReady)); 
		startGameCommandEnabledProperty.set(false);
	}
	
	private void getLobbyStatus()
	{
		ContinuousUiThreadActionListener listener = new ContinuousUiThreadActionListener(MessageType.UpdateLobby, 
			response -> onUpdateLobbyStatusResponseArrived(response));
		ClientConnectionManager.getGameServerClient().registerListener(listener);
		ClientConnectionManager.getGameServerClient().sendRequest(new EmptyRequestDto(MessageType.UpdateLobby));
	}
	
	private void onAllClientsReadyResponse()
	{
		UiThreadActionListener listener 
			= new UiThreadActionListener(MessageType.CurrentPlayer, content -> onCurrentPlayerResponse(content));
		ClientConnectionManager.getGameServerClient().registerListener(listener);
		
		listener = new UiThreadActionListener(MessageType.GameState, content -> onGameStateResponse(content));
		ClientConnectionManager.getGameServerClient().registerListener(listener);
	}
	
	private void onGameStateResponse(Object content)
	{
		gameState = GameStateResponseDto.class.cast(content);
		if(currentPlayer != null)
			navigateToGameView();			
	}
	
	private void onCurrentPlayerResponse(Object content)
	{
		currentPlayer = String.class.cast(content);
		if(gameState != null)
			navigateToGameView();
	}
	
	private void navigateToGameView()
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		if(user.getRole() == LobbyUserKind.PLAYER)
			parameters.put(GameViewModel.CURRENT_PLAYER_PARAMETER, user.getName());
		parameters.put(GameViewModel.BOARD_SIZE_PARAMETER, configuration.getSize().getX());
		parameters.put(GameViewModel.ACTIVE_PLAYER_PARAMETER, currentPlayer);
		parameters.put(GameViewModel.GAME_STATE_PARAMETER, gameState);
		parameters.put(GameViewModel.IS_AI_PARAMETER, isAIProperty.get());
		NavigationManager.navigate(GameView.class, parameters);
	}
	
	private void onUpdateLobbyStatusResponseArrived(Object content)
	{
		//Add Lobby members to the local Lobby instance
		UpdateLobbyResponseDto response = UpdateLobbyResponseDto.class.cast(content);
		
		//Refresh the users
		refreshUsers(response);
		
		//Change command executable depending on permission
		if(user.getPermission() == UserPermission.ADMIN && lobby.getPlayerList().size() > 1)
			startGameCommandEnabledProperty.set(true);
		else if(user.getPermission() == UserPermission.ADMIN)
			startGameCommandEnabledProperty.set(false);
		configurationCommandEnabledProperty.set(user.getPermission() == UserPermission.ADMIN);
		
		//Handle the change of the user
		handleUserChanged();
		
		//Refresh the bindable lists
		refreshBindingLists();
	}
	
	private void refreshUsers(UpdateLobbyResponseDto response)
	{
		lobby.getPlayerList().clear();
		lobby.getSpectatorList().clear();
		response.getMembers().stream().filter(member -> member.getRole() == LobbyUserKind.PLAYER)
			.forEach(lobby.getPlayerList()::add);
		response.getMembers().stream().filter(member -> member.getRole() == LobbyUserKind.SPECTATOR)
			.forEach(lobby.getSpectatorList()::add);	
		user = response.getMembers().stream().filter(member -> member.getName().equals(username)).findFirst().get();
	}
	
	private void refreshBindingLists()
	{
		bindablePlayerList.clear();
		bindablePlayerList.addAll(lobby.getPlayerList());
		bindableSpectatorList.clear();
		bindableSpectatorList.addAll(lobby.getSpectatorList());
	}
	
	private void handleUserChanged()
	{
		//Set the caption of the startGameCommand
		if(user.getPermission() == UserPermission.ADMIN)
			startGameCommandCaptionProperty.set(Messages.getLobby_StartGameCommandCaption());
		else
			startGameCommandCaptionProperty.set(Messages.getLobby_SignalReadyCommandCaption());
	}
	
	private void updateConfiguration(Object content)
	{
		GameConfiguration response = GameConfiguration.class.cast(content);
		configuration.setBoni(response.getBoni());
		configuration.setBoniProbability(response.getBoniProbability());
		configuration.setGameLengthLimit(response.getGameLengthLimit());
		configuration.setSize(response.getSize());
		configuration.setTreasureCount(response.getTreasureCount());
		configuration.setTurnLengthLimit(response.getTurnLengthLimit());
	}
}
