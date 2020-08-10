package labyrinth.client.ui;

import java.util.Map;
import labyrinth.client.ui.resources.Errors;
import labyrinth.client.ui.viewModels.ViewModelBase;
import labyrinth.client.ui.views.ViewBase;

/**
 * Manager class for navigating to different views
 * @author Lukas Reinhardt
 * @version 1.1
 */
public final class NavigationManager 
{
	//Properties
	
	private IStageWrapper stage;
	
	private static NavigationManager instance = new NavigationManager();
	
	//Constructors
	
	private NavigationManager() {}
	
	//Methods
	
	/**
	 * Initializes the NavigationManager with the specific stage in which the navigation should take place
	 * @param stage The stage in which the navigation should take place
	 */
	public static void initialize(IStageWrapper stage) { instance.stage = stage; }
	
	/**
	 * Navigates to a specific View
	 * @param viewClass Class information of the target view
	 * @param parameters Parameters transfered to the ViewModel 
	 * @param <ViewT> Type of the View
	 * @param <ViewModelT> Type of the corresponding ViewModel
	 * @throws IllegalStateException when the NavigationManager has not been initialized with a stage
	 */
	public static <ViewModelT extends ViewModelBase, ViewT extends ViewBase<ViewModelT>> void navigate(
		Class<ViewT> viewClass, Map<String, Object> parameters)
	{
		//Test if the NaviagtionManager has been initialized
		if(instance.stage == null)
			throw new IllegalStateException(Errors.getNavigationManager_NotInitialized());
	    
		//Navigate to the View
	    instance.stage.navigate(viewClass, parameters);
	}
	
	/**
	 * Navigates to a specific View
	 * @param viewClass Class information of the target view
	 * @param <ViewT> Type of the View
	 * @param <ViewModelT> Type of the corresponding ViewModel
	 * @throws IllegalStateException when the NavigationManager has not been initialized with a stage
	 */
	public static <ViewModelT extends ViewModelBase, ViewT extends ViewBase<ViewModelT>> void navigate(
		Class<ViewT> viewClass)
	{ navigate(viewClass, null); }
	
	/**
	 * Opens the specified View in a modal dialog
	 * @param viewClass Class information of the target view
	 * @param parameters Parameters transfered to the ViewModel 
	 * @param <ViewT> Type of the View
	 * @param <ViewModelT> Type of the corresponding ViewModel
	 * @throws IllegalStateException when the NavigationManager has not been initialized with a stage
	 */
	public static <ViewModelT extends ViewModelBase, ViewT extends ViewBase<ViewModelT>> void showDialog(
		Class<ViewT> viewClass, Map<String, Object> parameters)
	{
		//Test if the NaviagtionManager has been initialized
		if(instance.stage == null)
			throw new IllegalStateException(Errors.getNavigationManager_NotInitialized());
	    
	    //Open View in a dialog
	    instance.stage.showDialog(viewClass, parameters);
	}
	
	/**
	 * Opens the specified View in a modal dialog
	 * @param viewClass Class information of the target view
	 * @param <ViewT> Type of the View
	 * @param <ViewModelT> Type of the corresponding ViewModel
	 * @throws IllegalStateException when the NavigationManager has not been initialized with a stage
	 */
	public static <ViewModelT extends ViewModelBase, ViewT extends ViewBase<ViewModelT>> void showDialog(
			Class<ViewT> viewClass)
	{ showDialog(viewClass, null); }
	
	/**
	 * Navigates back to the last View navigated to
	 */
	public static void navigateBack() { instance.stage.navigateBack(); }
}
