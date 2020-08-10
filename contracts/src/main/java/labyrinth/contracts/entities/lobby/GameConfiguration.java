package labyrinth.contracts.entities.lobby;

import labyrinth.contracts.entities.Coordinates;

/**
 * Class representing the settings of the game
 * @author Robert Heinemann
 * @version 1.1
 */
public final class GameConfiguration
{
	//Properties
	
	private String serverName;
	/**
	 * Corresponding getter 
	 * @return The name of the server
	 */
	public String getServerName() { return serverName; }
	/**
	 * Corresponding setter
	 * @param serverName Name of the server
	 */
	public void setServerName(String serverName) { this.serverName = serverName; }

	private Coordinates size = new Coordinates(11,11);
	/** 
	 * Corresponding getter
	 * @return The size of the board
	 */
	public Coordinates getSize() { return size; }
	/**
	 * Corresponding setter
	 * @param value size of the board
	 */
	public void setSize(Coordinates value) { this.size = value; }

	private Integer treasureCount = 5;
	/** 
	 * Corresponding getter
	 * @return The number of collected treasures
	 */
	public Integer getTreasureCount() { return treasureCount; }
	/**
	 * Corresponding setter
	 * @param treasureCount The number of collected treasures to be set
	 */
	public void setTreasureCount(Integer treasureCount) { this.treasureCount = treasureCount; }

	private BoniConfiguration boni = new BoniConfiguration();
	/** 
	 * Corresponding getter
	 * @return The configuration which boni are enabled
	 */
	public BoniConfiguration getBoni() { return boni; }
	/** 
	 * Corresponding setter
	 * @param value The configuration which boni are enabled
	 */
	public void setBoni(BoniConfiguration value) { boni = value; }

	private Double boniProbability = 0.15;
	/** 
	 * corresponding getter
	 * @return Probability of a boni to be generated
	 */
	public Double getBoniProbability() { return boniProbability; }
	/**
	 * corresponding getter
	 * @param boniProbability Probability of a boni to be generated
	 */
	public void setBoniProbability(Double boniProbability) { this.boniProbability = boniProbability; }

	private Integer gameLengthLimit = 15;
	/** 
	 * corresponding getter
	 * @return Maximum count of turns of for the game
	 */
	public Integer getGameLengthLimit() { return gameLengthLimit; }
	/**
	 * corresponding setter
	 * @param gameLengthLimit Maximum count of turns of for the game
	 */
	public void setGameLengthLimit(Integer gameLengthLimit) { this.gameLengthLimit = gameLengthLimit; }

	private Integer turnLengthLimit = 60;
	/** 
	 * corresponding getter
	 * @return The maximum time limit for a players turn
	 */
	public Integer getTurnLengthLimit() { return turnLengthLimit; }
	/**
	 * corresponding setter
	 * @param turnLengthLimit The maximum time limit for a players turn
	 */
	public void setTurnLengthLimit(Integer turnLengthLimit) { this.turnLengthLimit = turnLengthLimit; }

	private String admin;
	/** 
	 * corresponding getter
	 * @return The name of the current admin
	 */
	public String getAdmin() { return admin; }
	/**
	 * corresponding setter
	 * @param admin The name of the current admin
	 */
	public void setAdmin(String admin) { this.admin = admin; }
	
	//Constructors
	
	/**
	 * GameConfiguration constructors
	 */
	public GameConfiguration() { }
	
	/**
	 * ConfigurationDto constructor
	 * @param serverName The name of the server
	 * @param size The size of the game board
	 * @param treasureCount The count of treasures to collect
	 * @param boni The configuration which boni are available
	 * @param boniProbability The probability of boni to appear
	 * @param gameLengthLimit The maximum game length
	 * @param turnLengthLimit The maximum turn length
	 * @param admin The user name of the new admin
	 */
	public GameConfiguration(String serverName, Coordinates size, int treasureCount, BoniConfiguration boni, 
		double boniProbability, int gameLengthLimit, int turnLengthLimit, String admin)
	{
		this.serverName = serverName;
		this.size = size;
		this.treasureCount = treasureCount;
		this.boni = boni;
		this.boniProbability = boniProbability;
		this.gameLengthLimit = gameLengthLimit;
		this.turnLengthLimit = turnLengthLimit;
		this.admin = admin;
	}
}