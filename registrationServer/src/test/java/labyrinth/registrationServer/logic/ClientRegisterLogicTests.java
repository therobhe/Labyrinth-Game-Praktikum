package labyrinth.registrationServer.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.requests.EmptyRequestDto;
import labyrinth.contracts.communication.dtos.responses.ClientRegisterResponseDto;
import labyrinth.registrationServer.RegistrationServerConnectionMock;
import labyrinth.registrationServer.RegistrationServerStateManager;

/**
 * TestClass for @see ClientRegisterLogic
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ClientRegisterLogicTests extends RegistrationServerLogicTestBase 
{
	/**
	 * Tests that the client is registered correctly
	 */
	@Test
	public void clientRegisterLogicTest()
	{
		RegistrationServerConnectionMock mock = new RegistrationServerConnectionMock();
		ClientRegisterLogic logic = new ClientRegisterLogic(mock);
		int lastCount = RegistrationServerStateManager.getUserIds().size();
		logic.execute(new EmptyRequestDto(MessageType.ClientRegister));
		int newCount = RegistrationServerStateManager.getUserIds().size();
		
		assertEquals(lastCount + 1, newCount);
		assertEquals(newCount - 1, (int)RegistrationServerStateManager.getUserIds().get(newCount - 1));
		assertEquals(newCount - 1, mock.getUserId());
		assertEquals(ClientRegisterResponseDto.class, mock.getLastResponseSent().getClass());
		ClientRegisterResponseDto response = ClientRegisterResponseDto.class.cast(mock.getLastResponseSent());
		assertEquals(newCount - 1, response.getUserID());
	}
}
