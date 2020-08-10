package labyrinth.contracts.entities.game;

/**
 * An achievement a player gets for specific actions in the game
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class Achievement 
{
	//Properties
	
	private String name;
	/**
	 * Corresponding getter
	 * @return The name of the achievement 
	 */
	public String getName() { return name; }
	
	private String description;
	/**
	 * Corresponding getter
	 * @return The description of the achievement
	 */
	public String getDescription() { return description; }
	
	private int required;
	/**
	 * Corresponding getter
	 * @return The requires amount of x for getting the achievement
	 */
	public int getRequired() { return required; }
	
	private int progress;
	/**
	 * Corresponding getter
	 * @return The current progress of the achievement
	 */
	public int getProgress() { return progress; }
	
	//Constructors
	
	/**
	 * Achievement constructor
	 * @param name The name of the achievement
	 * @param description The description of the achievement
	 * @param required The requires amount of x for getting the achievement
	 */
	public Achievement(String name, String description, int required)
	{
		this.name = name;
		this.description = description;
		this.required = required;
	}
	
	//Methods
	
	/**
	 * Adds progress to the achievement
	 * @param value The progress to add
	 */
	public void addProgress(int value)
	{
		if(progress + value > required)
			progress = required;
		else
			progress +=  value;
	}
}
