package labyrinth.gameServer.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.requests.*;
import labyrinth.contracts.communication.dtos.responses.ResponseDtoBase;
import labyrinth.contracts.communication.dtos.responses.SimpleSuccessResponseDto;
import labyrinth.contracts.entities.lobby.*;
import labyrinth.gameServer.GameServerConnectionMock;
import labyrinth.gameServer.ObjectAccessMonitor;
import labyrinth.gameServer.stateManagers.LobbyStateManager;

/**
 * TestClass for @see ChangeRoleLogic
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ChangeRoleLogicTests extends LobbyLogicTestBase
{
	/**
	 * Tests that the role of the user is changed successfully to Spectator
	 */
	@Test
	public void changeRoleLogicSpectatorSuccessTest() { changeRoleLogicSuccessTestCore(LobbyUserKind.SPECTATOR); }
	
	/**
	 * Tests that the role of the user is changed successfully to Player
	 */
	@Test
	public void changeRoleLogicPlayerSuccessTest() { changeRoleLogicSuccessTestCore(LobbyUserKind.PLAYER); }
	
	/**
	 * Tests that the role of the user is not changed successfully to Player if the maximum count of players is reached
	 */
	@Test
	public void changeRoleLogicPlayerFailedWithPlayerlimitTest() 
	{ 		
		GameServerConnectionMock mock = new GameServerConnectionMock();
		ObjectAccessMonitor<Lobby> monitor = LobbyStateManager.getLobbyMonitor();
		LobbyUser user = new LobbyUser("Testuser", null, UserPermission.DEFAULT, LobbyUserKind.SPECTATOR);
		monitor.executeAction(lobby -> lobby.getPlayerList().clear());
		monitor.executeAction(lobby -> lobby.getSpectatorList().clear());
		monitor.executeAction(lobby -> 
		{
			lobby.getPlayerList().add(new LobbyUser("Player1", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER));
			lobby.getPlayerList().add(new LobbyUser("Player2", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER));
			lobby.getPlayerList().add(new LobbyUser("Player3", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER));
			lobby.getPlayerList().add(new LobbyUser("Player4", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER));
			lobby.getSpectatorList().add(user);
		});
		mock.setUser(user);
		ChangeRoleLogic logic = new ChangeRoleLogic(mock);
		logic.execute(new ChangeRoleDto(LobbyUserKind.PLAYER));
	
		assertEquals(LobbyUserKind.SPECTATOR, user.getRole());
		assertEquals(4, (int)monitor.executeFunction(lobby -> lobby.getPlayerList().size()));
		assertEquals(1, (int)monitor.executeFunction(lobby -> lobby.getSpectatorList().size()));
		assertFalse(monitor.executeFunction(lobby -> lobby.getPlayerList().contains(user)));
		assertTrue(monitor.executeFunction(lobby -> lobby.getSpectatorList().contains(user)));
	
		testResponseCore(true, mock.getLastResponseSent());		
	}
	
	private void changeRoleLogicSuccessTestCore(LobbyUserKind role)
	{
		int expectedPlayerCount = role == LobbyUserKind.PLAYER ? 1 : 0;
		int expectedSpectatorCount = role == LobbyUserKind.PLAYER ? 0 : 1;
		LobbyUserKind startRole = role == LobbyUserKind.PLAYER ? LobbyUserKind.SPECTATOR : LobbyUserKind.PLAYER;		
		LobbyUser user = new LobbyUser("Testuser", null, UserPermission.DEFAULT, startRole);
		Consumer<Lobby> addUserAction = role == LobbyUserKind.PLAYER ? lobby -> lobby.getSpectatorList().add(user)
			: lobby -> lobby.getPlayerList().add(user);	
		
		GameServerConnectionMock mock = new GameServerConnectionMock();
		ObjectAccessMonitor<Lobby> monitor = LobbyStateManager.getLobbyMonitor();
		monitor.executeAction(addUserAction);
		mock.setUser(user);
		ChangeRoleLogic logic = new ChangeRoleLogic(mock);
		logic.execute(new ChangeRoleDto(role));
	
		assertEquals(role, user.getRole());
		assertEquals(expectedPlayerCount, (int)monitor.executeFunction(lobby -> lobby.getPlayerList().size()));
		assertEquals(expectedSpectatorCount, (int)monitor.executeFunction(lobby -> lobby.getSpectatorList().size()));
		
		if(role == LobbyUserKind.PLAYER)
		{
			assertTrue(monitor.executeFunction(lobby -> lobby.getPlayerList().contains(user)));
			assertFalse(monitor.executeFunction(lobby -> lobby.getSpectatorList().contains(user)));
		}
		else
		{
			assertFalse(monitor.executeFunction(lobby -> lobby.getPlayerList().contains(user)));
			assertTrue(monitor.executeFunction(lobby -> lobby.getSpectatorList().contains(user)));
		}
		
		testResponseCore(true, mock.getLastResponseSent());
		checkUpdateLobbyBroadcastResponse();
	}
	
	private void testResponseCore(boolean expected, ResponseDtoBase responseBase)
	{
		assertEquals(SimpleSuccessResponseDto.class, responseBase.getClass());
		SimpleSuccessResponseDto response = SimpleSuccessResponseDto.class.cast(responseBase);
		assertEquals(MessageType.ChangeRole, response.getMessageType());
		assertEquals(expected, response.getSuccess());	
	}
}
