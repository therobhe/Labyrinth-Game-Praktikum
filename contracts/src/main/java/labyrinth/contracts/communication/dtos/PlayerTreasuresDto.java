package labyrinth.contracts.communication.dtos;

/**
 * Dto for informations about the treasures of a player
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class PlayerTreasuresDto 
{
	//Properties
	
	private Integer current;
	/**
	 * Corresponding getter
	 * @return The collected amount treasures 
	 */
	public Integer getCurrent() { return current; }
	
	private int remaining;
	/**
	 * Corresponding getter
	 * @return The remaining amount of treasures
	 */
	public int getRemaining() { return remaining; }
	
	//Constructors
	
	/**
	 * PlayerTreasuresDto constructors
	 * @param current The collected amount treasures 
	 * @param remaining The remaining amount of treasures
	 */
	public PlayerTreasuresDto(Integer current, int remaining)
	{
		this.current = current;
		this.remaining = remaining;
	}
}
