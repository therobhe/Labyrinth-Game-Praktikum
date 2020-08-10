package labyrinth.contracts.communication;

/**
 * Class for listening to the server on another Thread
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ServerListener extends Thread
{
	//Properties
	
	private SocketClient client;
	
	//Constructors
	
	/**
	 * ServerListener constructor
	 * @client The Client which should listen to the server
	 */
	public ServerListener(SocketClient client) { this.client = client; }
	
	//Methods
	
	/**
	 * {@inheritDoc}
	 */
    @Override
    public void run() { startListening(); }
    
	/**
	 * Cancels the Thread
	 */
    public void cancel() { interrupt(); }
	
	/**
	 * Starts listening to the server
	 */
	private void startListening()
	{
		try 
	    {
			while (!interrupted()) 
				client.listen();
	    } 
	   	catch (Exception e) 
		{
	   		client.close();
	      	e.printStackTrace();
	 	} 
	}
}
