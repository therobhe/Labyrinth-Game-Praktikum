package labyrinth.contracts.communication;

import labyrinth.contracts.communication.dtos.responses.ResponseDtoBase;

/**
 * WrapperClass for a ResponseMessage
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ResponseWrapper extends MessageWrapperBase
{
	//Constructors
	
	/**
	 * RequestWrapper constructor
	 */
	public ResponseWrapper() { }
	
	/**
	 * RequestWrapper constructor
	 * @param response The response from which the wrapper should be created
	 */
	public ResponseWrapper(ResponseDtoBase response) { super(response.getMessageType(), response.getContent()); }
	
	//Methods
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toJson() { return toJsonCore("response"); }
	
	/**
	 * Parses the ResponseMessage from the json-String
	 * @param json The json-String
	 * @return The Response
	 */
	public static ResponseWrapper fromJson(String json)
	{ 
		ResponseWrapper response = new ResponseWrapper();
		response.fromJsonCore(json, "response", (type) -> type.getResponseContentType()); 
		return response;
	}
}
