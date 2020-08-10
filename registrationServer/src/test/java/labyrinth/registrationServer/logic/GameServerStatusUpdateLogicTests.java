package labyrinth.registrationServer.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import labyrinth.contracts.communication.dtos.requests.GameServerStatusUpdateDto;
import labyrinth.contracts.entities.lobby.GameServerEntry;
import labyrinth.contracts.entities.lobby.GameServerStatus;
import labyrinth.contracts.entities.lobby.LobbyUserKind;
import labyrinth.contracts.entities.lobby.User;
import labyrinth.registrationServer.RegistrationServerConnectionMock;

/**
 * TestClass for @see GameServerRegisterLogic
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class GameServerStatusUpdateLogicTests extends RegistrationServerLogicTestBase 
{
	/**
	 * Tests that the GameServer is registered correctly
	 */
	@Test
	public void gameServerStatusUpdateLogicTest()
	{
		RegistrationServerConnectionMock mock = new RegistrationServerConnectionMock();	
		mock.setServer(new GameServerEntry("Testserver", GameServerStatus.LOBBY, "12.12.12", 1000));
		mock.getServer().getMembers().add(new User("tests", LobbyUserKind.PLAYER));
		GameServerStatusUpdateLogic logic = new GameServerStatusUpdateLogic(mock);
		GameServerStatusUpdateDto request = new GameServerStatusUpdateDto("Testserver1", GameServerStatus.GAME_STARTED);
		request.getMembers().add(new User("player", LobbyUserKind.PLAYER));
		request.getMembers().add(new User("spectator", LobbyUserKind.SPECTATOR));
		logic.execute(request);
		
		assertEquals(request.getServerName(), mock.getServer().getServerName());
		assertEquals(request.getState(), mock.getServer().getState());
		assertEquals(2, mock.getServer().getMembers().size());
		assertTrue(mock.getServer().getMembers().contains(request.getMembers().get(0)));
		assertTrue(mock.getServer().getMembers().contains(request.getMembers().get(1)));		
		assertNull(mock.getLastResponseSent());
	}
}