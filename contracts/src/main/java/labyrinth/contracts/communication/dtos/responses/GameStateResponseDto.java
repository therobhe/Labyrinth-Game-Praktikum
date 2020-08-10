package labyrinth.contracts.communication.dtos.responses;

import java.util.ArrayList;
import java.util.List;
import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.BoardDto;
import labyrinth.contracts.communication.dtos.PlayerDto;

/**
 * Dto for a response to @see MessageType.GameState
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class GameStateResponseDto extends ResponseDtoBase
{
	//Properties
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageType getMessageType() { return MessageType.GameState; }
	
	private List<PlayerDto> players = new ArrayList<PlayerDto>();
	/**
	 * Corresponding getter
	 * @return The players of the game
	 */
	public List<PlayerDto> getPlayers() { return players; }
	
	private BoardDto board;
	/**
	 * Corresponding getter
	 * @return The GameBoard
	 */
	public BoardDto getBoard() { return board; }
	
	//Constructors
	
	/**
	 * GameStateResponseDto constructors
	 * @param board The GameBoard
	 */
	public GameStateResponseDto(BoardDto board) { this.board = board; }
}
