package client.ui.viewModels;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import client.ui.StageWrapperMock;
import de.saxsys.mvvmfx.utils.commands.Command;
import labyrinth.client.ui.NavigationManager;
import labyrinth.client.ui.viewModels.MainMenuViewModel;
import labyrinth.client.ui.views.GameServerSelectionView;
import labyrinth.client.ui.views.StartServerDialogView;

/**
 * TestClass for @see MainMenuViewModel
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class MainMenuViewModelTests 
{
	/**
	 * Tests the correct execution of @see MainMenuViewModel.joinGameCommand
	 */
	@Test
	public void joinGameCommandTest() 
	{ 
		MainMenuViewModel viewModel = new MainMenuViewModel();
		Command command = viewModel.getJoinGameCommand();		
		StageWrapperMock mock = new StageWrapperMock();
		NavigationManager.initialize(mock);
		command.execute();
		
		assertEquals(GameServerSelectionView.class, mock.getLastViewNavigatedTo(), 
			"The execution of the command did not result in navigating to the GameServerSelectionView.");
	} 
	
	/**
	 * Tests the correct execution of @see MainMenuViewModel.startServerCommand
	 */
	@Test
	public void startServerCommandTest() 
	{ 
		MainMenuViewModel viewModel = new MainMenuViewModel();
		StageWrapperMock mock = new StageWrapperMock();
		NavigationManager.initialize(mock);
		viewModel.getStartServerCommand().execute();
		assertEquals(StartServerDialogView.class, mock.getLastViewShownAsDialog());
	} 
}
