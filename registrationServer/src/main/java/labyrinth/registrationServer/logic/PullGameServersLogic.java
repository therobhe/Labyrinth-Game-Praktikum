package labyrinth.registrationServer.logic;

import labyrinth.contracts.communication.dtos.requests.EmptyRequestDto;
import labyrinth.contracts.communication.dtos.responses.PullGameServersResponseDto;
import labyrinth.contracts.communication.logic.LogicBase;
import labyrinth.registrationServer.IRegistrationServerConnection;
import labyrinth.registrationServer.RegistrationServerStateManager;

/**
 * Logic for a PullGameServers-Request
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class PullGameServersLogic extends LogicBase<EmptyRequestDto>
{
	//Constructors
	
	/**
	 * PullGameServersLogic constructor
	 * @param connection The Connection which called the logic
	 */
	public PullGameServersLogic(IRegistrationServerConnection connection) { super(connection); }
	
	//Methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(EmptyRequestDto request) 
	{
		PullGameServersResponseDto response = new PullGameServersResponseDto();
		response.getItems().addAll(RegistrationServerStateManager.getServers());
		getConnection().sendResponse(response);
	}
}
