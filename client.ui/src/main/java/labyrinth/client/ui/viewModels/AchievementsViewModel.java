package labyrinth.client.ui.viewModels;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import labyrinth.client.ui.ClientAchievementManager;
import labyrinth.client.ui.NavigationManager;

/**
 * ViewModel for @see AchievementsView
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class AchievementsViewModel extends ViewModelBase
{
	//Properties
	
	private List<AchievementBindingModel> playedGamesAchievements = new ArrayList<AchievementBindingModel>();
	/**
	 * Corresponding getter
	 * @return The list of all achievements for played games
	 */
	public List<AchievementBindingModel> getPlayedGamesAchievements() { return playedGamesAchievements; }
	
	private List<AchievementBindingModel> wonGamesAchievements = new ArrayList<AchievementBindingModel>();
	/**
	 * Corresponding getter
	 * @return The list of all achievements for won games
	 */
	public List<AchievementBindingModel> getWonGamesAchievements() { return wonGamesAchievements; }
	
	private List<AchievementBindingModel> aquiredScoreAchievements = new ArrayList<AchievementBindingModel>();
	/**
	 * Corresponding getter
	 * @return The list of all achievements for aquired points
	 */
	public List<AchievementBindingModel> getAquiredScoreAchievements() { return aquiredScoreAchievements; }
	
	private Command navigateBackCommand = new DelegateCommand(() -> new Action() 
	{
		@Override
		protected void action() { NavigationManager.navigateBack(); }
	});
	/**
	 * Getter for navigateBackCommand
	 * @return navigateBackCommand
	 */
	public Command getNavigateBackCommand() { return navigateBackCommand; }
	
	//Constructors
	
	/**
	 * AchievementsViewModel constructor
	 */
	public AchievementsViewModel()
	{
		//add achievements for played games
		playedGamesAchievements.addAll(ClientAchievementManager.getPlayedGamesAchievements().stream().map(
			achievement -> new AchievementBindingModel(achievement)).collect(Collectors.toList()));
		//add achievements for won games
		wonGamesAchievements.addAll(ClientAchievementManager.getWonGamesAchievements().stream().map(
			achievement -> new AchievementBindingModel(achievement)).collect(Collectors.toList()));
		//add achievements for aquired score
		aquiredScoreAchievements.addAll(ClientAchievementManager.getAquiredScoreAchievements().stream().map(
			achievement -> new AchievementBindingModel(achievement)).collect(Collectors.toList()));
	}
}
