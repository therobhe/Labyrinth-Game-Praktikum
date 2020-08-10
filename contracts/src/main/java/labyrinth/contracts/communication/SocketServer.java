package labyrinth.contracts.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import labyrinth.contracts.communication.dtos.responses.ResponseDtoBase;

/**
 * A SocketServer
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class SocketServer<ServerConnectionT extends IServerConnection> implements ISocketServer<ServerConnectionT>
{
	//Properties
	
	private ServerSocket server;
	
	private Class<ServerConnectionT> connectionClass;
	
	private int port;
	
	private List<ServerConnectionT> connections = new ArrayList<ServerConnectionT>();
	/**
	 * {@inheritDoc}
	 */
	public List<ServerConnectionT> getConnections() { return connections; }
	
	//Constructors
	
	/**
	 * SocketServer constructor
	 * @param port Port number of the server
	 * @param connectionClass Type of the server connection
	 */
	public SocketServer(int port, Class<ServerConnectionT> connectionClass) 
	{ 
		this.port = port; 
		this.connectionClass = connectionClass;
	}
	
	//Methods
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start()
	{
		try 
		{
			//Start the actual server
			System.out.println("Starting server...");
			server = new ServerSocket(port);
			System.out.println("Server started.");
			
			//Listen for clients
			listenForClients();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			try 
			{
				close();
			} 
			catch (Exception e1) 
			{
				e1.printStackTrace();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <ResponseT extends ResponseDtoBase> void broadcast(ResponseT response) 
	{
		for(ServerConnectionT connection : connections)
			connection.sendResponse(response);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <ResponseT extends ResponseDtoBase> void broadcast(ResponseT response, IServerConnection excluded) 
	{
		for(ServerConnectionT connection : connections)
			if(connection != excluded)
				connection.sendResponse(response);		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close()
	{
		try 
		{
			//Close connections
			List<ServerConnectionT> copiedList = new ArrayList<ServerConnectionT>(connections);
			for(ServerConnectionT connection : copiedList)
				connection.cancel();
			//Close server
			server.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	private void listenForClients() 
	{
		System.out.println("Listening for clients...");
		try
		{
			while(true) 	
		  	{
		     	Socket client = server.accept();
		     	System.out.println("Client connected. (Ip: " + client.getInetAddress().getHostAddress() 
		     		+ " Port: " + client.getPort() + ")");
		     	
		     	Consumer<ServerConnectionBase> consumer = (connection) -> { handleConnectionClosing(connection); };
		     	ServerConnectionT connection = connectionClass.getConstructor(Socket.class, Consumer.class, String.class)
		     		.newInstance(client, consumer, client.getInetAddress().getHostAddress());     	
		     	connections.add(connection);
		     	
		     	//Start listening to client requests in another thread
		     	connection.start();
		  	}
		}
		catch(Exception e)
		{
			close();
		}
	}
	
	private synchronized void handleConnectionClosing(ServerConnectionBase connection) { connections.remove(connection); }
}
