package labyrinth.client.ui.viewModels;

import java.util.Map;
import de.saxsys.mvvmfx.ViewModel;

/**
 * Base-class for all ViewModels
 * @author Lukas Reinhardt
 * @version 1.0
 */
public abstract class ViewModelBase implements ViewModel
{
	/**
	 * Handles the navigation to the specific View of the ViewModel 
	 * @param parameters The navigation parameters
	 */
	public void handleNavigation(Map<String, Object> parameters) { }
}
