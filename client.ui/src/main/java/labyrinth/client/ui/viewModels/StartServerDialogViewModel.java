package labyrinth.client.ui.viewModels;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import labyrinth.client.ui.App;
import labyrinth.client.ui.ClientConnectionManager;
import labyrinth.client.ui.NavigationManager;
import labyrinth.client.ui.views.LobbyView;
import labyrinth.contracts.communication.*;
import labyrinth.contracts.communication.dtos.requests.ClientJoinDto;
import labyrinth.contracts.communication.dtos.responses.SuccessMessageResponseDto;
import labyrinth.contracts.communication.listeners.UiThreadActionListener;
import labyrinth.contracts.entities.lobby.GameServerEntry;
import labyrinth.contracts.utilities.FileAccessor;
import labyrinth.contracts.utilities.FileLogger;
import labyrinth.gameServer.*;

/**
 * ViewModel for @see StartServerDialogView
 * @author Lukas Reinhardt
 * @version 1.1
 */
public final class StartServerDialogViewModel extends ViewModelBase
{
	//Properties
	
	private Runnable closeDialogAction;
	/**
	 * Corresponding setter
	 * @param value Action for closing the dialog
	 */
	public void setCloseDialogAction(Runnable value) { closeDialogAction = value; }
	
	private StringProperty serverNameProperty = new SimpleStringProperty();
	/**
	 * Getter for the serverNameProperty
	 * @return serverNameProperty
	 */
    public StringProperty serverNameProperty() { return serverNameProperty; }   
	/**
	 * Getter for the value of serverNameProperty
	 * @return The value of serverNameProperty
	 */
    public String getServerNameProperty() { return serverNameProperty.get(); }
	/**
	 * Setter for the value of serverNameProperty
	 * @param value The value of serverNameProperty
	 */
    public void setServerNameProperty(String value) { serverNameProperty.set(value); }
	
    private Command startGameServerCommand = new DelegateCommand(() -> new Action() 
	{
		@Override
		protected void action() { startGameServer(); }
	});
    /**
	 * Getter for connectCommand
	 * @return connectCommand
	 */
    public Command getStartGameServerCommand() { return startGameServerCommand; }
    
    //Methods
    
	private void startGameServer()
    {
    	try 
    	{
    		//Start the game server
    		startServerCore();
	    	
	    	//Send Register-Request
	    	ClientJoinDto request = new ClientJoinDto("Admin", App.getUserId());
	    	ClientConnectionManager.getGameServerClient().registerListener(
	    		new UiThreadActionListener(MessageType.ClientJoin, response -> onClientJoin(response)));
	    	ClientConnectionManager.getGameServerClient().sendRequest(request);
	    	
		} 
    	catch (Exception e) 
    	{
			e.printStackTrace();
		}
    }
    
	private void startServerCore()
    {
    	//Create server
    	GameServerEntry server = new GameServerEntry(getServerNameProperty());
		try 
		{
			String ip = InetAddress.getLocalHost().getHostAddress();
			server.setIp(ip);
			server.setPort(1200);
	    	//Start the GameServer and connect to it
	    	LoggingManager.initialize(new FileLogger("logs.txt", new FileAccessor()));
	    	GameServerManager.initialize(new SocketClient(ip, 1300), 
	    		new SocketServer<GameServerConnection>(1200, GameServerConnection.class));
	    	GameServerManager.startServer(getServerNameProperty()); 	
	    	ClientConnectionManager.startGameServerClient(server);
		} 
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		}
    	server.setPort(1200);
    	
    }
    
    private void onClientJoin(Object content)
    {
    	SuccessMessageResponseDto response = SuccessMessageResponseDto.class.cast(content);
    	if(!response.getSuccess())
    		return;
    	
    	//Close the dialog
    	closeDialogAction.run();
    	
    	//Navigate to the LobbyView
    	Map<String, Object> parameters = new HashMap<String, Object>();
    	parameters.put(LobbyViewModel.USER_PARAMETER, "Admin");
    	parameters.put(LobbyViewModel.SERVER_PARAMETER, getServerNameProperty());
    	NavigationManager.navigate(LobbyView.class, parameters);
    }
}
