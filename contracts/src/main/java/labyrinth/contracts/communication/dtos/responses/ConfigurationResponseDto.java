package labyrinth.contracts.communication.dtos.responses;

import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.entities.lobby.GameConfiguration;

/**
 * Dto for a response @see MessageType.Configuration
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ConfigurationResponseDto extends ResponseDtoBase
{
	//Properties
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageType getMessageType() { return MessageType.Configuration; }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getContent() { return configuration; }
	
	private GameConfiguration configuration;
	/**
	 * Corresponding getter
	 * @return The actual configuration
	 */
	public GameConfiguration getConfiguration() { return configuration; }
	
	//Constructors
	
	/**
	 * ConfigurationDto constructor
	 * @param configuration The Configuration
	 */
	public ConfigurationResponseDto(GameConfiguration configuration) { this.configuration = configuration; }
}
