package labyrinth.contracts.communication.dtos.requests;

import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.entities.lobby.LobbyUserKind;

/**
 * Dto for @see MessageType.ChangeRole
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ChangeRoleDto extends RequestDtoBase
{
	//Properties
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageType getMessageType() { return MessageType.ChangeRole; }
	
	private LobbyUserKind role;
	/**
	 * Corresponding getter
	 * @return The target role
	 */
	public LobbyUserKind getRole() { return role; }
	
	//Methods
	
	/**
	 * ChangeRoleDto constructor
	 * @param role The target role
	 */
	public ChangeRoleDto(LobbyUserKind role) { this.role = role; }
}
