package labyrinth.gameServer;

import labyrinth.contracts.communication.dtos.responses.PropagateAchievementResponseDto;
import labyrinth.contracts.entities.game.Achievement;

/**
 * A wrapper for the server achievements ogf a specific player
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ServerAchievementsWrapper 
{
	//Properties
	
	private static final String MOVED_TILES_DESCRIPTION = "";
	
	private static final String USED_BONI_DESCRIPTION = "";
	
	private String player;
	
	private Achievement movedTilesAchievement1 = new Achievement("Marathonlaeufer I", MOVED_TILES_DESCRIPTION, 25);
	private Achievement movedTilesAchievement2 = new Achievement("Marathonlaeufer II", MOVED_TILES_DESCRIPTION, 50);
	private Achievement movedTilesAchievement3 = new Achievement("Marathonlaeufer III", MOVED_TILES_DESCRIPTION, 75);
	private Achievement movedTilesAchievement4 = new Achievement("Marathonlaeufer IV", MOVED_TILES_DESCRIPTION, 100);
	private Achievement movedTilesAchievement5 = new Achievement("Marathonlaeufer V", MOVED_TILES_DESCRIPTION, 150);
	
	private Achievement usedBoniAchievement1 = new Achievement("Taktiker I", USED_BONI_DESCRIPTION, 3);
	private Achievement usedBoniAchievement2 = new Achievement("Taktiker II", USED_BONI_DESCRIPTION, 5);
	private Achievement usedBoniAchievement3 = new Achievement("Taktiker III", USED_BONI_DESCRIPTION, 7);
	private Achievement usedBoniAchievement4 = new Achievement("Taktiker IV", USED_BONI_DESCRIPTION, 10);
	private Achievement usedBoniAchievement5 = new Achievement("Taktiker V", USED_BONI_DESCRIPTION, 15);
	
	//Constructors
	
	/**
	 * ServerAchievementsWrapper constructor
	 * @param player Name of the player
	 */
	public ServerAchievementsWrapper(String player) { this.player = player; }
	
	//Methods
	
	/**
	 * Adds an amount of moved tiles to the achievements progress
	 * @param movedTiles The amount of tiles the player moved
	 */
	public void addMovedTiles(int movedTiles)
	{
		addMovedTilesCore(movedTilesAchievement1, movedTiles);
		addMovedTilesCore(movedTilesAchievement2, movedTiles);
		addMovedTilesCore(movedTilesAchievement3, movedTiles);
		addMovedTilesCore(movedTilesAchievement4, movedTiles);
		addMovedTilesCore(movedTilesAchievement5, movedTiles);
	}
	
	/**
	 * Adds a use of a bonus to the achievements progress
	 */
	public void addUsedBonus()
	{
		addUsedBonusCore(usedBoniAchievement1);
		addUsedBonusCore(usedBoniAchievement2);
		addUsedBonusCore(usedBoniAchievement3);
		addUsedBonusCore(usedBoniAchievement4);
		addUsedBonusCore(usedBoniAchievement5);
	}
		
	private void addMovedTilesCore(Achievement achievement, int movedTiles)
	{
		if(achievement.getProgress() != achievement.getRequired())
		{
			achievement.addProgress(movedTiles);
			if(achievement.getProgress() == achievement.getRequired())
				propagateAchievement(achievement);		
		}
	}
	
	private void addUsedBonusCore(Achievement achievement)
	{
		if(achievement.getProgress() != achievement.getRequired())
		{
			achievement.addProgress(1);
			if(achievement.getProgress() == achievement.getRequired())
				propagateAchievement(achievement);		
		}
	}
	
	private void propagateAchievement(Achievement achievement)
	{
		PropagateAchievementResponseDto response 
			= new PropagateAchievementResponseDto(player, achievement.getName(), achievement.getDescription());
		GameServerManager.getRunningServer().broadcastResponse(response);
	}
}
