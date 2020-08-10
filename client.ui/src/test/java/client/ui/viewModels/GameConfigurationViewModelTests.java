package client.ui.viewModels;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import client.ui.StageWrapperMock;
import labyrinth.client.ui.ClientConnectionManager;
import labyrinth.client.ui.NavigationManager;
import labyrinth.client.ui.viewModels.GameConfigurationViewModel;
import labyrinth.contracts.communication.SocketClientMock;
import labyrinth.contracts.communication.dtos.requests.ConfigurationDto;
import labyrinth.contracts.entities.lobby.BoniConfiguration;
import labyrinth.contracts.entities.lobby.GameConfiguration;
import labyrinth.contracts.entities.lobby.GameServerEntry;

/**
 * TestClass for @see GameConfigurationViewModel
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class GameConfigurationViewModelTests 
{
	/**
	 * Tests that the correct exception is thrown when navigating to the ViewModel without the Configuration-Parameter
	 */
	@Test
	public void throwsCorrectExceptionWhenNavigatingWithoutConfigurationParameterTest()
	{
		GameConfigurationViewModel viewModel = new GameConfigurationViewModel();
		assertThrows(IllegalArgumentException.class, () -> viewModel.handleNavigation(null), 
				"Expected exception of type IllegalArgumentException. has not been thrown.");
		Map<String, Object> parameters = new HashMap<String, Object>();
		assertThrows(IllegalArgumentException.class, () -> viewModel.handleNavigation(parameters), 
				"Expected exception of type IllegalArgumentException. has not been thrown.");
	}
	
	/**
	 * Tests that the bindable Properties of the VM are initialized with the correct values from the configuration 
	 */
	@Test
	public void doesInitializeBindablePropertiesWithCorrectValuesTest()
	{
		GameConfigurationViewModel viewModel = new GameConfigurationViewModel();
		Map<String, Object> parameters = new HashMap<String, Object>();
		GameConfiguration configuration = new GameConfiguration();
		parameters.put(GameConfigurationViewModel.CONFIGURATION_PARAMETER, configuration);
		viewModel.handleNavigation(parameters);
		
		assertEquals(configuration.getServerName(), viewModel.getGameServerNameProperty(), 
			"The value was initialized correctly.");
		assertEquals(configuration.getBoniProbability(), viewModel.getBoniProbabilityProperty(), 
			"The value was initialized correctly.");
		assertEquals(configuration.getGameLengthLimit(), viewModel.getGameLengthLimitProperty(),
			"The value was initialized correctly.");
		assertEquals(configuration.getSize().getX(), (int)viewModel.getGameBoardSizeProperty(),
			"The value was initialized correctly.");
		assertEquals(configuration.getTreasureCount(), viewModel.getTreasureCountProperty(),
			"The value was initialized correctly.");
		assertEquals(configuration.getTurnLengthLimit(), viewModel.getTurnLengthLimitProperty(),
			"The value was initialized correctly.");
		BoniConfiguration boniConfiguration = configuration.getBoni();
		assertEquals(boniConfiguration.getIsBeamEnabled(), viewModel.getIsBeamEnabledProperty(),
			"The value was initialized correctly.");
		assertEquals(boniConfiguration.getIsShiftSolidEnabled(), viewModel.getIsShiftSolidEnabledProperty(),
			"The value was initialized correctly.");
		assertEquals(boniConfiguration.getIsShiftTwiceEnabled(), viewModel.getIsShiftTwiceEnabledProperty(),
			"The value was initialized correctly.");
		assertEquals(boniConfiguration.getIsSwapEnabled(), viewModel.getIsSwapEnabledProperty(),
			"The value was initialized correctly.");
	}
	
	/**
	 * Tests that the bindable Properties with a numeric type are not set if the value is not in the correct format
	 */
	@Test
	public void valuesAreNotSetForNumericPropertiesWithWrongFormatTest()
	{
		GameConfigurationViewModel viewModel = new GameConfigurationViewModel();
		
		//boniProbabilityProperty
		String value = viewModel.boniProbabilityProperty().get();
		viewModel.boniProbabilityProperty().set("This is not a number");
		assertEquals(value, viewModel.boniProbabilityProperty().get(), 
			"The value was set although it had the wrong format.");
		
		//gameBoardSizeProperty
		value = viewModel.gameBoardSizeProperty().get();
		viewModel.gameBoardSizeProperty().set("This is not a number");
		assertEquals(value, viewModel.gameBoardSizeProperty().get(), 
			"The value was set although it had the wrong format.");
		
		//gameLengthLimitProperty
		value = viewModel.gameLengthLimitProperty().get();
		viewModel.gameLengthLimitProperty().set("This is not a number");
		assertEquals(value, viewModel.gameLengthLimitProperty().get(), 
			"The value was set although it had the wrong format.");
		
		//treasureCountProperty
		value = viewModel.treasureCountProperty().get();
		viewModel.treasureCountProperty().set("This is not a number");
		assertEquals(value, viewModel.treasureCountProperty().get(), 
			"The value was set although it had the wrong format.");
		
		//turnLengthLimitProperty
		value = viewModel.turnLengthLimitProperty().get();
		viewModel.turnLengthLimitProperty().set("This is not a number");
		assertEquals(value, viewModel.turnLengthLimitProperty().get(), 
			"The value was set although it had the wrong format.");
	}
	
	/**
	 * Tests @see GameConfigurationViewModel.cancelCommand
	 */
	@Test
	public void cancelCommandTest()
	{
		StageWrapperMock mock = new StageWrapperMock();
		NavigationManager.initialize(mock);
		GameConfigurationViewModel viewModel = new GameConfigurationViewModel();
		viewModel.getCancelCommand().execute();
		assertTrue(mock.getWasNavigatedBack(), "The execution of the cancelCommand did not result in navigating back.");
	}
	
	/**
	 * Tests @see GameConfigurationViewModel.cancelCommand
	 */
	@Test
	public void saveCommandCopyValuesBackTest() 
		throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	{
		ClientConnectionManager.initialize(SocketClientMock.class);
		ClientConnectionManager.startGameServerClient(new GameServerEntry("Testserver"));
		StageWrapperMock mock = new StageWrapperMock();
		NavigationManager.initialize(mock);
		GameConfigurationViewModel viewModel = new GameConfigurationViewModel();		
		Field field = viewModel.getClass().getDeclaredField("configuration");
		field.setAccessible(true);
		GameConfiguration configuration = new GameConfiguration();
		field.set(viewModel, configuration);
		fillViewModelWithTestData(viewModel);
		viewModel.getSaveCommand().execute();
		
		//Test if values have been copied back to the configuration
		assertEquals(0.5, (double)configuration.getBoniProbability());
		assertEquals(7, configuration.getSize().getX());
		assertEquals(7, configuration.getSize().getY());
		assertEquals(10, (int)configuration.getGameLengthLimit());
		assertEquals("Testserver", configuration.getServerName());
		assertEquals(5, (int)configuration.getTreasureCount());
		assertEquals(12, (int)configuration.getTurnLengthLimit());
		BoniConfiguration boniConfiguration = configuration.getBoni();
		assertFalse(boniConfiguration.getIsBeamEnabled());
		assertTrue(boniConfiguration.getIsShiftSolidEnabled());
		assertFalse(boniConfiguration.getIsShiftTwiceEnabled());
		assertTrue(boniConfiguration.getIsSwapEnabled());
		
		//Test that navigateBack is executed after copying back the values to configuration
		assertTrue(mock.getWasNavigatedBack(), "The execution of the cancelCommand did not result in navigating back.");
	}
	
	/**
	 * Tests @see GameConfigurationViewModel.cancelCommand
	 */
	@Test
	public void saveCommandSendsConfigurationRequestsTest() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	{
		ClientConnectionManager.initialize(SocketClientMock.class);
		ClientConnectionManager.startGameServerClient(new GameServerEntry("Testserver"));
		SocketClientMock clientMock = SocketClientMock.class.cast(ClientConnectionManager.getGameServerClient());
		StageWrapperMock mock = new StageWrapperMock();
		NavigationManager.initialize(mock);
		GameConfigurationViewModel viewModel = new GameConfigurationViewModel();		
		Field field = viewModel.getClass().getDeclaredField("configuration");
		field.setAccessible(true);
		GameConfiguration configuration = new GameConfiguration();
		field.set(viewModel, configuration);
		fillViewModelWithTestData(viewModel);
		viewModel.getSaveCommand().execute();
		
		assertEquals(ConfigurationDto.class, clientMock.getLastRequest().getClass());
		ConfigurationDto request = ConfigurationDto.class.cast(clientMock.getLastRequest());
		viewModel.getSaveCommand().execute();
		assertEquals(configuration.getAdmin(), request.getAdmin());
		assertSame(configuration.getBoni(), request.getBoni());
		assertEquals(configuration.getBoniProbability(), request.getBoniProbability());
		assertEquals(configuration.getGameLengthLimit(), request.getGameLengthLimit());
		assertEquals(configuration.getServerName(), request.getServerName());
		assertEquals(configuration.getSize().getX(), request.getSize().getX());
		assertEquals(configuration.getSize().getY(), request.getSize().getY());
		assertEquals(configuration.getTreasureCount(), request.getTreasureCount());
		assertEquals(configuration.getTurnLengthLimit(), request.getTurnLengthLimit());
	}
	
	private void fillViewModelWithTestData(GameConfigurationViewModel viewModel)
	{
		viewModel.boniProbabilityProperty().set("0.5");
		viewModel.gameBoardSizeProperty().set("7");
		viewModel.gameLengthLimitProperty().set("10");
		viewModel.gameServerNameProperty().set("Testserver");
		viewModel.isBeamEnabledProperty().set(false);
		viewModel.isShiftSolidEnabledProperty().set(true);
		viewModel.isShiftTwiceEnabledProperty().set(false);
		viewModel.isSwapEnabledProperty().set(true);
		viewModel.treasureCountProperty().set("5");
		viewModel.turnLengthLimitProperty().set("12");
	}
}
