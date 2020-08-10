package labyrinth.registrationServer;

import labyrinth.contracts.communication.IServerConnection;
import labyrinth.contracts.entities.lobby.GameServerEntry;

/**
 * Interface for all Connections to the RegistrationServer
 * @author Lukas Reinhardt
 * @version 1.0
 */
public interface IRegistrationServerConnection extends IServerConnection
{
	/**
	 * Corresponding getter 
	 * @return The userId of the Client connected to the RegistrationServer
	 */
	public int getUserId();
	/**
	 * Corresponding setter 
	 * @param value The userId of the Client connected to the RegistrationServer
	 */
	public void setUserId(int value);
	
	/**
	 * Corresponding setter 
	 * @return The GameServer connected to the RegistrationServer
	 */
	public GameServerEntry getServer();
	/**
	 * Corresponding setter 
	 * @param value The GameServer connected to the RegistrationServer
	 */
	public void setServer(GameServerEntry value);
}
