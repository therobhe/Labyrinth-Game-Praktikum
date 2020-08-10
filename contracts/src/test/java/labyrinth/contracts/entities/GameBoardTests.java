package labyrinth.contracts.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import labyrinth.contracts.entities.game.BonusKind;
import labyrinth.contracts.entities.game.GameBoard;
import labyrinth.contracts.entities.game.Player;
import labyrinth.contracts.entities.game.Tile;
import labyrinth.contracts.entities.lobby.GameConfiguration;

/**
 * TestClass for @see GameBoard
 * @author Omar Al Masalma
 * @version 1.0
 */
public final class GameBoardTests 
{	
	/**
	 * Test that the players are placed on the correct tiles of the board
	 */
	@Test
	public void doesPlacePlayersTest()
	{
		GameConfiguration configuration = new GameConfiguration();
		configuration.setSize(new Coordinates(7, 7));
		List<Player> players = new ArrayList<Player>();
		Player player1 = new Player("player1", null);
		Player player2 = new Player("player2", null);
		Player player3 = new Player("player3", null);
		Player player4 = new Player("player4", null);
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		
		GameBoard board = new GameBoard(configuration, players);
		int size = board.getBoardSize() - 1;
		assertSame(player1, board.getTile(new Coordinates(0, 0)).getPlayer());
		assertSame("player1", board.getTile(new Coordinates(0, 0)).getPlayerBase());
		assertSame(player2, board.getTile(new Coordinates(0, size)).getPlayer());
		assertSame("player2", board.getTile(new Coordinates(0, size)).getPlayerBase());
		assertSame(player3, board.getTile(new Coordinates(size, 0)).getPlayer());
		assertSame("player3", board.getTile(new Coordinates(size, 0)).getPlayerBase());
		assertSame(player4, board.getTile(new Coordinates(size, size)).getPlayer());
		assertSame("player4", board.getTile(new Coordinates(size, size)).getPlayerBase());
	}
	
	/**
	 * Test that a Player cannot be placed outside of the board 
	 */
	@Test
	public void cannotAddPlayerOutsideOfTheBoardTest() 
	{
		GameBoard board = new GameBoard(1);
		Player player = new Player("Player", "test");
		assertThrows(IllegalArgumentException.class, () -> board.addPlayer(player, new Coordinates(3, 0)), 
			"Expected exception of type IllegalStateException has not been thrown.");
		assertThrows(IllegalArgumentException.class, () -> board.addPlayer(player, new Coordinates(0, 3)), 
			"Expected exception of type IllegalStateException has not been thrown.");
		assertThrows(IllegalArgumentException.class, () -> board.addPlayer(player, new Coordinates(-1, 0)), 
			"Expected exception of type IllegalStateException has not been thrown.");
		assertThrows(IllegalArgumentException.class, () -> board.addPlayer(player, new Coordinates(0, -1)), 
			"Expected exception of type IllegalStateException has not been thrown.");
	}

	/**
	 * Test that a Bonus cannot be placed outside of the board 
	 */
	@Test
	public void cannotAddBonusOutsideOfTheBoardTest() 
	{
		GameBoard board = new GameBoard(1);
		assertThrows(IllegalArgumentException.class, () -> board.addBonus(BonusKind.BEAM, new Coordinates(3, 0)), 
			"Expected exception of type IllegalStateException has not been thrown.");
		assertThrows(IllegalArgumentException.class, () -> board.addBonus(BonusKind.BEAM, new Coordinates(0, 3)), 
			"Expected exception of type IllegalStateException has not been thrown.");
		assertThrows(IllegalArgumentException.class, () -> board.addBonus(BonusKind.BEAM, new Coordinates(-1, 0)), 
			"Expected exception of type IllegalStateException has not been thrown.");
		assertThrows(IllegalArgumentException.class, () -> board.addBonus(BonusKind.BEAM, new Coordinates(0, -1)), 
			"Expected exception of type IllegalStateException has not been thrown.");
	}
	
	/**
	 * Test that a PlayerBase cannot be placed outside of the board 
	 */
	@Test
	public void cannotAddPlayerBaseOutsideOfTheBoardTest() 
	{
		GameBoard board = new GameBoard(1);
		assertThrows(IllegalArgumentException.class, () -> board.addPlayerBase("Test", new Coordinates(3, 0)), 
			"Expected exception of type IllegalStateException has not been thrown.");
		assertThrows(IllegalArgumentException.class, () -> board.addPlayerBase("Test", new Coordinates(0, 3)), 
			"Expected exception of type IllegalStateException has not been thrown.");
		assertThrows(IllegalArgumentException.class, () -> board.addPlayerBase("Test", new Coordinates(-1, 0)), 
			"Expected exception of type IllegalStateException has not been thrown.");
		assertThrows(IllegalArgumentException.class, () -> board.addPlayerBase("Test", new Coordinates(0, -1)), 
			"Expected exception of type IllegalStateException has not been thrown.");
	}
	
