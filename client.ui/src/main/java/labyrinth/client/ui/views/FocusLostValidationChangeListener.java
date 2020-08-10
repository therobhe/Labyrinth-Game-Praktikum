package labyrinth.client.ui.views;

import java.util.function.Function;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * ChangeListener that validates a property on focus lost of the control
 * @author Lukas Reinhardt
 * @version 1.0
 * @param <T> Type of the value of the property
 */
public class FocusLostValidationChangeListener<T> implements ChangeListener<Boolean>
{
	//Properties
	
	private Function<T, Boolean> validationAction;
	
	private T oldValue;
	
	private Property<T> property;
	
	//Constructors
	
	/**
	 * FocusLostValidationChangeListener constructor
	 * @param property The Property that is validated
	 * @param validationAction The action to perform to validate the property
	 */
	public FocusLostValidationChangeListener(Property<T> property, Function<T, Boolean> validationAction)
	{ 
		this.validationAction = validationAction;
		this.property = property;
		oldValue = property.getValue();
	}

	//Methods
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) 
	{
		if(newValue == false)
		{
			if(validationAction.apply(property.getValue()))
				this.oldValue = property.getValue();
			else
				property.setValue(this.oldValue);
		}			
	}
}