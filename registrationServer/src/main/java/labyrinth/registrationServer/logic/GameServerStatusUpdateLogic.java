package labyrinth.registrationServer.logic;

import labyrinth.contracts.communication.dtos.requests.GameServerStatusUpdateDto;
import labyrinth.contracts.communication.logic.LogicBase;
import labyrinth.contracts.entities.lobby.GameServerEntry;
import labyrinth.registrationServer.IRegistrationServerConnection;

/**
 * Logic for a GameServerStatusUpdate-Request
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class GameServerStatusUpdateLogic extends LogicBase<GameServerStatusUpdateDto>
{
	//Properties

	/**
	 * GameServerStatusUpdateLogic constructor
	 * @param connection The Connection which called the logic
	 */
	public GameServerStatusUpdateLogic(IRegistrationServerConnection connection) { super(connection); }
	
	//Constructors

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(GameServerStatusUpdateDto request) 
	{
		System.out.printf("StatusUpdate");
		GameServerEntry server = IRegistrationServerConnection.class.cast(getConnection()).getServer();
		server.getMembers().clear();
		server.getMembers().addAll(request.getMembers());
		server.setServerName(request.getServerName());
		server.setState(request.getState());
		System.out.printf("StatusUpdate - End");
	}
}
