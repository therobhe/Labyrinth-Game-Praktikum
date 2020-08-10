package labyrinth.client.ui;

import java.net.InetAddress;
import labyrinth.contracts.communication.ISocketClient;
import labyrinth.contracts.entities.lobby.GameServerEntry;

/**
 * Manager-class containing the connections of the client
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ClientConnectionManager 
{
	//Properties
	
	private static Class<? extends ISocketClient> activeClientType;
	
	private static ISocketClient gameServerClient;
	/**
	 * Corresponding getter
	 * @return The Client connected to the GameServer
	 */
	public static ISocketClient getGameServerClient() { return gameServerClient; }
	
	private static ISocketClient registrationServerClient;
	/**
	 * Corresponding getter
	 * @return The Client connected to the RegistrationServer
	 */
	public static ISocketClient getRegistrationServerClient() { return registrationServerClient; }
	
	//Constructors
	
	private ClientConnectionManager() { }
	
	//Methods
	
	/**
	 * Initializes the ServerManager with the types to use for SocketClient and SocketServer
	 * @param clientType The Type which should be used for the client
	 */
	public static void initialize(Class<? extends ISocketClient> clientType) { activeClientType = clientType; }
	
	/**
	 * Connects to the RegistrationServer
	 */
	public static void startRegistrationServerClient()
	{
		try 
		{
			String ip = InetAddress.getLocalHost().getHostAddress();
			registrationServerClient = activeClientType.getConstructor(String.class, int.class).newInstance(ip, 1300);	
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Connects the GameServer
	 * @param server The server to connect to
	 */
	public static void startGameServerClient(GameServerEntry server)
	{
		try 
		{
			gameServerClient = activeClientType.getConstructor(String.class, int.class)
				.newInstance(server.getIp(), server.getPort());	
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes all connections of the client
	 */
	public static void closeAllConnections()
	{
		try 
		{
			if(gameServerClient != null)
				gameServerClient.close();
			registrationServerClient.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
