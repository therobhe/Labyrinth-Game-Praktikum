package labyrinth.contracts.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import labyrinth.contracts.entities.game.PlayerMovementManager;
import labyrinth.contracts.entities.game.Tile;

/**
 * Testclass for @see PlayerMovementManager
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class PlayerMovementManagerTests 
{
	/**
	 * Tests @see PlayerMovementManager.getReachableCoordinatesTest
	 */
	@Test
	public void getReachableCoordinatesTest()
	{
		Tile[][] board = new Tile[3][3];
		board[0][0] = new Tile(0);
		board[1][0] = new Tile(8); 
		board[2][0] = new Tile(6);
		board[0][1] = new Tile(7);
		board[1][1] = new Tile(8); 
		board[2][1] = new Tile(2);
		board[0][1] = new Tile(7);
		board[1][1] = new Tile(8); 
		board[2][1] = new Tile(2);
		
		List<Coordinates> coordinates = PlayerMovementManager.getReachableCoordinates(new Coordinates(0, 0), board, 3);
		assertEquals(6, coordinates.size());
		assertTrue(coordinates.stream().anyMatch(x -> x.getX() == 0 && x.getY() == 0));
		assertTrue(coordinates.stream().anyMatch(x -> x.getX() == 1 && x.getY() == 0));
		assertTrue(coordinates.stream().anyMatch(x -> x.getX() == 2 && x.getY() == 0));
		assertTrue(coordinates.stream().anyMatch(x -> x.getX() == 0 && x.getY() == 1));
		assertTrue(coordinates.stream().anyMatch(x -> x.getX() == 1 && x.getY() == 1));
		assertTrue(coordinates.stream().anyMatch(x -> x.getX() == 2 && x.getY() == 1));
	}
}
