package labyrinth.gameServer;

import labyrinth.contracts.utilities.FileLogger;

/**
 * Manager-class for logging the players actions
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class LoggingManager 
{
	//Properties
	
	private static FileLogger fileLogger;
	
	//Constructors
	
	private LoggingManager() {}
	
	//Methods
	
	/**
	 * Initializes the logging manager
	 * @param logger The logger used for logging
	 */
	public static void initialize(FileLogger logger) { fileLogger = logger; }
	
	/**
	 * Initializes the logging manager
	 * @param playerName Name of the player
	 * @param action The action the player performed
	 */
	public static void LogPlayerAction(String playerName, String action) { fileLogger.LogMessage(playerName, action); }
}
