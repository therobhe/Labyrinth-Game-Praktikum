package labyrinth.registrationServer;

import java.util.ArrayList;
import java.util.List;
import labyrinth.contracts.entities.lobby.GameServerEntry;

/**
 * ManagerClass for managing the State of the RegistrationServer
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class RegistrationServerStateManager 
{
	//Properties
	
	private static List<Integer> userIds = new ArrayList<Integer>();
	/**
	 * Corresponding getter
	 * @return The registered users
	 */
	public static synchronized List<Integer> getUserIds() { return userIds; }
		
	private static List<GameServerEntry> gameServers = new ArrayList<GameServerEntry>();
	/**
	 * Corresponding getter
	 * @return The registered servers
	 */
	public static synchronized List<GameServerEntry> getServers() { return gameServers; }
	
	//Constructors
	
	private RegistrationServerStateManager() {}
	
	//Methods
	
	/**
	 * Adds a new user id to the registered clients
	 * @param userId The user id
	 */
	public static synchronized void addUserId(int userId) { userIds.add(userId); }
	
	/**
	 * Removes a user id from the registered clients
	 * @param userId The user id
	 */
	public static synchronized void removeUserId(int userId) 
	{ 
		//Integer cast because otherwise it would remove the element at the index and not the element
		//This is bad design. Why do remove(index) and remove(element) have the same name?
		userIds.remove((Integer)userId); 
	}
	
	/**
	 * Returns whether the user id is a registered client
	 * @param userId The user id
	 */
	public static synchronized boolean containsUserId(int userId) { return userIds.contains(userId); }
	
	/**
	 * Adds a new GameServer to the registered servers
	 * @param server The GameServer to add
	 */
	public static synchronized void addGameServer(GameServerEntry server) { gameServers.add(server); }
	
	/**
	 * Removes a GameServer from the registered servers
	 * @param server The GameServer to remove
	 */
	public static synchronized void removeGameServer(GameServerEntry server) { gameServers.remove(server); }
}
