package labyrinth.registrationServer.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.requests.GameServerRegisterDto;
import labyrinth.contracts.communication.dtos.responses.SimpleSuccessResponseDto;
import labyrinth.registrationServer.RegistrationServerConnectionMock;
import labyrinth.registrationServer.RegistrationServerStateManager;

/**
 * TestClass for @see GameServerRegisterLogic
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class GameServerRegisterLogicTests extends RegistrationServerLogicTestBase 
{
	/**
	 * Tests that the GameServer is registered correctly
	 */
	@Test
	public void gameServerRegisterLogicTest()
	{
		RegistrationServerConnectionMock mock = new RegistrationServerConnectionMock();	
		GameServerRegisterLogic logic = new GameServerRegisterLogic(mock);
		GameServerRegisterDto request = new GameServerRegisterDto("Testserver", 1000);
		logic.execute(request);
		
		assertEquals(request.getServerName(), mock.getServer().getServerName());
		assertEquals(request.getPort(), mock.getServer().getPort());
		assertTrue(RegistrationServerStateManager.getServers().contains(mock.getServer()));
		assertEquals(SimpleSuccessResponseDto.class, mock.getLastResponseSent().getClass());
		SimpleSuccessResponseDto response = SimpleSuccessResponseDto.class.cast(mock.getLastResponseSent());
		assertTrue(response.getSuccess());
		assertEquals(MessageType.GameServerRegister, response.getMessageType());
	}
}
