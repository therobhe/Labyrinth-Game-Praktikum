package labyrinth.contracts.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import labyrinth.contracts.entities.game.Achievement;

/**
 * Unit test for @see Achievement
 * @author Lukas Reinhardt
 * @version 1.0
 */
public class AchievementTests
{
	/**
	 * Tests @see Achievement.addProgress
	 */
	@Test
	public void addProgressTest()
	{
		Achievement achievement = new Achievement("test", "test", 10);
		achievement.addProgress(5);
		assertEquals(5, achievement.getProgress());
		achievement.addProgress(10);
		assertEquals(10, achievement.getProgress());
	}
}
