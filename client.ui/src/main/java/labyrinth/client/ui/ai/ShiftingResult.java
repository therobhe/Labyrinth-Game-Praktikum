package labyrinth.client.ui.ai;

import java.util.List;
import java.util.function.Function;
import labyrinth.contracts.communication.dtos.PlayerDto;
import labyrinth.contracts.entities.Coordinates;
import labyrinth.contracts.entities.game.PlayerMovementManager;
import labyrinth.contracts.entities.game.Tile;

/**
 * Class represent the result of a shifting action of the AI
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ShiftingResult 
{
	//Properties
	
	Tile[][] boardAfterShift;
	
	private Coordinates bestCoordinatesAfterShift;
	/**
	 * Corresponding getter
	 * @return The closest coordinates to the target treasure after the shifting
	 */
	public Coordinates getBestCoordinatesAfterShift() { return bestCoordinatesAfterShift; }
	
	private int boardSize;
	
	private int slot;
	/**
	 * Corresponding getter
	 * @return The Slot which was used for shifting
	 */
	public int getSlot() { return slot; }
	
	private int distance;
	/**
	 * Corresponding getter
	 * @return The distance of the closest tile to the target treasure after the shifting
	 */
	public int getDistance() { return distance; }
	
	private int tileType;
	/**
	 * Corresponding getter
	 * @return The tile type inserted
	 */
	public int getTileType() { return tileType; }
	
	//Constructors
	
	/**
	 * ShiftingResult constructor
	 * @param slot The Slot which was used for shifting
	 * @param tileType The tile type inserted
	 * @param boardSize The size of the board
	 * @param board The board after the shifting
	 * @param player The current player
	 */
	public ShiftingResult(int slot, int tileType, int boardSize, Tile[][] board, PlayerDto player)
	{
		this.boardAfterShift = board;
		this.boardSize = boardSize;
		this.tileType = tileType;
		this.slot = slot;
		findBestFieldAfterShifting(player.getName(), player.getTreasures().getCurrent());
	}
	
	//Properties
	
	/**
	 * ShiftingResult constructor
	 * @param slot The Slot which was used for shifting
	 * @param tileType The tile type inserted
	 * @param boardSize The size of the board
	 * @param boardAfterShift The board after the shifting
	 */
	private void findBestFieldAfterShifting(String playerName, Integer targetTreasure)
	{
		Coordinates current = getCoordinatesOfPlayer(playerName);
		Coordinates target = getCoordinatesOfTreasure(targetTreasure);
		
		//Don't use shifting results with target treasure outside of the board
		if(target == null || current.getX() < 0 || current.getY() < 0)
		{
			boardSize = Integer.MAX_VALUE;
			return;
		}
			
		//Find the best tile after shifting
		Tile currentTile = boardAfterShift[current.getX()][current.getY()];
		Tile targetTile = boardAfterShift[target.getX()][target.getY()];
		Coordinates currentPosition = getCoordinatesOfTile(currentTile);
		Coordinates targetPosition = getCoordinatesOfTile(targetTile);
		List<Coordinates> possibleTargets 
			= PlayerMovementManager.getReachableCoordinates(currentPosition, boardAfterShift, boardSize);
				
		bestCoordinatesAfterShift = possibleTargets.stream().sorted((coordinates1, coordinates2) -> Integer.compare(
			getDistanceBetweenCoordinates(coordinates1, targetPosition), getDistanceBetweenCoordinates(coordinates2, targetPosition)))
				.findFirst().get();
		//Calculate distance from best tile to the target
		distance = getDistanceBetweenCoordinates(bestCoordinatesAfterShift, targetPosition);
	}
	
	private int getDistanceBetweenCoordinates(Coordinates source, Coordinates target)
	{
		int distance = 0;		
		if(source.getX() > target.getX())
			distance = source.getX() - target.getX();
		else
			distance = target.getX() - source.getX();
		if(source.getY() > target.getY())
			distance += source.getY() - target.getY();
		else
			distance += target.getY() - source.getY();
		return distance;
	}
	
	private Coordinates getCoordinatesOfTile(Tile field) { return getCoordinatesCore(field1 -> field1 == field); }
	
	private Coordinates getCoordinatesOfPlayer(String player) 
	{ return getCoordinatesCore(field1 -> field1.getPlayer() != null && field1.getPlayer().getPlayerName().equals(player)); }
	
	private Coordinates getCoordinatesOfTreasure(Integer treasure) 
	{ return getCoordinatesCore(field1 -> field1.getTreasure() != null && field1.getTreasure() == treasure); }
	
	private Coordinates getCoordinatesCore(Function<Tile, Boolean> selector)
	{
		for(int i = 0;i< boardSize;i++)
			for(int j = 0;j < boardSize;j++)
				if(selector.apply(boardAfterShift[i][j]))
					return new Coordinates(i, j);	
		return null;
	}
}