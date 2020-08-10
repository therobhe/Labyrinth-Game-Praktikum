package labyrinth.contracts.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import labyrinth.contracts.communication.dtos.*;
import labyrinth.contracts.communication.dtos.requests.*;
import labyrinth.contracts.communication.dtos.responses.*;
import labyrinth.contracts.entities.*;
import labyrinth.contracts.entities.game.BonusKind;
import labyrinth.contracts.entities.lobby.BoniConfiguration;
import labyrinth.contracts.entities.lobby.GameConfiguration;
import labyrinth.contracts.entities.lobby.GameServerEntry;
import labyrinth.contracts.entities.lobby.GameServerStatus;
import labyrinth.contracts.entities.lobby.LobbyUser;
import labyrinth.contracts.entities.lobby.LobbyUserKind;
import labyrinth.contracts.entities.lobby.User;
import labyrinth.contracts.entities.lobby.UserPermission;

/**
 * TestClass for @see RequestWrapper
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ApiComplianceTests 
{
	/**
	 * Tests that the json format of the requests from client to the registration server conform to the defined API
	 */
	@Test
	public void ClientRegistrationServerRequestApiTest()
	{
		RequestWrapper request = new RequestWrapper(new EmptyRequestDto(MessageType.ClientRegister));
		assertEquals("{\"type\":\"ClientRegister\",\"request\":{\"ClientRegister\":null}}", request.toJson());
		
		request = new RequestWrapper(new EmptyRequestDto(MessageType.ClientUnregister));
		assertEquals("{\"type\":\"ClientUnregister\",\"request\":{\"ClientUnregister\":null}}", request.toJson());
		
		request = new RequestWrapper(new EmptyRequestDto(MessageType.PullGameServers));
		assertEquals("{\"type\":\"PullGameServers\",\"request\":{\"PullGameServers\":null}}", request.toJson());
	}
	
	/**
	 * Tests that the json format of the responses from  the registration server to the client conform to the defined API
	 */
	@Test
	public void ClientRegistrationServerResponseApiTest()
	{
		ResponseWrapper response = new ResponseWrapper(new ClientRegisterResponseDto(100));
		String expected = "{\"type\":\"ClientRegister\",\"response\":{\"ClientRegister\":{\"userID\":100}}}";
		assertEquals(expected, response.toJson());
		
		expected = "{\"type\":\"ClientUnregister\",\"response\":{\"ClientUnregister\":null}}";
		response = new ResponseWrapper(new EmptyResponseDto(MessageType.ClientUnregister));
		assertEquals(expected, response.toJson());
		
		expected = "{\"type\":\"PullGameServers\",\"response\":{\"PullGameServers\":[{\"serverName\":\"server1\",\"ip\":\"12.12\","
			+ "\"port\":1000,\"state\":\"lobby\",\"members\":[{\"name\":\"user1\",\"role\":\"player\"}]},"
			+ "{\"serverName\":\"server2\",\"ip\":\"13.13\",\"port\":1001,\"state\":\"lobby\",\"members\":"
			+ "[{\"name\":\"user2\",\"role\":\"spectator\"}]}]}}";
		PullGameServersResponseDto dto = new PullGameServersResponseDto();
		GameServerEntry server1 = new GameServerEntry("server1", GameServerStatus.LOBBY, "12.12", 1000);
		GameServerEntry server2 = new GameServerEntry("server2", GameServerStatus.LOBBY, "13.13", 1001);
		dto.getItems().add(server1);
		dto.getItems().add(server2);
		User member1 = new User("user1", LobbyUserKind.PLAYER);
		User member2 = new User("user2", LobbyUserKind.SPECTATOR);
		server1.getMembers().add(member1);
		server2.getMembers().add(member2);	
		response = new ResponseWrapper(dto);
		assertEquals(expected, response.toJson());
	}
	
	/**
	 * Tests that the json format of the requests from game server to the registration server conform to the defined API
	 */
	@Test
	public void GameServerRegistrationServerRequestApiTest()
	{
		RequestWrapper request = new RequestWrapper(new GameServerRegisterDto("Testserver", 1000));
		String expected = "{\"type\":\"GameServerRegister\",\"request\":{\"GameServerRegister\":"
			+ "{\"serverName\":\"Testserver\",\"port\":1000}}}";
		assertEquals(expected, request.toJson());
		
		expected = "{\"type\":\"GameServerUnregister\",\"request\":{\"GameServerUnregister\":null}}";
		request = new RequestWrapper(new EmptyRequestDto(MessageType.GameServerUnregister));
		assertEquals(expected, request.toJson());
		
		expected = "{\"type\":\"CheckUID\",\"request\":{\"CheckUID\":{\"userID\":42}}}";
		request = new RequestWrapper(new CheckUserIdDto(42));
		assertEquals(expected, request.toJson());
		
		expected = "{\"type\":\"GameServerStatusUpdate\",\"request\":{\"GameServerStatusUpdate\":{\"serverName\":\"Testserver\","
				+ "\"state\":\"lobby\",\"members\":[{\"name\":\"user1\",\"role\":\"player\"},{\"name\":\"user2\",\"role\":\"spectator\"}]}}}";
		GameServerStatusUpdateDto dto = new GameServerStatusUpdateDto("Testserver", GameServerStatus.LOBBY);
		dto.getMembers().add(new User("user1", LobbyUserKind.PLAYER));
		dto.getMembers().add(new User("user2", LobbyUserKind.SPECTATOR));
		request = new RequestWrapper(dto);
		assertEquals(expected, request.toJson());
	}
	
	/**
	 * Tests that the json format of the responses from the registration server to the game server conform to the defined API
	 */
	@Test
	public void GameServerRegistrationServerResponseApiTest()
	{
		ResponseWrapper response = new ResponseWrapper(new SimpleSuccessResponseDto(MessageType.GameServerRegister, true));
		String expected = "{\"type\":\"GameServerRegister\",\"response\":{\"GameServerRegister\":{\"success\":true}}}";
		assertEquals(expected, response.toJson());
		
		expected = "{\"type\":\"GameServerUnregister\",\"response\":{\"GameServerUnregister\":null}}";
		response = new ResponseWrapper(new EmptyResponseDto(MessageType.GameServerUnregister));
		assertEquals(expected, response.toJson());
		
		expected = "{\"type\":\"CheckUID\",\"response\":{\"CheckUID\":{\"success\":false}}}";
		response = new ResponseWrapper(new SimpleSuccessResponseDto(MessageType.CheckUID, false));
		assertEquals(expected, response.toJson());
	}
	
	/**
	 * Tests that the json format of the requests from client to the game server conform to the defined API
	 */
	@Test
	public void ClientGameServerRequestApiTest()
	{
		String expected = "{\"type\":\"ClientJoin\",\"request\":{\"ClientJoin\":{\"name\":\"user\",\"userID\":12}}}";
		RequestWrapper request = new RequestWrapper(new ClientJoinDto("user", 12));
		assertEquals(expected, request.toJson());
		
		expected = "{\"type\":\"ChangeColor\",\"request\":{\"ChangeColor\":{\"color\":\"test\"}}}";
		request = new RequestWrapper(new ChangeColorDto("test"));
		assertEquals(expected, request.toJson());
		
		expected = "{\"type\":\"ClientLeave\",\"request\":{\"ClientLeave\":null}}";
		request = new RequestWrapper(new EmptyRequestDto(MessageType.ClientLeave));
		assertEquals(expected, request.toJson());
		
		expected = "{\"type\":\"UpdateLobby\",\"request\":{\"UpdateLobby\":null}}";
		request = new RequestWrapper(new EmptyRequestDto(MessageType.UpdateLobby));
		assertEquals(expected, request.toJson());
		
		expected = "{\"type\":\"StartGame\",\"request\":{\"StartGame\":null}}";
		request = new RequestWrapper(new EmptyRequestDto(MessageType.StartGame));
		assertEquals(expected, request.toJson());
		
		expected = "{\"type\":\"ClientReady\",\"request\":{\"ClientReady\":null}}";
		request = new RequestWrapper(new EmptyRequestDto(MessageType.ClientReady));
		assertEquals(expected, request.toJson());
		
		expected = "{\"type\":\"TurnShiftTiles\",\"request\":{\"TurnShiftTiles\":{\"slot\":2,\"tileType\":4}}}";
		request = new RequestWrapper(new TurnShiftTilesDto(2, 4));
		assertEquals(expected, request.toJson());
		
		expected = "{\"type\":\"TurnMovePlayer\",\"request\":{\"TurnMovePlayer\":{\"targetX\":2,\"targetY\":4}}}";
		request = new RequestWrapper(new TurnMovePlayerDto(2, 4));
		assertEquals(expected, request.toJson());
	}
	
	/**
	 * Tests that the json format of the Configuration request conforms to the defined API
	 */
	@Test
	public void ClientGameServerConfigurationRequestApiTest()
	{
		String expected = "{\"type\":\"Configuration\",\"request\":{\"Configuration\":{\"serverName\":\"Testserver\","
			+ "\"size\":{\"x\":2,\"y\":2},\"treasureCount\":12,\"boni\":{\"beam\":true,\"shiftSolid\":true,\"swap\":true,"
			+ "\"shiftTwice\":true},\"boniProbability\":0.5,\"gameLengthLimit\":11,\"turnLengthLimit\":12,"
			+ "\"admin\":\"admin\"}}}";	
		GameConfiguration configuration = new GameConfiguration("Testserver", new Coordinates(2, 2), 12, new BoniConfiguration(),
			0.5, 11, 12, "admin");
		RequestWrapper request = new RequestWrapper(new ConfigurationDto(configuration));
		assertEquals(expected, request.toJson());
	}
	
	/**
	 * Tests that the json format of the TurnUseBonus request conforms to the defined API
	 */
	@Test
	public void ClientGameServerTurnUseBonusApiTest()
	{
		String expected = "{\"type\":\"TurnUseBonus\",\"request\":{\"TurnUseBonus\":{\"type\":\"beam\",\"beam\":{\"targetX\":4"
			+ ",\"targetY\":7}}}}";
		RequestWrapper request = new RequestWrapper(new TurnUseBonusDto(new TargetCoordinatesDto(4, 7)));
		assertEquals(expected, request.toJson());
		
		expected = "{\"type\":\"TurnUseBonus\",\"request\":{\"TurnUseBonus\":{\"type\":\"shiftSolid\"}}}";
		request = new RequestWrapper(new TurnUseBonusDto(BonusKind.SHIFT_SOLID));
		assertEquals(expected, request.toJson());
		
		expected = "{\"type\":\"TurnUseBonus\",\"request\":{\"TurnUseBonus\":{\"type\":\"swap\",\"swap\":"
			+ "{\"targetPlayer\":\"player\"}}}}";
		request = new RequestWrapper(new TurnUseBonusDto(new TargetPlayerDto("player")));
		assertEquals(expected, request.toJson());
		
		expected = "{\"type\":\"TurnUseBonus\",\"request\":{\"TurnUseBonus\":{\"type\":\"shiftTwice\"}}}";
		request = new RequestWrapper(new TurnUseBonusDto(BonusKind.SHIFT_TWICE));
		assertEquals(expected, request.toJson());
	}
	
	/**
	 * Tests that the json format of the responses from the game server to the client conform to the defined API
	 */
	@Test
	public void ClientGameServerResponseApiTest()
	{
		String expected = "{\"type\":\"AllClientsReady\",\"response\":{\"AllClientsReady\":null}}";
		ResponseWrapper response = new ResponseWrapper(new EmptyResponseDto(MessageType.AllClientsReady));
		assertEquals(expected, response.toJson());
		
		expected = "{\"type\":\"ClientJoin\",\"response\":{\"ClientJoin\":{\"message\":\"test\",\"success\":true}}}";
		response = new ResponseWrapper(new SuccessMessageResponseDto(MessageType.ClientJoin, true, "test"));
		assertEquals(expected, response.toJson());
		
		expected = "{\"type\":\"ChangeRole\",\"response\":{\"ChangeRole\":{\"success\":true}}}";
		response = new ResponseWrapper(new SimpleSuccessResponseDto(MessageType.ChangeRole, true));
		assertEquals(expected, response.toJson());
		
		expected = "{\"type\":\"ChangeColor\",\"response\":{\"ChangeColor\":{\"success\":true}}}";
		response = new ResponseWrapper(new SimpleSuccessResponseDto(MessageType.ChangeColor, true));
		assertEquals(expected, response.toJson());
		
		expected = "{\"type\":\"CurrentPlayer\",\"response\":{\"CurrentPlayer\":\"player\"}}";
		response = new ResponseWrapper(new CurrentPlayerResponseDto("player"));
		assertEquals(expected, response.toJson());
		
		expected = "{\"type\":\"TurnUseBonus\",\"response\":{\"TurnUseBonus\":{\"success\":true}}}";
		response = new ResponseWrapper(new SimpleSuccessResponseDto(MessageType.TurnUseBonus, true));
		assertEquals(expected, response.toJson());
		
		expected = "{\"type\":\"TurnShiftTiles\",\"response\":{\"TurnShiftTiles\":{\"success\":true}}}";
		response = new ResponseWrapper(new SimpleSuccessResponseDto(MessageType.TurnShiftTiles, true));
		assertEquals(expected, response.toJson());
		
		expected = "{\"type\":\"TurnMovePlayer\",\"response\":{\"TurnMovePlayer\":{\"success\":true}}}";
		response = new ResponseWrapper(new SimpleSuccessResponseDto(MessageType.TurnMovePlayer, true));
		assertEquals(expected, response.toJson());
		
		expected = "{\"type\":\"PropagateAchievement\",\"response\":{\"PropagateAchievement\""
			+ ":{\"player\":\"player\",\"achievement\":\"Testachievement\",\"description\":\"blabla\"}}}";
		response = new ResponseWrapper(new PropagateAchievementResponseDto("player", "Testachievement", "blabla"));
		assertEquals(expected, response.toJson());
	}
	
	/**
	 * Tests that the json format of the UpdateLobby response conforms to the defined API
	 */
	@Test
	public void ClientGameServerUpdateLobbyResponseApiTest()
	{
		String expected = "{\"type\":\"UpdateLobby\",\"response\":{\"UpdateLobby\":{\"members\":[{\"color\":\"blue\","
			+ "\"permission\":\"default\",\"name\":\"player\",\"role\":\"player\"},"
			+ "{\"color\":\"blue\",\"permission\":\"admin\",\"name\":\"spectator\",\"role\":\"spectator\"}]}}}";
		UpdateLobbyResponseDto updateLobby = new UpdateLobbyResponseDto();
		LobbyUser member1 = new LobbyUser("player", "blue", UserPermission.DEFAULT, LobbyUserKind.PLAYER);
		LobbyUser member2 = new LobbyUser("spectator", "blue", UserPermission.ADMIN, LobbyUserKind.SPECTATOR);
		updateLobby.getMembers().add(member1);
		updateLobby.getMembers().add(member2);
		ResponseWrapper response = new ResponseWrapper(updateLobby);
		assertEquals(expected, response.toJson());
	}
	
	/**
	 * Tests that the json format of the EndGame response conforms to the defined API
	 */
	@Test
	public void ClientGameServerEndGameResponseApiTest()
	{
		String expected = "{\"type\":\"EndGame\",\"response\":{\"EndGame\":{\"players\":[{\"name\":\"player1\",\"score\":12,"
			+ "\"disconnected\":false},{\"name\":\"player2\",\"score\":12,\"disconnected\":true}]}}}";
		EndGameResponseDto endGame = new EndGameResponseDto();
		PlayerDtoBase player1 = new PlayerDtoBase("player1", 12, false);
		PlayerDtoBase player2 = new PlayerDtoBase("player2", 12, true);
		endGame.getPlayers().add(player1);
		endGame.getPlayers().add(player2);
		ResponseWrapper response = new ResponseWrapper(endGame);
		assertEquals(expected, response.toJson());
	}
	
	/**
	 * Tests that the json format of the Configuration response conforms to the defined API
	 */
	@Test
	public void ClientGameServerConfigurationResponseApiTest()
	{
		String expected = "{\"type\":\"Configuration\",\"response\":{\"Configuration\":{\"serverName\":\"Testserver\","
			+ "\"size\":{\"x\":2,\"y\":2},\"treasureCount\":12,\"boni\":{\"beam\":true,\"shiftSolid\":true,\"swap\":true,"
			+ "\"shiftTwice\":true},\"boniProbability\":0.5,\"gameLengthLimit\":11,\"turnLengthLimit\":12,"
			+ "\"admin\":\"admin\"}}}";
		GameConfiguration configuration = new GameConfiguration("Testserver", new Coordinates(2, 2), 12, new BoniConfiguration(), 
				0.5, 11, 12, "admin");
		ResponseWrapper response = new ResponseWrapper(new ConfigurationResponseDto(configuration));
		assertEquals(expected, response.toJson());
	}
	
	/**
	 * Tests that the json format of the PlayerAction response conforms to the defined API
	 */
	@Test
	public void ClientGameServerPlayerActionResponseApiTest()
	{
		String expected = "{\"type\":\"PlayerAction\",\"response\":{\"PlayerAction\":{\"player\":\"player\",\"TurnUseBonus\":"
			+ "{\"type\":\"shiftTwice\"}}}}";
		ResponseWrapper response = new ResponseWrapper(new PlayerActionResponseDto("player", 
			new TurnUseBonusDto(BonusKind.SHIFT_TWICE)));
		assertEquals(expected, response.toJson());
		
		expected = "{\"type\":\"PlayerAction\",\"response\":{\"PlayerAction\":{\"player\":\"player\",\"TurnShiftTiles\":"
			+ "{\"slot\":2,\"tileType\":4}}}}";
		response = new ResponseWrapper(new PlayerActionResponseDto("player", new TurnShiftTilesDto(2, 4)));
		assertEquals(expected, response.toJson());
		
		expected = "{\"type\":\"PlayerAction\",\"response\":{\"PlayerAction\":{\"player\":\"player\",\"TurnMovePlayer\":"
			+ "{\"targetX\":2,\"targetY\":4}}}}";
		response = new ResponseWrapper(new PlayerActionResponseDto("player", new TurnMovePlayerDto(2, 4)));
		assertEquals(expected, response.toJson());
	}
	
	/**
	 * Tests that the json format of the GameState response conforms to the defined API
	 */
	@Test
	public void ClientGameServerGameStateResponseApiTest()
	{
		String expected = "{\"type\":\"GameState\",\"response\":{\"GameState\":{\"players\":[{\"color\":\"green\","
			+ "\"treasures\":{\"current\":1,\"remaining\":2},\"boni\":{\"beam\":1,\"swap\":2,\"shiftSolid\":3,\"shiftTwice\":4},"
			+ "\"name\":\"player1\",\"score\":100,\"disconnected\":false}],\"board\":{\"tiles\":["
			+ "{\"x\":0,\"y\":0,\"type\":1,\"boni\":\"beam\"},{\"x\":0,\"y\":1,\"type\":2,\"player\":\"player\"},"
			+ "{\"x\":1,\"y\":0,\"type\":3,\"playerBase\":\"player\"},{\"x\":1,\"y\":1,\"type\":4,\"treasure\":1}]}}}}";
		
		BoardDto board = new BoardDto();
		TileDto tile1 = new TileDto(0, 0, 1);
		tile1.setBoni(BonusKind.BEAM);
		TileDto tile2 = new TileDto(0, 1, 2);
		tile2.setPlayer("player");
		TileDto tile3 = new TileDto(1, 0, 3);
		tile3.setPlayerBase("player");
		TileDto tile4 = new TileDto(1, 1, 4);
		tile4.setTreasure(1);
		board.getTiles().add(tile1);
		board.getTiles().add(tile2);
		board.getTiles().add(tile3);
		board.getTiles().add(tile4);
		
		GameStateResponseDto gameState = new GameStateResponseDto(board);
		PlayerDto player1 = new PlayerDto("player1", "green", 100, false, new PlayerTreasuresDto(1, 2), 
			new PlayerBoniDto(1, 2, 3, 4));
		gameState.getPlayers().add(player1);		
		
		ResponseWrapper response = new ResponseWrapper(gameState);
		assertEquals(expected, response.toJson());
	}
}
