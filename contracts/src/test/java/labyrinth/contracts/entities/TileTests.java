package labyrinth.contracts.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;
import labyrinth.contracts.entities.game.BonusKind;
import labyrinth.contracts.entities.game.MovementDirection;
import labyrinth.contracts.entities.game.Player;
import labyrinth.contracts.entities.game.Tile;

/**
 * Unit test for @see Tile
 * @author Mira Almohsen
 * @version 1.0
 */
public final class TileTests 
{
	/**
     * Test that a treasure can not be placed on a tile that already has a player's starting point
     */
	@Test
    public void CannotAddTreasureOnPlayerBaseTileTest()
    {
    	Tile field = initializeTile();
    	assertThrows(IllegalStateException.class, () -> field.setTreasure(1), 
    		"Expected exception of type IllegalStateException has not been thrown.");
        assertNull(field.getTreasure());
    }
	
    /**
     * Test that a Bonus can not be placed on a tile that already has a player's starting point
     */
	@Test
    public void CannotAddBounsOnPlayerBaseTileTest()
    {
    	Tile tile = initializeTile();
    	assertThrows(IllegalStateException.class, () -> tile.setBonus(BonusKind.BEAM), 
        	"Expected exception of type IllegalStateException has not been thrown.");;
        assertNull(tile.getBonus());
    }
	
	 /**
      * Test that a player can move from his current tile into the upper direction
      */
	@Test
	public void canEnterTileFromUpsideTest()
	{
		assertFalse(new Tile(0).canEnterTile(MovementDirection.UP));
		assertFalse(new Tile(1).canEnterTile(MovementDirection.UP));
		assertTrue(new Tile(2).canEnterTile(MovementDirection.UP));
		assertTrue(new Tile(3).canEnterTile(MovementDirection.UP));
		assertTrue(new Tile(4).canEnterTile(MovementDirection.UP));
		assertFalse(new Tile(5).canEnterTile(MovementDirection.UP));
		assertTrue(new Tile(6).canEnterTile(MovementDirection.UP));
		assertTrue(new Tile(7).canEnterTile(MovementDirection.UP));
		assertFalse(new Tile(8).canEnterTile(MovementDirection.UP));
		assertTrue(new Tile(9).canEnterTile(MovementDirection.UP));
	}
	
	 /**
      * Test that a player can move from his current tile into the down direction
      */
	@Test
	public void canEnterTileFromDownsideTest()
	{
		assertTrue(new Tile(0).canEnterTile(MovementDirection.DOWN));
		assertTrue(new Tile(1).canEnterTile(MovementDirection.DOWN));
		assertFalse(new Tile(2).canEnterTile(MovementDirection.DOWN));
		assertFalse(new Tile(3).canEnterTile(MovementDirection.DOWN));
		assertTrue(new Tile(4).canEnterTile(MovementDirection.DOWN));
		assertTrue(new Tile(5).canEnterTile(MovementDirection.DOWN));
		assertTrue(new Tile(6).canEnterTile(MovementDirection.DOWN));
		assertFalse(new Tile(7).canEnterTile(MovementDirection.DOWN));
		assertFalse(new Tile(8).canEnterTile(MovementDirection.DOWN));
		assertTrue(new Tile(9).canEnterTile(MovementDirection.DOWN));
	}
	
	/**
     * Test that a player cannot move from his current tile into the left direction
     */
	@Test
	public void canEnterTileFromLeftTest()
	{
		assertFalse(new Tile(0).canEnterTile(MovementDirection.LEFT));
		assertTrue(new Tile(1).canEnterTile(MovementDirection.LEFT));
		assertTrue(new Tile(2).canEnterTile(MovementDirection.LEFT));
		assertFalse(new Tile(3).canEnterTile(MovementDirection.LEFT));
		assertFalse(new Tile(4).canEnterTile(MovementDirection.LEFT));
		assertTrue(new Tile(5).canEnterTile(MovementDirection.LEFT));
		assertTrue(new Tile(6).canEnterTile(MovementDirection.LEFT));
		assertTrue(new Tile(7).canEnterTile(MovementDirection.LEFT));
		assertTrue(new Tile(8).canEnterTile(MovementDirection.LEFT));
		assertFalse(new Tile(9).canEnterTile(MovementDirection.LEFT));
	}
	
	/**
     * Test that a player cannot move from his current tile into the right direction
     */
	@Test
	public void canEnterTileFromRightTest()
	{
		assertTrue(new Tile(0).canEnterTile(MovementDirection.RIGHT));
		assertFalse(new Tile(1).canEnterTile(MovementDirection.RIGHT));
		assertFalse(new Tile(2).canEnterTile(MovementDirection.RIGHT));
		assertTrue(new Tile(3).canEnterTile(MovementDirection.RIGHT));
		assertTrue(new Tile(4).canEnterTile(MovementDirection.RIGHT));
		assertTrue(new Tile(5).canEnterTile(MovementDirection.RIGHT));
		assertFalse(new Tile(6).canEnterTile(MovementDirection.RIGHT));
		assertTrue(new Tile(7).canEnterTile(MovementDirection.RIGHT));
		assertTrue(new Tile(8).canEnterTile(MovementDirection.RIGHT));
		assertFalse(new Tile(9).canEnterTile(MovementDirection.RIGHT));
	}
	
