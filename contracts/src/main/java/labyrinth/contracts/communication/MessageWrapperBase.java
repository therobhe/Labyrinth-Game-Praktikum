package labyrinth.contracts.communication;

import java.util.List;
import java.util.function.Function;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import labyrinth.contracts.entities.lobby.GameServerEntry;

/**
 * Base-class for all MessageWrappers
 * @author Lukas Reinhardt
 * @version 1.0
 */
public abstract class MessageWrapperBase 
{
	private MessageType messageType;
	/**
	 * Corresponding Getter
	 * @return The Message Type
	 */
	public MessageType getMessageType() { return messageType; }
	
	private Object content;
	/**
	 * Corresponding Getter
	 * @return The content of the message
	 */
	public Object getContent() { return content; }
	
	//Constructors
	
	/**
	 * MessageWrapperBase constructor
	 */
	public MessageWrapperBase() { }
	
	/**
	 * ResponseWrapper constructor
	 * @param messageType Type of the message
	 * @param content The Content of the message
	 */
	public MessageWrapperBase(MessageType messageType, Object content) 
	{ 
		this.messageType = messageType;
		this.content = content; 
	}
	
	//Methods
	
	/**
	 * Returns the json string of the message
	 * @return The json string of the message
	 */
	public abstract String toJson();
	
	/**
	 * Generates the json string for the message
	 * @param  messageTypeName The name of the type of message
	 * @return The json string of the message 
	 */
	protected String toJsonCore(String messageTypeName)
	{
		JsonObject request = new JsonObject();
		request.addProperty("type", messageType.toString());
		JsonObject requestContent = new JsonObject();
		
		//If Content not null add serialized content else null
		if(content != null)
		{
			if(content instanceof String)
				requestContent.addProperty(messageType.toString(), String.class.cast(content));
			else
			{
				JsonElement jsonContent = new JsonParser().parse(new Gson().toJson(content));
				requestContent.add(messageType.toString(), jsonContent);
			}
		}
		else
			requestContent.add(messageType.toString(), null);

		request.add(messageTypeName, requestContent);
		return request.toString();
	}
	
	/**
	 * Parses the message from the json string
	 * @param  json The json string
	 * @param  messageTypeName The name of the type of message
	 * @param  contentTypeResolver Function for resolving the type of the content of the message
	 */
	protected void fromJsonCore(String json, String messageTypeName, 
		Function<MessageType, Class<? extends Object>> contentTypeResolver)
	{
	    JsonElement element = new JsonParser().parse(json);
	    JsonObject  object = element.getAsJsonObject();
	    JsonElement typeElement = object.get("type");
	    messageType = MessageType.valueOf(typeElement.getAsString());
	    
	    //Resolve content type
	    Class<? extends Object> contentType = contentTypeResolver.apply(messageType);
	    
	    //Set content only if a content is expected in the message
	    if(contentType != null)
	    {
	    	//Get content element
		    JsonElement jsonContent = object.get(messageTypeName).getAsJsonObject().get(typeElement.getAsString());
		    //Deserialize content
		    deserializeContent(jsonContent, contentType);
	    }
	}
	
	private void deserializeContent(JsonElement jsonContent, Class<? extends Object> contentType)
	{
		//Extra handling for MessageType.PullGameServers because of an irregularity in the message format
		if(messageType == MessageType.PullGameServers)
		{
			List<GameServerEntry> servers = new Gson().fromJson(jsonContent, 
				new TypeToken<List<GameServerEntry>>(){}.getType());
			content = servers;
		}
		else
			content = new Gson().fromJson(jsonContent, contentType);	
	}
}
