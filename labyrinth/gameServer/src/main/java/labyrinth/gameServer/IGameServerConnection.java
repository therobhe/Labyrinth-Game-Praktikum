package labyrinth.gameServer;

import labyrinth.contracts.communication.IServerConnection;
import labyrinth.contracts.entities.lobby.LobbyUser;

/**
 * Interface for all Connections to the GameServer
 * @author Lukas Reinhardt
 * @version 1.0
 */
public interface IGameServerConnection extends IServerConnection
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
	 * Corresponding getter 
	 * @return The user of the Client
	 */
	public LobbyUser getUser();
	
	/**
	 * Corresponding setter 
	 * @param value The user of the Client
	 */
	public void setUser(LobbyUser value); 
}
