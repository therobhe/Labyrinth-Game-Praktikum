package labyrinth.contracts.communication.logic;

import labyrinth.contracts.communication.IServerConnection;
import labyrinth.contracts.communication.dtos.requests.RequestDtoBase;

/**
 * BaseClass for all Logic-classes
 * @param <RequestT> Type of the request the logic-class can handle
 * @author Lukas Reinhardt
 * @version 1.0
 */
public abstract class LogicBase<RequestT extends RequestDtoBase>
{
	//Properties
	
	private IServerConnection connection;
	/**
	 * Corresponding getter
	 * @return The Connection which called the logic
	 */
	protected IServerConnection getConnection() { return connection; }
	
	//Constructors
	
	/**
	 * Executes the logic
	 * @param request The request for which the logic should be executed
	 */
	public abstract void execute(RequestT request);
	
	//Methods
	
	/**
	 * LogicBase constructor
	 * @param connection The Connection which called the logic
	 */
	public LogicBase(IServerConnection connection) { this.connection = connection; }
}
