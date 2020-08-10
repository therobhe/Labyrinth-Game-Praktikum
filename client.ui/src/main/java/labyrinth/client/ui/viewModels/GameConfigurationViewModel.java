package labyrinth.client.ui.viewModels;

import java.util.Map;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import labyrinth.client.ui.ClientConnectionManager;
import labyrinth.client.ui.NavigationManager;
import labyrinth.client.ui.resources.Errors;
import labyrinth.contracts.communication.dtos.requests.ConfigurationDto;
import labyrinth.contracts.entities.Coordinates;
import labyrinth.contracts.entities.lobby.BoniConfiguration;
import labyrinth.contracts.entities.lobby.GameConfiguration;
import labyrinth.contracts.utilities.HelperMethods;

/**
 * ViewModel for @see GameConfigurationView
 * @author Lukas Reinhardt
 * @version 1.1
 */
public final class GameConfigurationViewModel extends ViewModelBase 
{
	//Properties
	
	/**
	 * Navigation-Parameter for the configuration of the game
	 */
	public static final String CONFIGURATION_PARAMETER = "CONFIGURATION_PARAMETER";
	
	private GameConfiguration configuration;
	
	private Command cancelCommand = new DelegateCommand(() -> new Action() 
	{
		@Override
		protected void action() { navigateBack(); }
	});
    /**
	 * Getter for cancelCommand
	 * @return cancelCommand
	 */
	public Command getCancelCommand() { return cancelCommand; }
	
	private Command saveCommand = new DelegateCommand(() -> new Action() 
	{
		@Override
		protected void action() { saveChanges(); }
	});
    /**
	 * Getter for saveCommand
	 * @return saveCommand
	 */
	public Command getSaveCommand() { return saveCommand; }
	
	private StringProperty gameServerNameProperty = new SimpleStringProperty();
	/**
	 * Getter for gameServerNameProperty
	 * @return gameServerNameProperty
	 */
	public StringProperty gameServerNameProperty() { return gameServerNameProperty; }
	/**
	 * Getter for the value of gameServerNameProperty
	 * @return The value of gameServerNameProperty
	 */
	public String getGameServerNameProperty() { return gameServerNameProperty.get(); }
	
	private StringProperty gameBoardSizeProperty = new SimpleStringProperty()
	{
		@Override
		public void set(String value) 
		{
			if(validateGameBoardSize(value))
				super.set(value);
		}
	};
	/**
	 * Getter for gameBoardSizeProperty
	 * @return gameBoardSizeProperty
	 */
	public StringProperty gameBoardSizeProperty() { return gameBoardSizeProperty; }
	/**
	 * Getter for the value of gameBoardSizeProperty
	 * @return The value of gameBoardSizeProperty
	 */
	public Integer getGameBoardSizeProperty() 
	{ 
		String value = gameBoardSizeProperty.get();
		return value != null ? Integer.parseInt(gameBoardSizeProperty.get()) : null;
	}
	
	private StringProperty treasureCountProperty = new SimpleStringProperty()
	{
		@Override
		public void set(String value) 
		{
			if(HelperMethods.canParseInt(value))
				super.set(value);
		}
	};
	/**
	 * Getter for treasureCountProperty
	 * @return treasureCountProperty
	 */
	public StringProperty treasureCountProperty() { return treasureCountProperty; }
	/**
	 * Getter for the value of treasureCountProperty
	 * @return The value of treasureCountProperty
	 */
	public Integer getTreasureCountProperty() 
	{ 
		String value = treasureCountProperty.get();
		return value != null ? Integer.parseInt(treasureCountProperty.get()) : null;
	}
	
