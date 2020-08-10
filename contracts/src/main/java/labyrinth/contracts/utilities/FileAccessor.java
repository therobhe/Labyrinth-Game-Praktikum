package labyrinth.contracts.utilities;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Implementation of @see IFileAccessor
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class FileAccessor implements IFileAccessor
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeToFile(String path, String text, boolean override) 
	{
		Path file = Paths.get(path);
		try 
		{
			if (!Files.exists(file, LinkOption.NOFOLLOW_LINKS))
			    Files.createFile(file);
			if(override)
				Files.write(file, text.getBytes("UTF-8"));
			else 
				Files.write(file, text.getBytes("UTF-8"), StandardOpenOption.APPEND);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String readFile(String path)
	{
		String result = null;
		File file = new File(path);
		if(!file.exists())
			return null;
		
		try 
		{
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			result = new String(encoded, StandardCharsets.UTF_8);		
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return result;
	}
}
