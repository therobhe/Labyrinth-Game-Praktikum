package labyrinth.contracts.entities.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import labyrinth.contracts.entities.Coordinates;
import labyrinth.contracts.entities.lobby.GameConfiguration;
import labyrinth.contracts.resources.Errors;
import java.util.Collections;

/**
 * Class representing the game board of the crazy labyrinth
 * @author Mira
 * @version 1.2
 */
public final class GameBoard
{
	//Properties
	
	private Tile tileOutsideOfBoard;
	/**
	 * Corresponding get-method
	 * @return The tile outside of the board 
	 */
	public Tile getTileOutSideOfBoard() { return tileOutsideOfBoard; }
	
	private Tile[][] board;	

	private int boardSize;
	/** 
	 * Corresponding get-method
	 * @return The size of the Board
	 */
	public int getBoardSize() { return boardSize; }
	
	private List<Player> players = new ArrayList<Player>();
	/** 
	 * Corresponding get-method
	 * @return The List of players
	 */
	public List<Player> getPlayers() { return players; }
	
	//Constructors
	
	/**
	 * GameBoard constructor 
	 * @param size of the board 
	 */
	public GameBoard(int size) 
	{ 
		board = new Tile[size][size];
		boardSize = size;
		generateTiles();
	}
	
	/**
	 * GameBoard constructor 
	 * @param configuration The Configuration of the game
	 * @param players The players of the game
	 */
	public GameBoard(GameConfiguration configuration, List<Player> players) 
	{ 
		this(configuration.getSize().getX());
		this.players.addAll(players);
		
		//Place players on the board
		placePlayers();
		//Place treasures on the board
		placeTreasures();
		//Place boni on the board
		placeBoni(configuration.getBoniProbability());
		//Distribute the mission cards to the players
		distributeMissionCards(configuration.getTreasureCount());
	}
	
	//Methods
	
	/** 
	 * Places the player on the tile with the specified coordinates
	 * @param player @see Player class
	 * @param coordinates The coordinates where to add the player
	 * @throws IllegalArgumentException If the Coordinates are outside of the board
	 */
	public void addPlayer(Player player, Coordinates coordinates) { getTile(coordinates).setPlayer(player); }
	
	/** 
	 * Places the Bonus on the tile with the specified coordinates
	 * @param bonus The Bonus
	 * @param coordinates The coordinates where to add the bonus
	 * @throws IllegalArgumentException If the Coordinates are outside of the board
	 */
	public void addBonus(BonusKind bonus, Coordinates coordinates) { getTile(coordinates).setBonus(bonus); }
	
	/** 
	 * Places the treasure on the tile with the specified coordinates
	 * @param treasure @see Treasure class 
	 * @param coordinates The coordinates where to add the treasure
	 * @throws IllegalArgumentException If the Coordinates are outside of the board
	 */
	public void addTreasure(int treasure, Coordinates coordinates) { getTile(coordinates).setTreasure(treasure); }
	
	/**
	 * Places the PlayerBase on the tile with the specified coordinates
	 * @param playerBase The base of a player
	 * @param coordinates The coordinates where to add the PlayerBase
	 * @throws IllegalArgumentException If the Coordinates are outside of the board
	 */
	public void addPlayerBase(String playerBase, Coordinates coordinates) { getTile(coordinates).setPlayerBase(playerBase); }
	
	/**
	 * Returns the tile with the specified coordinates
	 * @param coordinates The coordinates of the tile
	 * @return Tile with the specified coordinates
	 * @throws IllegalArgumentException If the Coordinates are outside of the board
	 */
	public Tile getTile(Coordinates coordinates)
	{
		if(checkOutsideOfBoard(coordinates))
			throw new IllegalArgumentException(Errors.getGameBoard_CoordinatesOutsideOfBoard());
		return board[coordinates.getX()][coordinates.getY()];
	} 
	
