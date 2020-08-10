package client.ui.viewModels;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.google.gson.Gson;
import labyrinth.client.ui.ClientAchievementManager;
import labyrinth.client.ui.viewModels.GameEndViewModel;
import labyrinth.contracts.communication.dtos.PlayerDtoBase;
import labyrinth.contracts.communication.dtos.responses.EndGameResponseDto;
import labyrinth.contracts.entities.game.Achievement;
import labyrinth.contracts.utilities.FileAccessorMock;

/**
 * TestClass for @see GameEndViewModel
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class GameEndViewModelTests 
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
	 * Tests that the correct exception is thrown when navigating to the ViewModel without the GameState-Parameter
	 */
	@Test
	public void throwsCorrectExceptionWhenNavigatingWithoutEndGameParameterTest()
	{
		GameEndViewModel viewModel = new GameEndViewModel();
		assertThrows(IllegalArgumentException.class, () -> viewModel.handleNavigation(null), 
			"Expected exception of type IllegalArgumentException. has not been thrown.");
		Map<String, Object> parameters = new HashMap<String, Object>();
		assertThrows(IllegalArgumentException.class, () -> viewModel.handleNavigation(parameters), 
			"Expected exception of type IllegalArgumentException. has not been thrown.");
	}
	
	/**
	 * Tests that the bindable properties for the players are initialized correctly from the EndGame-Response
	 */
	@Test
	public void doesInitializePlayersCorrectlyFromEndGameResponseTest()
	{
		GameEndViewModel viewModel = new GameEndViewModel();
		Map<String, Object> parameters = new HashMap<String, Object>();
		EndGameResponseDto response = new EndGameResponseDto();
		PlayerDtoBase player1 = new PlayerDtoBase("player1", 15);
		PlayerDtoBase player2 = new PlayerDtoBase("player2", 25);
		PlayerDtoBase player3 = new PlayerDtoBase("player3", 10);
		response.getPlayers().add(player1);
		response.getPlayers().add(player2);
		response.getPlayers().add(player3);
		parameters.put(GameEndViewModel.END_GAME_RESPONSE_PARAMETER, response);
		viewModel.handleNavigation(parameters);
		
		assertEquals("player2", viewModel.player1NameProperty().get());
		assertEquals("player1", viewModel.player2NameProperty().get());
		assertEquals("player3", viewModel.player3NameProperty().get());
		assertEquals(25, viewModel.player1ScoreProperty().get());
		assertEquals(15, viewModel.player2ScoreProperty().get());
		assertEquals(10, viewModel.player3ScoreProperty().get());
		assertTrue(viewModel.player3VisibleProperty().get());
		assertFalse(viewModel.player4VisibleProperty().get());
	}
	
	/**
	 * Tests that no results are added to the achievements if the user of the client was a spectator
	 */
	@Test
	public void doesNotAddAnythingToAchievementsIfSpectatorTest()
	{ addResultsToAchievementsTestCore("player1", 15, 0, 1); }
	
	/**
	 * Tests that the played game and the score is added to the achievements if the user lost
	 */
	@Test
	public void doesAddPlayedGameAndScoreToAchievementsIfLooserTest()
	{ addResultsToAchievementsTestCore(null, 0, 0, 0); }
	
	/**
	 * Tests that the played game, won game and the score is added to the achievements if the user won
	 */
	@Test
	public void doesAddPlayedGameWonGameAndScoreToAchievementsIfWinnerTest()
	{ addResultsToAchievementsTestCore("player2", 25, 1, 1); }
	
	private static void addResultsToAchievementsTestCore(String playerName, int expectedScore, int expectedWonGames, 
		int expectedPlayedGames)
	{
		createAchievementsFile(new FileAccessorMock());
		GameEndViewModel viewModel = new GameEndViewModel();
		Map<String, Object> parameters = new HashMap<String, Object>();
		EndGameResponseDto response = new EndGameResponseDto();
		PlayerDtoBase player1 = new PlayerDtoBase("player1", 15);
		PlayerDtoBase player2 = new PlayerDtoBase("player2", 25);
		PlayerDtoBase player3 = new PlayerDtoBase("player3", 10);
		response.getPlayers().add(player1);
		response.getPlayers().add(player2);
		response.getPlayers().add(player3);
		parameters.put(GameEndViewModel.END_GAME_RESPONSE_PARAMETER, response);
		parameters.put(GameEndViewModel.CURRENT_PLAYER_PARAMETER, playerName);
		viewModel.handleNavigation(parameters);
		
		assertEquals(expectedScore, ClientAchievementManager.getAquiredScoreAchievements().get(0).getProgress());
		assertEquals(expectedPlayedGames, ClientAchievementManager.getPlayedGamesAchievements().get(0).getProgress());
		assertEquals(expectedWonGames, ClientAchievementManager.getWonGamesAchievements().get(0).getProgress());
	}
	
	private static void createAchievementsFile(FileAccessorMock mock)
	{
		List<Achievement> achievements = new ArrayList<Achievement>();
		achievements.add(new Achievement("Gespielte Spiele I", "test1", 200));
		achievements.add(new Achievement("Gewonnene Spiele I", "test2", 200));
		achievements.add(new Achievement("Gesammelte Punkte I", "test3", 200));
		mock.addTextToRead("achievements.json", new Gson().toJson(achievements));
		ClientAchievementManager.initialize(mock);
	}
}
