package client.ui.viewModels;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import client.ui.StageWrapperMock;
import de.saxsys.mvvmfx.utils.commands.Command;
import labyrinth.client.ui.*;
import labyrinth.client.ui.resources.Messages;
import labyrinth.client.ui.viewModels.*;
import labyrinth.client.ui.views.*;
import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.SocketClientMock;
import labyrinth.contracts.communication.dtos.BoardDto;
import labyrinth.contracts.communication.dtos.requests.ChangeColorDto;
import labyrinth.contracts.communication.dtos.requests.ChangeRoleDto;
import labyrinth.contracts.communication.dtos.requests.ConfigurationDto;
import labyrinth.contracts.communication.dtos.requests.EmptyRequestDto;
import labyrinth.contracts.communication.dtos.responses.ConfigurationResponseDto;
import labyrinth.contracts.communication.dtos.responses.CurrentPlayerResponseDto;
import labyrinth.contracts.communication.dtos.responses.EmptyResponseDto;
import labyrinth.contracts.communication.dtos.responses.GameStateResponseDto;
import labyrinth.contracts.communication.dtos.responses.UpdateLobbyResponseDto;
import labyrinth.contracts.communication.listeners.ContinuousUiThreadActionListener;
import labyrinth.contracts.communication.listeners.UiThreadActionListener;
import labyrinth.contracts.entities.*;
import labyrinth.contracts.entities.lobby.*;

