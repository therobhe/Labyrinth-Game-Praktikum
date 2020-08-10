package labyrinth.contracts.resources;

import java.util.ResourceBundle;

/**
* Class for loading localized Strings from the "Errors.properties" resource file
* @author Mira Almohsen
* @version 1.0
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
	 * Returns the localized resource for "Tile_CannotPlaceBonusOnTileWithPlayerBase"
	 * @return The localized resource for "Tile_CannotPlaceBonusOnTileWithPlayerBase"
	 */
	public static String getTile_CannotPlaceBonusOnTileWithPlayerBase()
	{ return instance.bundle.getString("Tile_CannotPlaceBonusOnTileWithPlayerBase"); }

	/**
	 * Returns the localized resource for "Tile_CannotPlaceTreasureOnTileWithPlayerBase"
	 * @return The localized resource for "Tile_CannotPlaceTreasureOnTileWithPlayerBase"
	 */
	public static String getTile_CannotPlaceTreasureOnTileWithPlayerBase() 
	{ return instance.bundle.getString("Tile_CannotPlaceTreasureOnTileWithPlayerBase"); }
	
	/**
	 * Returns the localized resource for "GameBoard_CoordinatesOutsideOfBoard"
	 * @return The localized resource for "GameBoard_CoordinatesOutsideOfBoard"
	 */
	public static String getGameBoard_CoordinatesOutsideOfBoard() 
	{ return instance.bundle.getString("GameBoard_CoordinatesOutsideOfBoard"); }
	
	/**
	 * Returns the localized resource for "Lobby_UnknownUser"
	 * @return The localized resource for "Lobby_UnknownUser"
	 */
	public static String getLobby_UnknownUser() 
	{ return instance.bundle.getString("Lobby_UnknownUser"); }
	
	/**
	 * Returns the localized resource for "RequestBase_NotInitialized"
	 * @return The localized resource for "RequestBase_NotInitialized"
	 */
	public static String getRequestBase_NotInitialized() 
	{ return instance.bundle.getString("RequestBase_NotInitialized"); }
}
