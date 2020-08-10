package labyrinth.gameServer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

import labyrinth.contracts.entities.game.Achievement;

/**
 * TestClass for @see GameServerAchievementManager
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class GameServerAchievementManagerTests 
{
	/**
	 * Tests that a ServerAchievementWrapper is created for each player
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void initializeTest() 
		throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	{
		List<String> players = new ArrayList<String>();
		players.add("player1");
		players.add("player2");
		GameServerAchievementManager.initialize(players);
		
		Field field = GameServerAchievementManager.class.getDeclaredField("playerAchievements");
		field.setAccessible(true);
		Map<String, ServerAchievementsWrapper> playerAchievements = Map.class.cast(field.get(null));
		assertNotNull(playerAchievements.get("player1"));
		assertNotNull(playerAchievements.get("player2"));
	}
	
	/**
	 * Tests GameServerAchievementManager.addMovedTiles
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void addMovedTilesTest() 
		throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		List<String> players = new ArrayList<String>();
		players.add("player1");
		players.add("player2");
		GameServerAchievementManager.initialize(players);
		GameServerAchievementManager.addMovedTiles("player1", 10);	
		Field field = GameServerAchievementManager.class.getDeclaredField("playerAchievements");
		field.setAccessible(true);
		Map<String, ServerAchievementsWrapper> playerAchievements = Map.class.cast(field.get(null));
		
		ServerAchievementsWrapper wrapper1 = playerAchievements.get("player1");
		testAchievementProgressCore(wrapper1, "movedTilesAchievement1", 10);
		testAchievementProgressCore(wrapper1, "movedTilesAchievement2", 10);
		testAchievementProgressCore(wrapper1, "movedTilesAchievement3", 10);
		testAchievementProgressCore(wrapper1, "movedTilesAchievement4", 10);
		testAchievementProgressCore(wrapper1, "movedTilesAchievement5", 10);
		
		ServerAchievementsWrapper wrapper2 = playerAchievements.get("player2");
		testAchievementProgressCore(wrapper2, "movedTilesAchievement1", 0);
		testAchievementProgressCore(wrapper2, "movedTilesAchievement2", 0);
		testAchievementProgressCore(wrapper2, "movedTilesAchievement3", 0);
		testAchievementProgressCore(wrapper2, "movedTilesAchievement4", 0);
		testAchievementProgressCore(wrapper2, "movedTilesAchievement5", 0);		
	}
	
	/**
	 * Tests GameServerAchievementManager.addUsedBonus
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void addUsedBonusTest() 
		throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		List<String> players = new ArrayList<String>();
		players.add("player1");
		players.add("player2");
		GameServerAchievementManager.initialize(players);
		GameServerAchievementManager.addUsedBonus("player1");	
		Field field = GameServerAchievementManager.class.getDeclaredField("playerAchievements");
		field.setAccessible(true);
		Map<String, ServerAchievementsWrapper> playerAchievements = Map.class.cast(field.get(null));
		
		ServerAchievementsWrapper wrapper1 = playerAchievements.get("player1");
		testAchievementProgressCore(wrapper1, "usedBoniAchievement1", 1);
		testAchievementProgressCore(wrapper1, "usedBoniAchievement2", 1);
		testAchievementProgressCore(wrapper1, "usedBoniAchievement3", 1);
		testAchievementProgressCore(wrapper1, "usedBoniAchievement4", 1);
		testAchievementProgressCore(wrapper1, "usedBoniAchievement5", 1);
		
		ServerAchievementsWrapper wrapper2 = playerAchievements.get("player2");
		testAchievementProgressCore(wrapper2, "usedBoniAchievement1", 0);
		testAchievementProgressCore(wrapper2, "usedBoniAchievement2", 0);
		testAchievementProgressCore(wrapper2, "usedBoniAchievement3", 0);
		testAchievementProgressCore(wrapper2, "usedBoniAchievement4", 0);
		testAchievementProgressCore(wrapper2, "usedBoniAchievement5", 0);	
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
