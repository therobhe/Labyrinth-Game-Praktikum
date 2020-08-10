package labyrinth.contracts.communication.dtos.requests;

import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.entities.Coordinates;
import labyrinth.contracts.entities.lobby.BoniConfiguration;
import labyrinth.contracts.entities.lobby.GameConfiguration;

/**
 * Dto for @see MessageType.Configuration
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ConfigurationDto extends RequestDtoBase
{
	//Properties
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageType getMessageType() { return MessageType.Configuration; }
	
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

	private Integer treasureCount;
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

	private Double boniProbability;
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

	private Integer gameLengthLimit;
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

	private Integer turnLengthLimit;
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
	 * ConfigurationDto constructor
	 * @param configuration The actual configuration
	 */
	public ConfigurationDto(GameConfiguration configuration) 
	{ 
		admin = configuration.getAdmin();
		serverName = configuration.getServerName(); 
		boni = configuration.getBoni();
		boniProbability = configuration.getBoniProbability(); 
		gameLengthLimit = configuration.getGameLengthLimit(); 
		size = configuration.getSize(); 
		treasureCount = configuration.getTreasureCount(); 
		turnLengthLimit = configuration.getTurnLengthLimit(); 
	}
}
