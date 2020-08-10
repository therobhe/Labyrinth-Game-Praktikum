package labyrinth.contracts.communication.dtos;

import labyrinth.contracts.entities.game.BonusKind;

/**
 * Dto for a Tile/Field of the GameBoard
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class TileDto
{
	//Properties
	
	private int x;
	/**
	 * Corresponding getter
	 * @return The X-Coordinate of the field
	 */
	public int getX() { return x; }
	
	private int y;
	/**
	 * Corresponding getter
	 * @return The Y-Coordinate of the field
	 */
	public int getY() { return y; }
	
	private int type;
	/**
	 * Corresponding getter
	 * @return The type of the field
	 */
	public int getType() { return type; }
	/**
	 * Corresponding setter
	 * @param value The type of the field
	 */
	public void setType(int value) { type = value; }
	
	private Integer treasure;
	/**
	 * Corresponding getter
	 * @return The type of the treasure placed on the field
	 */
	public Integer getTreasure() { return treasure; }
	/**
	 * Corresponding setter
	 * @param value The type of the treasure placed on the field
	 */
	public void setTreasure(Integer value) { treasure = value; }
	
	private BonusKind boni;
	/**
	 * Corresponding getter
	 * @return The type of the bonus placed on the field
	 */
	public BonusKind getBoni() { return boni; }
	/**
	 * Corresponding setter
	 * @param value The type of the bonus placed on the field
	 */
	public void setBoni(BonusKind value) { boni = value; }
	
	private String player;
	/**
	 * Corresponding getter
	 * @return The player placed on the field
	 */
	public String getPlayer() { return player; }
	/**
	 * Corresponding setter
	 * @param value The player placed on the field
	 */
	public void setPlayer(String value) { player = value; }
	
	private String playerBase;
	/**
	 * Corresponding getter
	 * @return The playerBase placed on the field
	 */
	public String getPlayerBase() { return playerBase; }
	/**
	 * Corresponding setter
	 * @param value The playerBase placed on the field
	 */
	public void setPlayerBase(String value) { playerBase = value; }
	
	//Constructors
	
	/**
	 * TileDto constructors
	 * @param x The X-Coordinate of the field
	 * @param y The Y-Coordinate of the field
	 * @param type The type of the field
	 */
	public TileDto(int x, int y, int type)
	{
		this.x = x;
		this.y = y;
		this.type = type;
	}
}
