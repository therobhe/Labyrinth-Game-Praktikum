package labyrinth.gameServer.stateManagers;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import labyrinth.contracts.communication.dtos.responses.UpdateLobbyResponseDto;
import labyrinth.contracts.entities.lobby.LobbyUser;
import labyrinth.contracts.entities.lobby.LobbyUserKind;
import labyrinth.contracts.entities.lobby.UserPermission;
import labyrinth.gameServer.stateManagers.LobbyStateManager;

/**
 * TestClass for @see LobbyStateManager
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class LobbyStateManagerTests 
{
	/**
	 * Tests the Construction of the UpdateLobbyResponseDto
	 */
	@Test
	public void createUpdateLobbyResponseDtoResponse()
	{
		LobbyStateManager.initialize();
		LobbyUser user1 = new LobbyUser("Player", null, UserPermission.ADMIN, LobbyUserKind.PLAYER);
		LobbyUser user2 = new LobbyUser("Spectator", null, UserPermission.DEFAULT, LobbyUserKind.SPECTATOR);
		
		LobbyStateManager.getLobbyMonitor().executeAction(lobby ->
		{
			lobby.getPlayerList().add(user1);
			lobby.getSpectatorList().add(user2);
		});
		
		UpdateLobbyResponseDto response = LobbyStateManager.createUpdateLobbyResponseDto();
		assertTrue(response.getMembers().contains(user1));
		assertTrue(response.getMembers().contains(user2));
	}
	
	/**
	 * Cleans up the tests
	 */
	@AfterEach
	public void cleanup() 
	{
		LobbyStateManager.getLobbyMonitor().executeAction(lobby -> 
		{
			lobby.getPlayerList().clear();
			lobby.getSpectatorList().clear();
		});
	}
}
