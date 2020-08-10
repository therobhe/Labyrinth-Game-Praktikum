package labyrinth.contracts.communication.dtos;

import java.util.ArrayList;
import java.util.List;

/**
 * Dto for a GameBoard
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class BoardDto 
{
	private List<TileDto> tiles = new ArrayList<TileDto>();
	/**
	 * Corresponding getter
	 * @return The tiles/field of the board
	 */
	public List<TileDto> getTiles() { return tiles; }
}
