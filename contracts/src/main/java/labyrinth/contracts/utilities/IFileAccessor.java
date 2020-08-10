package labyrinth.contracts.utilities;

/**
 * Interface for a class for accessing files and folders
 * @author Lukas Reinhardt
 * @version 1.0
 */
public interface IFileAccessor 
{
	/**
	 * Writes text to a file
	 * @param path Path of the file
	 * @param text The text to write
	 * @param override If the existing content of the file should be overridden
	 */
	public void writeToFile(String path, String text, boolean override);
	
	/**
	 * Writes text to a file
	 * @param path Path of the file
	 */
	public String readFile(String path);
}
