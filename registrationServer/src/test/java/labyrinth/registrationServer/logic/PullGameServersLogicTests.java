package labyrinth.registrationServer.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.requests.EmptyRequestDto;
import labyrinth.contracts.communication.dtos.responses.PullGameServersResponseDto;
import labyrinth.contracts.entities.lobby.GameServerEntry;
import labyrinth.contracts.entities.lobby.GameServerStatus;
import labyrinth.registrationServer.RegistrationServerConnectionMock;
import labyrinth.registrationServer.RegistrationServerStateManager;

/**
 * TestClass for @see PullGameServersLogic
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class PullGameServersLogicTests extends RegistrationServerLogicTestBase 
{
	/**
	 * Tests that all GameServers are returned
	 */
	@Test
	public void pullGameServersLogicTest()
	{
		GameServerEntry server1 = new GameServerEntry("Testserver", GameServerStatus.LOBBY, "12.12.12", 1000);
		GameServerEntry server2 = new GameServerEntry("Server2", GameServerStatus.LOBBY, "13.13.13", 1001);
		RegistrationServerConnectionMock mock = new RegistrationServerConnectionMock();	
		RegistrationServerStateManager.getServers().add(server1);
		RegistrationServerStateManager.getServers().add(server2);
		PullGameServersLogic logic = new PullGameServersLogic(mock);
		logic.execute(new EmptyRequestDto(MessageType.PullGameServers));
		
		assertEquals(PullGameServersResponseDto.class, mock.getLastResponseSent().getClass());	
		PullGameServersResponseDto response = PullGameServersResponseDto.class.cast(mock.getLastResponseSent());
		assertEquals(2, response.getItems().size());
		assertTrue(response.getItems().contains(server1));
		assertTrue(response.getItems().contains(server2));
	}
}