	private StringProperty boniProbabilityProperty = new SimpleStringProperty()
	{
		@Override
		public void set(String value) 
		{
			if(validateBonusProbability(value))
				super.set(value);			
		}
	};
	/**
	 * Getter for boniProbabilityProperty
	 * @return boniProbabilityProperty
	 */
	public StringProperty boniProbabilityProperty() { return boniProbabilityProperty; }
	/**
	 * Getter for the value of boniProbabilityProperty
	 * @return The value of boniProbabilityProperty
	 */
	public Double getBoniProbabilityProperty() 
	{ 
		String value = boniProbabilityProperty.get();
		return value != null ? Double.parseDouble(boniProbabilityProperty.get()) : null; 
	}
	
	private StringProperty turnLengthLimitProperty = new SimpleStringProperty()
	{
		@Override
		public void set(String value) 
		{
			if(HelperMethods.canParseInt(value))
				super.set(value);
		}
	};
	/**
	 * Getter for turnLengthLimitProperty
	 * @return turnLengthLimitProperty
	 */
	public StringProperty turnLengthLimitProperty() { return turnLengthLimitProperty; }
	/**
	 * Getter for the value of turnLengthLimitProperty
	 * @return The value of turnLengthLimitProperty
	 */
	public Integer getTurnLengthLimitProperty() 
	{ 
		String value = turnLengthLimitProperty.get();
		return value != null ? Integer.parseInt(turnLengthLimitProperty.get()) : null; 
	}
	
	private StringProperty gameLengthLimitProperty = new SimpleStringProperty()
	{
		@Override
		public void set(String value) 
		{
			if(HelperMethods.canParseInt(value))
				super.set(value);
		}
	};
	/**
	 * Getter for gameLengthLimitProperty
	 * @return gameLengthLimitProperty
	 */
	public StringProperty gameLengthLimitProperty() { return gameLengthLimitProperty; }
	/**
	 * Getter for the value of gameLengthLimitProperty
	 * @return The value of gameLengthLimitProperty
	 */
	public Integer getGameLengthLimitProperty() 
	{ 
		String value = gameLengthLimitProperty.get();
		return value != null ? Integer.parseInt(gameLengthLimitProperty.get()) : null; 
	}
	
	private BooleanProperty isBeamEnabledProperty = new SimpleBooleanProperty();
	/**
	 * Getter for isBeamEnabledProperty
	 * @return isBeamEnabledProperty
	 */
	public BooleanProperty isBeamEnabledProperty() { return isBeamEnabledProperty; }
	/**
	 * Getter for the value of isBeamEnabledProperty
	 * @return The value of isBeamEnabledProperty
	 */
	public boolean getIsBeamEnabledProperty() { return isBeamEnabledProperty.get(); }
	
	private BooleanProperty isShiftSolidEnabledProperty = new SimpleBooleanProperty();
	/**
	 * Getter for isShiftSolidEnabledProperty
	 * @return isShiftSolidEnabledProperty
	 */
	public BooleanProperty isShiftSolidEnabledProperty() { return isShiftSolidEnabledProperty; }
	/**
	 * Getter for the value of isShiftSolidEnabledProperty
	 * @return The value of isShiftSolidEnabledProperty
	 */
	public boolean getIsShiftSolidEnabledProperty() { return isShiftSolidEnabledProperty.get(); }
	
	private BooleanProperty isShiftTwiceEnabledProperty = new SimpleBooleanProperty();
	/**
	 * Getter for isShiftTwiceEnabledProperty
	 * @return isShiftTwiceEnabledProperty
	 */
	public BooleanProperty isShiftTwiceEnabledProperty() { return isShiftTwiceEnabledProperty; }
	/**
	 * Getter for the value of isShiftTwiceEnabledProperty
	 * @return The value of isShiftTwiceEnabledProperty
	 */
	public boolean getIsShiftTwiceEnabledProperty() { return isShiftTwiceEnabledProperty.get(); }
	
	private BooleanProperty isSwapEnabledProperty = new SimpleBooleanProperty();
	/**
	 * Getter for isSwapEnabledProperty
	 * @return isSwapEnabledProperty
	 */
	public BooleanProperty isSwapEnabledProperty() { return isSwapEnabledProperty; }
	/**
	 * Getter for the value of isSwapEnabledProperty
	 * @return The value of isSwapEnabledProperty
	 */
	public boolean getIsSwapEnabledProperty() { return isSwapEnabledProperty.get(); }
	
