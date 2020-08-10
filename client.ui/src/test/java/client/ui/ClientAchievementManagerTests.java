package client.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.google.gson.Gson;
import labyrinth.client.ui.ClientAchievementManager;
import labyrinth.contracts.entities.game.Achievement;
import labyrinth.contracts.utilities.FileAccessorMock;

/**
 * TestClass for @see ClientAchievementManager
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ClientAchievementManagerTests 
{
	/**
	 * Sets up the tests
	 */
	@BeforeEach
	public void steUp()
	{
		ClientAchievementManager.getAquiredScoreAchievements().clear();
		ClientAchievementManager.getPlayedGamesAchievements().clear();
		ClientAchievementManager.getWonGamesAchievements().clear();
	}
	
	/**
	 * Tests that the achievements are created if no saved achievements exists
	 */
	@Test
	public void doesCreateNewAchievementsIfFileNotExistsTest()
	{
		ClientAchievementManager.initialize(new FileAccessorMock());
		assertEquals(5, ClientAchievementManager.getAquiredScoreAchievements().size());
		assertEquals(5, ClientAchievementManager.getPlayedGamesAchievements().size());
		assertEquals(5, ClientAchievementManager.getWonGamesAchievements().size());
	}
	
	/**
	 * Tests that achievements are loaded if saved achievements exists
	 */
	@Test
	public void doesLoadAchievementsFromFileTest()
	{
		FileAccessorMock mock = new FileAccessorMock();
		createAchievementsFile(mock);
		
		assertEquals(1, ClientAchievementManager.getPlayedGamesAchievements().size());
		assertEquals("Gespielte Spiele I", ClientAchievementManager.getPlayedGamesAchievements().get(0).getName());
		assertEquals("test1", ClientAchievementManager.getPlayedGamesAchievements().get(0).getDescription());
		assertEquals(10, ClientAchievementManager.getPlayedGamesAchievements().get(0).getRequired());
		
		assertEquals(1, ClientAchievementManager.getWonGamesAchievements().size());
		assertEquals("Gewonnene Spiele I", ClientAchievementManager.getWonGamesAchievements().get(0).getName());
		assertEquals("test2", ClientAchievementManager.getWonGamesAchievements().get(0).getDescription());
		assertEquals(11, ClientAchievementManager.getWonGamesAchievements().get(0).getRequired());
		
		assertEquals(1, ClientAchievementManager.getAquiredScoreAchievements().size());
		assertEquals("Gesammelte Punkte I", ClientAchievementManager.getAquiredScoreAchievements().get(0).getName());
		assertEquals("test3", ClientAchievementManager.getAquiredScoreAchievements().get(0).getDescription());
		assertEquals(12, ClientAchievementManager.getAquiredScoreAchievements().get(0).getRequired());
	}
	
	/**
	 * Tests @see ClientAchievementManager.addPlayedGame
	 */
	@Test
	public void addPlayedGameTest()
	{
		FileAccessorMock mock = new FileAccessorMock();
		createAchievementsFile(mock);
		ClientAchievementManager.addPlayedGame();
		
		assertEquals(1, ClientAchievementManager.getPlayedGamesAchievements().get(0).getProgress());
		assertEquals(0, ClientAchievementManager.getWonGamesAchievements().get(0).getProgress());
		assertEquals(0, ClientAchievementManager.getAquiredScoreAchievements().get(0).getProgress());
		testSavedAchievements(mock);
	}
	
	/**
	 * Tests @see ClientAchievementManager.addWonGame
	 */
	@Test
	public void addWonGameTest()
	{
		FileAccessorMock mock = new FileAccessorMock();
		createAchievementsFile(mock);
		ClientAchievementManager.addWonGame();
		
		assertEquals(1, ClientAchievementManager.getPlayedGamesAchievements().get(0).getProgress());
		assertEquals(1, ClientAchievementManager.getWonGamesAchievements().get(0).getProgress());
		assertEquals(0, ClientAchievementManager.getAquiredScoreAchievements().get(0).getProgress());
		testSavedAchievements(mock);
	}
	
	/**
	 * Tests @see ClientAchievementManager.addAquiredScore
	 */
	@Test
	public void addAquiredScoreGameTest()
	{
		FileAccessorMock mock = new FileAccessorMock();
		createAchievementsFile(mock);
		ClientAchievementManager.addAquiredScore(9);
		
		assertEquals(0, ClientAchievementManager.getPlayedGamesAchievements().get(0).getProgress());
		assertEquals(0, ClientAchievementManager.getWonGamesAchievements().get(0).getProgress());
		assertEquals(9, ClientAchievementManager.getAquiredScoreAchievements().get(0).getProgress());
		testSavedAchievements(mock);
	}
	
	private static void createAchievementsFile(FileAccessorMock mock)
	{
		List<Achievement> achievements = new ArrayList<Achievement>();
		achievements.add(new Achievement("Gespielte Spiele I", "test1", 10));
		achievements.add(new Achievement("Gewonnene Spiele I", "test2", 11));
		achievements.add(new Achievement("Gesammelte Punkte I", "test3", 12));
		mock.addTextToRead("achievements.json", new Gson().toJson(achievements));
		ClientAchievementManager.initialize(mock);
	}
	
	private static void testSavedAchievements(FileAccessorMock mock)
	{
		List<Achievement> achievements = new ArrayList<Achievement>();
		achievements.addAll(ClientAchievementManager.getPlayedGamesAchievements());
		achievements.addAll(ClientAchievementManager.getWonGamesAchievements());
		achievements.addAll(ClientAchievementManager.getAquiredScoreAchievements());
		String expected = new Gson().toJson(achievements);
		assertEquals(expected, mock.getWrittenTexts().get("achievements.json"));
	}
}
