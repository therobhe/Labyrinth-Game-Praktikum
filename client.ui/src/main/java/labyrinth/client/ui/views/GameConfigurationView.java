package labyrinth.client.ui.views;

import java.net.URL;
import java.util.ResourceBundle;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import labyrinth.client.ui.viewModels.GameConfigurationViewModel;
import labyrinth.contracts.utilities.HelperMethods;

/**
 * Code behind class of the view for the GameConfigurationViewModel
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class GameConfigurationView extends ViewBase<GameConfigurationViewModel>
{
	//Controls
	
	@FXML
	private HBox topHBox;
	
	@FXML
	private Button navigateBackButton;
	@FXML
	private Button cancelButton;
	@FXML
	private Button saveButton;
	
	@FXML
	private TextField maxGameLengthTextField;
	@FXML
	private TextField maxTurnLengthTextField;
	@FXML
	private TextField serverNameTextField;
	@FXML
	private TextField treasureCountTextField;
	@FXML
	private TextField boardSizeTextField;
	@FXML
	private TextField boniProbabilityTextField;
	
	@FXML
	private CheckBox shiftTwiceEnabledCheckBox;
	@FXML
	private CheckBox shiftSolidEnabledCheckBox;
	@FXML
	private CheckBox beamEnabledCheckBox;
	@FXML
	private CheckBox swapEnabledCheckBox;
	
	//ViewModel
	
	@InjectViewModel
	private GameConfigurationViewModel viewModel;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		topHBox.setStyle("-fx-border-color: black; -fx-border-width: 0px 0px 0.5px 0px;");		
		initializeCheckBoxBindings();
	}

	//Methods
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onLoaded() { initializeTextFieldBindings(); }
	
	private void initializeTextFieldBindings()
	{
		serverNameTextField.textProperty().bindBidirectional(viewModel.gameServerNameProperty());
		
		maxGameLengthTextField.textProperty().bindBidirectional(viewModel.gameLengthLimitProperty());
		maxGameLengthTextField.focusedProperty().addListener(new FocusLostValidationChangeListener<String>(
			maxGameLengthTextField.textProperty(), value -> HelperMethods.canParseInt(value)));
		
		maxTurnLengthTextField.textProperty().bindBidirectional(viewModel.turnLengthLimitProperty());
		maxTurnLengthTextField.focusedProperty().addListener(new FocusLostValidationChangeListener<String>(
			maxTurnLengthTextField.textProperty(), value -> HelperMethods.canParseInt(value)));
		
		treasureCountTextField.textProperty().bindBidirectional(viewModel.treasureCountProperty());
		treasureCountTextField.focusedProperty().addListener(new FocusLostValidationChangeListener<String>(
			treasureCountTextField.textProperty(), value -> HelperMethods.canParseInt(value)));
		
		boardSizeTextField.textProperty().bindBidirectional(viewModel.gameBoardSizeProperty());
		boardSizeTextField.focusedProperty().addListener(new FocusLostValidationChangeListener<String>(
			boardSizeTextField.textProperty(), value -> viewModel.validateGameBoardSize(value)));
		
		boniProbabilityTextField.textProperty().bindBidirectional(viewModel.boniProbabilityProperty());
		boniProbabilityTextField.focusedProperty().addListener(new FocusLostValidationChangeListener<String>(
			boniProbabilityTextField.textProperty(), value -> viewModel.validateBonusProbability(value)));
	}
	
	private void initializeCheckBoxBindings()
	{
		shiftTwiceEnabledCheckBox.selectedProperty().bindBidirectional(viewModel.isShiftTwiceEnabledProperty());
		shiftSolidEnabledCheckBox.selectedProperty().bindBidirectional(viewModel.isShiftSolidEnabledProperty());
		beamEnabledCheckBox.selectedProperty().bindBidirectional(viewModel.isBeamEnabledProperty());
		swapEnabledCheckBox.selectedProperty().bindBidirectional(viewModel.isSwapEnabledProperty());
	}
	
    @FXML
    private void navigateBack() { viewModel.getCancelCommand().execute(); }
    
    @FXML
    private void save() { viewModel.getSaveCommand().execute(); }
}
