package client.ui;

import java.util.Map;
import labyrinth.client.ui.IStageWrapper;
import labyrinth.client.ui.viewModels.ViewModelBase;
import labyrinth.client.ui.views.ViewBase;

/**
 * Mock for @see StageWrapper
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class StageWrapperMock implements IStageWrapper
{
	private boolean wasNavigatedBack = false;
	/**
	 * Corresponding getter
	 * @return If navigateBack was executed
	 */
	public boolean getWasNavigatedBack() { return wasNavigatedBack; }
	
	private Class<?> lastViewNavigatedTo;
	/**
	 * Corresponding getter
	 * @return Class of the last view navigated to
	 */
	public Class<?> getLastViewNavigatedTo() { return lastViewNavigatedTo; }
	
	private Class<?> lastViewShownAsDialog;	
	/**
	 * Corresponding getter
	 * @return Class of the last view shown as a dialog
	 */
	public Class<?> getLastViewShownAsDialog() { return lastViewShownAsDialog; }
	
	private Map<String, Object> parameters;
	/**
	 * Corresponding getter
	 * @return The last navigation parameters
	 */
	public Map<String, Object> getParameters() { return parameters; }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <ViewModelT extends ViewModelBase, ViewT extends ViewBase<ViewModelT>> void navigate(Class<ViewT> viewClass, 
		Map<String, Object> parameters) 
	{ 
		lastViewNavigatedTo = viewClass; 
		this.parameters = parameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <ViewModelT extends ViewModelBase, ViewT extends ViewBase<ViewModelT>> void showDialog(Class<ViewT> viewClass, 
			Map<String, Object> parameters) 
	{ 
		lastViewShownAsDialog = viewClass; 
		this.parameters = parameters;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void navigateBack() { wasNavigatedBack = true; }
}
