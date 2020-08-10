package labyrinth.contracts.entities.game;

import java.util.ArrayList;

/**
 * Class representing the figure of a Player of the game
 * @author Omar
 * @version 1.2
 */
public final class Player 
{
	//Properties
	
	private boolean isDisconnected = false;
	/** 
	 * Corresponding getter
	 * @return If the player has disconnected
	 */
	public boolean getIsDisconnected() { return isDisconnected; }
	/** 
	 * Corresponding Setter
	 * @param value If the player has disconnected
	 */
	public void setIsDisconnected(boolean value) { isDisconnected = value; }
	
	private String playerName;
	/** 
	 * Corresponding getter
	 * @return The name of the player
	 */
	public String getPlayerName() { return playerName; }
	/** 
	 * Corresponding setter
	 * @param playerName The name of the player
	 */
	public void setPlayerName(String playerName) { this.playerName = playerName; }
	
	private String playerColor; 
	/** 
	 * Corresponding getter
	 * @return Color of the player figure
	 */
	public String getPlayerColor() { return playerColor; }
	/** 
	 * Corresponding setter
	 * @param playerColor Color of the player figure
	 */
	public void setPlayerColor(String playerColor) { this.playerColor = playerColor; }
	
	private int points = 0; 
	/** 
	 * Corresponding getter
	 * @return Points of the player
	 */
	public int getPoints() { return points; }
	/** 
	 * Corresponding setter
	 * @param points Points of the player 
	 */
	public void setPoints(int points) { this.points = points; }
	
	private ArrayList<Integer> targetTreasures = new ArrayList<Integer>();
	/** 
	 * Corresponding getter
	 * @return The treasures the player is supposed to collect
	 */
	public ArrayList<Integer> getTargetTreasures() { return targetTreasures; }
	
	private ArrayList<BonusKind> boni = new ArrayList<BonusKind>();
	/** 
	 * Corresponding getter
	 * @return The Collected boni
	 */
	public ArrayList<BonusKind> getBoni() { return boni; }
	
	private BonusKind activeBonus;
	/** 
	 * Corresponding getter
	 * @return The active bonus 
	 */
	public BonusKind getActiveBonus() { return activeBonus; }
	/** 
	 * Corresponding set-method
	 * @param activeBonus Active bonus of the player
	 */
	public void setActiveBonus(BonusKind activeBonus) 
	{ 
		this.activeBonus = activeBonus; 
		boni.remove(activeBonus);
	} 
	
	/**
	 * Corresponding getter
	 * @return The next treasure the player should collect
	 */
	public Integer getNextTreasure()
	{
		if (targetTreasures != null && targetTreasures.size() > 0)
			return targetTreasures.get(targetTreasures.size() - 1);
		else
			return null;
	}
	
	//Constructors
	
	/** 
	 * Player constructor
	 * @param playerName The name of the player  
	 */
	public Player(String playerName) { this.playerName = playerName; }
	
	/** 
	 * Player constructor
	 * @param playerName The name of the player 
	 * @param playerColor The color of the player 
	 */
	public Player(String playerName, String playerColor) 
	{
		this(playerName);
		this.playerColor = playerColor;
	}
}