	/**
	 * Test that a Treasure cannot be placed outside of the board 
	 */
	@Test
	public void cannotAddTreasureOutsideOfTheBoardTest() 
	{
		GameBoard board = new GameBoard(1);
		assertThrows(IllegalArgumentException.class, () -> board.addTreasure(1, new Coordinates(3, 0)), 
			"Expected exception of type IllegalStateException has not been thrown.");
		assertThrows(IllegalArgumentException.class, () -> board.addTreasure(1, new Coordinates(0, 3)), 
			"Expected exception of type IllegalStateException has not been thrown.");
		assertThrows(IllegalArgumentException.class, () -> board.addTreasure(1, new Coordinates(-1, 0)), 
			"Expected exception of type IllegalStateException has not been thrown.");
		assertThrows(IllegalArgumentException.class, () -> board.addTreasure(1, new Coordinates(0, -1)), 
			"Expected exception of type IllegalStateException has not been thrown.");
	}
	
	/**
	 * Test that it's impossible to get a tile with coordinates outside of the board 
	 */
	@Test
	public void cannotGetTileOutsideOfTheBoardTest() 
	{
		GameBoard board = new GameBoard(1);
		assertThrows(IllegalArgumentException.class, () -> board.getTile(new Coordinates(3, 0)), 
			"Expected exception of type IllegalStateException has not been thrown.");
		assertThrows(IllegalArgumentException.class, () -> board.getTile(new Coordinates(0, 3)), 
			"Expected exception of type IllegalStateException has not been thrown.");
		assertThrows(IllegalArgumentException.class, () -> board.getTile(new Coordinates(-1, 0)), 
			"Expected exception of type IllegalStateException has not been thrown.");
		assertThrows(IllegalArgumentException.class, () -> board.getTile(new Coordinates(0, -1)), 
			"Expected exception of type IllegalStateException has not been thrown.");
	}
	
	/**
	 * Tests @see GameBoard.getPlayerPosition
	 */
	@Test
	public void getPlayerPositionTest()
	{
		// create new GameBoard
		GameBoard board1 = new GameBoard(4);
		
		// define Player position to check
		Coordinates playerpos = new Coordinates(2,2);
		
		// create Player and set on position (2,2)
		Player player1 = new Player("Peter", "test");
		board1.addPlayer(player1, new Coordinates(2, 2));
		
		// test if the checked coordinates are equal to the defined
		assertEquals(playerpos.getX(), board1.getPlayerPosition(player1).getX(), "Die Koordinaten stimmen nicht überein.");
		assertEquals(playerpos.getY(), board1.getPlayerPosition(player1).getY(), "Die Koordinaten stimmen nicht überein.");
	}
	
	/**
	 * Test that it's impossible to shift a tile when the parameter canShiftSolid false is
	 */
	@Test
	public void cannotShiftSolidTileWhenCanShiftSolidIsFalseTest()
	{
		GameBoard board = new GameBoard(7);
		assertFalse(board.tryShiftTile(false, 2, 0));
		assertFalse(board.tryShiftTile(false, 4, 0));
		assertFalse(board.tryShiftTile(false, 9, 0));
		assertFalse(board.tryShiftTile(false, 11, 0));
		assertFalse(board.tryShiftTile(false, 16, 0));
		assertFalse(board.tryShiftTile(false, 18, 0));
		assertFalse(board.tryShiftTile(false, 23, 0));
		assertFalse(board.tryShiftTile(false, 25, 0));
	}
	
