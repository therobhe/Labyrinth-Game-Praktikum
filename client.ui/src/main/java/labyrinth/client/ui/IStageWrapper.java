package labyrinth.client.ui;

import java.util.Map;
import labyrinth.client.ui.viewModels.ViewModelBase;
import labyrinth.client.ui.views.ViewBase;

/**
 * Interface for a wrapper class of @see Stage to be used in @see NavigationManager
 * @author Lukas Reinhardt
 * @version 1.0
 */
public interface IStageWrapper 
{
	/**
	 * Navigates to the specified Parent.
	 * @param viewClass The view to navigate to
	 * @param parameters The parameters passed to the ViewModel
	 */
	public <ViewModelT extends ViewModelBase, ViewT extends ViewBase<ViewModelT>> void navigate(Class<ViewT> viewClass,
		Map<String, Object> parameters);
	
	/**
	 * Opens the specified Parent as a new modal dialog.
	 * @param viewClass The view to show as a dialog
	 * @param parameters The parameters passed to the ViewModel
	 */
	public <ViewModelT extends ViewModelBase, ViewT extends ViewBase<ViewModelT>> void showDialog(Class<ViewT> viewClass,
		Map<String, Object> parameters);
	
	/**
	 * Navigates back to the last View navigated to
	 */
	public void navigateBack();
}
