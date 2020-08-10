package labyrinth.contracts.communication.dtos;

/**
 * Dto for a the target player for the swap bonus
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class TargetPlayerDto 
{
	//Properties
	
	private String targetPlayer;
	/**
	 * Corresponding setters
	 * @return The name of the target player
	 */
	public String getTargetPlayer() { return targetPlayer; }
	
	//Constructors
	
	/**
	 * TargetPlayerDto constructor
	 * @param targetPlayer The name of the target player
	 */
	public TargetPlayerDto(String targetPlayer) { this.targetPlayer = targetPlayer; }
}