	/**
     * Test that a player can leave his current tile into the upper direction
     */
	@Test
	public void canLeaveTileUpwardsTest()
	{
		assertFalse(new Tile(0).canLeaveTile(MovementDirection.UP));
		assertFalse(new Tile(1).canLeaveTile(MovementDirection.UP));
		assertTrue(new Tile(2).canLeaveTile(MovementDirection.UP));
		assertTrue(new Tile(3).canLeaveTile(MovementDirection.UP));
		assertTrue(new Tile(4).canLeaveTile(MovementDirection.UP));
		assertFalse(new Tile(5).canLeaveTile(MovementDirection.UP));
		assertTrue(new Tile(6).canLeaveTile(MovementDirection.UP));
		assertTrue(new Tile(7).canLeaveTile(MovementDirection.UP));
		assertFalse(new Tile(8).canLeaveTile(MovementDirection.UP));
		assertTrue(new Tile(9).canLeaveTile(MovementDirection.UP));
	}
	
	/**
     * Test that a player can leave his current tile into the down direction
     */
	@Test
	public void canLeaveTileDownwardsTest()
	{
		assertTrue(new Tile(0).canLeaveTile(MovementDirection.DOWN));
		assertTrue(new Tile(1).canLeaveTile(MovementDirection.DOWN));
		assertFalse(new Tile(2).canLeaveTile(MovementDirection.DOWN));
		assertFalse(new Tile(3).canLeaveTile(MovementDirection.DOWN));
		assertTrue(new Tile(4).canLeaveTile(MovementDirection.DOWN));
		assertTrue(new Tile(5).canLeaveTile(MovementDirection.DOWN));
		assertTrue(new Tile(6).canLeaveTile(MovementDirection.DOWN));
		assertFalse(new Tile(7).canLeaveTile(MovementDirection.DOWN));
		assertFalse(new Tile(8).canLeaveTile(MovementDirection.DOWN));
		assertTrue(new Tile(9).canLeaveTile(MovementDirection.DOWN));
	}
	
	/**
     * Test that a player can leave his current tile into the left direction
     */
	@Test
	public void canLeaveTileIntoDirectionLeftTest()
	{
		assertFalse(new Tile(0).canLeaveTile(MovementDirection.LEFT));
		assertTrue(new Tile(1).canLeaveTile(MovementDirection.LEFT));
		assertTrue(new Tile(2).canLeaveTile(MovementDirection.LEFT));
		assertFalse(new Tile(3).canLeaveTile(MovementDirection.LEFT));
		assertFalse(new Tile(4).canLeaveTile(MovementDirection.LEFT));
		assertTrue(new Tile(5).canLeaveTile(MovementDirection.LEFT));
		assertTrue(new Tile(6).canLeaveTile(MovementDirection.LEFT));
		assertTrue(new Tile(7).canLeaveTile(MovementDirection.LEFT));
		assertTrue(new Tile(8).canLeaveTile(MovementDirection.LEFT));
		assertFalse(new Tile(9).canLeaveTile(MovementDirection.LEFT));
	}
	
	 /**
      * Test that a player can leave his current tile into the right direction
      */
	@Test
	 public void canLeaveTileOnTheRightTest()
	{
		assertTrue(new Tile(0).canLeaveTile(MovementDirection.RIGHT));
		assertFalse(new Tile(1).canLeaveTile(MovementDirection.RIGHT));
		assertFalse(new Tile(2).canLeaveTile(MovementDirection.RIGHT));
		assertTrue(new Tile(3).canLeaveTile(MovementDirection.RIGHT));
		assertTrue(new Tile(4).canLeaveTile(MovementDirection.RIGHT));
		assertTrue(new Tile(5).canLeaveTile(MovementDirection.RIGHT));
		assertFalse(new Tile(6).canLeaveTile(MovementDirection.RIGHT));
		assertTrue(new Tile(7).canLeaveTile(MovementDirection.RIGHT));
		assertTrue(new Tile(8).canLeaveTile(MovementDirection.RIGHT));
		assertFalse(new Tile(9).canLeaveTile(MovementDirection.RIGHT));
	}

	/**
	 * Test for picking up bonus
	 */
	@Test
	public void canPickUpBonusTest()
	{
		//add player
		Player testPlayer = new Player("Kevin", "etst");
		
		//add bonus
		Tile bonusField = new Tile(0);
		bonusField.setBonus(BonusKind.BEAM);
		bonusField.setPlayer(testPlayer);
		
		//test if the bonus was removed from the field & added to the boni list of the player
		assertNull(bonusField.getBonus(), "Der Bonus wurde nicht aufgenommen!");
		assertSame(BonusKind.BEAM, testPlayer.getBoni().get(0));
		assertFalse(testPlayer.getBoni().isEmpty(), "Der Spieler hat keinen Bonus eingesammelt!");
	}

	/**
	 * Test for picking up treasure
	 */
	@Test
	public void canPickUpTreasureTest()
	{
		//add player and give him a missionCard
		Player testPlayer = new Player("Kevin", "test");
		testPlayer.getTargetTreasures().add(1);
		testPlayer.getTargetTreasures().add(2);
		
		//add treasures		
		Tile treasureField = new Tile(0);
		treasureField.setTreasure(testPlayer.getNextTreasure());
		treasureField.setPlayer(testPlayer);
		
		//test if the treasure was removed from the field & missionCard removed from player
		assertNull(treasureField.getTreasure(), "Der Schatz wurde nicht aufgenommen!");
		assertEquals(1, (int)testPlayer.getNextTreasure());
		assertEquals(5, testPlayer.getPoints());
		
		//test for a not matching missionCard
		treasureField.setTreasure(2);
		treasureField.setPlayer(testPlayer);
		assertEquals(1, (int)testPlayer.getNextTreasure());
	}
	
	private static Tile initializeTile()
	{
    	Tile tile = new Tile(1);
    	tile.setPlayerBase("Test");
    	return tile;
	}
}