	/**
	 * gets the position of the specified Player
	 * @param player
	 * @return position of Player
	 */
	public Coordinates getPlayerPosition(Player player)
	{
		for (int i = 0;i < boardSize;i++)
			for (int j = 0;j < boardSize;j++)
			    if(board[i][j].getPlayer()==player)
					return new Coordinates(i, j);
		return null;
	}

	/**
	 * Tries to move Player to the specified Coordinates
	 * @param player The player to should be moved
	 * @param targetCoordinates The target coordinates the player wants to move to
	 * @throws IllegalArgumentException If the Coordinates are outside of the board
	 */
	public boolean tryMovePlayer(Player player, Coordinates targetCoordinates)
	{
		//Return if target position outside of the board
		if(checkOutsideOfBoard(targetCoordinates))
			throw new IllegalArgumentException(Errors.getGameBoard_CoordinatesOutsideOfBoard());
			
		Coordinates position = getPlayerPosition(player);
		if(PlayerMovementManager.getReachableCoordinates(position, board, boardSize).stream()
			.anyMatch(coordinates -> targetCoordinates.getX() == coordinates.getX() && targetCoordinates.getY() == coordinates.getY()))
			return tryMovePlayerCore(player, position, targetCoordinates);
		return false;
	}
	
	/**
	 * Tries to shift the the tile outside of the board into the board
	 * @param canShiftSolid If the player can shift solid tiles
	 * @param slot The slot on which the tile should be shifted
	 * @return True if the shifting was successful, else false
	 */
	public boolean tryShiftTile(boolean canShiftSolid, int slot, int tileType) 
	{	
		if(!canShiftTile(canShiftSolid, slot))
			return false;
		
		shiftTile(slot, tileType);		
		return true; 
	}
	
	private boolean canShiftTile(boolean canShiftSolid, int slot)
	{
		//Player base cannot be pushed out of the board
		if(tileIsStartTile(slot))
			return false;
		
		//Test if solid tile and player allowed to shift solid tile
		if(!canShiftSolid && isEvenCoordiante(slot)) 
		    return false;
		
		return true;
	}
	
	private void shiftTile(int slot, int tileType)
	{
		Tile tileOutsideTmp;
		if(slot < boardSize) //shift down
		{
			int x = slot % boardSize;
			tileOutsideTmp = board[x][boardSize - 1];
			BoardShiftingManager.shiftTileDown(x, tileType, boardSize, board, tileOutsideOfBoard);
		} 
		else if(slot < 2 * boardSize) //shift left 
		{
			int y = slot % boardSize;
			tileOutsideTmp = board[0][y];
			BoardShiftingManager.shiftTileLeft(y, tileType, boardSize, board, tileOutsideOfBoard);
		}		
		else if(slot < 3 * boardSize) //shift up
		{
			int x = boardSize - slot % boardSize - 1;
			tileOutsideTmp = board[x][0];
			BoardShiftingManager.shiftTileUp(x, tileType, boardSize, board, tileOutsideOfBoard);
		}			
		else //shift right
		{
			int y = boardSize - slot % boardSize - 1;
			tileOutsideTmp = board[boardSize - 1][y];
			BoardShiftingManager.shiftTileRight(y, tileType, boardSize, board, tileOutsideOfBoard);
		}
		tileOutsideOfBoard = tileOutsideTmp;
		tileOutsideOfBoard.setPlayer(null);
	}
	
	private boolean isEvenCoordiante(int slot)
	{
		if(slot >= 0 && slot < boardSize || slot >= 2 * boardSize && slot < 3 * boardSize)
			return slot % 2 == 0;
		else
			return slot % 2 == 1;
	}

	private boolean tileIsStartTile(int slot) 
	{
		return slot == 0 || slot == boardSize - 1 ||  slot == boardSize || slot == 2 * boardSize - 1 
			|| slot == 2 * boardSize || slot == 3 * boardSize - 1 || slot == 3 * boardSize || slot == 4 * boardSize - 1;
	}
	
	
	private boolean checkOutsideOfBoard(Coordinates coordinates)
	{ 
		return coordinates.getX() < 0 || coordinates.getY() < 0 || coordinates.getX() > board.length - 1 
			|| coordinates.getY() > board[coordinates.getX()].length - 1; 
	}
	
