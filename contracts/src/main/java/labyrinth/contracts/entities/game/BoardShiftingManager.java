package labyrinth.contracts.entities.game;

/**
 * Class containing the logic for shifting the tiles of a board in a specific direction
 * @author Lisa Pillep
 * @version 1.0
 */
public final class BoardShiftingManager 
{
	//Constructor
	
	private BoardShiftingManager() { }
	
	//Methods
	
	/**
	 * Shifts the tiles to the left
	 * @param y Y-Coordinate
	 * @param tileType Type of the new tile inserted
	 * @param boardSize The size of the board
	 * @param board The board
	 */
	public static void shiftTileLeft(int y, int tileType, int boardSize, Tile[][] board, Tile newTile) 
	{
		Player player = board[0][y].getPlayer();		
		for (int i = 1;i < boardSize; i++) 
			board[i - 1][y] = board[i][y];
		
		insertNewTile(board, boardSize - 1, y, tileType, newTile, player);
	}
	
	/**
	 * Shifts the tiles to the right
	 * @param y Y-Coordinate
	 * @param tileType Type of the new tile inserted
	 * @param boardSize The size of the board
	 * @param board The board
	 */
	public static void shiftTileRight(int y, int tileType, int boardSize, Tile[][] board, Tile newTile) 
	{
		Player player = board[boardSize - 1][y].getPlayer();	
		for (int i = boardSize - 2;i >= 0;i--) 
			board[i + 1][y] = board[i][y];
		
		insertNewTile(board, 0, y, tileType, newTile, player);
	}
	
	/**
	 * Shifts the tiles to the down
	 * @param x X-Coordinate
	 * @param tileType Type of the new tile inserted
	 * @param boardSize The size of the board
	 * @param board The board
	 */
	public static void shiftTileDown(int x, int tileType, int boardSize, Tile[][] board, Tile newTile) 
	{
		Player player = board[x][boardSize - 1].getPlayer();	
		for (int i = boardSize - 2;i >= 0;i--)
			board[x][i + 1] = board[x][i];
		
		insertNewTile(board, x, 0, tileType, newTile, player);
	}
	
	/**
	 * Shifts the tiles to the up
	 * @param x X-Coordinate
	 * @param tileType Type of the new tile inserted
	 * @param boardSize The size of the board
	 * @param board The board
	 */
	public static void shiftTileUp(int x, int tileType, int boardSize, Tile[][] board, Tile newTile) 
	{
		Player player = board[x][0].getPlayer();	
		for (int i = 1;i < boardSize; i++) 
			board[x][i - 1] = board[x][i];
		
		insertNewTile(board, x, boardSize - 1, tileType, newTile, player);
	}
	
	private static void insertNewTile(Tile[][] board, int x, int y, int tileType, Tile newTile, Player player)
	{
		newTile.setType(tileType);
		board[x][y] = newTile;
		
		//If player shifted outside of board place on tile that has been pushed in
		if(player != null)
			board[x][y].setPlayer(player);
	}
}
