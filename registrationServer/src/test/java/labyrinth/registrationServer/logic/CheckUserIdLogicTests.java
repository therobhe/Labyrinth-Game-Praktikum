package labyrinth.registrationServer.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.requests.CheckUserIdDto;
import labyrinth.contracts.communication.dtos.responses.SimpleSuccessResponseDto;
import labyrinth.registrationServer.RegistrationServerConnectionMock;
import labyrinth.registrationServer.RegistrationServerStateManager;
import labyrinth.registrationServer.logic.CheckUserIdLogic;

/**
 * TestClass for @see CheckUserIdLogic
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class CheckUserIdLogicTests extends RegistrationServerLogicTestBase 
{
	/**
	 * Tests that the result is successful if a client with the user id is registered
	 */
	@Test
	public void checkUserIdSuccessTest()
	{
		RegistrationServerConnectionMock mock = new RegistrationServerConnectionMock();
		CheckUserIdLogic logic = new CheckUserIdLogic(mock);
		RegistrationServerStateManager.addUserId(1);
		logic.execute(new CheckUserIdDto(1));
		
		assertEquals(SimpleSuccessResponseDto.class, mock.getLastResponseSent().getClass());
		SimpleSuccessResponseDto response = SimpleSuccessResponseDto.class.cast(mock.getLastResponseSent());
		assertEquals(MessageType.CheckUID, response.getMessageType());
		assertTrue(response.getSuccess());
	}
	
	/**
	 * Tests that the result is not successful if a client with the user id is not registered
	 */
	@Test
	public void checkUserIdNoSuccessTest()
	{
		RegistrationServerConnectionMock mock = new RegistrationServerConnectionMock();
		CheckUserIdLogic logic = new CheckUserIdLogic(mock);
		RegistrationServerStateManager.addUserId(1);
		logic.execute(new CheckUserIdDto(2));
		
		assertEquals(SimpleSuccessResponseDto.class, mock.getLastResponseSent().getClass());
		SimpleSuccessResponseDto response = SimpleSuccessResponseDto.class.cast(mock.getLastResponseSent());
		assertEquals(MessageType.CheckUID, response.getMessageType());
		assertFalse(response.getSuccess());
	}
}
