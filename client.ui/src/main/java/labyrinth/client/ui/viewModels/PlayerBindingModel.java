package labyrinth.client.ui.viewModels;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import labyrinth.contracts.communication.dtos.PlayerDto;
import labyrinth.contracts.entities.game.BonusKind;

/**
 * Model with bindable properties for a Player
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class PlayerBindingModel 
{
	//Properties
	
	private BonusKind activeBonus;
	/**
	 * Corresponding getter
	 * @return The active bonus of the player
	 */
	public BonusKind getActiveBonus() { return activeBonus; }
	/**
	 * Corresponding setter
	 * @param value The active bonus of the player
	 */
	public void setActiveBonus(BonusKind value) { activeBonus = value; }
	
	private static boolean isUnitTest = false;
	/**
	 * Corresponding setter
	 * @param value If a UnitTest is running
	 */
	public static void setIsUnitTest(boolean value) { isUnitTest = value; }
	
	private StringProperty playerNameProperty = new SimpleStringProperty();
	/**
	 * Corresponding getter
	 * @return playerNameProperty
	 */
	public StringProperty playerNameProperty() { return playerNameProperty; }
	/**
	 * Corresponding getter
	 * @return Value of playerNameProperty
	 */
	public String getPlayerName() { return playerNameProperty.get(); }
	/**
	 * Corresponding setter
	 * @param value Value for playerNameProperty
	 */
	public void setPlayerName(String value) { playerNameProperty.set(value); }
	
	private String color;
	/**
	 * Corresponding getter
	 * @return Value of playerNameProperty
	 */
	public String getColor() { return color; }
	/**
	 * Corresponding setter
	 * @param value Value for playerNameProperty
	 */
	public void setColor(String value) { color = value; }
	
	private IntegerProperty scoreProperty = new SimpleIntegerProperty();
	/**
	 * Corresponding getter
	 * @return playerNameProperty
	 */
	public IntegerProperty scoreProperty() { return scoreProperty; }
	/**
	 * Corresponding getter
	 * @return Value of playerNameProperty
	 */
	public int getScore() { return scoreProperty.get(); }
	/**
	 * Corresponding setter
	 * @param value Value for playerNameProperty
	 */
	public void setScore(int value) { scoreProperty.set(value); }
	
	private ObjectProperty<Image> treasureImageProperty = new SimpleObjectProperty<Image>();
	/**
	 * Corresponding getter
	 * @return treasureImageProperty
	 */
	public ObjectProperty<Image> treasureImageProperty() { return treasureImageProperty; }
	
	private IntegerProperty nextTreasureProperty = new SimpleIntegerProperty();
	/**
	 * Corresponding getter
	 * @return nextTreasureProperty
	 */
	public IntegerProperty nextTreasureProperty() { return nextTreasureProperty; }
	/**
	 * Corresponding getter
	 * @return Value of nextTreasureProperty
	 */
	public Integer getNextTreasure() { return nextTreasureProperty.get(); }
	/**
	 * Corresponding setter
	 * @param value Value for nextTreasureProperty
	 */
	public void setNextTreasure(Integer value) 
	{ 
		nextTreasureProperty.set(value); 
		if(value != null && !isUnitTest && value < 22)
		{
			Image image = new Image("assets/treasures/treasure_" + value + ".png");
			treasureImageProperty.set(image);
		}
		else
			treasureImageProperty.set(null); 
	}
	
	private IntegerProperty remainingTreasuresProperty = new SimpleIntegerProperty();
	/**
	 * Corresponding getter
	 * @return nextTreasureProperty
	 */
	public IntegerProperty remainingTreasuresProperty() { return remainingTreasuresProperty; }
	/**
	 * Corresponding getter
	 * @return Value of remainingTreasuresProperty
	 */
	public int getRemainingTreasures() { return remainingTreasuresProperty.get(); }
	/**
	 * Corresponding setter
	 * @param value Value for remainingTreasuresProperty
	 */
	public void setRemainingTreasures(int value) { remainingTreasuresProperty.set(value); }
	
	private IntegerProperty swapProperty = new SimpleIntegerProperty();
	/**
	 * Corresponding getter
	 * @return swapProperty
	 */
	public IntegerProperty swapProperty() { return swapProperty; }
	/**
	 * Corresponding getter
	 * @return Value of swapProperty
	 */
	public int getSwap() { return swapProperty.get(); }
	/**
	 * Corresponding setter
	 * @param value Value for swapProperty
	 */
	public void setSwap(int value) { swapProperty.set(value); }
	
	private IntegerProperty beamProperty = new SimpleIntegerProperty();
	/**
	 * Corresponding getter
	 * @return beamProperty
	 */
	public IntegerProperty beamProperty() { return beamProperty; }
	/**
	 * Corresponding getter
	 * @return Value of beamProperty
	 */
	public int getBeam() { return beamProperty.get(); }
	/**
	 * Corresponding setter
	 * @param value Value for beamProperty
	 */
	public void setBeam(int value) { beamProperty.set(value); }
	
	private IntegerProperty shiftTwiceProperty = new SimpleIntegerProperty();
	/**
	 * Corresponding getter
	 * @return shiftTwiceProperty
	 */
	public IntegerProperty shiftTwiceProperty() { return shiftTwiceProperty; }
	/**
	 * Corresponding getter
	 * @return Value of shiftTwiceProperty
	 */
	public int getShiftTwice() { return shiftTwiceProperty.get(); }
	/**
	 * Corresponding setter
	 * @param value Value for shiftTwiceProperty
	 */
	public void setShiftTwice(int value) { shiftTwiceProperty.set(value); }
	
	private IntegerProperty shiftSolidProperty = new SimpleIntegerProperty();
	/**
	 * Corresponding getter
	 * @return shiftSolidProperty
	 */
	public IntegerProperty shiftSolidProperty() { return shiftSolidProperty; }
	/**
	 * Corresponding getter
	 * @return Value of shiftSolidProperty
	 */
	public int getShiftSolid() { return shiftSolidProperty.get(); }
	/**
	 * Corresponding setter
	 * @param value Value for shiftSolidProperty
	 */
	public void setShiftSolid(int value) { shiftSolidProperty.set(value); }
	
	//Constructors
	
	/**
	 * PlayerBindingModel constructor
	 * @param player The Dto from which the model should be created
	 */
	public PlayerBindingModel(PlayerDto player)
	{
		setPlayerName(player.getName());
		setColor(player.getColor());
		if(player.getTreasures().getCurrent() != null)
			setNextTreasure(player.getTreasures().getCurrent());
		setRemainingTreasures(player.getTreasures().getRemaining());
		setSwap(player.getBoni().getSwap());
		setBeam(player.getBoni().getBeam());
		setShiftTwice(player.getBoni().getShiftTwice());
		setShiftSolid(player.getBoni().getShiftSolid());
	}
}
