package client.ui.viewModels;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import client.ui.StageWrapperMock;
import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import labyrinth.client.ui.*;
import labyrinth.client.ui.viewModels.*;
import labyrinth.client.ui.views.ConnectToGameServerDialogView;
import labyrinth.contracts.communication.*;
import labyrinth.contracts.communication.dtos.requests.EmptyRequestDto;
import labyrinth.contracts.communication.dtos.responses.PullGameServersResponseDto;
import labyrinth.contracts.communication.listeners.UiThreadActionListener;
import labyrinth.contracts.entities.lobby.GameServerEntry;
import labyrinth.contracts.entities.lobby.GameServerStatus;
import labyrinth.contracts.entities.lobby.LobbyUserKind;
import labyrinth.contracts.entities.lobby.User;

/**
 * TestClass for @see GameServerSelectionViewModel
 * @author Lukas Reinhardt
 * @version 1.2
 */
public final class GameServerSelectionViewModelTests 
{	
	/**
	 * Tests that the GameServers are retrieved when navigating to the GameServerSelectionViewModel
	 */
	@Test
	public void doesRetrieveServersOnNavigationTest()
	{ retrieveServersTestCore((viewModel) -> viewModel.handleNavigation(null)); }
	
	/**
	 * Tests @see GameServerSelectionViewModel.refreshServerListCommand
	 */
	@Test
	public void refreshServerListCommandTest()
	{ retrieveServersTestCore((viewModel) -> viewModel.getRefreshServerListCommand().execute()); }
	
	/**
	 * Tests that the retrieved Servers are added to the server list once the response arrives
	 */
	@Test
	public void doesHandlePullGameServersResponseTest()
	{
		UiThreadActionListener.setIsUnitTest(true);
		ClientConnectionManager.initialize(SocketClientMock.class);
		ClientConnectionManager.startRegistrationServerClient();
		SocketClientMock mock  = SocketClientMock.class.cast(ClientConnectionManager.getRegistrationServerClient());
		GameServerSelectionViewModel viewModel = new GameServerSelectionViewModel();
		PullGameServersResponseDto response = new PullGameServersResponseDto();
		viewModel.handleNavigation(null);
		GameServerEntry server = new GameServerEntry("TestServer", GameServerStatus.LOBBY, "1", 1);
		response.getItems().add(server);
		mock.sendResponse(response);
		
		assertEquals(1, viewModel.getBindableServerList().size());
		assertTrue(viewModel.getBindableServerList().contains(server));
	}
	
	/**
	 * Tests that the player count is calculated correctly
	 */
	@Test
	public void calculatePlayerCountValueTest()
	{
		GameServerEntry server = new GameServerEntry("Test", GameServerStatus.LOBBY, "", 10);
		server.getMembers().add(new User("player", LobbyUserKind.PLAYER));
		server.getMembers().add(new User("spectator", LobbyUserKind.SPECTATOR));
		
		assertNull(GameServerSelectionViewModel.calculatePlayerCountValue(null));
		StringProperty property = GameServerSelectionViewModel.calculatePlayerCountValue(server);
		assertNotNull(property);
		assertEquals("1/4", property.get());
	}
	
	/**
	 * Tests @see GameServerSelectionViewModel.navigateBackCommand
	 */
	@Test
	public void navigateBackCommandTest()
	{
		StageWrapperMock mock = new StageWrapperMock();
		NavigationManager.initialize(mock);
		GameServerSelectionViewModel viewModel = new GameServerSelectionViewModel();
		Command command = viewModel.getNavigateBackCommand();
		command.execute();
		assertTrue(mock.getWasNavigatedBack());
	}
	
	/**
	 * Tests @see GameServerSelectionViewModel.onServerSelected
	 */
	@Test
	public void onServerSelectedTest()
	{
		StageWrapperMock mock = new StageWrapperMock();
		NavigationManager.initialize(mock);
		GameServerEntry entry = new GameServerEntry("Testserver", GameServerStatus.LOBBY, "12.12", 1000);
		GameServerSelectionViewModel viewModel = new GameServerSelectionViewModel();
		viewModel.onServerSelected(entry);
		assertEquals(ConnectToGameServerDialogView.class, mock.getLastViewShownAsDialog(), 
			"The dialog was not shown as expected.");
		assertSame(entry, mock.getParameters().get(ConnectToGameServerDialogViewModel.GAMESERVER_PARAMETER), 
			"The GameServer was not passed tothe DialogViewModel as parameter.");
	}
	
	/**
	 * Tests that the list of available GameServers is filtered correctly when changing the search text
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void filterServerListTest() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		GameServerSelectionViewModel viewModel = new GameServerSelectionViewModel();
		ObservableList<GameServerEntry> bindableList = viewModel.getBindableServerList();
		Field field = viewModel.getClass().getDeclaredField("serverList");
		field.setAccessible(true);
		List<GameServerEntry> serverList = (List<GameServerEntry>)field.get(viewModel);
		serverList.clear();
		bindableList.clear();
		
		GameServerEntry entry1 = new GameServerEntry("AAAAAAAA", GameServerStatus.GAME_STARTED, "", 1);
		GameServerEntry entry2 = new GameServerEntry("AABBBBAA", GameServerStatus.GAME_STARTED, "", 1);
		GameServerEntry entry3 = new GameServerEntry("CCCCCCCC", GameServerStatus.GAME_STARTED, "", 1);
		serverList.add(entry1);
		serverList.add(entry2);
		serverList.add(entry3);
		
		viewModel.setSearchTextProperty("BBB");
		assertFalse(bindableList.contains(entry1), "The list was not filtered correctly.");
		assertTrue(bindableList.contains(entry2), "The list was not filtered correctly.");
		assertFalse(bindableList.contains(entry3), "The list was not filtered correctly.");
		
		viewModel.setSearchTextProperty("");
		assertTrue(bindableList.contains(entry1), "The list was not filtered correctly.");
		assertTrue(bindableList.contains(entry2), "The list was not filtered correctly.");
		assertTrue(bindableList.contains(entry3), "The list was not filtered correctly.");
		
		viewModel.setSearchTextProperty("CC");
		assertFalse(bindableList.contains(entry1), "The list was not filtered correctly.");
		assertFalse(bindableList.contains(entry2), "The list was not filtered correctly.");
		assertTrue(bindableList.contains(entry3), "The list was not filtered correctly.");
	}
	
	private void retrieveServersTestCore(Consumer<GameServerSelectionViewModel> testAction)
	{
		ClientConnectionManager.initialize(SocketClientMock.class);
		ClientConnectionManager.startRegistrationServerClient();
		SocketClientMock mock  = SocketClientMock.class.cast(ClientConnectionManager.getRegistrationServerClient());
		GameServerSelectionViewModel viewModel = new GameServerSelectionViewModel();
		testAction.accept(viewModel);
		
		assertEquals(1, mock.getListeners(MessageType.PullGameServers).size());
		assertEquals(EmptyRequestDto.class, mock.getLastRequest().getClass());
		EmptyRequestDto request = EmptyRequestDto.class.cast(mock.getLastRequest());
		assertEquals(MessageType.PullGameServers, request.getMessageType());
	}
}
