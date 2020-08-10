package labyrinth.client.ui.views;

import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import labyrinth.client.ui.viewModels.GameEndViewModel;

/**
 * Code behind class of the view for the GameEndViewModel
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class GameEndView extends ViewBase<GameEndViewModel>
{
	//Properties
	
	@FXML
	private Label player1NameLabel;
	@FXML
	private Label player1ScoreLabel;
	
	@FXML
	private Label player2NameLabel;
	@FXML
	private Label player2ScoreLabel;
	
	@FXML
	private VBox player3VBox;
	@FXML
	private Label player3NameLabel;
	@FXML
	private Label player3ScoreLabel;
	
	@FXML
	private VBox player4VBox;
	@FXML
	private Label player4NameLabel;
	@FXML
	private Label player4ScoreLabel;
	
	//ViewModel
	
	@InjectViewModel
	private GameEndViewModel viewModel;
	
	//Methods
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onLoaded()
	{
		player1NameLabel.textProperty().bind(viewModel.player1NameProperty());
		player2NameLabel.textProperty().bind(viewModel.player2NameProperty());
		player3NameLabel.textProperty().bind(viewModel.player3NameProperty());
		player4NameLabel.textProperty().bind(viewModel.player4NameProperty());
		
		player1ScoreLabel.textProperty().bind(viewModel.player1ScoreProperty().asString());
		player2ScoreLabel.textProperty().bind(viewModel.player2ScoreProperty().asString());
		player3ScoreLabel.textProperty().bind(viewModel.player3ScoreProperty().asString());
		player4ScoreLabel.textProperty().bind(viewModel.player4ScoreProperty().asString());
		
		player3VBox.visibleProperty().bind(viewModel.player3VisibleProperty());
		player4VBox.visibleProperty().bind(viewModel.player4VisibleProperty());
	}
	
	@FXML
	private void next()	{ viewModel.getNavigateToLobbyCommand().execute(); }
}
