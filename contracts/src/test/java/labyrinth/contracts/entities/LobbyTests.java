package labyrinth.contracts.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import labyrinth.contracts.entities.lobby.Lobby;
import labyrinth.contracts.entities.lobby.LobbyUser;
import labyrinth.contracts.entities.lobby.LobbyUserKind;
import labyrinth.contracts.entities.lobby.UserPermission;

/**
 * Testclass for @see Lobby
 * @author Eugen Cravtov-Grandl
 * @version 1.2 
 */
public final class LobbyTests 
{
	/**
	 * Tests if the role Player of a LobbyUser can be changed to Spectator
	 */
	@Test
    public void canChangeRoleToSpectatorTest()
    {
		//create new Lobby
    	Lobby lobby = new Lobby();
    	
    	//create a lobbyUser with role Player to test
    	LobbyUser player1 = new LobbyUser("Turk", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER);
    	lobby.getPlayerList().add(player1);
    	
    	//test if role can be changed
    	assertTrue(lobby.tryChangeUserKind(player1), "Die Rolle wurde nicht geändert.");
    	assertEquals(player1.getRole(), LobbyUserKind.SPECTATOR);
    	assertTrue(!lobby.getPlayerList().contains(player1));
    	assertTrue(lobby.getSpectatorList().contains(player1));
    }
	
	/**
	 * Tests if the role Spectator of a LobbyUser cannot be changed to PLayer if PlayerList is full
	 */
	@Test
    public void cannotChangeRoleToPlayerIfPlayerCountIsExceededTest()
    {
		//create new Lobby
    	Lobby lobby = new Lobby();
    	
    	//create 4 lobbyUser with role Player to test
    	LobbyUser player1 = new LobbyUser("Turk", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER);
    	lobby.getPlayerList().add(player1);
    	LobbyUser player2 = new LobbyUser("J.D.", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER);
    	lobby.getPlayerList().add(player2);
    	LobbyUser player3 = new LobbyUser("Jan Itor", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER);
    	lobby.getPlayerList().add(player3);
    	LobbyUser player4 = new LobbyUser("Kelso", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER);
    	lobby.getPlayerList().add(player4);
    	
    	//create lobbyUser with role Spectator to test
    	LobbyUser spectator = new LobbyUser("Eugen", null, UserPermission.DEFAULT, LobbyUserKind.SPECTATOR);
    	lobby.getSpectatorList().add(spectator);
    	
    	//test if role can't be changed
    	assertFalse(lobby.tryChangeUserKind(spectator), "Die Rolle wurde geändert.");
    	assertEquals(spectator.getRole(), LobbyUserKind.SPECTATOR);
    	assertFalse(lobby.getPlayerList().contains(spectator));
    	assertFalse(!lobby.getSpectatorList().contains(spectator));
    }
	
	/**
	 * Tests if the role Spectator of a LobbyUser can be changed to Player
	 */
	@Test
	public void canChangeRoleToPlayerTest()
	{
		//create new Lobby
    	Lobby lobby = new Lobby();
    	
    	//create 3 lobbyUser with role Player to test
    	LobbyUser player1 = new LobbyUser("Turk", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER);
    	lobby.getPlayerList().add(player1);
    	LobbyUser player2 = new LobbyUser("J.D.", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER);
    	lobby.getPlayerList().add(player2);
    	LobbyUser player3 = new LobbyUser("Jan Itor", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER);
    	lobby.getPlayerList().add(player3);
    	
    	//create lobbyUser with role Spectator to test
    	LobbyUser spectator = new LobbyUser("Eugen", null, UserPermission.DEFAULT, LobbyUserKind.SPECTATOR);
    	lobby.getSpectatorList().add(spectator);
    	
    	//test if role can be changed
    	assertTrue(lobby.tryChangeUserKind(spectator), "Die Rolle wurde geändert.");
    	assertEquals(spectator.getRole(), LobbyUserKind.PLAYER);
     	assertTrue(lobby.getPlayerList().contains(spectator));
    	assertTrue(!lobby.getSpectatorList().contains(spectator)); 
	}
	
	/**
     * Tests if there is a LobbyUser with a certain name
     */
	@Test
    public void doesUserExistsTest()
    {
    	/**create new Lobby*/
    	Lobby testLobby = new Lobby();
    	
    	/**create a LobbyUser from type PLAYER and add him to the lobby*/
    	LobbyUser testPlayer = new LobbyUser("Kevin", null, UserPermission.DEFAULT, LobbyUserKind.PLAYER);
    	testLobby.getPlayerList().add(testPlayer);

    	/**create a LobbyUser from type SPECTATOR and add him to the lobby*/
    	LobbyUser testSpectator = new LobbyUser("Mike", null, UserPermission.DEFAULT, LobbyUserKind.SPECTATOR);
    	testLobby.getSpectatorList().add(testSpectator);
    	
    	/**assert-test-statements for player*/
    	assertTrue(testLobby.doesUserExists("Kevin"), "Es wurde kein LobbyUser mit diesem Namen gefunden!");
    	assertFalse(testLobby.doesUserExists("Anne"), "Es gibt einen User mit diesem Namen!");
    	
    	/**assert-test-statements for spectator*/
    	assertTrue(testLobby.doesUserExists("Mike"), "Es wurde kein LobbyUser mit diesem Namen gefunden!");
    	assertFalse(testLobby.doesUserExists("Lea"), "Es gibt einen User mit diesem Namen!");
    }
}