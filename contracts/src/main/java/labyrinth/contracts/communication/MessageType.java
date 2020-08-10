package labyrinth.contracts.communication;

import labyrinth.contracts.communication.dtos.responses.*;
import labyrinth.contracts.communication.dtos.requests.*;
import labyrinth.contracts.entities.lobby.GameConfiguration;
import java.util.List;

/**
 * Enum containing all available message Types
 * @author Lukas Reinhardt
 * @version 1.0
 */
public enum MessageType 
{
	//Enum members
	
	//Client <-> GameServer
	
	/** Request/Response for changing the game configuration */
	Configuration(ConfigurationDto.class, GameConfiguration.class),
	/** Request/Response for using a bonus */
	TurnUseBonus(TurnUseBonusDto.class, SimpleSuccessResponseDto.class),
	/** Request/Response for shifting the fields on the board */
	TurnShiftTiles(TurnShiftTilesDto.class, SimpleSuccessResponseDto.class),
	/** Request/Response for moving the player */
	TurnMovePlayer(TurnMovePlayerDto.class, SimpleSuccessResponseDto.class),
	/** Request/Response for signaling that all clients are ready */
	AllClientsReady(null, null),
	/** Request/Response for changing the role of a player */
	ChangeRole(ChangeRoleDto.class, SimpleSuccessResponseDto.class),
	/** Request/Response for changing the color of a player */
	ChangeColor(ChangeColorDto.class, SimpleSuccessResponseDto.class),
	/** Request/Response for a client to leave the server */
	ClientLeave(null, null),
	/** Request/Response for a client to join the server */
	ClientJoin(ClientJoinDto.class, SuccessMessageResponseDto.class), 
	/** Request/Response for updating the lobby of a server */
	UpdateLobby(null, UpdateLobbyResponseDto.class),
	/** Request/Response for starting the game */
	StartGame(null, SimpleSuccessResponseDto.class),
	/** Request/Response for signaling that a client is ready for the game to start */
	ClientReady(null, null),
	/** Request/Response for signaling which players turn it is */
	CurrentPlayer(null, String.class),
	/** Request/Response for sending the current state of the game */
	GameState(null, GameStateResponseDto.class),
	/** Request/Response for sending the performed action of a player */
	PlayerAction(null, PlayerActionResponseDto.class),
	/** Request/Response for notifying that a player got a achievement */
	PropagateAchievement(null, PropagateAchievementResponseDto.class),
	/** Request/Response for signaling that the game is finished */
	EndGame(null, EndGameResponseDto.class),
	
	//Client <-> RegistrationServer
	
	/** Request/Response for a client to register at the registration server */
	ClientRegister(null, ClientRegisterResponseDto.class),
	/** Request/Response for getting the list of available game servers */
	PullGameServers(null, List.class),
	/** Request/Response for a client to unregister from the registration server */
	ClientUnregister(null, null),
	
	//GameServer <-> RegistrationServer
	
	/** Request/Response for a game server to register at the registration server */
	GameServerRegister(GameServerRegisterDto.class, SimpleSuccessResponseDto.class),
	/** Request/Response for a game server to unregister from the registration server */
	GameServerUnregister(null, null),
	/** Request/Response for sending an update of the state of the game server to the registration server */
	GameServerStatusUpdate(GameServerStatusUpdateDto.class, null),
	/** Request/Response for checking the userId of a user */
	CheckUID(CheckUserIdDto.class, SimpleSuccessResponseDto.class);
	
	//Properties
	
	private Class<? extends Object> responseContentType;
	/**
	 * Returns the corresponding type of the content of the response
	 * @return The corresponding type of the content of the response
	 */
	public Class<? extends Object> getResponseContentType() { return responseContentType; }
	
	private Class<? extends Object> requestContentType;
	/**
	 * Returns the corresponding type of the content of the request
	 * @return The corresponding type of the content of the request
	 */
	public Class<? extends Object> getRequestContentType() { return requestContentType; }
	
	//Constructors
			
	private MessageType(Class<? extends Object> requestType, Class<? extends Object> responseType) 
	{ 
		this.responseContentType = responseType; 
		this.requestContentType = requestType;
	}
}
