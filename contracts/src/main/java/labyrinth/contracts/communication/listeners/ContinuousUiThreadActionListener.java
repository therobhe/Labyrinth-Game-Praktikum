package labyrinth.contracts.communication.listeners;

import java.util.function.Consumer;
import javafx.application.Platform;
import labyrinth.contracts.communication.ISocketClient;
import labyrinth.contracts.communication.MessageType;

/**
 * A listener that handles a response with a certain action performed on the UI thread
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ContinuousUiThreadActionListener extends ContinuousActionListener
{
	//Properties
	
	private static boolean isUnitTest = false;
	/**
	 * Corresponding setter
	 * @param value If a UnitTest is currently active
	 */
	public static void setIsUnitTest(boolean value) { isUnitTest = value; }
	
	//Constructor
	
	/**
	 * UiThreadActionListener constructor
	 * @param messageType The type of the response the listener should handle
	 * @param action The Action the listener should perform when a response arrives
	 */
	public ContinuousUiThreadActionListener(MessageType messageType, Consumer<Object> action) { super(messageType, action); }
	
	//Methods
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleResponse(Object response, ISocketClient client) 
	{ 
		if(isUnitTest)
			super.handleResponse(response, client);
		else
			Platform.runLater(() -> super.handleResponse(response, client));  
	}
}
