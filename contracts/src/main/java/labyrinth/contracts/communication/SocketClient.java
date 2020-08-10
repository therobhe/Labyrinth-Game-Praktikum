package labyrinth.contracts.communication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipException;
import labyrinth.contracts.communication.dtos.requests.RequestDtoBase;
import labyrinth.contracts.communication.listeners.ResponseListenerBase;

/**
 * A SocketClient
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class SocketClient implements ISocketClient
{
	//Properties
	
	private List<ResponseListenerBase> listeners = new ArrayList<ResponseListenerBase>();
	
	private Socket socket;
	
	//Constructors
	
	/**
	 * SocketClient constructor
	 * @param ipAdress IP-Address of the target server
	 * @param port Port number of the target server
	 */
	public SocketClient(String ipAdress, int port)
	{ 
		try
		{
			socket = new Socket(ipAdress, port); 
			new ServerListener(this).start();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//Methods
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <ContentT, RequestT extends RequestDtoBase> void sendRequest(RequestT request)
	{
		BufferedWriter writer;
		try 
		{
			//Send request compressed with gzip
			GZIPOutputStream zip = new GZIPOutputStream(socket.getOutputStream());
			writer = new BufferedWriter(new OutputStreamWriter(zip));
			RequestWrapper wrapper = new RequestWrapper(request);
		    writer.write(wrapper.toJson());
		    writer.newLine();
		    writer.flush();
		    zip.finish();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			close();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerListener(ResponseListenerBase listener) { listeners.add(listener); }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unregisterListener(ResponseListenerBase listener) { listeners.remove(listener); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close()
	{
		try 
		{
			socket.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Listens to the server for any kind of message
	 * @throws IOException
	 */
	public void listen() throws IOException
	{
		//Read the response
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(socket.getInputStream())));
		}
		catch(ZipException e)
		{
			System.out.println("Fehlerhaftes Format");
			e.printStackTrace();
		}
		String line = reader.readLine();
		System.out.println("Received Response: " + line);
	    ResponseWrapper response = ResponseWrapper.fromJson(line);
	    
	    //Look for listeners that can handle that response
	    List<ResponseListenerBase> listenersCopy = new ArrayList<ResponseListenerBase>(listeners);
	    for(ResponseListenerBase listener : listenersCopy)
	    	if(listener.canHandle(response.getMessageType()))
	    		listener.handleResponse(response.getContent(), this);
	}
}
