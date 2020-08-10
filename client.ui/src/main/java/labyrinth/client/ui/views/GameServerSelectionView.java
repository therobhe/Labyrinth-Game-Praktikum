package labyrinth.client.ui.views;

import java.net.URL;
import java.util.ResourceBundle;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import labyrinth.client.ui.viewModels.GameServerSelectionViewModel;
import labyrinth.contracts.entities.lobby.GameServerEntry;

/**
 * Code behind class of the view for the GameServerSelectionView
 * @author Lukas Reinhardt
 * @version 1.1
 */
public final class GameServerSelectionView extends ViewBase<GameServerSelectionViewModel>
{
	//Controls
	
	@FXML
	private Button refreshButton;		
	@FXML
	private Button navigateBackButton;
	
	@FXML
	private TableView<GameServerEntry> serverTable;	
	
	@FXML
	private TableColumn<GameServerEntry, String> serverNameColumn;
	@FXML
	private TableColumn<GameServerEntry, String> ipAddressColumn;
	@FXML
	private TableColumn<GameServerEntry, Integer> portNumberColumn;
	@FXML
	private TableColumn<GameServerEntry, String> statusColumn;	
	@FXML
	private TableColumn<GameServerEntry, String> playerCountColumn;
	
	@FXML
	private TextField searchTextTextField;
	
	//ViewModel
	
	@InjectViewModel
	private GameServerSelectionViewModel viewModel;
	
	//Methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		refreshButton.disableProperty().bind(viewModel.getRefreshServerListCommand().executableProperty().not());
		initializeTable();
	}
	
	private void initializeTable()
	{
		serverTable.setItems(viewModel.getBindableServerList());
		
		//Register double-click event
		serverTable.setRowFactory(tv -> 
		{
			TableRow<GameServerEntry> row = new TableRow<>();
			row.setOnMouseClicked(e -> 
			{
				if (e.getClickCount() == 2 && (!row.isEmpty())) 
					rowDoubleClick(row);		
			});
			return row;
		});
		
		//Calculate the playerCount dynamically
		playerCountColumn.setCellValueFactory(cellData -> 
		{ return GameServerSelectionViewModel.calculatePlayerCountValue(cellData.getValue()); });

		serverNameColumn.setCellValueFactory(new PropertyValueFactory<GameServerEntry, String>("serverName"));
		ipAddressColumn.setCellValueFactory(new PropertyValueFactory<GameServerEntry, String>("ip"));
		portNumberColumn.setCellValueFactory(new PropertyValueFactory<GameServerEntry, Integer>("port"));
		statusColumn.setCellValueFactory(new PropertyValueFactory<GameServerEntry, String>("state"));
		searchTextTextField.textProperty().bindBidirectional(viewModel.searchTextProperty());
	}
	
    private void rowDoubleClick(TableRow<GameServerEntry> row) { viewModel.onServerSelected(row.getItem()); }
	
    @FXML
    private void navigateBack() { viewModel.getNavigateBackCommand().execute(); }
    
    @FXML
    private void refreshList() { viewModel.getRefreshServerListCommand().execute(); }
}