	private void distributeMissionCards(int cardsPerUser)
	{
		List<Integer> treasureKinds = new ArrayList<Integer>();
		for(int i = 0;i < 22;i++)
			treasureKinds.add(i);		
       	Collections.shuffle(treasureKinds);
       	
       	int counter = 0;
       	for (Player player : players)
       		for (int tmp = 0;tmp < cardsPerUser;tmp++)
       		{       			
       			player.getTargetTreasures().add(treasureKinds.get(counter));
       			counter++;
       		}
	}
	
	private void generateTiles()
	{
		Random generator = new Random();
		for(int i = 0;i < boardSize;i++)
			for(int j = 0;j < boardSize;j++)
			{
				if(i == 0 && j == 0)
					board[i][j] = new Tile(0);
				else if(i == 0 && j == boardSize - 1)
					board[i][j] = new Tile(3);
				else if(i == boardSize - 1 && j == 0)
					board[i][j] = new Tile(1);
				else if(i == boardSize - 1 && j == boardSize - 1)
					board[i][j] = new Tile(2);
				else
					board[i][j] = new Tile(generator.nextInt(10));
			}
		tileOutsideOfBoard = new Tile(generator.nextInt(10));
	}
	
	private boolean tryMovePlayerCore(Player player, Coordinates position, Coordinates targetPosition)
	{
		int x = targetPosition.getX();
		int y = targetPosition.getY();
		if(board[x][y].getPlayer() != null && board[x][y].getPlayer() != player)
			return false;
	    board[position.getX()][position.getY()].setPlayer(null);
	    board[x][y].setPlayer(player);
	    return true;
	}	
	
	private void placePlayers()
	{
		//Player 1
		board[0][0].setPlayerBase(players.get(0).getPlayerName());
		board[0][0].setPlayer(players.get(0));	
		
		//Player 2
		board[0][boardSize - 1].setPlayerBase(players.get(1).getPlayerName());
		board[0][boardSize - 1].setPlayer(players.get(1));
		
		//Player 3
		if(players.size() > 2)
		{
			board[boardSize - 1][0].setPlayerBase(players.get(2).getPlayerName());
			board[boardSize - 1][0].setPlayer(players.get(2));
			
			if(players.size() > 3)
			{
				board[boardSize - 1][boardSize - 1].setPlayerBase(players.get(3).getPlayerName());
				board[boardSize - 1][boardSize - 1].setPlayer(players.get(3));
			}
		}
	}
	
	private void placeTreasures()
	{		
		for(int treasureKind = 0;treasureKind < 22;treasureKind++)
		{
			Random random = new Random();
			int x = random.nextInt(boardSize);
			int y = random.nextInt(boardSize);
			
			while(!canPlaceTreasure(x, y))
			{
				x = random.nextInt(boardSize);
				y = random.nextInt(boardSize);
			}
			
			board[x][y].setTreasure(treasureKind);
		}
	}
	
	private void placeBoni(double boniProbability)
	{
		Random random = new Random();
		for(int i = 0;i < boardSize;i++)
		{
			for(int j = 0;j < boardSize;j++)
			{
				Tile tile = getTile(new Coordinates(i, j));
				if(tile.getPlayer() == null && tile.getPlayerBase() == null && tile.getTreasure() == null 
					&& Math.random() < boniProbability)
				{
					int bonusIndex = random.nextInt(4);
					tile.setBonus(BonusKind.values()[bonusIndex]);
				}
			}
		}
	}	
	
	private boolean canPlaceTreasure(int x, int y)
	{ return board[x][y].getTreasure() == null && board[x][y].getPlayerBase() == null;  }
}