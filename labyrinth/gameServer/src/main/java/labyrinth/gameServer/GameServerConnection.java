package labyrinth.gameServer;

import java.net.Socket;
import java.util.function.Consumer;
import labyrinth.contracts.communication.*;
import labyrinth.contracts.communication.dtos.requests.*;
import labyrinth.contracts.communication.logic.LogicWrapper;
import labyrinth.contracts.entities.lobby.LobbyUser;
import labyrinth.gameServer.logic.*;

/**
 * Class for a Connection of a client to the GameServer
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class GameServerConnection extends ServerConnectionBase implements IGameServerConnection
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
	
	private LobbyUser user;
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LobbyUser getUser() { return user; }
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUser(LobbyUser value) { user = value; }
	
    //Consturctors

    /**
     * ServerConnection constructor
     * @param client The client connected to the server
     * @param closeHandler handler which is executed when closing the connection
     * @param ip IP-Adress of the client
     */
    public GameServerConnection(Socket client, Consumer<ServerConnectionBase> closeHandler, String ip) 
    { 
    	super(client, closeHandler, ip);  	
    	registerLogicClasses();
    }
    
    //Methods
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void closeConnection()
	{
		super.closeConnection();
		
		//Execute the client leave logic, when the connection was closed for some reason
		executeLogic(new EmptyRequestDto(MessageType.ClientLeave));
	}
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runCore() 
	{
		//Wait for ClientJoin: If no ClientJoin close connection 
		if(!waitForClientJoin())
		{
			closeConnection();
			return;
		}
		listen();
	}
    
    private boolean waitForClientJoin()
    {
    	//Read the next Request
    	RequestWrapper request = readRequest();
	    
	    //First request must be ClientJoin
	    if(request.getMessageType() != MessageType.ClientJoin)
	    	return false;
	    
	    //Execute the Logic for the client join
	    executeLogic(ClientJoinDto.class.cast(request.getContent()));	
	    
	    //If join was not successfull the connection is canceled
	    if(isInterrupted())
	    	return false;
	    return true;
    }
    
	private void registerLogicClasses()
	{
		registerLobbyLogicClasses();
		registerGameLogicClasses();
	}
	
	private void registerLobbyLogicClasses()
	{
		ClientJoinLogic clientJoinLogic = new ClientJoinLogic(this);
    	registerLogic(new LogicWrapper<ClientJoinDto>(MessageType.ClientJoin, ClientJoinDto.class, clientJoinLogic));
    	
		ChangeColorLogic changeColorLogic = new ChangeColorLogic(this);
		registerLogic(new LogicWrapper<ChangeColorDto>(MessageType.ChangeColor, ChangeColorDto.class, changeColorLogic));
		
		ChangeRoleLogic changeRoleLogic = new ChangeRoleLogic(this);
		registerLogic(new LogicWrapper<ChangeRoleDto>(MessageType.ChangeRole, ChangeRoleDto.class, changeRoleLogic));
		
		ClientLeaveLogic clientLeaveLogic = new ClientLeaveLogic(this);
		registerLogic(new LogicWrapper<EmptyRequestDto>(MessageType.ClientLeave, EmptyRequestDto.class, clientLeaveLogic));
		
		ClientReadyLogic clientReadyLogic = new ClientReadyLogic(this);
		registerLogic(new LogicWrapper<EmptyRequestDto>(MessageType.ClientReady, EmptyRequestDto.class, clientReadyLogic));
		
		ConfigurationLogic configurationLogic = new ConfigurationLogic(this);
		registerLogic(new LogicWrapper<ConfigurationDto>(MessageType.Configuration, ConfigurationDto.class, configurationLogic));
		
		StartGameLogic startGameLogic = new StartGameLogic(this);
		registerLogic(new LogicWrapper<EmptyRequestDto>(MessageType.StartGame, EmptyRequestDto.class, startGameLogic));
		
		UpdateLobbyLogic updateLobbyLogic = new UpdateLobbyLogic(this);
		registerLogic(new LogicWrapper<EmptyRequestDto>(MessageType.UpdateLobby, EmptyRequestDto.class, updateLobbyLogic));	
	}
	
	private void registerGameLogicClasses()
	{
		TurnUseBonusLogic turnUseBonusLogic = new TurnUseBonusLogic(this);
		registerLogic(new LogicWrapper<TurnUseBonusDto>(MessageType.TurnUseBonus, TurnUseBonusDto.class, turnUseBonusLogic));
		
		TurnShiftTilesLogic turnShiftTilesLogic = new TurnShiftTilesLogic(this);
		registerLogic(new LogicWrapper<TurnShiftTilesDto>(MessageType.TurnShiftTiles, TurnShiftTilesDto.class, turnShiftTilesLogic));
		
		TurnMovePlayerLogic turnMovePlayerLogic = new TurnMovePlayerLogic(this);
		registerLogic(new LogicWrapper<TurnMovePlayerDto>(MessageType.TurnMovePlayer, TurnMovePlayerDto.class, turnMovePlayerLogic));
	}
}