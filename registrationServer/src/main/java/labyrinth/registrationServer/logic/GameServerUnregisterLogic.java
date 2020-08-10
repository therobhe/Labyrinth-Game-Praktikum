package labyrinth.registrationServer.logic;

import labyrinth.contracts.communication.dtos.requests.EmptyRequestDto;
import labyrinth.contracts.communication.logic.LogicBase;
import labyrinth.contracts.entities.lobby.GameServerEntry;
import labyrinth.registrationServer.IRegistrationServerConnection;
import labyrinth.registrationServer.RegistrationServerStateManager;

/**
 * Logic for a GameServerUnregister-Request
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class GameServerUnregisterLogic extends LogicBase<EmptyRequestDto>
{
	//Constructors
	
	/**
	 * GameServerUnregisterLogic constructor
	 * @param connection The Connection which called the logic
	 */
	public GameServerUnregisterLogic(IRegistrationServerConnection connection) { super(connection); }

	//Methods
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(EmptyRequestDto request) 
	{
		GameServerEntry server = IRegistrationServerConnection.class.cast(getConnection()).getServer();
		RegistrationServerStateManager.removeGameServer(server);
		getConnection().cancel();
	}
}
