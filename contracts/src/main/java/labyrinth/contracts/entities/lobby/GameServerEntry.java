package labyrinth.contracts.entities.lobby;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for an entry in the server list retrieved from the administration server
 * @author Omar Al Masalma
 * @version 1.1
 */
public final class GameServerEntry
{
	//Properties
	
	private String serverName;
	/**
	 * Corresponding getter
	 * @return Name of the server
	 */
	public String getServerName() { return serverName; }
	/**
	 *Corresponding setter
	 *@param serverName Name of the server
	 */
	public void setServerName(String serverName) { this.serverName = serverName; }
	
	private String ip;
	/**
	 * Corresponding getter
	 * @return IP-address of the server
	 */
	public String getIp() { return ip; }
	/**
	 * Corresponding setter
	 * @param value IP-address of the server
	 */
	public void setIp(String value) { this.ip = value; }
	
	private int port;
	/**
	 * Corresponding getter
	 * @return Port number of the server
	 */
	public int getPort() { return port; }
	/**
	 * Corresponding setter
	 * @param value Port number of the server
	 */
	public void setPort(int value) { this.port = value; }
	
	private GameServerStatus state;
	/**
	 * Corresponding getter
	 * @return Current status of the server
	 */
	public GameServerStatus getState() { return state; }
	/**
	 * Corresponding setter
	 * @param value Current status of the server
	 */
	public void setState(GameServerStatus value) { this.state = value; }
	
	private List<User> members = new ArrayList<User>();
	/**
	 * Corresponding getter 
	 * @return All memebers of the lobby of the server
	 */
	public List<User> getMembers() { return members; }
	
	//Constructors
	
	/**
	 * GameServerEntry constructor
	 * @param serverName Name of the GameServer
	 */
	public GameServerEntry(String serverName) 
	{ 
		this.serverName = serverName;
		this.state = GameServerStatus.LOBBY;
	}
	
	/**
	 * GameServerEntry constructor
	 * @param serverName Name of the GameServer
	 * @param state Status of the GameServer
	 * @param ip IP-Address of the GameServer
	 * @param port Port-Number of the GameServer
	 */
	public GameServerEntry(String serverName, GameServerStatus state, String ip, int port) 
	{ 
		this.serverName = serverName;
		this.state = state;
		this.ip = ip;
		this.port = port;
	}
}