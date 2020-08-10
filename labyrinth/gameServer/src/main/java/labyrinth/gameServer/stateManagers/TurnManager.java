package labyrinth.gameServer.stateManagers;

import java.util.concurrent.TimeUnit;
import labyrinth.contracts.communication.dtos.responses.CurrentPlayerResponseDto;
import labyrinth.gameServer.GameServerManager;

/**
 * ManagerClass for managing the current Turn of a player
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class TurnManager 
{
	//Properties
	
	private int gameLengthLimit;
	
	private int turnCounter = 0;
	
	private String firstPlayer;
	
	private String activePlayer;
	/**
	 * Corresponding getter
	 * @return The currently active player
	 */
	public String getActivePlayer() { return activePlayer; }
	
	private int shiftedCount = 0;
	/**
	 * Corresponding getter
	 * @return The count how often the player has already shifted the field
	 */
	public int getShiftedCount() { return shiftedCount; }
	/**
	 * Corresponding setter
	 * @param value The count how often the player has already shifted the field
	 */
	public void setShiftedCount(int value) {  shiftedCount = value; }
	
	private boolean hasMoved = false;
	/**
	 * Corresponding getter
	 * @return If the player has already moved this turn
	 */
	public boolean getHasMoved() { return hasMoved; }
	/**
	 * Corresponding setter
	 * @param value If the player has already moved this turn
	 */
	public void setHasMoved(boolean value) { hasMoved = value; }
	
	private boolean hasUsedBonus = false;
	/**
	 * Corresponding getter
	 * @return If the player has already used a bonus this turn
	 */
	public boolean getHasUsedBonus() { return hasUsedBonus; }
	/**
	 * Corresponding setter
	 * @param value If the player has already used a bonus this turn
	 */
	public void setHasUsedBonus(boolean value) { hasUsedBonus= value; }
	
	//Constructors
	
	/**
	 * TurnManager constructor
	 * @param gameLengthLimit The maximum amount of rounds
	 */
	public TurnManager(int gameLengthLimit) { this.gameLengthLimit = gameLengthLimit; }
	
	//Methods
	
	/**
	 * Switches the turn to a new player
	 * @param activePlayer The new active player
	 * @param useDelay Should the Response be send with a delay
	 */
	public void switchTurn(String activePlayer, boolean useDelay)
	{
		shiftedCount = 0;
		hasMoved = false;
		hasUsedBonus = false;
		this.activePlayer = activePlayer;
		
		//Send current player response
		sendCurrentPlayerResponse(activePlayer, useDelay);
		
		if(firstPlayer != null && firstPlayer.equals(activePlayer))
			turnCounter++;
		
		//End game if game length maximum reached
		if(turnCounter == gameLengthLimit)
			GameBoardStateManager.endGame();
		
		//Set the first player of the round
		if(firstPlayer == null)
			firstPlayer = activePlayer;
	}	
	
	/**
	 * Returns if it's a players turn
	 * @param player The player on which the check should be performed
	 */
	public boolean getIsActivePlayer(String player) { return player == activePlayer; }
	
	private void sendCurrentPlayerResponse(String activePlayer, boolean useDelay)
	{
		try 
		{
			if(useDelay)
				TimeUnit.SECONDS.sleep(1);
			GameServerManager.getRunningServer().broadcastResponse(new CurrentPlayerResponseDto(activePlayer));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
