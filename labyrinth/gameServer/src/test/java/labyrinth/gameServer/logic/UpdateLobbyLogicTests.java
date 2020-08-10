package labyrinth.gameServer.logic;

import org.junit.jupiter.api.Test;
import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.requests.EmptyRequestDto;
import labyrinth.gameServer.GameServerConnectionMock;

/**
 * TestClass for @see UpdateLobbyLogic
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class UpdateLobbyLogicTests extends LobbyLogicTestBase
{
	/**
	 * Tests that the correct UpdateLobby-Response is sent back
	 */
	@Test
	public void updateLobbyLogicTest()
	{
		GameServerConnectionMock mock = new GameServerConnectionMock();
		UpdateLobbyLogic logic = new UpdateLobbyLogic(mock);
		logic.execute(new EmptyRequestDto(MessageType.UpdateLobby));		
		checkUpdateLobbyResponse(mock.getLastResponseSent());
	}
}
