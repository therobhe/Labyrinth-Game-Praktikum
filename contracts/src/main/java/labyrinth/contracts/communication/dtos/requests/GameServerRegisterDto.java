package labyrinth.contracts.communication.dtos.requests;

import labyrinth.contracts.communication.MessageType;

/**
 * Dto for @see MessageType.GameServerRegister
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class GameServerRegisterDto extends RequestDtoBase
{
	//Properties
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageType getMessageType() { return MessageType.GameServerRegister; }
	
	private String serverName;
	/**
	 * Corresponding getter
	 * @return The name of the server
	 */
	public String getServerName() { return serverName; }
	
	private int port;
	/**
	 * Corresponding getter
	 * @return The Port of the server
	 */
	public int getPort() { return port; }
	
	//Constructors
	
	/**
	 * GameServerRegisterDto constructor
	 * @param serverName Name of the GameServer
	 * @param port Port number of the GameServer
	 */
	public GameServerRegisterDto(String serverName, int port)
	{
		this.serverName = serverName;
		this.port = port;
	}
}
