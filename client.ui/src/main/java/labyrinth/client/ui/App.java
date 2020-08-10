package labyrinth.client.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import labyrinth.client.ui.views.MainMenuView;
import labyrinth.contracts.communication.ISocketClient;
import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.SocketClient;
import labyrinth.contracts.communication.dtos.requests.EmptyRequestDto;
import labyrinth.contracts.communication.dtos.responses.ClientRegisterResponseDto;
import labyrinth.contracts.communication.listeners.UiThreadActionListener;
import labyrinth.contracts.utilities.FileAccessor;
import labyrinth.gameServer.GameServerManager;

/**
 * Application class containing the entry point for the application.
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class App extends Application 
{
	//Properties
	
	private static double screenWidth;
	/**
	 * Corresponding getter
	 * @return The width of the users screen
	 */
	public static double getScreenWidth() { return screenWidth; } 
	
	private static double screenHeight;
	/**
	 * Corresponding getter
	 * @return The height of the users screen
	 */
	public static double getScreenHeight() { return screenHeight; }
	
	private static int userId;
	/**
	 * Corresponding getter
	 * @return The user id of the client
	 */
	public static int getUserId() { return userId; }
	/**
	 * Corresponding setter
	 * @param value The user id of the client
	 */
	public static void setUserId(int value) { userId = value; }
	
	//Methods
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start(Stage stage)
	{
		//Initialize the stage
		initializeStage(stage);
		
		//Connect to the registration server
		connectToRegistrationServer(stage);
		
		//Initialize the NaviagtionManager
		NavigationManager.initialize(new StageWrapper(stage));
		
		//Initialize the ClientAchievementManager
		ClientAchievementManager.initialize(new FileAccessor());
			
		//Navigate to the main menu	
	    NavigationManager.navigate(MainMenuView.class);
	    
	    stage.show();
		screenWidth = stage.getWidth();
		screenHeight = stage.getHeight();
	}
	
	private void initializeStage(Stage stage)
	{
		stage.setTitle("Labyrinth - Client");
		stage.setMaximized(true);
		stage.setResizable(false);
	}
	
	private void connectToRegistrationServer(Stage stage)
	{	
		//Connect to the RegistrationServer and Register the client
		UiThreadActionListener listener = new UiThreadActionListener(MessageType.ClientRegister, 
			(response) -> handleClientRegisterResponse(response));
		connectToRegistrationServer(listener);
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
    public void stop()
    {
    	//Unregister from RegistrationServer when closing the client
    	ISocketClient client = ClientConnectionManager.getRegistrationServerClient();
    	ClientConnectionManager.closeAllConnections();
    	GameServerManager.tryCloseServer();
    	try
    	{
        	client.sendRequest(new EmptyRequestDto(MessageType.ClientUnregister));
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }

	/**
	 * Entry point of the application.
	 */
	public static void main(String[] args) { Application.launch(args); }
	
	private void connectToRegistrationServer(UiThreadActionListener listener)
	{
		try 
		{
			//Connect to the RegistrationServer
			ClientConnectionManager.initialize(SocketClient.class);
			ClientConnectionManager.startRegistrationServerClient();
			ISocketClient client = ClientConnectionManager.getRegistrationServerClient();
			
			//Send ClientRegister-Request to registration server
			client.registerListener(listener);
			client.sendRequest(new EmptyRequestDto(MessageType.ClientRegister));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
		
	private void handleClientRegisterResponse(Object response)
	{
		ClientRegisterResponseDto registerResponse = ClientRegisterResponseDto.class.cast(response);
		userId = registerResponse.getUserID();
	}
}
