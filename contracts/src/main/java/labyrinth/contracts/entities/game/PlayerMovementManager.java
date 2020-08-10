package labyrinth.contracts.entities.game;

import java.util.ArrayList;
import java.util.List;
import labyrinth.contracts.entities.Coordinates;

/**
 * Class containing the logic for getting the possible target coordinates for moving a player
 * @author Lukas Reinhardt
 * @version 1.0
 */
public class PlayerMovementManager 
{
	/**
	 * Gets the coordinates of all possible fields the player can move to
	 * @param position Current position of the player
	 * @param board The game board
	 * @param boardSize The size of the board
	 */
	public static List<Coordinates> getReachableCoordinates(Coordinates position, Tile[][] board, int boardSize)
	{
		List<Coordinates> result = new ArrayList<Coordinates>();
		getReachableCoordinatesCore(position, result, board, boardSize);
		return result;
	}
	
	private static void getReachableCoordinatesCore(Coordinates position, List<Coordinates> coordinates, Tile[][] board, 
		int boardSize)
	{
		if(coordinates.stream().anyMatch(coordinate -> coordinate.getX() == position.getX() && coordinate.getY() == position.getY()))
			return;
		coordinates.add(position);
		
		if(canMoveUp(position, board))
			getReachableCoordinatesCore(new Coordinates(position.getX(), position.getY() - 1), coordinates, board, boardSize);
		if(canMoveDown(position, board, boardSize))
			getReachableCoordinatesCore(new Coordinates(position.getX(), position.getY() + 1), coordinates, board, boardSize);
		if(canMoveLeft(position, board))
			getReachableCoordinatesCore(new Coordinates(position.getX() - 1, position.getY()), coordinates, board, boardSize);
		if(canMoveRight(position, board, boardSize))
			getReachableCoordinatesCore(new Coordinates(position.getX() + 1, position.getY()), coordinates, board, boardSize);
	}
	
	private static boolean canMoveUp(Coordinates position, Tile[][] board)
	{
		return position.getY() > 0 && board[position.getX()][position.getY()].canLeaveTile(MovementDirection.UP) 
			&& board[position.getX()][position.getY() - 1].canEnterTile(MovementDirection.DOWN);
	}
	
	private static boolean canMoveDown(Coordinates position, Tile[][] board, int boardSize)
	{
		return position.getY() < boardSize - 1 && board[position.getX()][position.getY()].canLeaveTile(MovementDirection.DOWN) 
			&& board[position.getX()][position.getY() + 1].canEnterTile(MovementDirection.UP);
	}
	
	private static boolean canMoveLeft(Coordinates position, Tile[][] board)
	{
		return position.getX() > 0 && board[position.getX()][position.getY()].canLeaveTile(MovementDirection.LEFT) 
			&& board[position.getX() - 1][position.getY()].canEnterTile(MovementDirection.RIGHT);
	}
	
	private static boolean canMoveRight(Coordinates position, Tile[][] board, int boardSize)
	{
		return position.getX() < boardSize - 1 && board[position.getX()][position.getY()].canLeaveTile(MovementDirection.RIGHT) 
			&& board[position.getX() + 1][position.getY()].canEnterTile(MovementDirection.LEFT);
	}
}
