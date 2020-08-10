package labyrinth.gameServer.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import labyrinth.contracts.communication.*;
import labyrinth.contracts.communication.dtos.requests.ClientJoinDto;
import labyrinth.contracts.communication.dtos.responses.*;
import labyrinth.contracts.entities.lobby.*;
import labyrinth.gameServer.GameServerConnectionMock;
import labyrinth.gameServer.GameServerManager;
import labyrinth.gameServer.stateManagers.GameServerEntryStateManager;
import labyrinth.gameServer.stateManagers.LobbyStateManager;

/**
 * TestClass for @see ClientJoinLogic
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ClientJoinLogicTests extends LobbyLogicTestBase
{
	/**
	 * Tests that the join was denied if a user with the same user name already exists in the lobby
	 */
	@Test
	public void clientJoinDeniedForUserWithDublicateUsernameTest()
	{	
		SocketClientMock clientMock 
			= SocketClientMock.class.cast(GameServerManager.getRunningServer().getRegistrationServerClient());
		LobbyUser user1 = new LobbyUser("Testuser", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER);
		GameServerConnectionMock mock = new GameServerConnectionMock();
		LobbyStateManager.getLobbyMonitor().executeAction(lobby -> lobby.getPlayerList().add(user1));
		GameServerEntryStateManager.getServerMonitor().executeAction(server -> server.getMembers().add(user1));
		ClientJoinLogic logic = new ClientJoinLogic(mock);
		logic.execute(new ClientJoinDto("Testuser", 1));
		clientMock.sendResponse(new SimpleSuccessResponseDto(MessageType.CheckUID, true));
		
		assertEquals(1, (int)GameServerEntryStateManager.getServerMonitor().executeFunction(server -> server.getMembers().size()));
		assertEquals(1, (int)LobbyStateManager.getLobbyMonitor().executeFunction(lobby -> lobby.getPlayerList().size()));
		assertTrue(LobbyStateManager.getLobbyMonitor().executeFunction(lobby -> lobby.getPlayerList().contains(user1)));
		assertEquals(0, (int)LobbyStateManager.getLobbyMonitor().executeFunction(lobby -> lobby.getSpectatorList().size()));
		assertEquals(SuccessMessageResponseDto.class, mock.getLastResponseSent().getClass());
		SuccessMessageResponseDto response = SuccessMessageResponseDto.class.cast(mock.getLastResponseSent());
		assertEquals(MessageType.ClientJoin, response.getMessageType());
		assertFalse(response.getSuccess());
		assertTrue(mock.getWasCanceled());
	}
	
	/**
	 * Tests that the join was denied if the check of the user id was not successful
	 */
	@Test
	public void clientJoinDeniedForFailedUserIdCheckTest()
	{	
		SocketClientMock clientMock 
			= SocketClientMock.class.cast(GameServerManager.getRunningServer().getRegistrationServerClient());
		GameServerConnectionMock mock = new GameServerConnectionMock();
		ClientJoinLogic logic = new ClientJoinLogic(mock);
		logic.execute(new ClientJoinDto("Testuser", 1));
		clientMock.sendResponse(new SimpleSuccessResponseDto(MessageType.CheckUID, false));
		
		assertEquals(0, (int)GameServerEntryStateManager.getServerMonitor().executeFunction(server -> server.getMembers().size()));
		assertEquals(0, (int)LobbyStateManager.getLobbyMonitor().executeFunction(lobby -> lobby.getPlayerList().size()));
		assertEquals(0, (int)LobbyStateManager.getLobbyMonitor().executeFunction(lobby -> lobby.getSpectatorList().size()));
		assertEquals(SuccessMessageResponseDto.class, mock.getLastResponseSent().getClass());
		SuccessMessageResponseDto response = SuccessMessageResponseDto.class.cast(mock.getLastResponseSent());
		assertEquals(MessageType.ClientJoin, response.getMessageType());
		assertFalse(response.getSuccess());
		assertTrue(mock.getWasCanceled());
	}
	
	/**
	 * Tests a successful client join
	 */
	@Test
	public void clientJoinSuccessTest()
	{	
		SocketClientMock clientMock 
			= SocketClientMock.class.cast(GameServerManager.getRunningServer().getRegistrationServerClient());
		GameServerConnectionMock mock = new GameServerConnectionMock();
		ClientJoinLogic logic = new ClientJoinLogic(mock);
		logic.execute(new ClientJoinDto("Testuser", 1));
		clientMock.sendResponse(new SimpleSuccessResponseDto(MessageType.CheckUID, true));
		
		assertEquals(1, (int)GameServerEntryStateManager.getServerMonitor().executeFunction(server -> server.getMembers().size()));
		assertEquals(1, (int)LobbyStateManager.getLobbyMonitor().executeFunction(lobby -> lobby.getPlayerList().size()));
		assertEquals(0, (int)LobbyStateManager.getLobbyMonitor().executeFunction(lobby -> lobby.getSpectatorList().size()));
		assertEquals(SuccessMessageResponseDto.class, mock.getLastResponseSent().getClass());
		SuccessMessageResponseDto response = SuccessMessageResponseDto.class.cast(mock.getLastResponseSent());
		assertEquals(MessageType.ClientJoin, response.getMessageType());
		assertTrue(response.getSuccess());
		assertFalse(mock.getWasCanceled());
		checkUpdateLobbyBroadcastResponse();
	}
}
