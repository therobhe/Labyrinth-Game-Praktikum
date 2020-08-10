package labyrinth.contracts.entities;

/**
 * Size of a two dimensional field
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class Coordinates 
{
	//Properties
	
	private int x;
	/** 
	 * Corresponding get-method 
	 * @return Size of x direction
	 */
	public int getX() { return x; }
	
	private int y;
	/** 
	 * Corresponding get-method 
	 * @return Size of y direction
	 */
	public int getY() { return y; }
	
	//Constructors
	
	/** 
	 * Size constructor
	 * @param x Size of x direction
	 * @param y Size of y direction
	 */
	public Coordinates(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
}
