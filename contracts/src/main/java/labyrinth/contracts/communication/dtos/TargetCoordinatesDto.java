package labyrinth.contracts.communication.dtos;

/**
 * Dto for a the target position of nay operation
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class TargetCoordinatesDto 
{
	//Properties
	
	private int targetX;
	/**
	 * Corresponding getter
	 * @return x-coordinate
	 */
	public int getTargetX() { return targetX; }
	
	private int targetY;
	/**
	 * Corresponding getter
	 * @return y-coordinate
	 */
	public int getTargetY() { return targetY; }
	
	//Constructors
	
	/**
	 * TargetCoordinatesDto constructor
	 * @param targetX y-coordinate
	 * @param targetY y-coordinate
	 */
	public TargetCoordinatesDto(int targetX, int targetY)
	{
		this.targetX = targetX;
		this.targetY = targetY;
	}
}
