package labyrinth.gameServer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manager class for the achievements of the players
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class GameServerAchievementManager 
{
	//Properties
	
	private static Map<String, ServerAchievementsWrapper> playerAchievements 
		= new HashMap<String, ServerAchievementsWrapper>();
	
	//Constructors
	
	private GameServerAchievementManager() { }
	
	//Methods
	
	/**
	 * Initializes the GameServerAchievementManager 
	 * @param players The players of the game
	 */
	public static void initialize(List<String> players)
	{
		for(String player : players)
			playerAchievements.put(player, new ServerAchievementsWrapper(player));
	}
	
	/**
	 * Adds an amount of moved tiles to the achievements progress
	 * @param player Name of the player
	 * @param movedTiles The amount of tiles the player moved
	 */
	public static void addMovedTiles(String player, int movedTiles) 
	{ playerAchievements.get(player).addMovedTiles(movedTiles); }
	
	/**
	 * Adds a use of a bonus to the achievements progress
	 * @param player Name of the player
	 */
	public static void addUsedBonus(String player) { playerAchievements.get(player).addUsedBonus(); }
}