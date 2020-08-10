package labyrinth.client.ui.resources;

import java.util.ResourceBundle;

/**
 * Class for loading localized Strings from the "Messages.properties" resource file
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class Messages 
{
	//Properties
	
	private static Messages instance = new Messages();
	
	private ResourceBundle bundle = ResourceBundle.getBundle("localization.Messages");
	
	//Constructors
	
	private Messages() { }
	
	//Methods
	
	/**
	 * Returns the localized resource for "Lobby_StartGameCommandCaption"
	 * @return The localized resource for "Lobby_StartGameCommandCaption"
	 */
	public static String getLobby_StartGameCommandCaption() 
	{ return instance.bundle.getString("Lobby_StartGameCommandCaption"); }
	
	/**
	 * Returns the localized resource for "Lobby_SignalReadyCommandCaption"
	 * @return The localized resource for "Lobby_SignalReadyCommandCaption"
	 */
	public static String getLobby_SignalReadyCommandCaption() 
	{ return instance.bundle.getString("Lobby_SignalReadyCommandCaption"); }
}
