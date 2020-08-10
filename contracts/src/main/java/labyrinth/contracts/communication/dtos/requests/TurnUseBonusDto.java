package labyrinth.contracts.communication.dtos.requests;

import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.TargetCoordinatesDto;
import labyrinth.contracts.communication.dtos.TargetPlayerDto;
import labyrinth.contracts.entities.game.BonusKind;

/**
 * Dto for @see MessageType.TurnUseBonus
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class TurnUseBonusDto  extends RequestDtoBase
{
	//Properties
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageType getMessageType() { return MessageType.TurnUseBonus; } 
	
	private BonusKind type;
	/**
	 * Corresponding getter
	 * @return The Type of the bonus
	 */
	public BonusKind getType() { return type; }
	
	private TargetCoordinatesDto beam;
	/**
	 * Corresponding getter
	 * @return The target coordinates for the beam bonus
	 */
	public TargetCoordinatesDto getBeam() { return beam; }
	
	private TargetPlayerDto swap;
	/**
	 * Corresponding getter
	 * @return The target player for the swap bonus
	 */
	public TargetPlayerDto getSwap() { return swap; } 
	
	//Constructors
	
	/**
	 * TurnUseBonusDto constructor
	 * @param type The Type of the bonus
	 */
	public TurnUseBonusDto(BonusKind type) { this.type = type; }
	
	/**
	 * TurnUseBonusDto constructor
	 * @param beam The target coordinates for the beam bonus
	 */
	public TurnUseBonusDto(TargetCoordinatesDto beam)
	{
		type = BonusKind.BEAM;
		this.beam = beam;
	}
	
	/**
	 * TurnUseBonusDto constructor
	 * @param swap The target player for the swap bonus
	 */
	public TurnUseBonusDto(TargetPlayerDto swap)
	{
		type = BonusKind.SWAP;
		this.swap = swap;
	}
}
