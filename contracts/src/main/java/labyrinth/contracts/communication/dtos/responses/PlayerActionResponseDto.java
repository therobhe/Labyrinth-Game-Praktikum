package labyrinth.contracts.communication.dtos.responses;

import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.requests.TurnMovePlayerDto;
import labyrinth.contracts.communication.dtos.requests.TurnShiftTilesDto;
import labyrinth.contracts.communication.dtos.requests.TurnUseBonusDto;

/**
 * Dto for a response to @see MessageType.PlayerAction
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class PlayerActionResponseDto extends ResponseDtoBase
{
	//Properties
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageType getMessageType() { return MessageType.PlayerAction; }
	
	private String player;
	/**
	 * Corresponding getter
	 * @return The name of the player
	 */
	public String getPlayer() { return player; }
	
	private TurnUseBonusDto TurnUseBonus;
	/**
	 * Corresponding getter
	 * @return Data if action was TurnUseBonus
	 */
	public TurnUseBonusDto getTurnUseBonus() { return TurnUseBonus; }
	
	private TurnShiftTilesDto TurnShiftTiles;
	/**
	 * Corresponding getter
	 * @return Data if the action was TurnShiftTiles
	 */
	public TurnShiftTilesDto getTurnShiftTiles() { return TurnShiftTiles; }
	
	private TurnMovePlayerDto TurnMovePlayer;
	/**
	 * Corresponding getter
	 * @return Data if the action was TurnMovePlayer
	 */
	public TurnMovePlayerDto getTurnMovePlayer() { return TurnMovePlayer; }
	
	//Constructors
	
	/**
	 * PlayerActionResponseDto constructor
	 * @param player Name of the player
	 * @param TurnUseBonus Data if action was TurnUseBonus
	 */
	public PlayerActionResponseDto(String player, TurnUseBonusDto TurnUseBonus)
	{
		this.player = player;
		this.TurnUseBonus = TurnUseBonus;
	}
	
	/**
	 * PlayerActionResponseDto constructor
	 * @param player Name of the player
	 * @param TurnShiftTiles Data if action was TurnShiftTiles
	 */
	public PlayerActionResponseDto(String player, TurnShiftTilesDto TurnShiftTiles)
	{
		this.player = player;
		this.TurnShiftTiles = TurnShiftTiles;
	}
	
	/**
	 * PlayerActionResponseDto constructor
	 * @param player Name of the player
	 * @param TurnMovePlayer Data if action was TurnMovePlayer
	 */
	public PlayerActionResponseDto(String player, TurnMovePlayerDto TurnMovePlayer)
	{
		this.player = player;
		this.TurnMovePlayer = TurnMovePlayer;
	}
}
