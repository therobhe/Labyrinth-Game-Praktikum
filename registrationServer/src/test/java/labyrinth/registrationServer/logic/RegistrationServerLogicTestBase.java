package labyrinth.registrationServer.logic;

import org.junit.jupiter.api.BeforeAll;
import labyrinth.registrationServer.RegistrationServerStateManager;

/**
 * BaseClass for all LogicTests of the RegistrationServer
 * @author Lukas Reinhardt
 * @version 1.0
 */
public abstract class RegistrationServerLogicTestBase 
{
	/**
	 * Sets up the tests
	 */
	@BeforeAll
	public static void setUp() 
	{
		RegistrationServerStateManager.getServers().clear();
		RegistrationServerStateManager.getUserIds().clear();
	}
}
