package labyrinth.registrationServer.logic;

import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.requests.CheckUserIdDto;
import labyrinth.contracts.communication.dtos.responses.SimpleSuccessResponseDto;
import labyrinth.contracts.communication.logic.LogicBase;
import labyrinth.registrationServer.IRegistrationServerConnection;
import labyrinth.registrationServer.RegistrationServerStateManager;

/**
 * Logic for a CheckUserId-Request
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class CheckUserIdLogic extends LogicBase<CheckUserIdDto>
{
	//Constructors
	
	/**
	 * CheckUserIdLogic constructor
	 * @param connection The Connection which called the logic
	 */
	public CheckUserIdLogic(IRegistrationServerConnection connection) { super(connection); }
	
	//Methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(CheckUserIdDto request) 
	{
		boolean userExists = RegistrationServerStateManager.containsUserId(request.getUserID());
		getConnection().sendResponse(new SimpleSuccessResponseDto(MessageType.CheckUID, userExists));
	}
}
