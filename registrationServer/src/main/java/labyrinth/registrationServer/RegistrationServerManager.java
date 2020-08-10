package labyrinth.registrationServer;

import labyrinth.contracts.communication.ISocketServer;

/**
 * ManagerClass for the RegistrationServer
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class RegistrationServerManager 
{
	//Properties
	
	private static RegistrationServer runningServer;
	/**
	 * Corresponding getter
	 * @return The currently running server
	 */
	public static RegistrationServer getRunningServer() { return runningServer; }
	
	//Constructors
	
	private RegistrationServerManager() { }
	
	//Methods
	
	/**
	 * Starts the registration server
	 * @param server The server to use
	 */
	public static void startServer(ISocketServer<RegistrationServerConnection> server)
	{
		try 
		{
			runningServer = new RegistrationServer(server);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
}
