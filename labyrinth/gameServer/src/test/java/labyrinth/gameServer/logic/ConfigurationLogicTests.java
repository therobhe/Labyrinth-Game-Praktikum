package labyrinth.gameServer.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import labyrinth.contracts.communication.*;
import labyrinth.contracts.communication.dtos.requests.*;
import labyrinth.contracts.communication.dtos.responses.ConfigurationResponseDto;
import labyrinth.contracts.entities.Coordinates;
import labyrinth.contracts.entities.lobby.*;
import labyrinth.gameServer.*;
import labyrinth.gameServer.stateManagers.*;

/**
 * TestClass for @see ConfigurationLogic
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ConfigurationLogicTests extends LobbyLogicTestBase
{
	/**
	 * Tests updating the server name
	 */
	@Test
	public void updateServerNameTest()
	{
		LobbyUser user = new LobbyUser("Testuser", null, UserPermission.ADMIN, LobbyUserKind.PLAYER);
		GameServerConnectionMock mock = new GameServerConnectionMock();
		mock.setUser(user);
		ConfigurationLogic logic = new ConfigurationLogic(mock);
		logic.execute(new ConfigurationDto(new GameConfiguration("Testserver11", new Coordinates(0, 0), 0, null, 0, 0, 0, null)));
		
		assertEquals("Testserver11", 
			GameServerEntryStateManager.getServerMonitor().executeFunction(server -> server.getServerName()));
		SocketClientMock mockClient 
			= SocketClientMock.class.cast(GameServerManager.getRunningServer().getRegistrationServerClient());
		assertEquals(GameServerStatusUpdateDto.class, mockClient.getLastRequest().getClass());
		GameServerStatusUpdateDto request = GameServerStatusUpdateDto.class.cast(mockClient.getLastRequest());
		assertEquals("Testserver11", request.getServerName());
	}
	
	/**
	 * Tests that it's not possible to update the server name without being the admin
	 */
	@Test
	public void cannotUpdateServerNameWithoutBeingAdminTest()
	{
		LobbyUser user = new LobbyUser("Testuser", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER);
		GameServerConnectionMock mock = new GameServerConnectionMock();
		GameServerEntryStateManager.getServerMonitor().executeAction(server -> server.setServerName("Test"));
		mock.setUser(user);
		ConfigurationLogic logic = new ConfigurationLogic(mock);
		logic.execute(new ConfigurationDto(new GameConfiguration("Testserver11", new Coordinates(0, 0), 0, null, 0, 0, 0, null)));
		
		assertEquals("Test", 
			GameServerEntryStateManager.getServerMonitor().executeFunction(server -> server.getServerName()));
		assertNull(mock.getLastResponseSent());
	}
	
	/**
	 * Tests changing the admin
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void updateAdminTest()
	{
		LobbyUser user1 = new LobbyUser("Testuser", null, UserPermission.ADMIN, LobbyUserKind.PLAYER);
		LobbyUser user2 = new LobbyUser("Testuser1", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER);
		GameServerConnectionMock mock = new GameServerConnectionMock();
		LobbyStateManager.getLobbyMonitor().executeAction(lobby ->
		{
			lobby.getPlayerList().add(user1);
			lobby.getPlayerList().add(user2);
		});
		mock.setUser(user1);
		ConfigurationLogic logic = new ConfigurationLogic(mock);
		GameConfiguration configuration = new GameConfiguration();
		configuration.setAdmin("Testuser1");
		logic.execute(new ConfigurationDto(configuration));
		
		assertEquals(UserPermission.DEFAULT, user1.getPermission());
		assertEquals(UserPermission.ADMIN, user2.getPermission());
		SocketServerMock<GameServerConnection> mockServer 
			= SocketServerMock.class.cast(GameServerManager.getRunningServer().getServer());
		assertEquals(ConfigurationResponseDto.class, mockServer.getBroadcastedResponse().getClass());
	}
	
	/**
	 * Tests that it's not possible to change the admin without being the admin
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void cannotAdminWithoutBeingAdminTest()
	{
		LobbyUser user1 = new LobbyUser("Testuser", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER);
		LobbyUser user2 = new LobbyUser("Testuser1", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER);
		GameServerConnectionMock mock = new GameServerConnectionMock();
		LobbyStateManager.getLobbyMonitor().executeAction(lobby ->
		{
			lobby.getPlayerList().add(user1);
			lobby.getPlayerList().add(user2);
		});
		mock.setUser(user1);
		ConfigurationLogic logic = new ConfigurationLogic(mock);
		logic.execute(new ConfigurationDto(new GameConfiguration(null, new Coordinates(0, 0), 0, null, 0, 0, 0, "Testuser1")));
		
		assertEquals(UserPermission.DEFAULT, user1.getPermission());
		assertEquals(UserPermission.DEFAULT, user2.getPermission());
		SocketServerMock<GameServerConnection> mockServer 
			= SocketServerMock.class.cast(GameServerManager.getRunningServer().getServer());
		assertNull(mockServer.getBroadcastedResponse());
	}
	
	/**
	 * Tests the updating of the GameConfiguration
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void updateConfigurationTest()
	{
		SocketServerMock<GameServerConnection> mockServer 
			= SocketServerMock.class.cast(GameServerManager.getRunningServer().getServer());
		GameConfigurationStateManager.initialize();
		LobbyUser user = new LobbyUser("Testuser", null, UserPermission.ADMIN, LobbyUserKind.PLAYER);
		GameServerConnectionMock mock = new GameServerConnectionMock();
		mock.setUser(user);
		ConfigurationLogic logic = new ConfigurationLogic(mock);
		ConfigurationDto newConfiguration = createConfigurationRequest();
		logic.execute(newConfiguration);
		
		GameConfigurationStateManager.getConfigurationMonitor().executeAction(configuration -> 
		{
			assertEquals(newConfiguration.getTreasureCount(), configuration.getTreasureCount());
			assertEquals(newConfiguration.getBoniProbability(), configuration.getBoniProbability());
			assertEquals(newConfiguration.getGameLengthLimit(), configuration.getGameLengthLimit());
			assertEquals(newConfiguration.getSize().getX(), configuration.getSize().getX());
			assertEquals(newConfiguration.getSize().getY(), configuration.getSize().getY());
			assertEquals(newConfiguration.getTurnLengthLimit(), configuration.getTurnLengthLimit());
			assertFalse(configuration.getBoni().getIsSwapEnabled());
			assertFalse(configuration.getBoni().getIsBeamEnabled());
			assertFalse(configuration.getBoni().getIsShiftSolidEnabled());
			assertFalse(configuration.getBoni().getIsShiftTwiceEnabled());
		});
		assertEquals(ConfigurationResponseDto.class, mockServer.getBroadcastedResponse().getClass());
		ConfigurationResponseDto response = ConfigurationResponseDto.class.cast(mockServer.getBroadcastedResponse());
		assertSame(GameConfigurationStateManager.getConfigurationMonitor().executeFunction(configuration -> configuration),
			response.getConfiguration());
	}
	
	/**
	 * Tests that it's not possible to update the GameConfiguration without being the admin
	 */
	@Test
	public void cannotUpdateConfigurationWithoutBeingAdminTest()
	{
		GameConfigurationStateManager.initialize();
		LobbyUser user = new LobbyUser("Testuser", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER);
		GameServerConnectionMock mock = new GameServerConnectionMock();
		mock.setUser(user);
		ConfigurationLogic logic = new ConfigurationLogic(mock);
		ConfigurationDto newConfiguration = createConfigurationRequest();
		logic.execute(newConfiguration);
		
		GameConfigurationStateManager.getConfigurationMonitor().executeAction(configuration -> 
		{
			assertFalse(newConfiguration.getTreasureCount() == configuration.getTreasureCount());
			assertFalse(newConfiguration.getBoniProbability() == configuration.getBoniProbability());
			assertFalse(newConfiguration.getGameLengthLimit() == configuration.getGameLengthLimit());
			assertFalse(newConfiguration.getSize().getX() == configuration.getSize().getX());
			assertFalse(newConfiguration.getSize().getY() == configuration.getSize().getY());
			assertFalse(newConfiguration.getTurnLengthLimit() == configuration.getTurnLengthLimit());
			assertTrue(configuration.getBoni().getIsSwapEnabled());
			assertTrue(configuration.getBoni().getIsBeamEnabled());
			assertTrue(configuration.getBoni().getIsShiftSolidEnabled());
			assertTrue(configuration.getBoni().getIsShiftTwiceEnabled());
		});
	}
	
	private ConfigurationDto createConfigurationRequest()
	{
		BoniConfiguration boni = new BoniConfiguration();
		boni.setIsBeamEnabled(false);
		boni.setIsShiftSolidEnabled(false);
		boni.setIsShiftTwiceEnabled(false);
		boni.setIsSwapEnabled(false);
		return new ConfigurationDto(new GameConfiguration(null, new Coordinates(5, 5), 12, boni, 12, 12, 12, null));
	}
}
