package labyrinth.registrationServer.logic;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.requests.EmptyRequestDto;
import labyrinth.contracts.entities.lobby.GameServerEntry;
import labyrinth.contracts.entities.lobby.GameServerStatus;
import labyrinth.registrationServer.RegistrationServerConnectionMock;
import labyrinth.registrationServer.RegistrationServerStateManager;

/**
 * TestClass for @see GameServerUnregisterLogic
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class GameServerUnregisterLogicTests extends RegistrationServerLogicTestBase 
{
	/**
	 * Tests that the GameServer is unregistered correctly
	 */
	@Test
	public void gameServerUnregisterLogicTest()
	{
		RegistrationServerConnectionMock mock = new RegistrationServerConnectionMock();	
		mock.setServer(new GameServerEntry("Testserver", GameServerStatus.LOBBY, "12.12.12", 1000));
		RegistrationServerStateManager.getServers().add(mock.getServer());
		GameServerUnregisterLogic logic = new GameServerUnregisterLogic(mock);
		logic.execute(new EmptyRequestDto(MessageType.GameServerUnregister));
		
		assertFalse(RegistrationServerStateManager.getServers().contains(mock.getServer()));		
		assertNull(mock.getLastResponseSent());
		assertTrue(mock.getWasCanceled());
	}
}
