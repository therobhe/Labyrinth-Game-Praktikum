package labyrinth.contracts.entities.lobby;

import com.google.gson.annotations.SerializedName;

/**
 * Enumeration for different kinds of Users in Lobby @see LobbyUser
 * @author Eugen Cravtov-Grandl
 * @version 1.0
 */
public enum LobbyUserKind 
{
	/** Player */
	@SerializedName("player")
	PLAYER,
	/** Spectator */
	@SerializedName("spectator")
	SPECTATOR;
}
