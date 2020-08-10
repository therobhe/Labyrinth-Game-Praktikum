package labyrinth.registrationServer;

import labyrinth.contracts.communication.ISocketServer;

/**
 * Class for a registration server
 * @author Lukas Reinhardt
 * @version 1.0
 */
public class RegistrationServer 
{		
	/**
	 * RegistrationServer constructor
	 * @param server The server implementation
	 */
	public RegistrationServer(ISocketServer<RegistrationServerConnection> server) { server.start(); }
}
