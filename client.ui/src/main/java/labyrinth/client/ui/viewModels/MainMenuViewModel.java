package labyrinth.client.ui.viewModels;

import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import labyrinth.client.ui.NavigationManager;
import labyrinth.client.ui.views.AchievementsView;
import labyrinth.client.ui.views.GameServerSelectionView;
import labyrinth.client.ui.views.StartServerDialogView;

/**
 * ViewModel for @see MainMenuView
 * @author Lukas Reinhardt
 * @version 1.2
 */
public final class MainMenuViewModel extends ViewModelBase
{
	//Properties
	
	private Command joinGameCommand;
	/**
	 * Getter for startGameCommand
	 * @return startGameCommand
	 */
	public Command getJoinGameCommand() { return joinGameCommand; }
	
	private Command startServerCommand;
	/**
	 * Getter for startServerCommand
	 * @return startServerCommand
	 */
	public Command getStartServerCommand() { return startServerCommand; }
	
	private Command showAchievementsCommand;
	/**
	 * Getter for showAchievementsCommand
	 * @return showAchievementsCommand
	 */
	public Command getShowAchievementsCommand() { return showAchievementsCommand; }
	
	private Command exitGameCommand;
	/**
	 * Getter for exitGameCommand
	 * @return exitGameCommand
	 */
	public Command getExitGameCommand() { return exitGameCommand; }
	
	//Constructors
	
	/**
	* Constructor of @see MainMenuViewModel
	*/
	public MainMenuViewModel() { initializeCommands(); }
	
	//Methods
	
	private void initializeCommands()
	{
		joinGameCommand = new DelegateCommand(() -> new Action() 
		{
			@Override
			protected void action() { joinGame();}
		});
		
		startServerCommand = new DelegateCommand(() -> new Action()
		{
			@Override
			protected void action() { startServer();}
		});

		showAchievementsCommand = new DelegateCommand(() -> new Action()
		{
			@Override
			protected void action() { showAchievements(); }
		});
		
		exitGameCommand = new DelegateCommand(() -> new Action()
		{
			@Override
			protected void action() { exitGame(); }
		});
	}
	
	private void joinGame() { NavigationManager.navigate(GameServerSelectionView.class); }
	
	private void startServer() { NavigationManager.showDialog(StartServerDialogView.class); }
	
	private void showAchievements() { NavigationManager.navigate(AchievementsView.class); }
	
	private void exitGame() { System.exit(0); }
}
