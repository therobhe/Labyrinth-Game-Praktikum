package labyrinth.gameServer;

import java.util.List;
import java.util.stream.Collectors;
import labyrinth.contracts.communication.IServerConnection;
import labyrinth.contracts.communication.ISocketClient;
import labyrinth.contracts.communication.ISocketServer;
import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.requests.EmptyRequestDto;
import labyrinth.contracts.communication.dtos.requests.GameServerRegisterDto;
import labyrinth.contracts.communication.dtos.responses.ResponseDtoBase;
import labyrinth.contracts.communication.dtos.responses.SimpleSuccessResponseDto;
import labyrinth.contracts.communication.listeners.ActionListener;
import labyrinth.contracts.entities.lobby.GameServerEntry;
import labyrinth.gameServer.stateManagers.GameBoardStateManager;
import labyrinth.gameServer.stateManagers.GameConfigurationStateManager;
import labyrinth.gameServer.stateManagers.GameServerEntryStateManager;
import labyrinth.gameServer.stateManagers.LobbyStateManager;

/**
 * Class for a GameServer
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class GameServer extends Thread
{
	//Properties
	
	private String serverName;
	
	private ISocketServer<? extends IGameServerConnection> server;	
	/**
	 * Corresponding getter
	 * @return The actual Server
	 */
	public ISocketServer<? extends IGameServerConnection> getServer() { return server; }
	
	private ISocketClient registrationServerClient;
	/**
	 * Corresponding getter
	 * @return The client for the RegistrationServer
	 */
	public ISocketClient getRegistrationServerClient() { return registrationServerClient; }
	
	//Constructors
	
	/**
	 * GameServer constructor
	 * @param serverName Name of the server
	 * @param registrationServerClient The client for the RegistrationServer
	 * @param server The actual server
	 */
	public GameServer(String serverName, ISocketClient registrationServerClient, 
		ISocketServer<? extends IGameServerConnection> server) 
	{ 
		this.serverName = serverName;
		this.server = server;
		this.registrationServerClient = registrationServerClient;
		initializeStateManagers(serverName);
	}
	
	//Methods
	
	/**
	 * Commits the current state of the game
	 */
	public void commitCurrentGameState()
	{ 
		List<IGameServerConnection> connections = server.getConnections().stream()
			.map(connection -> (IGameServerConnection)connection).collect(Collectors.toList());
		GameBoardStateManager.commitCurrentGameState(connections); 
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run()
	{
		registerServer(serverName);
		
		//start the actual server
		server.start();
	}
	
	/**
	 * Broadcasts the response to all clients
	 * @param <ResponseT> The response type
	 * @param response The response to broadcast
	 */
	public <ResponseT extends ResponseDtoBase> void broadcastResponse(ResponseT response) { server.broadcast(response); }
	
	/**
	 * Broadcasts the response to all clients except the excluded one
	 * @param <ResponseT> The response type
	 * @param response The response to broadcast
	 * @param excluded The client to which the message shouldn't be send to
	 */
	public <ResponseT extends ResponseDtoBase> void broadcastResponse(ResponseT response, IServerConnection excluded)
	{ server.broadcast(response, excluded); }
	
	/**
	 * Closes the server
	 */
	public void close()
	{
		registrationServerClient.sendRequest(new EmptyRequestDto(MessageType.GameServerUnregister));
		try 
		{
			server.close();
			registrationServerClient.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	private void initializeStateManagers(String serverName)
	{
		GameServerEntryStateManager.initialize(new GameServerEntry(serverName));
		LobbyStateManager.initialize();
		GameConfigurationStateManager.initialize();
	}
	
	private void registerServer(String serverName)
	{
		try
		{
			registrationServerClient.registerListener(new ActionListener(MessageType.GameServerRegister, 
				(result) -> onRegistrationAccepted(result)));
			registrationServerClient.sendRequest(new GameServerRegisterDto(serverName, 1200));
		}
		catch(Exception e)
		{
			close();
		}
	}
	
	private void onRegistrationAccepted(Object result)
	{
		SimpleSuccessResponseDto reply = SimpleSuccessResponseDto.class.cast(result);
		
		//Don't start server if registration was not successful
		if(!reply.getSuccess())
		{
			close();
			return;
		}
	}
}
