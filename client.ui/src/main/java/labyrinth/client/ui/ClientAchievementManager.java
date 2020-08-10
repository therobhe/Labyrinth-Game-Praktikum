package labyrinth.client.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import labyrinth.contracts.entities.game.Achievement;
import labyrinth.contracts.utilities.IFileAccessor;

/**
 * Manager class containing all the client achievements
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ClientAchievementManager
{
	//Properties
	
	private static final String PLAYED_GAMES_DESCRIPTION = "Spiele mehr als %d Spiele.";
	
	private static final String WON_GAMES_DESCRIPTION = "Gewinne mehr als %d Spiele.";
	
	private static final String AQUIRED_POINTS_DESCRIPTION = "Erhalte mehr als %d Punkte über alle Spiele hinweg.";
	
	private static final String FILE_NAME = "achievements.json";
	
	private static IFileAccessor accessor;
	
	private static List<Achievement> playedGamesAchievements = new ArrayList<Achievement>();
	/**
	 * Corresponding getter
	 * @return All played games Achievements
	 */
	public static List<Achievement> getPlayedGamesAchievements() { return playedGamesAchievements; }
	
	private static List<Achievement> wonGamesAchievements = new ArrayList<Achievement>();
	/**
	 * Corresponding getter
	 * @return All won games Achievements
	 */
	public static List<Achievement> getWonGamesAchievements() { return wonGamesAchievements; }
	
	private static List<Achievement> aquiredScoreAchievements = new ArrayList<Achievement>();
	/**
	 * Corresponding getter
	 * @return All acquired score Achievements
	 */
	public static List<Achievement> getAquiredScoreAchievements() { return aquiredScoreAchievements; }
	
	//Constructors
	
	private ClientAchievementManager() { }
	
	//Methods
	
	/**
	 * Initializes the achievements
	 * @param fileAccessor The file accessor used for loading the achievements
	 */
	public static void initialize(IFileAccessor fileAccessor) 
	{ 
		accessor = fileAccessor;
		loadOrCreateAchievements(accessor); 
	}
	
	/**
	 * Adds a played game to all relevant achievements
	 */
	public static void addPlayedGame()
	{
		for(Achievement playedGamesAchievement : playedGamesAchievements)
			playedGamesAchievement.addProgress(1);
		saveAchievements();
	}
	
	/**
	 * Adds a won game to all relevant achievements
	 */
	public static void addWonGame()
	{
		for(Achievement wonGamesAchievement : wonGamesAchievements)
			wonGamesAchievement.addProgress(1);
		addPlayedGame();
	}
	
	/**
	 * Adds the acquired score to all relevant achievements
	 */
	public static void addAquiredScore(int score)
	{
		for(Achievement aquiredScoreAchievement : aquiredScoreAchievements)
			aquiredScoreAchievement.addProgress(score);
		saveAchievements();
	}
	
	private static void saveAchievements()
	{
		List<Achievement> achievements = new ArrayList<Achievement>(playedGamesAchievements);
		achievements.addAll(wonGamesAchievements);
		achievements.addAll(aquiredScoreAchievements);
		String json = new Gson().toJson(achievements);
		accessor.writeToFile(FILE_NAME, json, true);
	}
	
	private static void loadOrCreateAchievements(IFileAccessor accessor)
	{
		String json = accessor.readFile(FILE_NAME);
		if(json == null) //Create achievements
		{
			initializePlayedGamesAchievements();
			initializeWonGamesAchievements();
			initializeAquiredScoreAchievements();
		}
		else //use loaded achievements
		{
			List<Achievement> achievements = new Gson().fromJson(json, new TypeToken<List<Achievement>>(){}.getType());
			playedGamesAchievements.addAll(achievements.stream().filter(
				achievement -> achievement.getName().contains("Gespielte Spiele")).collect(Collectors.toList()));
			wonGamesAchievements.addAll(achievements.stream().filter(
				achievement -> achievement.getName().contains("Gewonnene Spiele")).collect(Collectors.toList()));
			aquiredScoreAchievements.addAll(achievements.stream().filter(
				achievement -> achievement.getName().contains("Gesammelte Punkte")).collect(Collectors.toList()));
		}
	}
	
	private static void initializePlayedGamesAchievements()
	{
		playedGamesAchievements.add(new Achievement("Gespielte Spiele I", String.format(PLAYED_GAMES_DESCRIPTION, 3), 3));
		playedGamesAchievements.add(new Achievement("Gespielte Spiele II", String.format(PLAYED_GAMES_DESCRIPTION, 7), 7));
		playedGamesAchievements.add(new Achievement("Gespielte Spiele III", String.format(PLAYED_GAMES_DESCRIPTION, 15), 15));
		playedGamesAchievements.add(new Achievement("Gespielte Spiele IV", String.format(PLAYED_GAMES_DESCRIPTION, 25), 25));
		playedGamesAchievements.add(new Achievement("Gespielte Spiele V", String.format(PLAYED_GAMES_DESCRIPTION, 50), 50));
	}
	
	private static void initializeWonGamesAchievements()
	{
		wonGamesAchievements.add(new Achievement("Gewonnene Spiele I", String.format(WON_GAMES_DESCRIPTION, 1), 1));
		wonGamesAchievements.add(new Achievement("Gewonnene Spiele II", String.format(WON_GAMES_DESCRIPTION, 3), 3));
		wonGamesAchievements.add(new Achievement("Gewonnene Spiele III", String.format(WON_GAMES_DESCRIPTION, 7), 7));
		wonGamesAchievements.add(new Achievement("Gewonnene Spiele IV", String.format(WON_GAMES_DESCRIPTION, 15), 15));
		wonGamesAchievements.add(new Achievement("Gewonnene Spiele V", String.format(WON_GAMES_DESCRIPTION, 25), 25));
	}
	
	private static void initializeAquiredScoreAchievements()
	{
		aquiredScoreAchievements.add(new Achievement("Gesammelte Punkte I", String.format(AQUIRED_POINTS_DESCRIPTION, 50), 50));
		aquiredScoreAchievements.add(new Achievement("Gesammelte Punkte II", String.format(AQUIRED_POINTS_DESCRIPTION, 100), 100));
		aquiredScoreAchievements.add(new Achievement("Gesammelte Punkte III", String.format(AQUIRED_POINTS_DESCRIPTION, 200), 200));
		aquiredScoreAchievements.add(new Achievement("Gesammelte Punkte IV", String.format(AQUIRED_POINTS_DESCRIPTION, 500), 500));
		aquiredScoreAchievements.add(new Achievement("Gesammelte Punkte V", String.format(AQUIRED_POINTS_DESCRIPTION, 1000), 1000));
	}
}