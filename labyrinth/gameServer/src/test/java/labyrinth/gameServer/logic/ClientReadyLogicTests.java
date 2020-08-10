package labyrinth.gameServer.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import labyrinth.contracts.communication.*;
import labyrinth.contracts.communication.dtos.requests.EmptyRequestDto;
import labyrinth.contracts.communication.dtos.responses.GameStateResponseDto;
import labyrinth.contracts.entities.lobby.*;
import labyrinth.gameServer.*;
import labyrinth.gameServer.stateManagers.LobbyStateManager;

/**
 * TestClass for @see ClientLeaveLogic
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ClientReadyLogicTests extends LobbyLogicTestBase
{
	/**
	 * Tests that the ready status of the user is set
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void clientReadyLogicTest()
	{	
		LobbyUser user1 = new LobbyUser("Testuser", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER);
		LobbyUser user2 = new LobbyUser("Testuser1", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER);
		GameServerConnectionMock mock = new GameServerConnectionMock();
		mock.setUser(user1);
		LobbyStateManager.getLobbyMonitor().executeAction(lobby -> 
		{
			lobby.getPlayerList().add(user1);
			lobby.getPlayerList().add(user2);
		});
		ClientReadyLogic logic = new ClientReadyLogic(mock);
		SocketServerMock<GameServerConnection> mockServer 
			= SocketServerMock.class.cast(GameServerManager.getRunningServer().getServer());
		
		logic.execute(new EmptyRequestDto(MessageType.ClientReady));	
		assertTrue(user1.getIsReady());
		assertNull(mockServer.getBroadcastedResponse());
		
		mock.setUser(user2);
		logic.execute(new EmptyRequestDto(MessageType.ClientReady));	
		assertTrue(user2.getIsReady());
		assertEquals(GameStateResponseDto.class, mockServer.getBroadcastedResponse().getClass());
	}
}
