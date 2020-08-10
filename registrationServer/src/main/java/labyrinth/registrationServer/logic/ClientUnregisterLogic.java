package labyrinth.registrationServer.logic;

import labyrinth.contracts.communication.dtos.requests.EmptyRequestDto;
import labyrinth.contracts.communication.logic.LogicBase;
import labyrinth.registrationServer.IRegistrationServerConnection;
import labyrinth.registrationServer.RegistrationServerStateManager;

/**
 * Logic for a ClientUnregister-Request
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ClientUnregisterLogic extends LogicBase<EmptyRequestDto>
{
	//Constructors
	
	/**
	 * ClientUnregisterLogic constructor
	 * @param connection The Connection which called the logic
	 */
	public ClientUnregisterLogic(IRegistrationServerConnection connection) { super(connection); }

	//Methods
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(EmptyRequestDto request) 
	{
		int userId = IRegistrationServerConnection.class.cast(getConnection()).getUserId();
		RegistrationServerStateManager.removeUserId(userId);	
		getConnection().cancel();
	}
}
