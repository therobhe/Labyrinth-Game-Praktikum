package labyrinth.contracts.entities.game;

import labyrinth.contracts.resources.Errors;

/**
 * A Field of the game board.
 * @author Eugen Cravtov-Grandl
 * @version 1.3
 */
public final class Tile 
{
	//Properties
	
	private Player player;
	/**
	 * Corresponding getter
     * @return Player placed on the tile
     */
	public Player getPlayer() { return player; }
    /**
     * Corresponding setter
     * @param player The player placed on the tile
     */
    public void setPlayer(Player player) 
    { 
    	this.player = player;
    	if(player != null)
    	{
        	if(bonus != null)
        		collectBonus(); 
        	if(treasure != null)
        		tryCollectTreasure();
    	}
    }

	private Integer treasure;
    /**
     * Corresponding getter
     * @return Treasure placed on the field
     */
	public Integer getTreasure() { return treasure; }
	/**
     * Corresponding setter
     * @param value The treasure placed on the tile
     * @throws IllegalStateException If the PlayerBase is already set
     */
	public void setTreasure(Integer value)
	{
		if(this.playerBase == null || value == null)
			this.treasure = value;
		else 
			throw new IllegalStateException(Errors.getTile_CannotPlaceTreasureOnTileWithPlayerBase());
	}
	
	private BonusKind bonus;
	/**
	 * Corresponding getter
     * @return Bonus placed on the tile
     */
    public BonusKind getBonus() { return bonus; }
    /**
     * Corresponding setter
     * @param bonus The bonus placed on the tile
     * @throws IllegalStateException If the PlayerBase is already set
     */
    public void setBonus(BonusKind bonus) 
    {
    	if(this.playerBase == null)
    		this.bonus = bonus;
    	else 
    		throw new IllegalStateException(Errors.getTile_CannotPlaceBonusOnTileWithPlayerBase());
    }
	
	private String playerBase;
	/**
	 * Corresponding getter
     * @return PlayerBase placed on the tile
     */
    public String getPlayerBase() { return playerBase; }
    /**
     * Corresponding setter
     * @param playerBase The player base that should be placed on the tile
     */
    public void setPlayerBase(String playerBase) { this.playerBase = playerBase; }

	private int type;
	/**
	 * Corresponding getter
     * @return Type of the tile
     */
    public int getType() { return type; }
	/**
	 * Corresponding getter
     * @param value The type of the tile
     */
    public void setType(int value) { type = value; }
    
    //Constructors

    /**
     * Tile constructor
     * @param type The type of the tile
     */
	public Tile(int type) { this.type = type; }
	
	//Methods
	
    /**
     * Checks if entering the tile from the specified directions is possible
     * @param direction The direction the player wants to move
     * @return True if the player is able to enter the tile given by the transmitted direction, else false
     */
	public boolean canEnterTile(MovementDirection direction)
	{
		switch(direction)
		{
			case UP:
				return type == 2 || type == 3 || type == 4 || type == 6 || type == 7 || type == 9;
			case DOWN:
				return type == 0 || type == 1 || type == 4 || type == 5 || type == 6 || type == 9;
			case LEFT:
				return type == 1 || type == 2 || type == 5 || type == 6 || type == 7 || type == 8;
			case RIGHT:
				return type == 0 || type == 3 || type == 4 || type == 5 || type == 7 || type == 8;
			default:
				break;
		}
		return false;
	}
	
    /**
     * Checks if leaving the tile in the specified directions is possible
     * @param direction Direction the player wants to move in
     * @return True if the player is able to leave the field into the transmitted direction, else false
     */
	public boolean canLeaveTile(MovementDirection direction)
	{
		switch(direction)
		{
			case UP:
				return type == 2 || type == 3 || type == 4 || type == 6 || type == 7 || type == 9;
			case DOWN:
				return type == 0 || type == 1 || type == 4 || type == 5 || type == 6 || type == 9;
			case LEFT:
				return type == 1 || type == 2 || type == 5 || type == 6 || type == 7 || type == 8;
			case RIGHT:
				return type == 0 || type == 3 || type == 4 || type == 5 || type == 7 || type == 8;
			default:
				break;
		}
		return false;
	}
	
	private void tryCollectTreasure() 
	{
		//does the field's treasureKind matches the treasureKind of player's actual missionCard?
		if(treasure == player.getNextTreasure()) 
		{ 
			//remove the missionCard from the player's missionCardList
			player.getTargetTreasures().remove(player.getNextTreasure());
			
			//add the matching amount of points to the player
			player.setPoints(player.getPoints() + 5);
			
			//remove the treasure from the field
			setTreasure(null);
		}
	}
		
	private void collectBonus() 
	{
		//add bonus to player's boni list
		player.getBoni().add(bonus);
		
		//delete bonus from field
		setBonus(null);
	}
}