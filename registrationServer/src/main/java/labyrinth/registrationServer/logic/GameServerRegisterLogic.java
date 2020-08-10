package labyrinth.registrationServer.logic;

import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.requests.GameServerRegisterDto;
import labyrinth.contracts.communication.dtos.responses.SimpleSuccessResponseDto;
import labyrinth.contracts.communication.logic.LogicBase;
import labyrinth.contracts.entities.lobby.GameServerEntry;
import labyrinth.contracts.entities.lobby.GameServerStatus;
import labyrinth.registrationServer.IRegistrationServerConnection;
import labyrinth.registrationServer.RegistrationServerStateManager;

/**
 * Logic for a GameServerRegister-Request
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class GameServerRegisterLogic extends LogicBase<GameServerRegisterDto>
{
	//Constructors
	
	/**
	 * GameServerRegisterLogic constructor
	 * @param connection The Connection which called the logic
	 */
	public GameServerRegisterLogic(IRegistrationServerConnection connection) { super(connection); }

	//Methods
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(GameServerRegisterDto request) 
	{
		GameServerEntry server = new GameServerEntry(request.getServerName(), GameServerStatus.LOBBY, 
			getConnection().getIp(), request.getPort());	
		IRegistrationServerConnection.class.cast(getConnection()).setServer(server);
		RegistrationServerStateManager.addGameServer(server);
		getConnection().sendResponse(new SimpleSuccessResponseDto(MessageType.GameServerRegister, true));
	}
}
