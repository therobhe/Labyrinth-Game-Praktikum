package labyrinth.gameServer.logic;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import labyrinth.contracts.communication.SocketClientMock;
import labyrinth.contracts.communication.SocketServerMock;
import labyrinth.contracts.entities.lobby.GameServerEntry;
import labyrinth.gameServer.GameServerConnectionMock;
import labyrinth.gameServer.GameServerManager;
import labyrinth.gameServer.stateManagers.GameServerEntryStateManager;
import labyrinth.gameServer.stateManagers.LobbyStateManager;

/**
 * Base-class for a Logic-tests of the GameServer
 */
public abstract class GameServerLogicTestBase 
{
	/**
	 * Sets up the tests
	 */
	@BeforeAll
	public static void setUp() 
	{
		SocketServerMock<GameServerConnectionMock> mockServer = new SocketServerMock<GameServerConnectionMock>();
		GameServerManager.initialize(new SocketClientMock(), mockServer);
		GameServerManager.startServer("test");
		GameServerEntryStateManager.initialize(new GameServerEntry("Testserver"));
	}
	
	/**
	 * Cleans up the tests
	 */
	@AfterEach
	public void cleanup() 
	{
		LobbyStateManager.getLobbyMonitor().executeAction(lobby -> 
		{
			lobby.getPlayerList().clear();
			lobby.getSpectatorList().clear();
		});
		GameServerEntryStateManager.getServerMonitor().executeAction(server -> server.getMembers().clear());
		SocketClientMock clientMock 
			= SocketClientMock.class.cast(GameServerManager.getRunningServer().getRegistrationServerClient());
		clientMock.cleanup();
	}
}
