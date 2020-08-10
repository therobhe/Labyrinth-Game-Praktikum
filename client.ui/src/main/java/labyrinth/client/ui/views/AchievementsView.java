package labyrinth.client.ui.views;

import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import labyrinth.client.ui.viewModels.AchievementsViewModel;

/**
 * Code behind class of the view for the AchievementsView
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class AchievementsView extends ViewBase<AchievementsViewModel>
{
	//Controls
	
	@FXML
	private Button navigateBackButton;
	
	@FXML
	private VBox achievementsVBox;
	
	@FXML
	private VBox playedGamesAchievement1VBox;
	@FXML
	private Label playedGamesAchievement1NameLabel;
	@FXML
	private Label playedGamesAchievement1DescriptionLabel;
	@FXML
	private Label playedGamesAchievement1ProgressLabel;
	@FXML
	private ProgressBar playedGamesAchievement1ProgressBar;
	
	@FXML
	private VBox playedGamesAchievement2VBox;
	@FXML
	private Label playedGamesAchievement2NameLabel;
	@FXML
	private Label playedGamesAchievement2DescriptionLabel;
	@FXML
	private Label playedGamesAchievement2ProgressLabel;
	@FXML
	private ProgressBar playedGamesAchievement2ProgressBar;
	
	@FXML
	private VBox playedGamesAchievement3VBox;
	@FXML
	private Label playedGamesAchievement3NameLabel;
	@FXML
	private Label playedGamesAchievement3DescriptionLabel;
	@FXML
	private Label playedGamesAchievement3ProgressLabel;
	@FXML
	private ProgressBar playedGamesAchievement3ProgressBar;
	
	@FXML
	private VBox playedGamesAchievement4VBox;
	@FXML
	private Label playedGamesAchievement4NameLabel;
	@FXML
	private Label playedGamesAchievement4DescriptionLabel;
	@FXML
	private Label playedGamesAchievement4ProgressLabel;
	@FXML
	private ProgressBar playedGamesAchievement4ProgressBar;
	
	@FXML
	private VBox playedGamesAchievement5VBox;
	@FXML
	private Label playedGamesAchievement5NameLabel;
	@FXML
	private Label playedGamesAchievement5DescriptionLabel;
	@FXML
	private Label playedGamesAchievement5ProgressLabel;
	@FXML
	private ProgressBar playedGamesAchievement5ProgressBar;
	
	@FXML
	private VBox wonGamesAchievement1VBox;
	@FXML
	private Label wonGamesAchievement1NameLabel;
	@FXML
	private Label wonGamesAchievement1DescriptionLabel;
	@FXML
	private Label wonGamesAchievement1ProgressLabel;
	@FXML
	private ProgressBar wonGamesAchievement1ProgressBar;
	
	@FXML
	private VBox wonGamesAchievement2VBox;
	@FXML
	private Label wonGamesAchievement2NameLabel;
	@FXML
	private Label wonGamesAchievement2DescriptionLabel;
	@FXML
	private Label wonGamesAchievement2ProgressLabel;
	@FXML
	private ProgressBar wonGamesAchievement2ProgressBar;
	
	@FXML
	private VBox wonGamesAchievement3VBox;
	@FXML
	private Label wonGamesAchievement3NameLabel;
	@FXML
	private Label wonGamesAchievement3DescriptionLabel;
	@FXML
	private Label wonGamesAchievement3ProgressLabel;
	@FXML
	private ProgressBar wonGamesAchievement3ProgressBar;
	
	@FXML
	private VBox wonGamesAchievement4VBox;
	@FXML
	private Label wonGamesAchievement4NameLabel;
	@FXML
	private Label wonGamesAchievement4DescriptionLabel;
	@FXML
	private Label wonGamesAchievement4ProgressLabel;
	@FXML
	private ProgressBar wonGamesAchievement4ProgressBar;
	
	@FXML
	private VBox wonGamesAchievement5VBox;
	@FXML
	private Label wonGamesAchievement5NameLabel;
	@FXML
	private Label wonGamesAchievement5DescriptionLabel;
	@FXML
	private Label wonGamesAchievement5ProgressLabel;
	@FXML
	private ProgressBar wonGamesAchievement5ProgressBar;
	
	@FXML
	private VBox aquiredScoreAchievement1VBox;
	@FXML
	private Label aquiredScoreAchievement1NameLabel;
	@FXML
	private Label aquiredScoreAchievement1DescriptionLabel;
	@FXML
	private Label aquiredScoreAchievement1ProgressLabel;
	@FXML
	private ProgressBar aquiredScoreAchievement1ProgressBar;
	
	@FXML
	private VBox aquiredScoreAchievement2VBox;
	@FXML
	private Label aquiredScoreAchievement2NameLabel;
	@FXML
	private Label aquiredScoreAchievement2DescriptionLabel;
	@FXML
	private Label aquiredScoreAchievement2ProgressLabel;
	@FXML
	private ProgressBar aquiredScoreAchievement2ProgressBar;
	
	@FXML
	private VBox aquiredScoreAchievement3VBox;
	@FXML
	private Label aquiredScoreAchievement3NameLabel;
	@FXML
	private Label aquiredScoreAchievement3DescriptionLabel;
	@FXML
	private Label aquiredScoreAchievement3ProgressLabel;
	@FXML
	private ProgressBar aquiredScoreAchievement3ProgressBar;
	
	@FXML
	private VBox aquiredScoreAchievement4VBox;
	@FXML
	private Label aquiredScoreAchievement4NameLabel;
	@FXML
	private Label aquiredScoreAchievement4DescriptionLabel;
	@FXML
	private Label aquiredScoreAchievement4ProgressLabel;
	@FXML
	private ProgressBar aquiredScoreAchievement4ProgressBar;
	
	@FXML
	private VBox aquiredScoreAchievement5VBox;
	@FXML
	private Label aquiredScoreAchievement5NameLabel;
	@FXML
	private Label aquiredScoreAchievement5DescriptionLabel;
	@FXML
	private Label aquiredScoreAchievement5ProgressLabel;
	@FXML
	private ProgressBar aquiredScoreAchievement5ProgressBar;
	
	//ViewModel
	
	@InjectViewModel
	private AchievementsViewModel viewModel;
	
	//Methods
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onLoaded()
	{
		bindAchievementNames();
		bindAchievementDescriptions();
		bindAchievementProgressTexts();
		bindAchievementProgress();
		setVBoxStyle();
	}
	
	private void bindAchievementNames()
	{
		playedGamesAchievement1NameLabel.textProperty().bind(viewModel.getPlayedGamesAchievements().get(0).nameProperty());
		playedGamesAchievement2NameLabel.textProperty().bind(viewModel.getPlayedGamesAchievements().get(1).nameProperty());
		playedGamesAchievement3NameLabel.textProperty().bind(viewModel.getPlayedGamesAchievements().get(2).nameProperty());
		playedGamesAchievement4NameLabel.textProperty().bind(viewModel.getPlayedGamesAchievements().get(3).nameProperty());
		playedGamesAchievement5NameLabel.textProperty().bind(viewModel.getPlayedGamesAchievements().get(4).nameProperty());
		
		wonGamesAchievement1NameLabel.textProperty().bind(viewModel.getWonGamesAchievements().get(0).nameProperty());
		wonGamesAchievement2NameLabel.textProperty().bind(viewModel.getWonGamesAchievements().get(1).nameProperty());
		wonGamesAchievement3NameLabel.textProperty().bind(viewModel.getWonGamesAchievements().get(2).nameProperty());
		wonGamesAchievement4NameLabel.textProperty().bind(viewModel.getWonGamesAchievements().get(3).nameProperty());
		wonGamesAchievement5NameLabel.textProperty().bind(viewModel.getWonGamesAchievements().get(4).nameProperty());
		
		aquiredScoreAchievement1NameLabel.textProperty().bind(viewModel.getAquiredScoreAchievements().get(0).nameProperty());
		aquiredScoreAchievement2NameLabel.textProperty().bind(viewModel.getAquiredScoreAchievements().get(1).nameProperty());
		aquiredScoreAchievement3NameLabel.textProperty().bind(viewModel.getAquiredScoreAchievements().get(2).nameProperty());
		aquiredScoreAchievement4NameLabel.textProperty().bind(viewModel.getAquiredScoreAchievements().get(3).nameProperty());
		aquiredScoreAchievement5NameLabel.textProperty().bind(viewModel.getAquiredScoreAchievements().get(4).nameProperty());
	}
	
	private void bindAchievementDescriptions()
	{
		playedGamesAchievement1DescriptionLabel.textProperty()
			.bind(viewModel.getPlayedGamesAchievements().get(0).descriptionProperty());
		playedGamesAchievement2DescriptionLabel.textProperty()
			.bind(viewModel.getPlayedGamesAchievements().get(1).descriptionProperty());
		playedGamesAchievement3DescriptionLabel.textProperty()
			.bind(viewModel.getPlayedGamesAchievements().get(2).descriptionProperty());
		playedGamesAchievement4DescriptionLabel.textProperty()
			.bind(viewModel.getPlayedGamesAchievements().get(3).descriptionProperty());
		playedGamesAchievement5DescriptionLabel.textProperty()
			.bind(viewModel.getPlayedGamesAchievements().get(4).descriptionProperty());
		
		wonGamesAchievement1DescriptionLabel.textProperty()
			.bind(viewModel.getWonGamesAchievements().get(0).descriptionProperty());
		wonGamesAchievement2DescriptionLabel.textProperty()
			.bind(viewModel.getWonGamesAchievements().get(1).descriptionProperty());
		wonGamesAchievement3DescriptionLabel.textProperty()
			.bind(viewModel.getWonGamesAchievements().get(2).descriptionProperty());
		wonGamesAchievement4DescriptionLabel.textProperty()
			.bind(viewModel.getWonGamesAchievements().get(3).descriptionProperty());
		wonGamesAchievement5DescriptionLabel.textProperty()
			.bind(viewModel.getWonGamesAchievements().get(4).descriptionProperty());
	
		aquiredScoreAchievement1DescriptionLabel.textProperty()
			.bind(viewModel.getAquiredScoreAchievements().get(0).descriptionProperty());
		aquiredScoreAchievement2DescriptionLabel.textProperty()
			.bind(viewModel.getAquiredScoreAchievements().get(1).descriptionProperty());
		aquiredScoreAchievement3DescriptionLabel.textProperty()
			.bind(viewModel.getAquiredScoreAchievements().get(2).descriptionProperty());
		aquiredScoreAchievement4DescriptionLabel.textProperty()
			.bind(viewModel.getAquiredScoreAchievements().get(3).descriptionProperty());
		aquiredScoreAchievement5DescriptionLabel.textProperty()
			.bind(viewModel.getAquiredScoreAchievements().get(4).descriptionProperty());
	}
	
	private void bindAchievementProgressTexts()
	{
		playedGamesAchievement1ProgressLabel.textProperty().bind(
			viewModel.getPlayedGamesAchievements().get(0).progressTextProperty());
		playedGamesAchievement2ProgressLabel.textProperty().bind(
			viewModel.getPlayedGamesAchievements().get(1).progressTextProperty());
		playedGamesAchievement3ProgressLabel.textProperty().bind(
			viewModel.getPlayedGamesAchievements().get(2).progressTextProperty());
		playedGamesAchievement4ProgressLabel.textProperty().bind(
			viewModel.getPlayedGamesAchievements().get(3).progressTextProperty());
		playedGamesAchievement5ProgressLabel.textProperty().bind(
			viewModel.getPlayedGamesAchievements().get(4).progressTextProperty());
		
		wonGamesAchievement1ProgressLabel.textProperty().bind(
			viewModel.getWonGamesAchievements().get(0).progressTextProperty());
		wonGamesAchievement2ProgressLabel.textProperty().bind(
			viewModel.getWonGamesAchievements().get(1).progressTextProperty());
		wonGamesAchievement3ProgressLabel.textProperty().bind(
			viewModel.getWonGamesAchievements().get(2).progressTextProperty());
		wonGamesAchievement4ProgressLabel.textProperty().bind(
			viewModel.getWonGamesAchievements().get(3).progressTextProperty());
		wonGamesAchievement5ProgressLabel.textProperty().bind(
			viewModel.getWonGamesAchievements().get(4).progressTextProperty());
			
		aquiredScoreAchievement1ProgressLabel.textProperty().bind(
			viewModel.getAquiredScoreAchievements().get(0).progressTextProperty());
		aquiredScoreAchievement2ProgressLabel.textProperty().bind(
			viewModel.getAquiredScoreAchievements().get(1).progressTextProperty());
		aquiredScoreAchievement3ProgressLabel.textProperty().bind(
			viewModel.getAquiredScoreAchievements().get(2).progressTextProperty());
		aquiredScoreAchievement4ProgressLabel.textProperty().bind(
			viewModel.getAquiredScoreAchievements().get(3).progressTextProperty());
		aquiredScoreAchievement5ProgressLabel.textProperty().bind(
			viewModel.getAquiredScoreAchievements().get(4).progressTextProperty());
	}
	
	private void bindAchievementProgress()
	{
		playedGamesAchievement1ProgressBar.progressProperty().bind(
			viewModel.getPlayedGamesAchievements().get(0).progressProperty());
		playedGamesAchievement2ProgressBar.progressProperty().bind(
			viewModel.getPlayedGamesAchievements().get(1).progressProperty());
		playedGamesAchievement3ProgressBar.progressProperty().bind(
			viewModel.getPlayedGamesAchievements().get(2).progressProperty());
		playedGamesAchievement4ProgressBar.progressProperty().bind(
			viewModel.getPlayedGamesAchievements().get(3).progressProperty());
		playedGamesAchievement5ProgressBar.progressProperty().bind(
			viewModel.getPlayedGamesAchievements().get(4).progressProperty());
		
		wonGamesAchievement1ProgressBar.progressProperty().bind(
			viewModel.getWonGamesAchievements().get(0).progressProperty());
		wonGamesAchievement2ProgressBar.progressProperty().bind(
			viewModel.getWonGamesAchievements().get(1).progressProperty());
		wonGamesAchievement3ProgressBar.progressProperty().bind(
			viewModel.getWonGamesAchievements().get(2).progressProperty());
		wonGamesAchievement4ProgressBar.progressProperty().bind(
			viewModel.getWonGamesAchievements().get(3).progressProperty());
		wonGamesAchievement5ProgressBar.progressProperty().bind(
			viewModel.getWonGamesAchievements().get(4).progressProperty());
			
		aquiredScoreAchievement1ProgressBar.progressProperty().bind(
			viewModel.getAquiredScoreAchievements().get(0).progressProperty());
		aquiredScoreAchievement2ProgressBar.progressProperty().bind(
			viewModel.getAquiredScoreAchievements().get(1).progressProperty());
		aquiredScoreAchievement3ProgressBar.progressProperty().bind(
			viewModel.getAquiredScoreAchievements().get(2).progressProperty());
		aquiredScoreAchievement4ProgressBar.progressProperty().bind(
			viewModel.getAquiredScoreAchievements().get(3).progressProperty());
		aquiredScoreAchievement5ProgressBar.progressProperty().bind(
			viewModel.getAquiredScoreAchievements().get(4).progressProperty());
	}
	
	private void setVBoxStyle()
	{
		String style = "-fx-border-color: black;";
		
		playedGamesAchievement1VBox.setStyle(style);
		playedGamesAchievement2VBox.setStyle(style);
		playedGamesAchievement3VBox.setStyle(style);
		playedGamesAchievement4VBox.setStyle(style);
		playedGamesAchievement5VBox.setStyle(style);
		
		wonGamesAchievement1VBox.setStyle(style);
		wonGamesAchievement2VBox.setStyle(style);
		wonGamesAchievement3VBox.setStyle(style);
		wonGamesAchievement4VBox.setStyle(style);
		wonGamesAchievement5VBox.setStyle(style);
		
		aquiredScoreAchievement1VBox.setStyle(style);
		aquiredScoreAchievement2VBox.setStyle(style);
		aquiredScoreAchievement3VBox.setStyle(style);
		aquiredScoreAchievement4VBox.setStyle(style);
		aquiredScoreAchievement5VBox.setStyle(style);
		
		achievementsVBox.setStyle("-fx-background-color: white; -fx-border-color: gray; -fx-border-insets: 0,1,0,0;");
	}
	
	@FXML
    private void navigateBack() { viewModel.getNavigateBackCommand().execute(); }
}
