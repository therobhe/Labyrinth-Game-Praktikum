package labyrinth.contracts.entities.lobby;

import java.util.ArrayList;
import java.util.List;
import labyrinth.contracts.resources.Errors;

/**
 * Class representing a game lobby
 * @author Jana Kaszyda
 * @version 1.1
 */
public final class Lobby 
{
	//Properties
	
	private List<LobbyUser> playerList = new ArrayList<LobbyUser>();
	/**
	 * Corresponding getter
	 * @return The list of players in the lobby
	 */
	public List<LobbyUser> getPlayerList() { return playerList; }
	
	private List<LobbyUser> spectatorList = new ArrayList<LobbyUser>();
	/**
	 * Corresponding getter
	 * @return The list of spectators in the lobby
	 */
	public List<LobbyUser> getSpectatorList() { return spectatorList; }	
	
	//Methods
	
	/**
	 * Tries to change the role of a lobbyUser
	 * @param user The user whose role is going to be changed
	 * @return true, if role of lobbyUser could be changed
	 */
	public boolean tryChangeUserKind(LobbyUser user)
	{
		if(user.getRole() == LobbyUserKind.PLAYER )
		    return tryChangeRoleToSpectator(user);
		else
		    return tryChangeRoleToPlayer(user);
	}
	
	private boolean tryChangeRoleToPlayer(LobbyUser user)
	{
	    if(!spectatorList.contains(user))
	        throw new IllegalArgumentException(Errors.getLobby_UnknownUser());
	        
        if (playerList.size() > 3)  
			return false;
		else
		{
			user.setRole(LobbyUserKind.PLAYER);
			spectatorList.remove(user);
			playerList.add(user);
			return true;
		}
	}
	
	private boolean tryChangeRoleToSpectator(LobbyUser user)
	{
	    if(!playerList.contains(user))
	        throw new IllegalArgumentException(Errors.getLobby_UnknownUser());
	   
	   	user.setRole(LobbyUserKind.SPECTATOR);
		playerList.remove(user);
		spectatorList.add(user);
		return true;
	}
	
    /**
	 * Checks if user already exists in a lobby
	 * @param userName The name of the new user
	 * @return true if a user with the user name exists else false
	 */
	public boolean doesUserExists(String userName)
	{
		for(LobbyUser player: playerList) 
			if(userName.equals(player.getName())) 
				return true; 
		
		for(LobbyUser spectator: spectatorList)
			if(userName.equals(spectator.getName())) 
				return true;
		
		return false;
	}
}
