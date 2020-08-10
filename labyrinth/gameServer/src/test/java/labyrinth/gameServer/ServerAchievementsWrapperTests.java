package labyrinth.gameServer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;
import labyrinth.contracts.communication.SocketClientMock;
import labyrinth.contracts.communication.SocketServerMock;
import labyrinth.contracts.communication.dtos.responses.PropagateAchievementResponseDto;
import labyrinth.contracts.entities.game.Achievement;
import labyrinth.contracts.entities.lobby.GameServerEntry;
import labyrinth.gameServer.stateManagers.GameServerEntryStateManager;

/**
 * TestClass for @see ServerAchievementsWrapper
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ServerAchievementsWrapperTests 
{
	/**
	 * Tests @see ServerAchievementsWrapper.addMovedTiles
	 */
	@Test
	public void doesAddMovedTilesTest() 
		throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException 
	{
		ServerAchievementsWrapper wrapper = new ServerAchievementsWrapper("player1");
		wrapper.addMovedTiles(10);
		
		testAchievementProgressCore(wrapper, "movedTilesAchievement1", 10);
		testAchievementProgressCore(wrapper, "movedTilesAchievement2", 10);
		testAchievementProgressCore(wrapper, "movedTilesAchievement3", 10);
		testAchievementProgressCore(wrapper, "movedTilesAchievement4", 10);
		testAchievementProgressCore(wrapper, "movedTilesAchievement5", 10);
	}
	
	/**
	 * Tests @see ServerAchievementsWrapper.addUsedBonus
	 */
	@Test
	public void doesAddUsedBonusTest() 
		throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException 
	{
		ServerAchievementsWrapper wrapper = new ServerAchievementsWrapper("player1");
		wrapper.addUsedBonus();
		
		testAchievementProgressCore(wrapper, "usedBoniAchievement1", 1);
		testAchievementProgressCore(wrapper, "usedBoniAchievement2", 1);
		testAchievementProgressCore(wrapper, "usedBoniAchievement3", 1);
		testAchievementProgressCore(wrapper, "usedBoniAchievement4", 1);
		testAchievementProgressCore(wrapper, "usedBoniAchievement5", 1);
	}
	
	/**
	 * Tests that a PropagateAchievement-Response is broadcasted if a player achieved an achievement
	 */
	@Test
	public void doesPropagateAchievementIfRequirementsFullfilledTest() 
		throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException 
	{
		SocketServerMock<GameServerConnectionMock> mockServer = new SocketServerMock<GameServerConnectionMock>();
		GameServerManager.initialize(new SocketClientMock(), mockServer);
		GameServerManager.startServer("test");
		GameServerEntryStateManager.initialize(new GameServerEntry("Testserver"));
		ServerAchievementsWrapper wrapper = new ServerAchievementsWrapper("player1");
		Field field = wrapper.getClass().getDeclaredField("movedTilesAchievement1");
		field.setAccessible(true);
		Achievement achievement = Achievement.class.cast(field.get(wrapper));
		wrapper.addMovedTiles(25);
		
		assertEquals(PropagateAchievementResponseDto.class, mockServer.getBroadcastedResponse().getClass());
		PropagateAchievementResponseDto response 
			= PropagateAchievementResponseDto.class.cast(mockServer.getBroadcastedResponse());
		assertEquals("player1", response.getPlayer());
		assertEquals(achievement.getName(), response.getAchievement());
		assertEquals(achievement.getDescription(), response.getDescription());
	}
	
	private static void testAchievementProgressCore(ServerAchievementsWrapper wrapper, String achievementName, 
		int expectedProgress) 
		throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		Field field = wrapper.getClass().getDeclaredField(achievementName);
		field.setAccessible(true);
		Achievement achievement = Achievement.class.cast(field.get(wrapper));
		assertEquals(expectedProgress, achievement.getProgress());
	}
}
