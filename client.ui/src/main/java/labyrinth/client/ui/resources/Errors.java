package labyrinth.client.ui.resources;

import java.util.ResourceBundle;

/**
 * Class for loading localized Strings from the "Error.properties" resource file
 * @author Lukas Reinhardt
 * @version 1.2
 */
public final class Errors 
{
	//Properties
	
	private static Errors instance = new Errors();
	
	private ResourceBundle bundle = ResourceBundle.getBundle("localization.Errors");
	
	//Constructors
	
	private Errors() { }
	
	//Methods
	
	/**
	 * Returns the localized resource for "NavigationManager_NotInitialized"
	 * @return The localized resource for "NavigationManager_NotInitialized"
	 */
	public static String getNavigationManager_NotInitialized() 
	{ return instance.bundle.getString("NavigationManager_NotInitialized"); }
	
	/**
	 * Returns the localized resource for "GameServerSelectionViewModel_MissingParameter"
	 * @return The localized resource for "GameServerSelectionViewModel_MissingParameter"
	 */
	public static String getGameServerSelectionViewModel_MissingParameter() 
	{ return instance.bundle.getString("GameServerSelectionViewModel_MissingParameter"); }
	
	/**
	 * Returns the localized resource for "ConnectToGameServerDialogViewModel_MissingParameter"
	 * @return The localized resource for "ConnectToGameServerDialogViewModel_MissingParameter"
	 */
	public static String getConnectToGameServerDialogViewModel_MissingParameter() 
	{ return instance.bundle.getString("ConnectToGameServerDialogViewModel_MissingParameter"); }
	
	/**
	 * Returns the localized resource for "LobbyViewModel_MissingParameter"
	 * @return The localized resource for "LobbyViewModel_MissingParameter"
	 */
	public static String getLobbyViewModel_MissingParameter() 
	{ return instance.bundle.getString("LobbyViewModel_MissingParameter"); }
	
	/**
	 * Returns the localized resource for "GameViewModel_MissingParameter"
	 * @return The localized resource for "GameViewModel_MissingParameter"
	 */
	public static String getGameViewModel_MissingParameter() 
	{ return instance.bundle.getString("GameViewModel_MissingParameter"); }
	
	/**
	 * Returns the localized resource for "GameEndViewModel_MissingParameter"
	 * @return The localized resource for "GameEndViewModel_MissingParameter"
	 */
	public static String getGameEndViewModel_MissingParameter() 
	{ return instance.bundle.getString("GameEndViewModel_MissingParameter"); }
}
