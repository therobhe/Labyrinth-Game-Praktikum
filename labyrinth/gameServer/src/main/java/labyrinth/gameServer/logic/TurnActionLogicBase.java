package labyrinth.gameServer.logic;

import labyrinth.contracts.communication.dtos.requests.RequestDtoBase;
import labyrinth.contracts.communication.logic.LogicBase;
import labyrinth.contracts.entities.game.GameBoard;
import labyrinth.contracts.entities.game.Player;
import labyrinth.gameServer.IGameServerConnection;

/**
 * Base-class for all Logic classes for turn actions
 * @author Lukas Reinhardt
 * @version 1.0
 */
public abstract class TurnActionLogicBase<RequestT extends RequestDtoBase> extends LogicBase<RequestT> 
{
	//Constructors

	/**
	 * Constructor for TurnActionLogicBase
	 * @param connection The Connection which called the logic
	 */
	public TurnActionLogicBase(IGameServerConnection connection) { super(connection); }
	
	//Methods
	
	/**
	 * Returns the player for the current connection
	 * @param board The GameBoard
	 */
	protected Player getCurrentPlayer(GameBoard board)
	{
		String playerName = IGameServerConnection.class.cast(getConnection()).getUser().getName();
		return board.getPlayers().stream().filter(player -> player.getPlayerName() == playerName).findFirst().get();
	}
}
