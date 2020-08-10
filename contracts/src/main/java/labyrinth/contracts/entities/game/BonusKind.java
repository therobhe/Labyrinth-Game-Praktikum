package labyrinth.contracts.entities.game;

import com.google.gson.annotations.SerializedName;

/**
 * Enumeration for the different kinds of boni @see Bonus
 * @author Robert Heinemann
 * @version 1.0
 */
public enum BonusKind 
{
	/** set player position to another one of your choice */
	@SerializedName("beam")
	BEAM,
	
	/** switch player position with one player of your choice */
	@SerializedName("swap")
	SWAP,
	
	/** player can move the labyrinth one extra time within the round */
	@SerializedName("shiftTwice")
	SHIFT_TWICE,
	
	/** player can move a fixed field one time within this round */
	@SerializedName("shiftSolid")
	SHIFT_SOLID;
}
