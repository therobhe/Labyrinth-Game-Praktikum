package labyrinth.contracts.communication;

import labyrinth.contracts.communication.dtos.requests.RequestDtoBase;

/**
 * WrapperClass for a RequestMessage
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class RequestWrapper extends MessageWrapperBase
{
	//Constructors
	
	/**
	 * RequestWrapper constructor
	 */
	public RequestWrapper() { }
	
	/**
	 * RequestWrapper constructor
	 * @param request The request from which the wrapper should be created
	 */
	public RequestWrapper(RequestDtoBase request) { super(request.getMessageType(), request.getContent()); }
	
	//Methods
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toJson() { return toJsonCore("request"); }
	
	/**
	 * Parses the RequestMessage from the json-String
	 * @param json The json-String
	 * @return The Request
	 */
	public static RequestWrapper fromJson(String json)
	{ 
		RequestWrapper response = new RequestWrapper();
		response.fromJsonCore(json, "request", (type) -> type.getRequestContentType()); 
		return response;
	}
}
