package labyrinth.contracts.entities.lobby;

/**
 * Class for a User in a Lobby.
 * @author Eugen Cravtov-Grandl
 * @version 1.1
 */
public final class LobbyUser extends User
{
	//Properties
	
	private transient boolean isReady = false;
	/**
	 * Corresponding getter
     * @return If the user is ready for the game to start
     */
	public boolean getIsReady() { return isReady; }
	/**
	 * Corresponding setter
     * @param value If the user is ready for the game to start
     */
	public void setIsReady(boolean value) { isReady = value; }
	
	private String color;
	/**
	 * Corresponding getter
	 * @return The color of the player
	 */
	public String getColor() { return color; }
	/**
	 * Corresponding setter
	 * @param value The color of the player
	 */
	public void setColor(String value) { color = value; }
	
	private UserPermission permission;
	/**
	 * Corresponding getter
     * @return The permission of the user
     */
	public UserPermission getPermission() { return permission; }
	/**
	 * Corresponding setter
     * @param permission The permission of the user
     */
	public void setPermission(UserPermission permission) { this.permission = permission; }	
	
	//Constructors
	
	/**
	 * GameServerEntry constructor 
	 * @param name The Name of the member
	 * @param role The role of the member of the server
	 * @param color The color of the member
	 * @param permission The permission of the member
	 */
	public LobbyUser(String name, String color, UserPermission permission, LobbyUserKind role)
	{
		super(name, role);
		this.permission = permission;
		this.color = color;
	}
}
