package labyrinth.registrationServer;

import java.net.Socket;
import java.util.function.Consumer;
import labyrinth.contracts.communication.*;
import labyrinth.contracts.communication.dtos.requests.*;
import labyrinth.contracts.communication.logic.LogicWrapper;
import labyrinth.contracts.entities.lobby.GameServerEntry;
import labyrinth.registrationServer.logic.*;

/**
 * Class for a Connection of a client or GameServer to the RegistrationServer
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class RegistrationServerConnection extends ServerConnectionBase implements IRegistrationServerConnection
{
	//Properties
	
	private int userId;
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getUserId() { return userId; }
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUserId(int value) { userId = value; }
	
	private GameServerEntry server;
	/**
	 * {@inheritDoc}
	 */
	@Override
	public GameServerEntry getServer() { return server; }
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setServer(GameServerEntry value) { server = value; }
	
	//Constructors
	
	/**
	 * RegistrationServerConnection constructor
	 * @param client The client connected to the server
     * @param closeHandler handler which is executed when closing the connection
     * @param ip IP-Address of the client
	 */
	public RegistrationServerConnection(Socket client, Consumer<ServerConnectionBase> closeHandler, String ip) 
	{
		super(client, closeHandler, ip);
		
		//Register the logic-classes for handling the requests
		registerLogicClasses();
	}
	
	//Methods
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runCore() 
	{
		//Wait for ClientJoin: If no ClientJoin close connection 
		if(!waitForRegisterRequest())
		{
			closeConnection();
			return;
		}
		listen();
	}
	    
	private boolean waitForRegisterRequest()
	{
		//Read the next request
		RequestWrapper request = readRequest();
		    
		//First request must be ClientRegister or GameServerRegister
		if(request.getMessageType() == MessageType.ClientRegister)
		{
			executeLogic(new EmptyRequestDto(MessageType.ClientRegister));
			return true;
		}	
		else if(request.getMessageType() == MessageType.GameServerRegister)
		{
			executeLogic(GameServerRegisterDto.class.cast(request.getContent()));
			return true;
		}
		return false;
	}
	
	private void registerLogicClasses()
	{
		CheckUserIdLogic checkUserIdLogic = new CheckUserIdLogic(this);
    	registerLogic(new LogicWrapper<CheckUserIdDto>(MessageType.CheckUID, CheckUserIdDto.class, checkUserIdLogic));
    	
		ClientRegisterLogic clientRegisterLogic = new ClientRegisterLogic(this);
    	registerLogic(new LogicWrapper<EmptyRequestDto>(MessageType.ClientRegister, 
    		EmptyRequestDto.class, clientRegisterLogic));
    	
		ClientUnregisterLogic clientUnregisterLogic = new ClientUnregisterLogic(this);
    	registerLogic(new LogicWrapper<EmptyRequestDto>(MessageType.ClientUnregister, 
    		EmptyRequestDto.class, clientUnregisterLogic));
    	
		GameServerRegisterLogic gameServerRegisterLogic = new GameServerRegisterLogic(this);
    	registerLogic(new LogicWrapper<GameServerRegisterDto>(MessageType.GameServerRegister, 
    		GameServerRegisterDto.class, gameServerRegisterLogic));
    	
		GameServerUnregisterLogic gameServerUnregisterLogic = new GameServerUnregisterLogic(this);
    	registerLogic(new LogicWrapper<EmptyRequestDto>(MessageType.GameServerUnregister, 
    		EmptyRequestDto.class, gameServerUnregisterLogic));
    	
    	GameServerStatusUpdateLogic gameServerStatusUpdateLogic = new GameServerStatusUpdateLogic(this);
    	registerLogic(new LogicWrapper<GameServerStatusUpdateDto>(MessageType.GameServerStatusUpdate, 
    		GameServerStatusUpdateDto.class, gameServerStatusUpdateLogic));
    	
    	PullGameServersLogic pullGameServerLogic = new PullGameServersLogic(this);
    	registerLogic(new LogicWrapper<EmptyRequestDto>(MessageType.PullGameServers, 
    		EmptyRequestDto.class, pullGameServerLogic));
	}
}
