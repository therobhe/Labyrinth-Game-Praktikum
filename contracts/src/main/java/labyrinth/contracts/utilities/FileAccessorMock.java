package labyrinth.contracts.utilities;

import java.util.HashMap;
import java.util.Map;

/**
 * Mock-Implementation of @see IFileAccessor
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class FileAccessorMock implements IFileAccessor
{
	//Properties
	
	private Map<String, String> writtenTexts = new HashMap<String, String>();
	/**
	 * Corresponding getter
	 * @return The texts written to the specific files
	 */
	public Map<String, String> getWrittenTexts() { return writtenTexts; }
	
	private Map<String, String> textsToRead = new HashMap<String, String>();
	
	//Methods
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeToFile(String path, String text, boolean override) { writtenTexts.put(path, text); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String readFile(String path) { return textsToRead.containsKey(path) ? textsToRead.get(path) : null; }
	
	/**
	 * Adds the text that should be read from the specific path
	 * @param path The path
	 * @param text The text that should be read from the path
	 */
	public void addTextToRead(String path, String text) { textsToRead.put(path, text); }

}
