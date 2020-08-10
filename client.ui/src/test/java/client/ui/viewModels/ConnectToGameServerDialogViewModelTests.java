package client.ui.viewModels;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import client.ui.StageWrapperMock;
import labyrinth.client.ui.App;
import labyrinth.client.ui.ClientConnectionManager;
import labyrinth.client.ui.NavigationManager;
import labyrinth.client.ui.viewModels.ConnectToGameServerDialogViewModel;
import labyrinth.client.ui.viewModels.LobbyViewModel;
import labyrinth.client.ui.views.LobbyView;
import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.SocketClientMock;
import labyrinth.contracts.communication.dtos.requests.ClientJoinDto;
import labyrinth.contracts.communication.dtos.responses.SuccessMessageResponseDto;
import labyrinth.contracts.communication.listeners.UiThreadActionListener;
import labyrinth.contracts.entities.lobby.GameServerEntry;
import labyrinth.contracts.entities.lobby.GameServerStatus;

/**
 * TestClass for @see ConnectToGameServerDialogViewModel
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ConnectToGameServerDialogViewModelTests 
{
	/**
	 * Tests that the correct exception is thrown when navigating to the ViewModel without the GameServer-Parameter
	 */
	@Test
	public void throwsCorrectExceptionWhenNavigatingWithoutGameServerParameterTest()
	{
		ConnectToGameServerDialogViewModel viewModel = new ConnectToGameServerDialogViewModel();
		assertThrows(IllegalArgumentException.class, () -> viewModel.handleNavigation(null), 
			"Expected exception of type IllegalArgumentException. has not been thrown.");
		Map<String, Object> parameters = new HashMap<String, Object>();
		assertThrows(IllegalArgumentException.class, () -> viewModel.handleNavigation(parameters), 
			"Expected exception of type IllegalArgumentException. has not been thrown.");
	}
	
	/**
	 * Tests that the server field is set correctly when navigating to the ViewModel with the corresponding parameter
	 */
	@Test
	public void doesSetServerFieldOnNavigationWithParameterTest() throws NoSuchFieldException, SecurityException, 
		IllegalArgumentException, IllegalAccessException
	{
		ConnectToGameServerDialogViewModel viewModel = new ConnectToGameServerDialogViewModel();
		Map<String, Object> parameters = new HashMap<String, Object>();
		GameServerEntry entry = new GameServerEntry("Testserver", GameServerStatus.GAME_STARTED, "", 1);
		parameters.put(ConnectToGameServerDialogViewModel.GAMESERVER_PARAMETER, entry);
		
		Field field = viewModel.getClass().getDeclaredField("server");
		field.setAccessible(true);
		
		viewModel.handleNavigation(parameters);
		assertSame(entry, (GameServerEntry)field.get(viewModel), "The field server has not been set.");
	}
	
	/**
	 * Tests that the correct error is displayed to the user when trying to connect to the server without entering a user name 
	 */
	@Test
	public void cannotConnectToGameServerWithEmptyUsernameTest() 
		throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		Field clientField = ClientConnectionManager.class.getDeclaredField("gameServerClient");
		clientField.setAccessible(true);
		clientField.set(null, null);
		
		ClientConnectionManager.initialize(SocketClientMock.class);
		ConnectToGameServerDialogViewModel viewModel = new ConnectToGameServerDialogViewModel();
		viewModel.setUsernameProperty(null);
		viewModel.getConnectToGameServerCommand().execute();
		assertEquals("Der Nutzername darf nicht leer sein.", viewModel.getErrorProperty(), 
			"The error has not been set correctly.");
		SocketClientMock clientMock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		assertNull(clientMock);
		
		viewModel.setUsernameProperty("");
		viewModel.getConnectToGameServerCommand().execute();
		assertEquals("Der Nutzername darf nicht leer sein.", viewModel.getErrorProperty(), 
			"The error has not been set correctly.");
		clientMock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		assertNull(clientMock);
	}
	
	/**
	 * Tests @see ConnectToGameServerDialogViewModel.connectToGameServerCommand
	 */
	@Test
	public void connectToGameServerCommandTest()
	{
		App.setUserId(10);
		ClientConnectionManager.initialize(SocketClientMock.class);
		ConnectToGameServerDialogViewModel viewModel = new ConnectToGameServerDialogViewModel();
		GameServerEntry entry = new GameServerEntry("Testserver", GameServerStatus.LOBBY, "", 1);
		viewModel.setUsernameProperty("Test");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(ConnectToGameServerDialogViewModel.GAMESERVER_PARAMETER, entry);
		viewModel.handleNavigation(parameters);
		viewModel.getConnectToGameServerCommand().execute();
		
		SocketClientMock clientMock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		assertNotNull(clientMock);
		assertEquals(1, clientMock.getListeners(MessageType.ClientJoin).size());
		assertEquals(ClientJoinDto.class, clientMock.getLastRequest().getClass());
		ClientJoinDto request = ClientJoinDto.class.cast(clientMock.getLastRequest());
		assertEquals("Test", request.getName());
		assertEquals(10, request.getUserID());
	}
	
	/**
	 * Tests @see ConnectToGameServerDialogViewModel.connectToGameServerCommand
	 */
	@Test
	public void doesNotNaviagteToTheLobbyViewOnNegativeJoinResponseTest()
	{
		UiThreadActionListener.setIsUnitTest(true);
		App.setUserId(10);
		ClientConnectionManager.initialize(SocketClientMock.class);
		ConnectToGameServerDialogViewModel viewModel = new ConnectToGameServerDialogViewModel();
		GameServerEntry entry = new GameServerEntry("Testserver", GameServerStatus.LOBBY, "", 1);
		viewModel.setUsernameProperty("Test");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(ConnectToGameServerDialogViewModel.GAMESERVER_PARAMETER, entry);
		viewModel.handleNavigation(parameters);
		viewModel.getConnectToGameServerCommand().execute();
		viewModel.setCloseDialogAction(() -> fail("The close action should not be called"));
		
		SocketClientMock clientMock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		clientMock.sendResponse(new SuccessMessageResponseDto(MessageType.ClientJoin, false, "Test"));
		assertEquals("Test", viewModel.getErrorProperty());
	}
	
	/**
	 * Tests that the dialog isn't closed when there is a negative response from the client join
	 */
	@Test
	public void doesNotCloseDialogOnNegativeJoinResponseTest()
	{
		UiThreadActionListener.setIsUnitTest(true);
		App.setUserId(10);
		ClientConnectionManager.initialize(SocketClientMock.class);
		ConnectToGameServerDialogViewModel viewModel = new ConnectToGameServerDialogViewModel();
		GameServerEntry entry = new GameServerEntry("Testserver", GameServerStatus.LOBBY, "", 1);
		viewModel.setUsernameProperty("Test");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(ConnectToGameServerDialogViewModel.GAMESERVER_PARAMETER, entry);
		viewModel.handleNavigation(parameters);
		viewModel.getConnectToGameServerCommand().execute();
		viewModel.setCloseDialogAction(() -> fail("The close action should not be called"));
		
		SocketClientMock clientMock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		clientMock.sendResponse(new SuccessMessageResponseDto(MessageType.ClientJoin, false, "Test"));
		assertEquals("Test", viewModel.getErrorProperty());
	}
	
	/**
	 * Tests that the dialog isn't closed when there is a negative response from the client join
	 */
	@Test
	public void navigatesToLobbyViewOnPositiveJoinResponseTest()
	{
		StageWrapperMock mock = new StageWrapperMock();
		NavigationManager.initialize(mock);
		UiThreadActionListener.setIsUnitTest(true);
		App.setUserId(10);
		ClientConnectionManager.initialize(SocketClientMock.class);
		ConnectToGameServerDialogViewModel viewModel = new ConnectToGameServerDialogViewModel();
		GameServerEntry entry = new GameServerEntry("Testserver", GameServerStatus.LOBBY, "", 1);
		viewModel.setUsernameProperty("Test");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(ConnectToGameServerDialogViewModel.GAMESERVER_PARAMETER, entry);
		viewModel.handleNavigation(parameters);
		viewModel.getConnectToGameServerCommand().execute();
		
		SocketClientMock clientMock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		clientMock.sendResponse(new SuccessMessageResponseDto(MessageType.ClientJoin, true, null));
		assertNull(viewModel.getErrorProperty());
		assertEquals(LobbyView.class, mock.getLastViewNavigatedTo());
		assertNotNull(mock.getParameters().get(LobbyViewModel.USER_PARAMETER));
	}
}
