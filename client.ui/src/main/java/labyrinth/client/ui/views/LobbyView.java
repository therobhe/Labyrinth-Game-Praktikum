package labyrinth.client.ui.views;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import labyrinth.client.ui.viewModels.LobbyViewModel;
import labyrinth.contracts.entities.lobby.LobbyUser;
import labyrinth.contracts.entities.lobby.LobbyUserKind;
import labyrinth.contracts.entities.lobby.UserPermission;

/**
 * Code behind class of the view for the LobbyView
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class LobbyView extends ViewBase<LobbyViewModel>
{
	private class LobbyUserCell extends ListCell<LobbyUser> 
	{
		//Controls
		
		@SuppressWarnings("serial")
		private List<Color> colors = new ArrayList<Color>()
		{{ 
			add(Color.AQUAMARINE); 
			add(Color.BISQUE);
			add(Color.CADETBLUE); 
			add(Color.BLUEVIOLET);
			add(Color.CORNFLOWERBLUE);
			add(Color.CRIMSON);
			add(Color.DARKORANGE);
			add(Color.FORESTGREEN);
			add(Color.GOLD);
			add(Color.LIGHTCORAL);
		}};
		
		ColorPicker colorPicker = new ColorPicker();
        HBox hBox = new HBox();
        Label label = new Label("");
        Pane pane = new Pane();
        MenuButton button = new MenuButton("Aktionen");
        CheckBox checkBox = new CheckBox();
        
        //Methods

    	/**
    	 * {@inheritDoc}
    	 */
        @Override
        protected void updateItem(LobbyUser item, boolean empty) 
        {
            super.updateItem(item, empty);
            setText(null);
            setGraphic(null);

            if (item != null && !empty)
            {
            	createContent(item);
                createContextMenu(item);
            }
        }
        
        private void createContent(LobbyUser user)
        { 
        	hBox.getChildren().clear();
        	
            label.setPrefWidth(200);
            label.setText(user.getName());
            hBox.getChildren().add(label);
            hBox.getChildren().add(pane);
            
            if(user.getRole() == LobbyUserKind.PLAYER)
            {
                createCheckBox(user);
                hBox.getChildren().add(checkBox);
                createColorPicker(user);
                hBox.getChildren().add(colorPicker);
            }
            
            hBox.getChildren().add(button);
            HBox.setHgrow(pane, Priority.ALWAYS);    
        	setGraphic(hBox);
        }
        
        private void createCheckBox(LobbyUser user)
        {
            checkBox.setText("KI");
            if(user == viewModel.getUser())
            	checkBox.selectedProperty().bindBidirectional(viewModel.getIsAIProperty());
            checkBox.disableProperty().set(user != viewModel.getUser());
            HBox.setMargin(checkBox, new Insets(3,5,0,5));         
        }
        
        private void createColorPicker(LobbyUser user)
        {
            colorPicker.setPrefWidth(150);
            if(user.getColor() != null)
            	colorPicker.setValue(Color.valueOf(user.getColor()));
            Collections.shuffle(colors);
            colorPicker.disableProperty().set(user != viewModel.getUser());
            colorPicker.setOnAction(e -> onColorPicked(user));
            HBox.setMargin(colorPicker, new Insets(0,10,0,10));
            if(user.getColor() == null && user == viewModel.getUser())
            {
            	colorPicker.setValue(colors.get(0));
            	onColorPicked(user);
            }  
        }
        
        private void onColorPicked(LobbyUser user)
        {
        	String color = getColorString(colorPicker.getValue());
        	viewModel.changeColor(color);
        	user.setColor(color);
        }
        
        private void createContextMenu(LobbyUser user)
        {    
        	button.getItems().clear();
            MenuItem changeRoleItem = new MenuItem("Rolle wechseln");
            changeRoleItem.setOnAction((event) -> changeRole());
            changeRoleItem.disableProperty().set(user != viewModel.getUser());
            
            MenuItem setAdminItem = new MenuItem("Zu Admin");
            setAdminItem.setOnAction((event) -> setAdmin(this.getItem()));
            setAdminItem.disableProperty().set(viewModel.getUser().getPermission() == UserPermission.DEFAULT 
            	|| user == viewModel.getUser());
        
        	button.getItems().addAll(changeRoleItem, setAdminItem);
        }
    }
	
	//Controls
	
	@FXML
	private ListView<LobbyUser> playerListView;
	@FXML
	private ListView<LobbyUser> spectatorListView;
	@FXML
	private Button configurationButton;
	@FXML
	private Button startGameButton;
	@FXML
	private Button leaveLobbyButton;

	//ViewModel
	
	@InjectViewModel
	private LobbyViewModel viewModel;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		//initialize the ListViews
		initializeListViews();
		
		//Command Bindings
		configurationButton.disableProperty().bind(viewModel.getGameConfigurationCommand().executableProperty()
			.not());
		startGameButton.textProperty().bind(viewModel.startGameCommandCaptionProperty());
		startGameButton.disableProperty().bind(viewModel.getStartGameCommand().executableProperty().not());
		leaveLobbyButton.disableProperty().bind(viewModel.getLeaveLobbyCommand().executableProperty().not());
		configurationButton.disableProperty().bind(viewModel.getGameConfigurationCommand().executableProperty().not());
	}
	
	private void initializeListViews()
	{
		String style = "-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-border-color: black;";
		playerListView.setStyle(style);
		playerListView.setCellFactory(new Callback<ListView<LobbyUser>, ListCell<LobbyUser>>() 
		{
		    @Override
		    public ListCell<LobbyUser> call(ListView<LobbyUser> param) { return new LobbyUserCell(); } 
		});
		playerListView.setItems(viewModel.getBindablePlayerList());
		
		spectatorListView.setStyle(style);
		spectatorListView.setCellFactory(new Callback<ListView<LobbyUser>, ListCell<LobbyUser>>() 
		{
		    @Override
		    public ListCell<LobbyUser> call(ListView<LobbyUser> param) { return new LobbyUserCell(); } 
		});
		spectatorListView.setItems(viewModel.getBindableSpectatorList());
	}
	
	private String getColorString(Color color)
	{
		return String.format( "#%02X%02X%02X", (int)(color.getRed() * 255 ), (int)(color.getGreen() * 255 ),
			(int)(color.getBlue() * 255 ) );
	}
	
	@FXML
	private void openConfigurationView() { viewModel.getGameConfigurationCommand().execute(); }
	
	@FXML
	private void startGame() { viewModel.getStartGameCommand().execute(); }
	
	@FXML
	private void leaveLobby() { viewModel.getLeaveLobbyCommand().execute(); }
	
	private void changeRole() { viewModel.changeRole(); }
	
	private void setAdmin(LobbyUser user) { viewModel.setAdmin(user); }
}
