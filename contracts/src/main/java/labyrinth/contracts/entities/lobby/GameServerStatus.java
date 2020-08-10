package labyrinth.contracts.entities.lobby;

import com.google.gson.annotations.SerializedName;

/**
 * Enumeration for GameserverEntry @see GameServerEntry class
 * @author Omar
 * @version 1.0
 */
public enum GameServerStatus
{
	/**Game Lobby waiting room*/
	@SerializedName("lobby")
	LOBBY,
	/**Game start*/
	@SerializedName("inGame")
	GAME_STARTED
}