	//Methods
	
	/**
	 * {@inheritDoc}
	 * @throws IllegalArgumentException when no CONFIGURATION_PARAMETER has been passed with the navigation
	 */
	@Override
	public void handleNavigation(Map<String, Object> parameters)
	{
		if(parameters == null || parameters.get(CONFIGURATION_PARAMETER) == null)
			throw new IllegalArgumentException(Errors.getConnectToGameServerDialogViewModel_MissingParameter());
		
		configuration = GameConfiguration.class.cast(parameters.get(CONFIGURATION_PARAMETER));
		
		//Initialize the bindable properties of the VM with the values of the current configuration
		initializeBindableProperties();
	}
	
	public boolean validateGameBoardSize(String value)
	{
		if(HelperMethods.canParseInt(value))
		{
			int numberValue = Integer.parseInt(value);
			if(numberValue < 7 || numberValue % 2 == 0)
				return false;
			return true;
		}
		return false;
	}
	
	public boolean validateBonusProbability(String value)
	{
		if(HelperMethods.canParseDouble(value))
		{
			double numberValue = Double.parseDouble(value);
			if(numberValue < 0 || numberValue > 1)
				return false;
			return true;
		}
		return false;
	}
	
	private void initializeBindableProperties()
	{
		gameServerNameProperty.set(configuration.getServerName());
		gameBoardSizeProperty.set("" + configuration.getSize().getX());
		treasureCountProperty.set("" + configuration.getTreasureCount());
		boniProbabilityProperty.set("" + configuration.getBoniProbability());
		turnLengthLimitProperty.set("" + configuration.getTurnLengthLimit());
		gameLengthLimitProperty.set("" + configuration.getGameLengthLimit());
		BoniConfiguration boniConfiguration = configuration.getBoni();
		isBeamEnabledProperty.set(boniConfiguration.getIsBeamEnabled());
		isShiftTwiceEnabledProperty.set(boniConfiguration.getIsShiftTwiceEnabled());
		isShiftSolidEnabledProperty.set(boniConfiguration.getIsShiftSolidEnabled());
		isSwapEnabledProperty.set(boniConfiguration.getIsSwapEnabled());
	}
	
	private void navigateBack() { NavigationManager.navigateBack(); }
	
	private void saveChanges()
	{
		//Copy values back to the configuration to "save"
		copyPropertiesbackToConfiguration();
		
		//Send Configuration-Request to GameServer
		sendConfigurationRequest();
		
		//Navigate back to the Lobby
		navigateBack();
	}
	
	private void copyPropertiesbackToConfiguration()
	{
		configuration.setServerName(getGameServerNameProperty());
		configuration.setBoniProbability(getBoniProbabilityProperty());
		configuration.setGameLengthLimit(getGameLengthLimitProperty());
		configuration.setSize(new Coordinates(getGameBoardSizeProperty(), getGameBoardSizeProperty()));
		configuration.setTreasureCount(getTreasureCountProperty());
		configuration.setTurnLengthLimit(getTurnLengthLimitProperty());
		BoniConfiguration boniConfiguration = configuration.getBoni();
		boniConfiguration.setIsBeamEnabled(getIsBeamEnabledProperty());
		boniConfiguration.setIsShiftSolidEnabled(getIsShiftSolidEnabledProperty());
		boniConfiguration.setIsShiftTwiceEnabled(getIsShiftTwiceEnabledProperty());
		boniConfiguration.setIsSwapEnabled(getIsSwapEnabledProperty());
	}
	
	private void sendConfigurationRequest()
	{
		ConfigurationDto request = new ConfigurationDto(configuration);
		ClientConnectionManager.getGameServerClient().sendRequest(request);
	}
}
