package labyrinth.contracts.entities.lobby;

import com.google.gson.annotations.SerializedName;

/**
 * Enumeration for the different permissions users can have
 * @author Lukas Reinhardt
 * @version 1.0
 */
public enum UserPermission 
{
	/** User is the administrator */
	@SerializedName("admin")
	ADMIN,
	/** User is a normal user */
	@SerializedName("default")
	DEFAULT
}
