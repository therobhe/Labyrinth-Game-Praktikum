package labyrinth.contracts.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import labyrinth.contracts.entities.game.Player;

/**
 * TestClass for @see Player
 * @author William Bechler
 * @version 1.0
 */
public final class PlayerTests
{
	/**
	 * Test for setting the last Mission Card added to MissionCards of a certain Player as the "active" one
	 */
	@Test
	public void getActiveMissionCardTest()
	{
		Player player = new Player("Player", "test");
		player.getTargetTreasures().add(3);
		player.getTargetTreasures().add(2);
		assertEquals(2, (int)player.getNextTreasure());
	}
}
