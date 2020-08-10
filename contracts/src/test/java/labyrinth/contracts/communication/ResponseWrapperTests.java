package labyrinth.contracts.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import labyrinth.contracts.communication.dtos.responses.EmptyResponseDto;
import labyrinth.contracts.communication.dtos.responses.SimpleSuccessResponseDto;

/**
 * TestClass for @see ResponseWrapper
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ResponseWrapperTests 
{
	/**
	 * Tests that the correct json string is returned for a RequestWrapper with content
	 */
	@Test
	public void doesCreateCorrectJsonFormatWithContentTest()
	{
		String expected = "{\"type\":\"GameServerRegister\",\"response\":{\"GameServerRegister\":{\"success\":true}}}";
		SimpleSuccessResponseDto dto = new SimpleSuccessResponseDto(MessageType.GameServerRegister, true);
		ResponseWrapper request = new ResponseWrapper(dto);
		String result = request.toJson();
		assertEquals(expected, result, "The Wrapper did not return the correct json.");
	}
	
	/**
	 * Tests that the correct json string is returned for a RequestWrapper without content
	 */
	@Test
	public void doesCreateCorrectJsonFormatWithoutContentTest()
	{
		String expected = "{\"type\":\"GameServerUnregister\",\"response\":{\"GameServerUnregister\":null}}";
		ResponseWrapper request = new ResponseWrapper(new EmptyResponseDto(MessageType.GameServerUnregister));
		String result = request.toJson();
		assertEquals(expected, result, "The Wrapper did not return the correct json.");
	}
	
	/**
	 * Tests that response is parsed correctly from the json string with content
	 */
	@Test
	public void doesGetCorrectResponseFromJsonWithContentTest()
	{
		String json = "{\"type\":\"GameServerRegister\",\"response\":{\"GameServerRegister\":{\"success\":true}}}";
		ResponseWrapper response = ResponseWrapper.fromJson(json);
		
		assertEquals(MessageType.GameServerRegister, response.getMessageType(), "The response has the wrong MessageType.");
		Object content = response.getContent();	
		assertNotNull(content, "The content of the response was null.");
		assertTrue(((SimpleSuccessResponseDto)content).getSuccess(), "The content has the wrong value for success.");
	}
	
	/**
	 * Tests that response is parsed correctly from the json string without content
	 */
	@Test
	public void doesGetCorrectResponseFromJsonWithoutContentTest()
	{
		String json = "{\"type\":\"GameServerUnregister\",\"response\":{\"GameServerUnregister\":null}}";
		ResponseWrapper response = ResponseWrapper.fromJson(json);
		
		assertEquals(MessageType.GameServerUnregister, response.getMessageType(), "The response has the wrong MessageType.");
		Object content = response.getContent();	
		assertNull(content, "The content of the response was not null.");
	}
}
