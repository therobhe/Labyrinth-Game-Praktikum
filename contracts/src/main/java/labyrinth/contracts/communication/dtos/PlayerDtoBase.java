package labyrinth.contracts.communication.dtos;

/**
 * Base-class of a Dto for a player of the game
 * @author Lukas Reinhardt
 * @version 1.0
 */
public class PlayerDtoBase 
{
	//Properties
	
	private String name;
	/**
	 * Corresponding getter
	 * @return The name of the player
	 */
	public String getName() { return name; }
	
	private int score;
	/**
	 * Corresponding getter
	 * @return The score of the player
	 */
	public int getScore() { return score; }
	
	private boolean disconnected;
	/**
	 * Corresponding getter
	 * @return If the player is disconnected
	 */
	public boolean getDisconnected() { return disconnected; }
	
	//Constructors
	
	/**
	 * PlayerDtoBase constructors
	 * @param name The name of the player
	 * @param score The score of the player
	 */
	public PlayerDtoBase(String name, int score) 
	{ 
		this(name);
		this.score = score;
	}
	
	/**
	 * PlayerDto constructors
	 * @param name The name of the player
	 * @param score The score of the player
	 * @param disconnected If the player is disconnected
	 */
	public PlayerDtoBase(String name, int score, boolean disconnected) 
	{ 
		this(name, score);
		this.disconnected = disconnected;
	}
	
	/**
	 * PlayerDtoBase constructors
	 * @param name The name of the player
	 */
	protected PlayerDtoBase(String name) { this.name = name; }
}
