package labyrinth.contracts.utilities;

/**
 * Helper class containing several helper methods 
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class HelperMethods 
{
	//Constructor
	
	private HelperMethods() {}
	
	//Methods
	
	/**
	 * Tests whether a String can be parsed as Int
	 * @param text The String to parse
	 * @return Whether the String can be parsed as Int
	 */
	public static boolean canParseInt(String text) { return text.matches("\\d*"); }
	
	/**
	 * Tests whether a String can be parsed as Double
	 * @param text The String to parse
	 * @return Whether the String can be parsed as Double
	 */
	public static boolean canParseDouble(String text) { return text.matches("\\d*(\\.\\d*)?");  }	
}
