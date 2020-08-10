package labyrinth.contracts.communication.dtos;

/**
 * Dto for a player of the game
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class PlayerDto extends PlayerDtoBase
{
	//Properties
	
	private String color;
	/**
	 * Corresponding getter
	 * @return The color of the player
	 */
	public String getColor() { return color; }
	
	private PlayerTreasuresDto treasures;
	/**
	 * Corresponding getter
	 * @return The treasures of the player
	 */
	public PlayerTreasuresDto getTreasures() { return treasures; }
	
	private PlayerBoniDto boni;
	/**
	 * Corresponding getter
	 * @return The boni of the player
	 */
	public PlayerBoniDto getBoni() { return boni; }
	
	//Constructors
	
	/**
	 * PlayerDto constructors
	 * @param name The name of the player
	 * @param color The color of the player
	 * @param treasures The treasures of the player
	 * @param boni The boni of the player
	 */
	public PlayerDto(String name, String color, PlayerTreasuresDto treasures, PlayerBoniDto boni)
	{
		super(name);
		this.color = color;
		this.treasures = treasures;
		this.boni = boni;
	}
	
	/**
	 * PlayerDto constructors
	 * @param name The name of the player
	 * @param color The color of the player
	 * @param score The score of the player
	 * @param treasures The treasures of the player
	 * @param boni The boni of the player
	 */
	public PlayerDto(String name, String color, int score, PlayerTreasuresDto treasures, PlayerBoniDto boni)
	{
		super(name, score);
		this.color = color;
		this.treasures = treasures;
		this.boni = boni;
	}
	
	/**
	 * PlayerDto constructors
	 * @param name The name of the player
	 * @param color The color of the player
	 * @param score The score of the player
	 * @param disconnected If the player is disconnected
	 * @param treasures The treasures of the player
	 * @param boni The boni of the player
	 */
	public PlayerDto(String name, String color, int score, boolean disconnected, PlayerTreasuresDto treasures, 
		PlayerBoniDto boni)
	{
		super(name, score, disconnected);
		this.color = color;
		this.treasures = treasures;
		this.boni = boni;
	}
}
