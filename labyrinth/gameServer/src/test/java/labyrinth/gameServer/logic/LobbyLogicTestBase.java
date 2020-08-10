package labyrinth.gameServer.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import labyrinth.contracts.communication.SocketServerMock;
import labyrinth.contracts.communication.dtos.responses.ResponseDtoBase;
import labyrinth.contracts.communication.dtos.responses.UpdateLobbyResponseDto;
import labyrinth.contracts.entities.lobby.LobbyUser;
import labyrinth.gameServer.GameServerConnection;
import labyrinth.gameServer.GameServerManager;
import labyrinth.gameServer.stateManagers.LobbyStateManager;

/**
 * BaseClass for all LogicTests for the Lobby processes
 * @author Lukas Reinhardt
 * @version 1.0
 */
public abstract class LobbyLogicTestBase extends GameServerLogicTestBase
{		
	/**
	 * Tests that the broadcasted LobbyUpdateResponse is correct
	 */
	@SuppressWarnings("unchecked")
	protected void checkUpdateLobbyBroadcastResponse()
	{	
		SocketServerMock<GameServerConnection> mockServer 
			= SocketServerMock.class.cast(GameServerManager.getRunningServer().getServer());
		checkUpdateLobbyResponse(mockServer.getBroadcastedResponse());
	}
	
	/**
	 * Tests that the broadcasted LobbyUpdateResponse is correct
	 * @param response The response sent
	 */
	protected void checkUpdateLobbyResponse(ResponseDtoBase response)
	{	
		assertEquals(UpdateLobbyResponseDto.class, response.getClass());
		UpdateLobbyResponseDto expected = LobbyStateManager.createUpdateLobbyResponseDto();
		UpdateLobbyResponseDto actual = UpdateLobbyResponseDto.class.cast(response);
		
		assertEquals(expected.getMembers().size(), actual.getMembers().size());
		for(LobbyUser user : expected.getMembers())
			actual.getMembers().contains(user);
	}
}
