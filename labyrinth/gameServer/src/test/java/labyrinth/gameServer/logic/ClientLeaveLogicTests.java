package labyrinth.gameServer.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import labyrinth.contracts.communication.*;
import labyrinth.contracts.communication.dtos.requests.EmptyRequestDto;
import labyrinth.contracts.entities.lobby.*;
import labyrinth.gameServer.*;
import labyrinth.gameServer.stateManagers.*;

/**
 * TestClass for @see ClientLeaveLogic
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ClientLeaveLogicTests extends LobbyLogicTestBase
{
	/**
	 * Tests that the user is removed from the Lobby and server
	 */
	@Test
	public void clientLeaveTest()
	{	
		LobbyUser user1 = new LobbyUser("Testuser", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER);
		LobbyUser user2 = new LobbyUser("Testuser", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER);
		GameServerConnectionMock mock = new GameServerConnectionMock();
		mock.setUser(user2);
		LobbyStateManager.getLobbyMonitor().executeAction(lobby -> 
		{
			lobby.getPlayerList().add(user1);
			lobby.getPlayerList().add(user2);
		});		
		GameServerEntryStateManager.getServerMonitor().executeAction(server -> 
		{
			server.getMembers().add(user1);
			server.getMembers().add(user2);
		});
		ClientLeaveLogic logic = new ClientLeaveLogic(mock);
		logic.execute(new EmptyRequestDto(MessageType.ClientLeave));
		
		assertEquals(1, (int)GameServerEntryStateManager.getServerMonitor().executeFunction(server -> server.getMembers().size()));
		assertEquals(1, (int)LobbyStateManager.getLobbyMonitor().executeFunction(lobby -> lobby.getPlayerList().size()));
		assertEquals(0, (int)LobbyStateManager.getLobbyMonitor().executeFunction(lobby -> lobby.getSpectatorList().size()));
		assertTrue(LobbyStateManager.getLobbyMonitor().executeFunction(lobby -> lobby.getPlayerList().contains(user1)));
		assertFalse(LobbyStateManager.getLobbyMonitor().executeFunction(lobby -> lobby.getPlayerList().contains(user2)));
		assertTrue(GameServerEntryStateManager.getServerMonitor().executeFunction(lobby -> lobby.getMembers().contains(user1)));
		assertFalse(GameServerEntryStateManager.getServerMonitor().executeFunction(lobby -> lobby.getMembers().contains(user2)));
		
		checkUpdateLobbyBroadcastResponse();
	}
	
	/**
	 * Tests if the server is closed when the last client leaves
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void serverIsClosedIfLastUserLeavesTest()
	{	
		LobbyUser user1 = new LobbyUser("Testuser", null, UserPermission.ADMIN, LobbyUserKind.PLAYER);
		GameServerConnectionMock mock = new GameServerConnectionMock();
		mock.setUser(user1);
		LobbyStateManager.getLobbyMonitor().executeAction(lobby -> lobby.getPlayerList().add(user1));		
		GameServerEntryStateManager.getServerMonitor().executeAction(server -> server.getMembers().add(user1));
		ClientLeaveLogic logic = new ClientLeaveLogic(mock);
		logic.execute(new EmptyRequestDto(MessageType.ClientLeave));
		
		SocketClientMock clientMock 
			= SocketClientMock.class.cast(GameServerManager.getRunningServer().getRegistrationServerClient());
		SocketServerMock<GameServerConnection> serverMock 
			= SocketServerMock.class.cast(GameServerManager.getRunningServer().getServer());
		assertTrue(clientMock.getWasClosed());
		assertTrue(serverMock.getWasClosed());
	}
	
	/**
	 * Tests that a new admin is selected when the current admin leaves
	 */
	@Test
	public void newAdminIsSelectedWhenAdminLeavesTest()
	{
		LobbyUser user1 = new LobbyUser("user1", null, UserPermission.ADMIN, LobbyUserKind.PLAYER);
		LobbyUser user2 = new LobbyUser("user2", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER);
		LobbyUser user3 = new LobbyUser("user3", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER);
		GameServerConnectionMock mock = new GameServerConnectionMock();
		mock.setUser(user1);
		LobbyStateManager.getLobbyMonitor().executeAction(lobby -> 
		{
			lobby.getPlayerList().add(user1);
			lobby.getPlayerList().add(user2);
			lobby.getPlayerList().add(user3);
		});		
		GameServerEntryStateManager.getServerMonitor().executeAction(server -> 
		{
			server.getMembers().add(user1);
			server.getMembers().add(user2);
			server.getMembers().add(user3);
		});
		ClientLeaveLogic logic = new ClientLeaveLogic(mock);
		logic.execute(new EmptyRequestDto(MessageType.ClientLeave));
		
		assertEquals(UserPermission.ADMIN, user2.getPermission());
	}
}
