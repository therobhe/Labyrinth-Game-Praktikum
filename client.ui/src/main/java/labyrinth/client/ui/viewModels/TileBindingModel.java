package labyrinth.client.ui.viewModels;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import labyrinth.contracts.communication.dtos.TileDto;
import labyrinth.contracts.entities.Coordinates;
import labyrinth.contracts.entities.game.BonusKind;

/**
 * Model with bindable properties for a Tile
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class TileBindingModel 
{
	//Properties
	
	private static boolean isUnitTest = false;
	/**
	 * Corresponding setter
	 * @param value If a UnitTest is running
	 */
	public static void setIsUnitTest(boolean value) { isUnitTest = value; }
	
	private ObjectProperty<Image> tileImageProperty = new SimpleObjectProperty<Image>();
	/**
	 * Corresponding getter
	 * @return tileImageProperty
	 */
	public ObjectProperty<Image> tileImageProperty() { return tileImageProperty; }

	private int tileType;
	/**
	 * Corresponding getter
	 * @return The type of the Tile
	 */
	public int getTileType() { return tileType; }
	/**
	 * Corresponding setter
	 * @param value The type of the Tile
	 */
	public void setTileType(int value) 
	{ 
		if(!isUnitTest)
		{
			Image image = new Image("assets/tiles/tile_" + value + ".png");
			tileImageProperty.set(image);
		}
		tileType = value;
	}
	
	private ObjectProperty<Image> treasureImageProperty = new SimpleObjectProperty<Image>();
	/**
	 * Corresponding getter
	 * @return treasureImageProperty
	 */
	public ObjectProperty<Image> treasureImageProperty() { return treasureImageProperty; }
	
	private Integer treasureType;
	/**
	 * Corresponding getter
	 * @return The type of the Treasure placed on the Tile
	 */
	public Integer getTreasureType() { return treasureType; }
	/**
	 * Corresponding setter
	 * @param value The type of the Treasure placed on the Tile
	 */
	public void setTreasureType(Integer value) 
	{ 
		if(value != null && !isUnitTest && value < 22)
		{
			Image image = new Image("assets/treasures/treasure_" + value + ".png");
			treasureImageProperty.set(image);
		}
		else
			treasureImageProperty.set(null); 
		treasureType = value;
	}
	
	private ObjectProperty<Image> bonusImageProperty = new SimpleObjectProperty<Image>();
	/**
	 * Corresponding getter
	 * @return bonusImageProperty
	 */
	public ObjectProperty<Image> bonusImageProperty() { return bonusImageProperty; }
	
	private BonusKind bonusKind;
	/**
	 * Corresponding getter
	 * @return The kind of the bonus placed on the Tile
	 */
	public BonusKind getBonusKind() { return bonusKind; }
	/**
	 * Corresponding setter
	 * @param value The kind of the bonus placed on the Tile
	 */
	public void setBonusKind(BonusKind value) 
	{ 
		bonusKind = value; 
		if(!isUnitTest)
		{
			if(value != null)
			{
				String imageName = null;
				switch(value)
				{
					case BEAM:
						imageName = "beam";
						break;
					case SWAP:
						imageName = "swap";
						break;
					case SHIFT_TWICE:
						imageName = "shiftTwice";
						break;
					case SHIFT_SOLID:
						imageName = "shiftSolid";
						break;
				}
				Image image = new Image("assets/boni/" + imageName + ".png");
				bonusImageProperty.set(image);
			}
			else
				bonusImageProperty.set(null);
		}
	}
	
	private ObjectProperty<Color> playerColorProperty = new SimpleObjectProperty<Color>();
	/**
	 * Corresponding getter
	 * @return  playerColorProperty
	 */
	public ObjectProperty<Color> playerColorProperty() { return playerColorProperty; }
	
	private BooleanProperty playerCircleVisibleProperty = new SimpleBooleanProperty();
	/**
	 * Corresponding getter
	 * @return playerCircleVisibleProperty
	 */
	public BooleanProperty playerCircleVisibleProperty () { return playerCircleVisibleProperty; }
	
	private String playerColor;
	/**
	 * Corresponding getter
	 * @return The color of the Player placed on the Tile
	 */
	public String getPlayerColor() { return playerColor; }
	/**
	 * Corresponding setter
	 * @param value The color of the Player placed on the Tile
	 */
	public void setPlayerColor(String value) 
	{ 
		playerColor = value; 
		if(value != null)
		{
			playerColorProperty.set(Color.valueOf(value));
			playerCircleVisibleProperty.set(true);
		}
		else
		{
			playerColorProperty.set(null);
			playerCircleVisibleProperty.set(false);
		}
	}
	
	private String player;
	/**
	 * Corresponding getter
	 * @return The name of the Player placed on the Tile
	 */
	public String getPlayer() { return player; }
	/**
	 * Corresponding setter
	 * @param value The name of the Player placed on the Tile
	 */
	public void setPlayer(String value)  { player = value; }
	
	private ObjectProperty<Color> playerBaseColorProperty = new SimpleObjectProperty<Color>();
	/**
	 * Corresponding getter
	 * @return playerBaseCircleVisibleProperty
	 */
	public ObjectProperty<Color> playerBaseColorProperty() { return playerBaseColorProperty; }
	
	private BooleanProperty playerBaseCircleVisibleProperty = new SimpleBooleanProperty();
	/**
	 * Corresponding getter
	 * @return playerBaseCircleVisibleProperty
	 */
	public BooleanProperty playerBaseCircleVisibleProperty () { return playerBaseCircleVisibleProperty; }
	
	private String playerBaseColor;
	/**
	 * Corresponding getter
	 * @return The color of the PlayerBases player placed on the Tile
	 */
	public String getPlayerBaseColor() { return playerBaseColor; }
	/**
	 * Corresponding setter
	 * @param value The color of the PlayerBases player placed on the Tile
	 */
	public void setPlayerBaseColor(String value) 
	{ 
		playerBaseColor = value; 
		if(value != null)
		{
			playerBaseColorProperty.set(Color.valueOf(value));
			playerBaseCircleVisibleProperty.set(true);
		}
		else
		{
			playerBaseColorProperty.set(null);
			playerBaseCircleVisibleProperty.set(false);
		}
	}
	
	private Coordinates coordinates;
	/**
	 * Corresponding setter
	 * @return The coordinates of the Tile
	 */
	public Coordinates getCoordinates() { return coordinates; }
	
	//Constructors
	
	/**
	 * TileBindingModel constructor
	 * @param tile The Dto from which the model should be created
	 */
	public TileBindingModel(TileDto tile)
	{
		coordinates = new Coordinates(tile.getX(), tile.getY());
		setTileType(tile.getType());
		setTreasureType(tile.getTreasure());
		setBonusKind(tile.getBoni());
		bonusKind = tile.getBoni();
		player = tile.getPlayer();
	}
}