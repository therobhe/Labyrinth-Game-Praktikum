package labyrinth.registrationServer.logic;

import labyrinth.contracts.communication.dtos.requests.EmptyRequestDto;
import labyrinth.contracts.communication.dtos.responses.ClientRegisterResponseDto;
import labyrinth.contracts.communication.logic.LogicBase;
import labyrinth.registrationServer.IRegistrationServerConnection;
import labyrinth.registrationServer.RegistrationServerStateManager;

/**
 * Logic for a ClientRegister-Request
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final  class ClientRegisterLogic extends LogicBase<EmptyRequestDto>
{
	//Constructors
	
	/**
	 * ClientRegisterLogic constructor
	 * @param connection The Connection which called the logic
	 */
	public ClientRegisterLogic(IRegistrationServerConnection connection) { super(connection); }
	
	//Methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(EmptyRequestDto request) 
	{
		int userId = RegistrationServerStateManager.getUserIds().size();
		RegistrationServerStateManager.addUserId(userId);
		IRegistrationServerConnection.class.cast(getConnection()).setUserId(userId);
		getConnection().sendResponse(new ClientRegisterResponseDto(userId));
	}
}
