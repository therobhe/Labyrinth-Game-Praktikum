package labyrinth.gameServer;

import labyrinth.contracts.communication.ISocketClient;
import labyrinth.contracts.communication.ISocketServer;

/**
 * ManagerClass for the GameServer
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class GameServerManager 
{
	//Properties
	
	private static ISocketClient registrationServerClient;
	
	private static ISocketServer<? extends IGameServerConnection> server;
	
	private static GameServer runningServer;
	/**
	 * Corresponding getter
	 * @return The currently running server
	 */
	public static GameServer getRunningServer() { return runningServer; }
	
	//Constructors
	
	private GameServerManager() { }
	
	//Methods
	
	/**
	 * Closes the server if it's running
	 */
	public static void tryCloseServer() 
	{ 
		if(runningServer != null)
			runningServer.close(); 
	}
	
	/**
	 * Initializes the ServerManager with the types to use for SocketClient and SocketServer
	 * @param client The client that should be used for communicating with the registration server
	 * @param socketServer The socket server to use for the game server
	 */
	public static void initialize(ISocketClient client, ISocketServer<? extends IGameServerConnection> socketServer)
	{
		registrationServerClient = client;
		server = socketServer;
	}
	
	/**
	 * Starts the GameServer
	 * @param serverName Name of the server
	 */
	public static void startServer(String serverName)
	{
		try 
		{
			runningServer = new GameServer(serverName, registrationServerClient, server);
			runningServer.start();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
