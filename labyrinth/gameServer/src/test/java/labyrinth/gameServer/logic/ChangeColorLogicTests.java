package labyrinth.gameServer.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import labyrinth.contracts.communication.*;
import labyrinth.contracts.communication.dtos.requests.ChangeColorDto;
import labyrinth.contracts.communication.dtos.responses.SimpleSuccessResponseDto;
import labyrinth.contracts.entities.lobby.*;
import labyrinth.gameServer.GameServerConnectionMock;

/**
 * TestClass for @see ChangeColorLogic
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ChangeColorLogicTests extends LobbyLogicTestBase
{
	/**
	 * Tests that the color of the user is changed correctly
	 */
	@Test
	public void changeColorLogicTest()
	{	
		LobbyUser user = new LobbyUser("Testuser", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER);
		GameServerConnectionMock mock = new GameServerConnectionMock();
		mock.setUser(user);
		ChangeColorLogic logic = new ChangeColorLogic(mock);
		logic.execute(new ChangeColorDto("Green"));
		
		assertEquals("Green", user.getColor());
		assertEquals(SimpleSuccessResponseDto.class, mock.getLastResponseSent().getClass());
		SimpleSuccessResponseDto response = SimpleSuccessResponseDto.class.cast(mock.getLastResponseSent());
		assertEquals(MessageType.ChangeColor, response.getMessageType());
		assertTrue(response.getSuccess());
		
		checkUpdateLobbyBroadcastResponse();
	}
}
