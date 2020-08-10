package labyrinth.client.ui.views;

import java.net.URL;
import java.util.ResourceBundle;
import de.saxsys.mvvmfx.FxmlView;
import javafx.fxml.Initializable;
import labyrinth.client.ui.viewModels.ViewModelBase;

/**
 * Base-class for all Views
 * @author Lukas Reinhardt
 * @version 1.0
 */
public abstract class ViewBase<ViewModelT extends ViewModelBase> implements FxmlView<ViewModelT>, Initializable
{
	/**
	 * Method executed after handleNavigation was called on the viewModel
	 */
	public void onLoaded() { }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) { }
}
