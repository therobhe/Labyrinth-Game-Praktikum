package labyrinth.client.ui.views;

import java.net.URL;
import java.util.ResourceBundle;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import labyrinth.client.ui.viewModels.ConnectToGameServerDialogViewModel;

/**
 * Code behind class of the view for the ConnectToGameServerDialogView
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ConnectToGameServerDialogView extends ViewBase<ConnectToGameServerDialogViewModel>
{
	//Controls
	
	@FXML
	private Button connectButton;	
	@FXML
	private Button cancelButton;
	@FXML
	private Label errorLabel;
	@FXML 
	private TextField usernameTextField;
	
	//ViewModel
	
	@InjectViewModel
	private ConnectToGameServerDialogViewModel viewModel;

	//Methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		errorLabel.setStyle("-fx-text-fill: red;");
		errorLabel.textProperty().bind(viewModel.errorProperty());
		usernameTextField.textProperty().bindBidirectional(viewModel.usernameProperty());
		viewModel.setCloseDialogAction(() -> closeDialog());
		
		//Run later because TextField is not ready yet
		Platform.runLater(new Runnable() 
		{
	        @Override
	        public void run() { usernameTextField.requestFocus(); }
	    });
	}
	
	@FXML
	private void cancel() { closeDialog(); }
	
	@FXML
	private void connect() { viewModel.getConnectToGameServerCommand().execute(); }
	
	private void closeDialog()
	{
	    Stage stage = (Stage)cancelButton.getScene().getWindow();
	    stage.close();
	}
}