/**
 * TestClass for @see LobbyViewModel
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class LobbyViewModelTests 
{
	/**
	 * Tests that the correct caption is selected for the startGameCommand if the current user is the admin
	 */
	@Test
	public void doesSelectCorrectCommandCaptionForAdminTest() 
	{ doesSelectCorrectCommandCaptionTestCore(UserPermission.ADMIN); }
	
	/**
	 * Tests that the correct caption is selected for the startGameCommand if the current user is a normal user
	 */
	@Test
	public void doesSelectCorrectCommandCaptionForNormalUserTest() 
	{ doesSelectCorrectCommandCaptionTestCore(UserPermission.DEFAULT); }
	
	/**
	 * Tests that the correct exception is thrown when navigating to the ViewModel without the User-Parameter
	 */
	@Test
	public void throwsCorrectExceptionWhenNavigatingWithoutUserParameterTest()
	{
		LobbyViewModel viewModel = new LobbyViewModel();
		assertThrows(IllegalArgumentException.class, () -> viewModel.handleNavigation(null), 
			"Expected exception of type IllegalArgumentException. has not been thrown.");
		Map<String, Object> parameters = new HashMap<String, Object>();
		assertThrows(IllegalArgumentException.class, () -> viewModel.handleNavigation(parameters), 
			"Expected exception of type IllegalArgumentException. has not been thrown.");
	}
	
	/**
	 * Tests that a UpdateLobby request is sent on navigating to the LobbyView
	 */
	@Test
	public void doesUpdateStartGameCommandEnabledPropertyOnStartGameResponseTest() 
		throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		UiThreadActionListener.setIsUnitTest(true);
		ClientConnectionManager.initialize(SocketClientMock.class);
		ClientConnectionManager.startGameServerClient(new GameServerEntry("Testserver"));
		SocketClientMock mock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		LobbyViewModel viewModel = new LobbyViewModel();
		
		Field field = viewModel.getClass().getDeclaredField("user");
		field.setAccessible(true);
		field.set(viewModel, new LobbyUser("tets", "test", UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		
		assertEquals(1, mock.getListeners(MessageType.StartGame).size());
		assertEquals(UiThreadActionListener.class, mock.getListeners(MessageType.StartGame).get(0).getClass());
		assertFalse(viewModel.startGameCommandEnabledProperty().get());
		mock.sendResponse(new EmptyResponseDto(MessageType.StartGame));
		assertTrue(viewModel.startGameCommandEnabledProperty().get());	
	}
	
	/**
	 * Tests that the configuration is updated when a configuration response arrives
	 */
	@Test
	public void doesUpdateConfigurationOnConfigurationResponseTest() 
		throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		ContinuousUiThreadActionListener.setIsUnitTest(true);
		ClientConnectionManager.initialize(SocketClientMock.class);
		ClientConnectionManager.startGameServerClient(new GameServerEntry("Testserver"));
		SocketClientMock mock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		LobbyViewModel viewModel = new LobbyViewModel();
		
		assertEquals(1, mock.getListeners(MessageType.Configuration).size());
		assertEquals(ContinuousUiThreadActionListener.class, mock.getListeners(MessageType.Configuration).get(0).getClass());
		BoniConfiguration boniConfiguration = new BoniConfiguration();
		GameConfiguration newConfiguration = new GameConfiguration(null, new Coordinates(3, 3), 1, boniConfiguration, 2, 3, 4, null);
		mock.sendResponse(new ConfigurationResponseDto(newConfiguration));
		
		Field field = viewModel.getClass().getDeclaredField("configuration");
		field.setAccessible(true);
		GameConfiguration configuration = GameConfiguration.class.cast(field.get(viewModel));
		
		assertEquals(newConfiguration.getSize().getX(), configuration.getSize().getX());
		assertEquals(newConfiguration.getSize().getY(), configuration.getSize().getY());
		assertEquals(newConfiguration.getBoniProbability(), configuration.getBoniProbability());
		assertEquals(newConfiguration.getGameLengthLimit(), configuration.getGameLengthLimit());
		assertEquals(newConfiguration.getTurnLengthLimit(), configuration.getTurnLengthLimit());
		assertEquals(newConfiguration.getTreasureCount(), configuration.getTreasureCount());
		assertSame(newConfiguration.getBoni(), configuration.getBoni());
	}
	
	/**
	 * Tests that further listeners for GameState and CurrentPlayer are registered on AllClientsReady-Response
	 */
	@SuppressWarnings("unused")
	@Test
	public void doesRegisterFurtherListenersOnAllClientsReadyResponseTest()
	{
		UiThreadActionListener.setIsUnitTest(true);
		ClientConnectionManager.initialize(SocketClientMock.class);
		ClientConnectionManager.startGameServerClient(new GameServerEntry("Testserver"));
		SocketClientMock mock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		LobbyViewModel viewModel = new LobbyViewModel();
		
		assertEquals(1, mock.getListeners(MessageType.AllClientsReady).size());
		assertEquals(UiThreadActionListener.class, mock.getListeners(MessageType.AllClientsReady).get(0).getClass());		
		mock.sendResponse(new EmptyResponseDto(MessageType.AllClientsReady));
		assertEquals(1, mock.getListeners(MessageType.GameState).size());
		assertEquals(1, mock.getListeners(MessageType.CurrentPlayer).size());
		assertEquals(UiThreadActionListener.class, mock.getListeners(MessageType.GameState).get(0).getClass());
		assertEquals(UiThreadActionListener.class, mock.getListeners(MessageType.CurrentPlayer).get(0).getClass());
	}
	
	/**
	 * Tests that the currentPlayer-Field is set on a CurrentPlayerResponse
	 */
	@Test
	public void doesSetCurrentPlayerOnCurrentPlayerResponseTest() 
		throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		UiThreadActionListener.setIsUnitTest(true);
		ClientConnectionManager.initialize(SocketClientMock.class);
		ClientConnectionManager.startGameServerClient(new GameServerEntry("Testserver"));
		SocketClientMock mock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		LobbyViewModel viewModel = new LobbyViewModel();	
		mock.sendResponse(new EmptyResponseDto(MessageType.AllClientsReady));
		mock.sendResponse(new CurrentPlayerResponseDto("Test"));
		
		Field field = viewModel.getClass().getDeclaredField("currentPlayer");
		field.setAccessible(true);
		assertEquals("Test", field.get(viewModel));
	}
	
	/**
	 * Tests that the gameState-Field is set on a GameStateResponse
	 */
	@Test
	public void doesSetGameStateOnGameStateResponseTest() 
		throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		UiThreadActionListener.setIsUnitTest(true);
		ClientConnectionManager.initialize(SocketClientMock.class);
		ClientConnectionManager.startGameServerClient(new GameServerEntry("Testserver"));
		SocketClientMock mock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		LobbyViewModel viewModel = new LobbyViewModel();	
		mock.sendResponse(new EmptyResponseDto(MessageType.AllClientsReady));
		GameStateResponseDto gameState = new GameStateResponseDto(new BoardDto());
		mock.sendResponse(gameState);
		
		Field field = viewModel.getClass().getDeclaredField("gameState");
		field.setAccessible(true);
		assertSame(gameState, field.get(viewModel));
	}
	
	/**
	 * Tests that to client navigates to the GameView on a CurrentPlayerResponse if gameState was set before
	 */
	@Test
	public void doesNavigateToGameViewOnGameStateResponseIfCurrentPlayerIsSetTest() 
		throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException 
	{
		StageWrapperMock stageMock = new StageWrapperMock();
		NavigationManager.initialize(stageMock);
		UiThreadActionListener.setIsUnitTest(true);
		ClientConnectionManager.initialize(SocketClientMock.class);
		ClientConnectionManager.startGameServerClient(new GameServerEntry("Testserver"));
		SocketClientMock mock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		LobbyViewModel viewModel = new LobbyViewModel();	
		mock.sendResponse(new EmptyResponseDto(MessageType.AllClientsReady));
		
		Field field = viewModel.getClass().getDeclaredField("user");
		field.setAccessible(true);
		field.set(viewModel, new LobbyUser("user1", "green", UserPermission.ADMIN, LobbyUserKind.PLAYER));
		GameStateResponseDto gameState = new GameStateResponseDto(new BoardDto());		
		mock.sendResponse(gameState);
		assertNull(stageMock.getLastViewNavigatedTo());
		mock.sendResponse(new CurrentPlayerResponseDto("Test"));
		
		assertEquals(GameView.class, stageMock.getLastViewNavigatedTo());
		assertEquals("Test", stageMock.getParameters().get(GameViewModel.ACTIVE_PLAYER_PARAMETER));
		assertSame(gameState, stageMock.getParameters().get(GameViewModel.GAME_STATE_PARAMETER));
		assertEquals(11, stageMock.getParameters().get(GameViewModel.BOARD_SIZE_PARAMETER));
		assertEquals("user1", stageMock.getParameters().get(GameViewModel.CURRENT_PLAYER_PARAMETER));
	}
	
	/**
	 * Tests that to client navigates to the GameView on a GameStateResponse if currentPlayer was set before
	 */
	@Test
	public void doesNavigateToGameViewOnCurrentPlayerIfGameStateIsSetTest() 
		throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException 
	{
		StageWrapperMock stageMock = new StageWrapperMock();
		NavigationManager.initialize(stageMock);
		UiThreadActionListener.setIsUnitTest(true);
		ClientConnectionManager.initialize(SocketClientMock.class);
		ClientConnectionManager.startGameServerClient(new GameServerEntry("Testserver"));
		SocketClientMock mock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		LobbyViewModel viewModel = new LobbyViewModel();	
		mock.sendResponse(new EmptyResponseDto(MessageType.AllClientsReady));
		
		Field field = viewModel.getClass().getDeclaredField("user");
		field.setAccessible(true);
		field.set(viewModel, new LobbyUser("user1", "green", UserPermission.ADMIN, LobbyUserKind.SPECTATOR));
		mock.sendResponse(new CurrentPlayerResponseDto("Test"));
		assertNull(stageMock.getLastViewNavigatedTo());
		GameStateResponseDto gameState = new GameStateResponseDto(new BoardDto());		
		mock.sendResponse(gameState);	
		
		assertEquals(GameView.class, stageMock.getLastViewNavigatedTo());
		assertEquals("Test", stageMock.getParameters().get(GameViewModel.ACTIVE_PLAYER_PARAMETER));
		assertSame(gameState, stageMock.getParameters().get(GameViewModel.GAME_STATE_PARAMETER));
		assertEquals(11, stageMock.getParameters().get(GameViewModel.BOARD_SIZE_PARAMETER));
		assertNull(stageMock.getParameters().get(GameViewModel.CURRENT_PLAYER_PARAMETER));
	}
	
	/**
	* Tests that a UpdateLobby request is sent on navigating to the LobbyView
	*/
	@Test
	public void doesSendUpdateLobbyRequestOnNavigationTest()
	{
		ClientConnectionManager.initialize(SocketClientMock.class);
		ClientConnectionManager.startGameServerClient(new GameServerEntry("Testserver"));
		SocketClientMock mock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		LobbyViewModel viewModel = new LobbyViewModel();
		String username = "Testuser";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(LobbyViewModel.USER_PARAMETER, username);
		viewModel.handleNavigation(parameters);
		
		assertEquals(1, mock.getListeners(MessageType.UpdateLobby).size());
		assertEquals(ContinuousUiThreadActionListener.class, mock.getListeners(MessageType.UpdateLobby).get(0).getClass());
		assertEquals(EmptyRequestDto.class, mock.getLastRequest().getClass());
		EmptyRequestDto request = EmptyRequestDto.class.cast(mock.getLastRequest());
		assertEquals(MessageType.UpdateLobby, request.getMessageType());
	}
	
	/**
	 * Tests that the currentUser-Field is set correctly when navigating to the ViewModel with the corresponding parameter
	 */
	@Test
	public void doesSetUsernameFieldOnNavigationWithParameterTest() throws NoSuchFieldException, SecurityException, 
		IllegalArgumentException, IllegalAccessException
	{ 
		ClientConnectionManager.initialize(SocketClientMock.class);
		ClientConnectionManager.startGameServerClient(new GameServerEntry("Testserver"));
		LobbyViewModel viewModel = new LobbyViewModel();
		String username = "Testuser";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(LobbyViewModel.USER_PARAMETER, username);
		viewModel.handleNavigation(parameters);
		
		Field field = viewModel.getClass().getDeclaredField("username");
		field.setAccessible(true);
		assertEquals("Testuser", field.get(viewModel));
	}
	
	/**
	 * Tests @see LobbyViewModel.gameConfigurationCommand
	 */
	@Test
	public void gameConfigurationCommandTest()
	{
		ClientConnectionManager.initialize(SocketClientMock.class);
		ClientConnectionManager.startGameServerClient(new GameServerEntry("Testserver"));
		StageWrapperMock mock = new StageWrapperMock();
		NavigationManager.initialize(mock);
		LobbyViewModel viewModel = new LobbyViewModel();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(LobbyViewModel.USER_PARAMETER, "Test");
		viewModel.handleNavigation(parameters);
		viewModel.configurationCommandEnabledProperty().set(true);
		Command command = viewModel.getGameConfigurationCommand();
		command.execute();
		
		assertEquals(GameConfigurationView.class, mock.getLastViewNavigatedTo(), 
			"The execution of the command did not result in navigating to the GameConfigurationView.");
		Class<? extends Object> parameterClass 
			= mock.getParameters().get(GameConfigurationViewModel.CONFIGURATION_PARAMETER).getClass();
		assertEquals(GameConfiguration.class, parameterClass, "The configuration was not passed as a navigation parameter.");
	}
	
	/**
	 * Tests @see LobbyViewModel.leaveLobbyCommand
	 */
	@Test
	public void leaveLobbyCommandTest()
	{
		ClientConnectionManager.initialize(SocketClientMock.class);
		ClientConnectionManager.startGameServerClient(new GameServerEntry("Testserver"));
		SocketClientMock clientMock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		StageWrapperMock mock = new StageWrapperMock();
		NavigationManager.initialize(mock);
		LobbyViewModel viewModel = new LobbyViewModel();
		Command command = viewModel.getLeaveLobbyCommand();
		command.execute();
		
		assertEquals(EmptyRequestDto.class, clientMock.getLastRequest().getClass());
		EmptyRequestDto request = EmptyRequestDto.class.cast(clientMock.getLastRequest());
		assertEquals(MessageType.ClientLeave, request.getMessageType());
		assertEquals(MainMenuView.class, mock.getLastViewNavigatedTo(), 
			"The execution of the command did not result in navigating to the MainMenuView.");
	}	
	
	/**
	 * Tests that the bindable lists are updated when a UpdateLobby-Response arrives
	 */
	@Test
	public void doesUpdateBindableListsOnLobbyUpdateTest()
	{
		ContinuousUiThreadActionListener.setIsUnitTest(true);
		ClientConnectionManager.initialize(SocketClientMock.class);
		ClientConnectionManager.startGameServerClient(new GameServerEntry("Testserver"));
		SocketClientMock mock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		LobbyViewModel viewModel = new LobbyViewModel();
		viewModel.getBindablePlayerList().add(new LobbyUser("User4", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		viewModel.getBindableSpectatorList().add(new LobbyUser("User5", null, UserPermission.DEFAULT, LobbyUserKind.SPECTATOR));
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(LobbyViewModel.USER_PARAMETER, "Test");
		viewModel.handleNavigation(parameters);
	
		UpdateLobbyResponseDto response = createUpdateLobbyResponse(UserPermission.ADMIN);
		mock.sendResponse(response);		
		
		assertEquals(2, viewModel.getBindablePlayerList().size());
		assertEquals(1, viewModel.getBindableSpectatorList().size());
		assertTrue(viewModel.getBindablePlayerList().stream().anyMatch(user -> user.getName() == "Test"));
		assertTrue(viewModel.getBindablePlayerList().stream().anyMatch(user -> user.getName() == "User2"));
		assertTrue(viewModel.getBindableSpectatorList().stream().anyMatch(user -> user.getName() == "User3"));
	}
	
	/**
	 * Tests @see LobbyViewModel.changeRole
	 */
	@Test
	public void changeRoleTest() 
		throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	{
		ClientConnectionManager.initialize(SocketClientMock.class);
		ClientConnectionManager.startGameServerClient(new GameServerEntry("Testserver"));
		SocketClientMock clientMock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		LobbyViewModel viewModel = new LobbyViewModel();	
		LobbyUser user = new LobbyUser("user", null, UserPermission.ADMIN, LobbyUserKind.PLAYER);
		Field field = viewModel.getClass().getDeclaredField("user");
		field.setAccessible(true);
		field.set(viewModel, user);		
		viewModel.changeRole();
		
		assertEquals(ChangeRoleDto.class, clientMock.getLastRequest().getClass());
		ChangeRoleDto request = ChangeRoleDto.class.cast(clientMock.getLastRequest());
		assertEquals(LobbyUserKind.SPECTATOR, request.getRole());
	}
	
	/**
	 * Tests @see LobbyViewModel.changeColor
	 */
	@Test
	public void changeColorTest()
	{
		ClientConnectionManager.initialize(SocketClientMock.class);
		ClientConnectionManager.startGameServerClient(new GameServerEntry("Testserver"));
		SocketClientMock clientMock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		LobbyViewModel viewModel = new LobbyViewModel();		
		viewModel.changeColor("bla");
		
		assertEquals(ChangeColorDto.class, clientMock.getLastRequest().getClass());
		ChangeColorDto request = ChangeColorDto.class.cast(clientMock.getLastRequest());
		assertEquals("bla", request.getColor());
	}
	
	/**
	 * Tests @see LobbyViewModel.setAdmin
	 */
	@Test
	public void setAdminTest() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	{
		ClientConnectionManager.initialize(SocketClientMock.class);
		ClientConnectionManager.startGameServerClient(new GameServerEntry("Testserver"));
		SocketClientMock clientMock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		LobbyViewModel viewModel = new LobbyViewModel();	
		viewModel.setAdmin(new LobbyUser("Test", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER));
		
		assertEquals(ConfigurationDto.class, clientMock.getLastRequest().getClass());
		ConfigurationDto request = ConfigurationDto.class.cast(clientMock.getLastRequest());
		assertEquals("Test", request.getAdmin());
	}
	
	/**
	 * Tests @see LobbyViewModel.startGameCommand for a Admin user
	 */
	@Test
	public void startGameCommandAdminTest() 
		throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException 
	{ startGameCommandTestCore(UserPermission.ADMIN); }
	
	/**
	 * Tests @see LobbyViewModel.startGameCommand for a Admin user
	 */
	@Test
	public void startGameCommandDefaultUserTest() 
		throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException 
	{ startGameCommandTestCore(UserPermission.DEFAULT); }
	
	private static void startGameCommandTestCore(UserPermission permission) 
		throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{	
		ClientConnectionManager.initialize(SocketClientMock.class);
		ClientConnectionManager.startGameServerClient(new GameServerEntry("Testserver"));
		SocketClientMock clientMock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		StageWrapperMock mock = new StageWrapperMock();
		NavigationManager.initialize(mock);
		LobbyViewModel viewModel = new LobbyViewModel();	
		viewModel.startGameCommandEnabledProperty().set(true);
		LobbyUser user = new LobbyUser("user", null, permission, LobbyUserKind.PLAYER);
		Field field = viewModel.getClass().getDeclaredField("user");
		field.setAccessible(true);
		field.set(viewModel, user);		
		viewModel.getStartGameCommand().execute();
		
		assertEquals(EmptyRequestDto.class, clientMock.getLastRequest().getClass());
		EmptyRequestDto request = EmptyRequestDto.class.cast(clientMock.getLastRequest());
		if(permission == UserPermission.ADMIN)
			assertEquals(MessageType.StartGame, request.getMessageType());
		else
			assertEquals(MessageType.ClientReady, request.getMessageType());
		assertFalse(viewModel.startGameCommandEnabledProperty().get());
	}
	
	private static void doesSelectCorrectCommandCaptionTestCore(UserPermission permission)
	{
		ContinuousUiThreadActionListener.setIsUnitTest(true);
		ClientConnectionManager.initialize(SocketClientMock.class);
		ClientConnectionManager.startGameServerClient(new GameServerEntry("Testserver"));
		SocketClientMock mock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		LobbyViewModel viewModel = new LobbyViewModel();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(LobbyViewModel.USER_PARAMETER, "Test");
		viewModel.handleNavigation(parameters);
		
		UpdateLobbyResponseDto response = createUpdateLobbyResponse(permission);
		mock.sendResponse(response);
		
		String expectedCaption;
		if(permission == UserPermission.ADMIN)
			expectedCaption = Messages.getLobby_StartGameCommandCaption();
		else
			expectedCaption = Messages.getLobby_SignalReadyCommandCaption();			
		assertEquals(expectedCaption, viewModel.getStartGameCommandCaptionProperty(), 
			"The wrong caption was selected for the startGameCommand");
	}
	
	private static UpdateLobbyResponseDto createUpdateLobbyResponse(UserPermission permission)
	{
		UpdateLobbyResponseDto response = new UpdateLobbyResponseDto();
		LobbyUser user1 = new LobbyUser("Test", null, permission, LobbyUserKind.PLAYER);
		LobbyUser user2 = new LobbyUser("User2", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER);
		LobbyUser user3 = new LobbyUser("User3", null, UserPermission.DEFAULT, LobbyUserKind.SPECTATOR);
		response.getMembers().add(user1);
		response.getMembers().add(user2);
		response.getMembers().add(user3);
		return response;
	}
}