	/**
	 * Test that it's impossible to shift a tile when the parameter canShiftSolid false is
	 */
	@Test
	public void shiftingPlayerOutsideOfTheBoardResultsInPlacingHimOnTheOtherSideTest() 
		throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		GameBoard board = new GameBoard(3);
		Field field = board.getClass().getDeclaredField("board");
		field.setAccessible(true);
		Tile[][] tiles = (Tile[][])field.get(board);
		Player player = new Player("player1", "green");
		tiles[1][2].setPlayer(player);
		board.tryShiftTile(false, 1, 1);
		assertSame(player, tiles[1][0].getPlayer());
		assertNull(tiles[1][2].getPlayer());
	}
	
	/**
	 * Test that it's possible to shift a tile when the parameter canShiftSolid true is
	 */
	@Test
	public void canShiftSolidTileWhenCanShiftSolidIsTrueTest()
	{
		GameBoard board = new GameBoard(7);
		assertTrue(board.tryShiftTile(true, 2, 0));
		assertTrue(board.tryShiftTile(true, 4, 0));
		assertTrue(board.tryShiftTile(true, 9, 0));
		assertTrue(board.tryShiftTile(true, 11, 0));
		assertTrue(board.tryShiftTile(true, 16, 0));
		assertTrue(board.tryShiftTile(true, 18, 0));
		assertTrue(board.tryShiftTile(true, 23, 0));
		assertTrue(board.tryShiftTile(true, 25, 0));
	}
	
	/**
	 * Test that it's impossible to shift a tile with a PlayerBase on it
	 */
	@Test
	public void cannotShiftTileWithPlayerBaseTest()
	{
		GameBoard board1 = new GameBoard(5);
		assertFalse(board1.tryShiftTile(false, 0, 0));
		assertFalse(board1.tryShiftTile(false, 4, 0));
		assertFalse(board1.tryShiftTile(false, 5, 0));
		assertFalse(board1.tryShiftTile(false, 9, 0));
		assertFalse(board1.tryShiftTile(false, 10, 0));
		assertFalse(board1.tryShiftTile(false, 14, 0));
		assertFalse(board1.tryShiftTile(false, 15, 0));
		assertFalse(board1.tryShiftTile(false, 19, 0));
	}
	
	/**
	 * Tests @see GameBoard.tryShiftTile
	 */
	@Test
	public void shiftTileDownTest()
	{
		GameBoard board = new GameBoard(5);
		Tile tile1 = board.getTile(new Coordinates(3, 0));
		Tile tile2 = board.getTile(new Coordinates(3, 1));
		Tile tile3 = board.getTile(new Coordinates(3, 2));
		Tile tile4 = board.getTile(new Coordinates(3, 3));
		
		assertTrue(board.tryShiftTile(false, 3, 7));
		assertEquals(7, board.getTile(new Coordinates(3, 0)).getType());
		assertSame(tile1, board.getTile(new Coordinates(3, 1)));
		assertSame(tile2, board.getTile(new Coordinates(3, 2)));
		assertSame(tile3, board.getTile(new Coordinates(3, 3)));
		assertSame(tile4, board.getTile(new Coordinates(3, 4)));
	}
	
	/**
	 * Tests @see GameBoard.tryShiftTile
	 */
	@Test
	public void shiftTileUpTest()
	{
		GameBoard board = new GameBoard(5);
		Tile tile1 = board.getTile(new Coordinates(1, 4));
		Tile tile2 = board.getTile(new Coordinates(1, 3));
		Tile tile3 = board.getTile(new Coordinates(1, 2));
		Tile tile4 = board.getTile(new Coordinates(1, 1));
		
		assertTrue(board.tryShiftTile(false, 13, 5));
		assertEquals(5, board.getTile(new Coordinates(1, 4)).getType());
		assertSame(tile1, board.getTile(new Coordinates(1, 3)));
		assertSame(tile2, board.getTile(new Coordinates(1, 2)));
		assertSame(tile3, board.getTile(new Coordinates(1, 1)));
		assertSame(tile4, board.getTile(new Coordinates(1, 0)));
	}
	
	/**
	 * Tests @see GameBoard.tryShiftTile
	 */
	@Test
	public void shiftTileLeftTest()
	{
		GameBoard board = new GameBoard(5);
		Tile tile1 = board.getTile(new Coordinates(4, 3));
		Tile tile2 = board.getTile(new Coordinates(3, 3));
		Tile tile3 = board.getTile(new Coordinates(2, 3));
		Tile tile4 = board.getTile(new Coordinates(1, 3));
		
		assertTrue(board.tryShiftTile(false, 8, 8));
		assertEquals(8, board.getTile(new Coordinates(4, 3)).getType());
		assertSame(tile1, board.getTile(new Coordinates(3, 3)));
		assertSame(tile2, board.getTile(new Coordinates(2, 3)));
		assertSame(tile3, board.getTile(new Coordinates(1, 3)));
		assertSame(tile4, board.getTile(new Coordinates(0, 3)));
	}
	
	/**
	 * Tests @see GameBoard.tryShiftTile
	 */
	@Test
	public void shiftTileRightTest()
	{
		GameBoard board = new GameBoard(5);
		Tile tile1 = board.getTile(new Coordinates(0, 1));
		Tile tile2 = board.getTile(new Coordinates(1, 1));
		Tile tile3 = board.getTile(new Coordinates(2, 1));
		Tile tile4 = board.getTile(new Coordinates(3, 1));
		
		assertTrue(board.tryShiftTile(false, 18, 1));
		assertEquals(1, board.getTile(new Coordinates(0, 1)).getType());
		assertSame(tile1, board.getTile(new Coordinates(1, 1)));
		assertSame(tile2, board.getTile(new Coordinates(2, 1)));
		assertSame(tile3, board.getTile(new Coordinates(3, 1)));
		assertSame(tile4, board.getTile(new Coordinates(4, 1)));
	}
	
	/**
	 * Test checking whether Players got their MissionCards randomly
	 */
	@Test
	public void distributeMissionCardsTest() 
		throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		Player player1 = new Player("Player1", "test");
		Player player2 = new Player("Player2", "test");
		GameBoard gameBoard = new GameBoard(2);
		gameBoard.getPlayers().add(player1);
		gameBoard.getPlayers().add(player2);
		Method method = gameBoard.getClass().getDeclaredMethod("distributeMissionCards", int.class);
		method.setAccessible(true);
		method.invoke(gameBoard, 2);
		assertEquals(2, player1.getTargetTreasures().size(), "This player should have several MissionCards!");
		assertEquals(2, player2.getTargetTreasures().size(), "this player should have several MissionCards!");
	}
}