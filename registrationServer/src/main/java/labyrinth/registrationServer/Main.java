package labyrinth.registrationServer;

import labyrinth.contracts.communication.SocketServer;

/**
 * Main class containing the entry point for the application.
 * @author Lukas Reinhardt
 * @version 1.0
 */
public class Main
{
	/**
	 * Entry point of the application
	 * @param args Command line arguments
	 */
	public static void main(String[] args) 
	{ 
		SocketServer<RegistrationServerConnection> server 
			= new SocketServer<RegistrationServerConnection>(1300, RegistrationServerConnection.class);
		RegistrationServerManager.startServer(server);
	}
}
