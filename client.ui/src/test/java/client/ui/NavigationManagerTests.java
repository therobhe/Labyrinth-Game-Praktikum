package client.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import labyrinth.client.ui.NavigationManager;
import labyrinth.client.ui.views.MainMenuView;

/**
 * TestClass for @see NavigationManager
 * @author Lukas Reinhardt
 * @version 1.1
 */
public final class NavigationManagerTests
{	
	/**
	 * Tests that the NavigationManager throws the correct exception when trying to use it without initializing it before
	 */
	@Test
	public void cannotNavigateWithoutInitializationTest()
	{
		NavigationManager.initialize(null);
		assertThrows(IllegalStateException.class, () -> NavigationManager.navigate(MainMenuView.class), 
			"Expected exception of type IllegalStateException has not been thrown.");
	}
	
	/**
	 * Tests @see NavigationManager.navigate
	 */
	@Test
	public void navigateTest()
	{
		StageWrapperMock mock = new StageWrapperMock();
		NavigationManager.initialize(mock);
		
		NavigationManager.navigate(TestView.class);
		assertEquals(TestView.class, mock.getLastViewNavigatedTo(), 
			"NavigationManager did not navigate to the target View correctly.");
	}
	
	/**
	 * Tests @see NavigationManager.showDialog
	 */
	@Test
	public void showDialogTest()
	{
		StageWrapperMock mock = new StageWrapperMock();
		NavigationManager.initialize(mock);
		
		NavigationManager.showDialog(TestView.class);
		assertEquals(TestView.class, mock.getLastViewShownAsDialog(), 
			"NavigationManager did not navigate to the target View correctly.");
	}
	
	/**
	 * Tests @see NavigationManager.navigateBack
	 */
	@Test
	public void navigateBackTest()
	{
		StageWrapperMock mock = new StageWrapperMock();
		NavigationManager.initialize(mock);
		
		NavigationManager.navigateBack();
		assertTrue(mock.getWasNavigatedBack(), "NavigationManager did not navigate back.");
	}
}