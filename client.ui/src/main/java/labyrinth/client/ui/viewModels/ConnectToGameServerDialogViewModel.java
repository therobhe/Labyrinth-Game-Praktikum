package labyrinth.client.ui.viewModels;

import java.util.HashMap;
import java.util.Map;
import de.saxsys.mvvmfx.utils.commands.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import labyrinth.client.ui.*;
import labyrinth.client.ui.resources.Errors;
import labyrinth.client.ui.views.LobbyView;
import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.requests.ClientJoinDto;
import labyrinth.contracts.communication.dtos.responses.SuccessMessageResponseDto;
import labyrinth.contracts.communication.listeners.UiThreadActionListener;
import labyrinth.contracts.entities.lobby.GameServerEntry;

/**
 * ViewModel for @see ConnectToGameServerDialogView
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ConnectToGameServerDialogViewModel extends ViewModelBase
{
	//Properties
	
	/**
	 * Navigation-Parameter determining to witch GameServer the user wants to connect to
	 */
	public static final String GAMESERVER_PARAMETER = "GAMESERVER_PARAMETER";
	
	private Runnable closeDialogAction;
	/**
	 * Corresponding setter
	 * @param value Action for closing the dialog
	 */
	public void setCloseDialogAction(Runnable value) { closeDialogAction = value; }
	
	private GameServerEntry server;
	
    private StringProperty usernameProperty = new SimpleStringProperty();
	/**
	 * Getter for the usernameProperty
	 * @return usernameProperty
	 */
    public StringProperty usernameProperty() { return usernameProperty; }   
	/**
	 * Getter for the value of usernameProperty
	 * @return The value of usernameProperty
	 */
    public String getUsernameProperty() { return usernameProperty.get(); }
	/**
	 * Setter for the value of usernameProperty
	 * @param value The value of usernameProperty
	 */
    public void setUsernameProperty(String value) { usernameProperty.set(value); }
    
    private StringProperty errorProperty = new SimpleStringProperty();
	/**
	 * Getter for the errorProperty
	 * @return errorProperty
	 */
    public StringProperty errorProperty() { return errorProperty; }   
	/**
	* Getter for the value of errorProperty
	* @return The value of errorProperty
	*/
    public String getErrorProperty() { return errorProperty.get(); }
	/**
	 * Setter for the value of errorProperty
	 * @param value The value of errorProperty
	 */
    public void setErrorProperty(String value) { errorProperty.set(value); }
    
    private Command connectToGameServerCommand = new DelegateCommand(() -> new Action() 
	{
		@Override
		protected void action() { connectToGameServer(); }
	});
    /**
	 * Getter for connectCommand
	 * @return connectCommand
	 */
    public Command getConnectToGameServerCommand() { return connectToGameServerCommand; }
    
    //Methods
    
	/**
	 * {@inheritDoc}
	 * @throws IllegalArgumentException when no GAMESERVER_PARAMETER has been passed with navigation
	 */
    @Override
    public void handleNavigation(Map<String, Object> parameters)
    {
		if(parameters == null || parameters.get(GAMESERVER_PARAMETER) == null)
			throw new IllegalArgumentException(Errors.getConnectToGameServerDialogViewModel_MissingParameter());
		
		server = GameServerEntry.class.cast(parameters.get(GAMESERVER_PARAMETER));
    }
    
    private void connectToGameServer()
    {
    	setErrorProperty(null);
    	if(getUsernameProperty() == null || getUsernameProperty().isEmpty()) //A empty username is not allowed
    	{
    		setErrorProperty("Der Nutzername darf nicht leer sein.");
    		return;
    	}
    	
    	//Send ClientJoin-Request to the server
    	ClientJoinDto request = new ClientJoinDto(getUsernameProperty(), App.getUserId());
    	ClientConnectionManager.startGameServerClient(server);
    	ClientConnectionManager.getGameServerClient().registerListener(
    		new UiThreadActionListener(MessageType.ClientJoin, response -> onClientJoin(response)));
    	ClientConnectionManager.getGameServerClient().sendRequest(request);
    }
    
    private void onClientJoin(Object content)
    {
    	SuccessMessageResponseDto response = SuccessMessageResponseDto.class.cast(content);
    	if(!response.getSuccess()) //Join was not successful
    	{
    		setErrorProperty(response.getMessage());
    		return;
    	}
    	
    	//Close the dialog
    	if(closeDialogAction != null)
    		closeDialogAction.run();
    	
    	//Navigate to the LobbyView
    	Map<String, Object> parameters = new HashMap<String, Object>();
    	parameters.put(LobbyViewModel.USER_PARAMETER, getUsernameProperty());
    	NavigationManager.navigate(LobbyView.class, parameters);
    }
}
