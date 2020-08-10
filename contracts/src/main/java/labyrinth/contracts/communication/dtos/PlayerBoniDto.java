package labyrinth.contracts.communication.dtos;

/**
 * Dto for informations about the boni of a player
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class PlayerBoniDto 
{
	//Properties
	
	private int beam;
	/**
	 * Corresponding getter
	 * @return The number of beam boni
	 */
	public int getBeam() { return beam; }
	
	private int swap;
	/**
	 * Corresponding getter
	 * @return The number of swap boni
	 */
	public int getSwap() { return swap; }
	
	private int shiftSolid;
	/**
	 * Corresponding getter
	 * @return The number of shiftSolid boni
	 */
	public int getShiftSolid() { return shiftSolid; }
	
	private int shiftTwice;
	/**
	 * Corresponding getter
	 * @return The number of shiftTwice boni
	 */
	public int getShiftTwice() { return shiftTwice; }
	
	//Constructors
	
	/**
	 * PlayerBoniDto constructors
	 * @param beam The number of beam boni
	 * @param swap The number of swap boni
	 * @param shiftSolid The number of shiftSolid boni
	 * @param shiftTwice The number of shiftTwice boni
	 */
	public PlayerBoniDto(int beam, int swap, int shiftSolid, int shiftTwice)
	{
		this.beam = beam;
		this.swap = swap;
		this.shiftSolid = shiftSolid;
		this.shiftTwice = shiftTwice;
	}
}
