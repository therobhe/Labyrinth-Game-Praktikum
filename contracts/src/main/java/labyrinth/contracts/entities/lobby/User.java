package labyrinth.contracts.entities.lobby;

/**
 * A user of the GameServer
 * @author Lukas Reinhardt
 * @version 1.0
 */
public class User
{
	//Properties
	
	private String name;
	/**
	 * Corresponding getter
	 * @return The user name
	 */
	public String getName() { return name; }
	
	private LobbyUserKind role;
	/**
	 * Corresponding getter
	 * @return The role of the member of the server
	 */
	public LobbyUserKind getRole() { return role; }
	/**
	 * Corresponding setter
     * @param role The role of the member of the server
     */
	public void setRole(LobbyUserKind role) { this.role = role; }
	
	//Constructors
	
	/**
	 * ServerMemberDto  constructor
	 * @param name The Name of the member
	 * @param role The role of the member of the server
	 */
	public User(String name, LobbyUserKind role)
	{
		this.name = name;
		this.role = role;
	}
}
