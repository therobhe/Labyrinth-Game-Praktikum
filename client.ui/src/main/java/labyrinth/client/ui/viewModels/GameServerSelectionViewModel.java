package labyrinth.client.ui.viewModels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import labyrinth.client.ui.ClientConnectionManager;
import labyrinth.client.ui.NavigationManager;
import labyrinth.client.ui.views.ConnectToGameServerDialogView;
import labyrinth.contracts.communication.ISocketClient;
import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.requests.EmptyRequestDto;
import labyrinth.contracts.communication.listeners.UiThreadActionListener;
import labyrinth.contracts.entities.lobby.GameServerEntry;
import labyrinth.contracts.entities.lobby.LobbyUserKind;

/**
 * ViewModel for @see GameServerSelectionView
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class GameServerSelectionViewModel extends ViewModelBase
{
	//Properties
	
	private List<GameServerEntry> serverList = new ArrayList<GameServerEntry>();
	
	private Command refreshServerListCommand = new DelegateCommand(() -> new Action() 
	{
		@Override
		protected void action() { loadGameServerEntries();}
	});;
	/**
	 * Getter for refreshServerListCommand
	 * @return refreshServerListCommand
	 */
	public Command getRefreshServerListCommand() { return refreshServerListCommand; }
	
	private Command navigateBackCommand = new DelegateCommand(() -> new Action() 
	{
		@Override
		protected void action() { NavigationManager.navigateBack(); }
	});
	/**
	 * Getter for navigateBackCommand
	 * @return navigateBackCommand
	 */
	public Command getNavigateBackCommand() { return navigateBackCommand; }
	
	private ObservableList<GameServerEntry> bindableServerList = FXCollections.observableArrayList();
	/**
	 * Getter for the list of available GameServers
	 * @return List of available GameServers
	 */
	public ObservableList<GameServerEntry> getBindableServerList() { return bindableServerList; }
	
    private StringProperty searchTextProperty = new SimpleStringProperty();
	/**
	 * Getter for searchTextProperty
	 * @return searchTextProperty
	 */
    public StringProperty searchTextProperty() { return searchTextProperty; }   
	/**
	 * Getter for the value of searchTextProperty
	 * @return The value of searchTextProperty
	 */
    public final String getSearchTextProperty() { return searchTextProperty.get(); }
	/**
	 * Setter for the value of searchTextProperty
	 * @param value The value of searchTextProperty
	 */
    public final void setSearchTextProperty(String value) { searchTextProperty.set(value); }
    
    //Constructors
    
    /**
     * GameServerSelectionViewModel constructor
     */
    public GameServerSelectionViewModel()
    {    	
    	//Add TextChanged-EventHandler for filtering the result list
    	searchTextProperty.addListener((observeable, oldValue, newValue) -> filterServerList(newValue));
    }
    
    //Methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleNavigation(Map<String, Object> parameters) 
	{ 	
    	//load the list of GameServers
    	loadGameServerEntries();
	}
	
	/**
	 * Handles the selection of a specific GameServer to join by the user
	 * @param entry The server the user wants to join
	 */
	public void onServerSelected(GameServerEntry entry)
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(ConnectToGameServerDialogViewModel.GAMESERVER_PARAMETER, entry);
		NavigationManager.showDialog(ConnectToGameServerDialogView.class, parameters);    
	}
	
	public static StringProperty calculatePlayerCountValue(GameServerEntry server)
	{
		if(server != null)
		{
			long playerCount = server.getMembers().stream()
				.filter(member -> member.getRole() == LobbyUserKind.PLAYER).count();
			return new SimpleStringProperty(playerCount + "/4");
		}
		return null;
	}
	
	private void loadGameServerEntries()
	{
		ISocketClient client = ClientConnectionManager.getRegistrationServerClient();
		Consumer<Object> onGameServersRetrievedHandler = (response) -> onGameServersRetrieved(response);
		client.registerListener(new UiThreadActionListener(MessageType.PullGameServers, onGameServersRetrievedHandler));
		client.sendRequest(new EmptyRequestDto(MessageType.PullGameServers));
	}
	
	private void onGameServersRetrieved(Object content)
	{
		//Generics suck in Java
		@SuppressWarnings("unchecked")
		List<GameServerEntry> response = List.class.cast(content);
		serverList.clear();
		serverList.addAll(response);
		filterServerList(getSearchTextProperty());
	}
	
 	private void filterServerList(String filterText)
	{
		bindableServerList.clear();
		if(filterText != null && !filterText.isEmpty())
			serverList.stream().filter(server -> server.getServerName().toLowerCase().contains(filterText.toLowerCase()))
		    	.forEach(bindableServerList::add);
		else
			serverList.stream().forEach(bindableServerList::add);
	}
}
