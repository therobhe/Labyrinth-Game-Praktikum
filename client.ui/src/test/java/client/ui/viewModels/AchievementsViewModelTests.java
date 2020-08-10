package client.ui.viewModels;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import labyrinth.client.ui.ClientAchievementManager;
import labyrinth.client.ui.viewModels.AchievementsViewModel;
import labyrinth.contracts.entities.game.Achievement;
import labyrinth.contracts.utilities.FileAccessorMock;

/**
 * TestClass for @see AchievementsViewModel
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class AchievementsViewModelTests
{
	/**
	 * Tests that the bindable models for the achievements are initialized correctly
	 */
	@Test
	public void doesInitializeAchievementModelsCorrectlyTest()
	{
		createAchievementsFile(new FileAccessorMock());
		AchievementsViewModel viewModel = new AchievementsViewModel();
		
		assertEquals(1, viewModel.getPlayedGamesAchievements().size());
		assertEquals("Gespielte Spiele I", viewModel.getPlayedGamesAchievements().get(0).nameProperty().get());
		assertEquals("test1", viewModel.getPlayedGamesAchievements().get(0).descriptionProperty().get());
		assertEquals("0/10", viewModel.getPlayedGamesAchievements().get(0).progressTextProperty().get());
		assertEquals(0.0, viewModel.getPlayedGamesAchievements().get(0).progressProperty().get());
		
		assertEquals(1, viewModel.getWonGamesAchievements().size());
		assertEquals("Gewonnene Spiele I", viewModel.getWonGamesAchievements().get(0).nameProperty().get());
		assertEquals("test2", viewModel.getWonGamesAchievements().get(0).descriptionProperty().get());
		assertEquals("0/11", viewModel.getWonGamesAchievements().get(0).progressTextProperty().get());
		assertEquals(0.0, viewModel.getWonGamesAchievements().get(0).progressProperty().get());
		
		assertEquals(1, viewModel.getAquiredScoreAchievements().size());
		assertEquals("Gesammelte Punkte I", viewModel.getAquiredScoreAchievements().get(0).nameProperty().get());
		assertEquals("test3", viewModel.getAquiredScoreAchievements().get(0).descriptionProperty().get());
		assertEquals("0/12", viewModel.getAquiredScoreAchievements().get(0).progressTextProperty().get());
		assertEquals(0.0, viewModel.getAquiredScoreAchievements().get(0).progressProperty().get());
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
}
