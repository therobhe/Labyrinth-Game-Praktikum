package client.ui.ai;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import labyrinth.client.ui.ai.ShiftingResult;
import labyrinth.contracts.communication.dtos.PlayerBoniDto;
import labyrinth.contracts.communication.dtos.PlayerDto;
import labyrinth.contracts.communication.dtos.PlayerTreasuresDto;
import labyrinth.contracts.entities.Coordinates;
import labyrinth.contracts.entities.game.Player;
import labyrinth.contracts.entities.game.Tile;

/**
 * TestClass for @see ShiftingResult
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ShiftingResultTests 
{
	/**
	* Test that the the best coordinates for the next move are found
	*/
	@Test
	public void doesFindBestCoordinatesTest()
	{
		PlayerDto player = new PlayerDto("player1", "green", new PlayerTreasuresDto(5, 5), new PlayerBoniDto(0, 0, 0, 0));
		
		doesFindBestCoordinatesTestCore(new Coordinates(1, 0), new Coordinates(4, 0), 3, player);
		doesFindBestCoordinatesTestCore(new Coordinates(1, 0), new Coordinates(3, 0), 2, player);
		doesFindBestCoordinatesTestCore(new Coordinates(1, 0), new Coordinates(2, 0), 1, player);
		doesFindBestCoordinatesTestCore(new Coordinates(1, 0), new Coordinates(1, 0), 0, player);
		doesFindBestCoordinatesTestCore(new Coordinates(0, 1), new Coordinates(0, 4), 3, player);
		doesFindBestCoordinatesTestCore(new Coordinates(0, 1), new Coordinates(0, 3), 2, player);
		doesFindBestCoordinatesTestCore(new Coordinates(0, 1), new Coordinates(0, 2), 1, player);
		doesFindBestCoordinatesTestCore(new Coordinates(0, 1), new Coordinates(0, 1), 0, player);
	}
	
	private void doesFindBestCoordinatesTestCore(Coordinates expectedCoordinates, Coordinates treasureCoordinates, 
		int expectedDistance, PlayerDto player)
	{
		Tile[][] board = createBoard(treasureCoordinates, player.getName());
		ShiftingResult result = new ShiftingResult(1, 1, 5, board, player);
		
		assertEquals(expectedCoordinates.getX(), result.getBestCoordinatesAfterShift().getX());
		assertEquals(expectedCoordinates.getY(), result.getBestCoordinatesAfterShift().getY());
		assertEquals(expectedDistance, result.getDistance());
	}
	
	private Tile[][] createBoard(Coordinates treasureCoordinates, String playerName)
	{
		Tile[][] board = new Tile[5][5];
		board[0][0] = new Tile(0);
		board[1][0] = new Tile(5);
		board[2][0] = new Tile(9);
		board[3][0] = new Tile(7);
		board[4][0] = new Tile(1);
		board[0][1] = new Tile(9);
		board[1][1] = new Tile(1);
		board[2][1] = new Tile(8);
		board[3][1] = new Tile(6);
		board[4][1] = new Tile(3);
		board[0][2] = new Tile(1);
		board[1][2] = new Tile(8);
		board[2][2] = new Tile(1);
		board[3][2] = new Tile(9);
		board[4][2] = new Tile(9);
		board[0][3] = new Tile(0);
		board[1][3] = new Tile(7);
		board[2][3] = new Tile(6);
		board[3][3] = new Tile(7);
		board[4][3] = new Tile(2);
		board[0][4] = new Tile(3);
		board[1][4] = new Tile(3);
		board[2][4] = new Tile(7);
		board[3][4] = new Tile(4);
		board[4][4] = new Tile(2);
		board[0][0].setPlayer(new Player(playerName));
		board[treasureCoordinates.getX()][treasureCoordinates.getY()].setTreasure(5);
		return board;
	}
}
