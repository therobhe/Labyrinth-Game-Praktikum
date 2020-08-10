package labyrinth.client.ui.views;

import java.net.URL;
import java.util.ResourceBundle;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import labyrinth.client.ui.viewModels.MainMenuViewModel;

/**
 * Code behind class of the view for the MainMenuView
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class MainMenuView extends ViewBase<MainMenuViewModel>
{
	//Controls
	
	@FXML
	private Button startGameButton;		
	@FXML
	private Button startGameAsAIButton;
	@FXML
	private Button startServerButton;
	@FXML
	private Button showAchievementsButton;	
	@FXML
	private Button exitGameButton;
	
	//ViewModel
	
	@InjectViewModel
	private MainMenuViewModel viewModel;
	
	//Methods
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) { initializeCommandBindings(); }
	
	private void initializeCommandBindings()
	{
		startGameButton.disableProperty().bind(viewModel.getJoinGameCommand().executableProperty().not());
		showAchievementsButton.disableProperty().bind(viewModel.getShowAchievementsCommand().executableProperty().not());
		exitGameButton.disableProperty().bind(viewModel.getExitGameCommand().executableProperty().not());
	}
	
    @FXML
    private void joinGame() { viewModel.getJoinGameCommand().execute(); }
    
    @FXML
    private void startServer() { viewModel.getStartServerCommand().execute(); }
    
    @FXML
    private void showAchievements() { viewModel.getShowAchievementsCommand().execute(); }
    
    @FXML
    private void exitGame() { viewModel.getExitGameCommand().execute(); }
}
