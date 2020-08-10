package labyrinth.contracts.utilities;

/**
 * Implementation of a Logger that logs to a specific file
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class FileLogger
{
	//Properties
	
	private IFileAccessor accessor;
	
	private String filePath;
	
	//Constructors
	
	/**
	 * FileLogger constructor
	 * @param filePath the path of the file to which the messages should be logged to
	 * @param accessor The file accessor used for writing the logs
	 */
	public FileLogger(String filePath, IFileAccessor accessor) 
	{ 
		this.filePath = filePath;
		this.accessor = accessor;
	}
	
	//Methods

	/**
	 * Logs a specific message to the file
	 * @param caption Caption of the message
	 * @param text Text of the message
	 */
	public void LogMessage(String caption, String text) 
	{
		String message = caption + ": " + text + System.lineSeparator();
		accessor.writeToFile(filePath, message, false);	
	}
}
