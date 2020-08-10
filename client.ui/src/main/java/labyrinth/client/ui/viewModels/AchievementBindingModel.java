package labyrinth.client.ui.viewModels;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import labyrinth.contracts.entities.game.Achievement;

/**
 * ViewModel for @see GameConfigurationView
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class AchievementBindingModel 
{
	//Properties
	
	private StringProperty nameProperty = new SimpleStringProperty();
	/**
	 * Corresponding getter
	 * @return nameProperty
	 */
	public StringProperty nameProperty() { return nameProperty; }
	
	private StringProperty descriptionProperty = new SimpleStringProperty();
	/**
	 * Corresponding getter
	 * @return descriptionProperty
	 */
	public StringProperty descriptionProperty() { return descriptionProperty; }
	
	private StringProperty progressTextProperty = new SimpleStringProperty();
	/**
	 * Corresponding getter
	 * @return progressTextProperty
	 */
	public StringProperty progressTextProperty() { return progressTextProperty; }
	
	private DoubleProperty progressProperty = new SimpleDoubleProperty();
	/**
	 * Corresponding getter
	 * @return progressProperty
	 */
	public DoubleProperty progressProperty() { return progressProperty; }
	
	//Constructors
	
	/**
	 * AchievementBindingModel constructor
	 * @param achievement The achievement from which to create the binding model
	 */
	public AchievementBindingModel(Achievement achievement)
	{
		nameProperty.set(achievement.getName());
		descriptionProperty.set(achievement.getDescription());
		progressTextProperty.set(achievement.getProgress() + "/" + achievement.getRequired());
		progressProperty.set(achievement.getProgress() / achievement.getRequired() * 100);
	}	
}
