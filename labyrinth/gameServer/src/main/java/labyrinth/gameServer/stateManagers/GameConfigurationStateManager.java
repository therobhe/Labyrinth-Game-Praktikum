package labyrinth.gameServer.stateManagers;

import labyrinth.contracts.entities.lobby.GameConfiguration;
import labyrinth.gameServer.ObjectAccessMonitor;

/**
 * ManagerClass for managing the State of the GameConfiguration of the GameServer
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class GameConfigurationStateManager
{
	//Properties
	
	private static GameConfiguration configuration;
	
	private static ObjectAccessMonitor<GameConfiguration> configurationMonitor;
	/**
	 * Corresponding getter
	 * @return The monitor containing the configuration
	 */
	public static ObjectAccessMonitor<GameConfiguration> getConfigurationMonitor() { return configurationMonitor; }
	
	//Constructors
	
	private GameConfigurationStateManager() { } 	
	
	//Methods
	
	/**
	 * Initializes the state manager 
	 */
	public static void initialize()
	{
		configuration = new GameConfiguration();
		configurationMonitor = new ObjectAccessMonitor<GameConfiguration>(configuration);
	}
}
