package labyrinth.contracts.communication.dtos.responses;

import labyrinth.contracts.communication.MessageType;

/**
 * Dto for a response to @see MessageType.PropagateAchievementResponseDto
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class PropagateAchievementResponseDto extends ResponseDtoBase
{
	//Properties
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageType getMessageType() { return MessageType.PropagateAchievement; }
	
	private String player;
	/**
	 * Corresponding getter
	 * @return The name of the player
	 */
	public String getPlayer() { return player; }
	
	private String achievement;
	/**
	 * Corresponding getter
	 * @return The name of the achievement
	 */
	public String getAchievement() { return achievement; }
	
	private String description;
	/**
	 * Corresponding getter
	 * @return The description of the achievement
	 */
	public String getDescription() { return description; }
	
	//Constructors
	
	/**
	 * PropagateAchievementResponseDto constructors
	 * @param player Name of the player
	 * @param achievement The name of the achievement
	 */
	public PropagateAchievementResponseDto(String player, String achievement)
	{
		this.player = player;
		this.achievement = achievement;
	}
	
	/**
	 * PropagateAchievementResponseDto constructors
	 * @param player Name of the player
	 * @param achievement The name of the achievement
	 * @param description The description of the achievement
	 */
	public PropagateAchievementResponseDto(String player, String achievement, String description)
	{
		this(player, achievement);
		this.description = description;
	}
}
