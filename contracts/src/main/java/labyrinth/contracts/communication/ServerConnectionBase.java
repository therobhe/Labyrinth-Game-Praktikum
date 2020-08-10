package labyrinth.contracts.communication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import labyrinth.contracts.communication.dtos.requests.EmptyRequestDto;
import labyrinth.contracts.communication.dtos.requests.RequestDtoBase;
import labyrinth.contracts.communication.dtos.responses.ResponseDtoBase;
import labyrinth.contracts.communication.logic.LogicWrapper;

/**
 * BaseClass for a connection to a SocketServer
 * @author Lukas Reinhardt
 * @version 1.1
 */
public abstract class ServerConnectionBase extends Thread implements IServerConnection
{	
	//Properties
	
	static int test = 0;
	
	Map<MessageType, LogicWrapper<? extends RequestDtoBase>> registeredLogicClasses 
		= new HashMap<MessageType, LogicWrapper<? extends RequestDtoBase>>();
	
    private Socket client;
    
    private Consumer<ServerConnectionBase> closeHandler;
    
    private DataInputStream din = null;
    /**
     * Corresponding getter
     * @return The input stream of the client
     */
    protected DataInputStream getDIn() { return din; }
    
    private DataOutputStream dout = null;
    /**
     * Corresponding getter
     * @return The output stream of the client
     */
    protected DataOutputStream getDOut() { return dout; }
    
    private String ip;
    /**
	 * {@inheritDoc}
	 */
    @Override
    public String getIp() { return ip; }
    
    //Constructors

    /**
     * ServerConnectionBase constructor
     * @param client The client connected to the server
     * @param closeHandler handler which is executed when closing the connection
     * @param ip IP-Address of the client
     */
    public ServerConnectionBase(Socket client, Consumer<ServerConnectionBase> closeHandler, String ip) 
    { 
    	this.client = client; 
    	this.closeHandler = closeHandler;
    	this.ip = ip;
    }
    
    //Methods

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void run() 
    {
		try 
		{
			din = new DataInputStream(client.getInputStream());
	        dout = new DataOutputStream(client.getOutputStream());

	        //Run the code of the connection
	        runCore();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
		}
		finally
		{
			closeConnection();
		}
    }
    
	/**
	 * Sends a response to client
	 * @param <ResponseT> Type of the response
	 * @param response The response
	 */
    public <ResponseT extends ResponseDtoBase> void sendResponse(ResponseT response)
    {
		try 
		{			
			GZIPOutputStream zip = new GZIPOutputStream(client.getOutputStream());
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(zip));
			ResponseWrapper wrapper = new ResponseWrapper(response);
			String message = wrapper.toJson();
			System.out.println("Sending Response: " + message + " (Ip: " + client.getInetAddress().getHostAddress() 
					+ " Port: " + client.getPort() + ")");
		    writer.write(message);
		    writer.newLine();
		    writer.flush();
		    zip.finish();
		    test++;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			closeConnection();
		}
    }
    
	/**
	 * Cancels the Thread
	 */
    @Override
    public void cancel() { interrupt(); }
    
    /**
     * Runs the code of the connection
     */
    protected void runCore() { listen(); };
    
    /**
     * Closes the connection to the client
     */
    protected void closeConnection()
    {
		try 
		{
			din.close();
			dout.close();
			client.close();
			closeHandler.accept(this);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}	
    }
    
    /**
     * Executes the registered logic for the specific request
     */
    protected <RequestT extends RequestDtoBase> void executeLogic(RequestT request)
    { registeredLogicClasses.get(request.getMessageType()).execute(request); }
    
    /**
     * Registers a logic-class for a specific request-type
     */
    protected void registerLogic(LogicWrapper<? extends RequestDtoBase> logic)
    { registeredLogicClasses.put(logic.getMessageType(), logic); }
    
    /**
     * Reads the next request
     */
    protected RequestWrapper readRequest()
    {
		try 
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(getDIn())));
			String line = reader.readLine();
			System.out.println("Received Request: " + line + " (Ip: " + client.getInetAddress().getHostAddress() 
				+ " Port: " + client.getPort() + ")");
			return RequestWrapper.fromJson(line);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			closeConnection();
		}
		return null;
    }
    
    /**
     * Listen for the arrival of requests
     */
    protected void listen()
    {
		while (!interrupted()) 
		{
	        RequestWrapper request = readRequest();
	        if(request.getContent() == null)
	        	executeLogic(new EmptyRequestDto(request.getMessageType()));
	        else
	        	executeLogic(RequestDtoBase.class.cast(request.getContent()));
		}
    }
}
