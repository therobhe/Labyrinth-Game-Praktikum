package labyrinth.contracts.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import labyrinth.contracts.communication.dtos.requests.EmptyRequestDto;
import labyrinth.contracts.communication.dtos.requests.GameServerRegisterDto;

/**
 * TestClass for @see RequestWrapper
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class RequestWrapperTests 
{
	/**
	 * Tests that the correct json string is returned for a RequestWrapper with content
	 */
	@Test
	public void doesCreateCorrectJsonFormatWithContentTest()
	{
		String expected = "{\"type\":\"GameServerRegister\",\"request\":{\"GameServerRegister\":{\"serverName\":\"Testserver\",\"port\":1000}}}";
		GameServerRegisterDto dto = new GameServerRegisterDto("Testserver", 1000);
		RequestWrapper request = new RequestWrapper(dto);
		String result = request.toJson();
		assertEquals(expected, result, "The Wrapper did not return the correct json.");
	}
	
	/**
	 * Tests that the correct json string is returned for a RequestWrapper without content
	 */
	@Test
	public void doesCreateCorrectJsonFormatWithoutContentTest()
	{
		String expected = "{\"type\":\"GameServerUnregister\",\"request\":{\"GameServerUnregister\":null}}";
		RequestWrapper request = new RequestWrapper(new EmptyRequestDto(MessageType.GameServerUnregister));
		String result = request.toJson();
		assertEquals(expected, result, "The Wrapper did not return the correct json.");
	}
	
	/**
	 * Tests that response is parsed correctly from the json string with content
	 */
	@Test
	public void doesGetCorrectRequestFromJsonWithContentTest()
	{
		String json = "{\"type\":\"GameServerRegister\",\"request\":{\"GameServerRegister\":{\"serverName\":\"Testserver\",\"port\":1000}}}";
		RequestWrapper request = RequestWrapper.fromJson(json);
		
		assertEquals(MessageType.GameServerRegister, request.getMessageType(), "The response has the wrong MessageType.");
		Object content = request.getContent();	
		assertNotNull(content, "The content of the response was null.");
		assertEquals("Testserver", ((GameServerRegisterDto)content).getServerName(), "The content has the wrong value for serverName");
		assertEquals(1000, ((GameServerRegisterDto)content).getPort(), "The content has the wrong value for port");
	}
	
	/**
	 * Tests that response is parsed correctly from the json string without content
	 */
	@Test
	public void doesGetCorrectRequestFromJsonWithoutContentTest()
	{
		String json = "{\"type\":\"GameServerUnregister\",\"request\":{\"GameServerUnregister\":null}}";
		RequestWrapper request = RequestWrapper.fromJson(json);
		
		assertEquals(MessageType.GameServerUnregister, request.getMessageType(), "The request has the wrong MessageType.");
		Object content = request.getContent();	
		assertNull(content, "The content of the request was not null.");
	}
}
