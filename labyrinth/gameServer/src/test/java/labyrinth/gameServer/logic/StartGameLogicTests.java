package labyrinth.gameServer.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import labyrinth.contracts.communication.*;
import labyrinth.contracts.communication.dtos.requests.EmptyRequestDto;
import labyrinth.contracts.communication.dtos.responses.SimpleSuccessResponseDto;
import labyrinth.contracts.entities.lobby.*;
import labyrinth.gameServer.*;
import labyrinth.gameServer.stateManagers.*;

/**
 * BaseClass for all LogicTests that broadcast a LobbyUpdateResponse
 * @author Lukas Reinhardt
 * @version 1.0
 */
public class StartGameLogicTests extends LobbyLogicTestBase
{
	/**
	 * Tests that it's not possible to start the game without being the admin
	 */
	@Test
	public void cannotStartGameWithoutAdminPermissionTest()
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
		
		cannotStartGameTestCore(mock, user2);
	}
	
	/**
	 * Tests that it is not possible to start the game without the minimum amount of players
	 */
	@Test
	public void cannotStartGameWithNotEnoughPlayersTest()
	{	
		LobbyUser user = new LobbyUser("Testuser", null, UserPermission.ADMIN, LobbyUserKind.PLAYER);
		GameServerConnectionMock mock = new GameServerConnectionMock();
		mock.setUser(user);
		LobbyStateManager.getLobbyMonitor().executeAction(lobby -> lobby.getPlayerList().add(user));		
		GameServerEntryStateManager.getServerMonitor().executeAction(server -> server.getMembers().add(user));
		
		cannotStartGameTestCore(mock, user);
	}
	
	/**
	 * Tests starting the game
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void startGameSuccessfulTest()
	{	
		LobbyUser user1 = new LobbyUser("Testuser", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER);
		LobbyUser user2 = new LobbyUser("Testuser", null, UserPermission.ADMIN, LobbyUserKind.PLAYER);
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
		StartGameLogic logic = new StartGameLogic(mock);
		logic.execute(new EmptyRequestDto(MessageType.StartGame));
		
		SocketServerMock<GameServerConnection> mockServer 
			= SocketServerMock.class.cast(GameServerManager.getRunningServer().getServer());
		assertEquals(SimpleSuccessResponseDto.class, mockServer.getBroadcastedResponse().getClass());
		SimpleSuccessResponseDto response = SimpleSuccessResponseDto.class.cast(mockServer.getBroadcastedResponse());
		assertEquals(MessageType.StartGame, response.getMessageType());
		assertTrue(response.getSuccess());
	}
	
	private void cannotStartGameTestCore(GameServerConnectionMock mock, LobbyUser user)
	{
		StartGameLogic logic = new StartGameLogic(mock);
		logic.execute(new EmptyRequestDto(MessageType.StartGame));
		
		assertFalse(user.getIsReady());
		assertEquals(SimpleSuccessResponseDto.class, mock.getLastResponseSent().getClass());
		SimpleSuccessResponseDto response = SimpleSuccessResponseDto.class.cast(mock.getLastResponseSent());
		assertEquals(MessageType.StartGame, response.getMessageType());
		assertFalse(response.getSuccess());
	}
}
