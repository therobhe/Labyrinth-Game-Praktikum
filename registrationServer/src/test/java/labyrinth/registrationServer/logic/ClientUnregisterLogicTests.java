package labyrinth.registrationServer.logic;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.requests.EmptyRequestDto;
import labyrinth.registrationServer.RegistrationServerConnectionMock;
import labyrinth.registrationServer.RegistrationServerStateManager;

/**
 * TestClass for @see ClientUnregisterLogic
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ClientUnregisterLogicTests extends RegistrationServerLogicTestBase 
{
	/**
	 * Tests that the client is unregistered correctly
	 */
	@Test
	public void clientUnregisterLogicTest()
	{
		RegistrationServerConnectionMock mock = new RegistrationServerConnectionMock();
		mock.setUserId(1);
		RegistrationServerStateManager.addUserId(1);
		ClientUnregisterLogic logic = new ClientUnregisterLogic(mock);
		logic.execute(new EmptyRequestDto(MessageType.ClientRegister));
		
		assertFalse(RegistrationServerStateManager.getUserIds().contains(1));
		assertNull(mock.getLastResponseSent());
		assertTrue(mock.getWasCanceled());
	}
}
