package labyrinth.client.ui.views;

import java.net.URL;
import java.util.ResourceBundle;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import labyrinth.client.ui.viewModels.StartServerDialogViewModel;

/**
 * Code behind class of the view for the StartServerDialogView
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class StartServerDialogView extends ViewBase<StartServerDialogViewModel>
{
	//Controls
	
	@FXML
	private Button okayButton;	
	@FXML
	private Button cancelButton;
	@FXML 
	private TextField serverNameTextField;
	
	//ViewModel
	
	@InjectViewModel
	private StartServerDialogViewModel viewModel;
	
	//Methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{ 
		serverNameTextField.textProperty().bindBidirectional(viewModel.serverNameProperty());
		viewModel.setCloseDialogAction(() -> closeDialog());
	}
	
	@FXML
	private void cancel() { closeDialog(); }
	
	@FXML
	private void startServer() { viewModel.getStartGameServerCommand().execute(); }
	
	private void closeDialog()
	{
	    Stage stage = (Stage)cancelButton.getScene().getWindow();
	    stage.close();
	}
}
