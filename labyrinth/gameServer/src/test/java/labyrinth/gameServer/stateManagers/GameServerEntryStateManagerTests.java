package labyrinth.gameServer.stateManagers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import labyrinth.contracts.communication.dtos.requests.GameServerStatusUpdateDto;
import labyrinth.contracts.entities.lobby.GameServerEntry;
import labyrinth.contracts.entities.lobby.GameServerStatus;
import labyrinth.contracts.entities.lobby.LobbyUser;
import labyrinth.contracts.entities.lobby.LobbyUserKind;
import labyrinth.contracts.entities.lobby.UserPermission;

/**
 * TestClass for @see GameServerEntryStateManager
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class GameServerEntryStateManagerTests 
{
	/**
	 * Tests the creation of the ServerStatusUpdateRequest
	 */
	@Test
	public void createServerStatusUpdateRequestTest()
	{
		LobbyUser user1 = new LobbyUser("Player", null, UserPermission.ADMIN, LobbyUserKind.PLAYER);
		LobbyUser user2 = new LobbyUser("Spectator", null, UserPermission.DEFAULT, LobbyUserKind.SPECTATOR);
		GameServerEntry server = new GameServerEntry("Testserver");
		server.setState(GameServerStatus.GAME_STARTED);
		server.getMembers().add(user1);
		server.getMembers().add(user2);
		GameServerEntryStateManager.initialize(server);
		GameServerStatusUpdateDto request = GameServerEntryStateManager.createServerStatusUpdateRequest();
		
		assertEquals("Testserver", request.getServerName());
		assertEquals(GameServerStatus.GAME_STARTED, request.getState());
		assertTrue(request.getMembers().contains(user1));
		assertTrue(request.getMembers().contains(user2));
	}
}
