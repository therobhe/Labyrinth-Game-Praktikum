package labyrinth.client.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import labyrinth.client.ui.viewModels.ViewModelBase;
import labyrinth.client.ui.views.ViewBase;

/**
 * Wrapper class of @see Stage to be used in @see NavigationManager
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class StageWrapper implements IStageWrapper 
{
	//Properties
	
	Stage stage;
	
	List<Parent> navigationStack = new ArrayList<Parent>();
	
	//Constructors
	
	/**
	 * StageWrapper constructor
	 * @param stage Stage used for navigation
	 */
	public StageWrapper(Stage stage) { this.stage = stage; }
	
	//Methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <ViewModelT extends ViewModelBase, ViewT extends ViewBase<ViewModelT>> void navigate(Class<ViewT> viewClass, 
			Map<String, Object> parameters) 
	{
		//Resolve View and handle navigation for VM
		Parent root = resolveView(viewClass, parameters);
		
		//Push navigated View on the navigation-stack for backwards navigation
		navigationStack.add(root);
		
		//Navigate to the View
	    navigateCore(root);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <ViewModelT extends ViewModelBase, ViewT extends ViewBase<ViewModelT>> void showDialog(Class<ViewT> viewClass, 
			Map<String, Object> parameters) 
	{
		//Resolve View and handle navigation for VM
		Parent root = resolveView(viewClass, parameters);
		
		//Show View as dialog
	    Stage dialog = new Stage();
	    dialog.setResizable(false);
	    dialog.setScene(new Scene(root));
	    dialog.initOwner(stage);
	    dialog.initModality(Modality.APPLICATION_MODAL); 
	    dialog.showAndWait();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void navigateBack()
	{
		int size = navigationStack.size();
		if(size < 2)
			return;
		
		Parent root = navigationStack.get(size - 2);
		navigationStack.remove(size - 1);
		
		//Navigate to the View
	    navigateCore(root);
	}

	private <ViewModelT extends ViewModelBase, ViewT extends ViewBase<ViewModelT>> Parent resolveView(
		Class<ViewT> viewClass, Map<String, Object> parameters) 
	{
	    ViewTuple<ViewT, ViewModelT> viewTuple = FluentViewLoader.fxmlView(viewClass).load();
	    viewTuple.getViewModel().handleNavigation(parameters);
	    viewTuple.getCodeBehind().onLoaded();
	    return viewTuple.getView();
	}
	
	private void navigateCore(Parent root)
	{
	    Scene scene = stage.getScene();
	    if(scene == null)
	    	stage.setScene(new Scene(root));
	    else
	    	scene.setRoot(root);
	}
}
