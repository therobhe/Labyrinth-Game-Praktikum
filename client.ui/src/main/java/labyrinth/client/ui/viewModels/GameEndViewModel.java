package labyrinth.client.ui.viewModels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import labyrinth.client.ui.ClientAchievementManager;
import labyrinth.client.ui.NavigationManager;
import labyrinth.client.ui.resources.Errors;
import labyrinth.client.ui.views.LobbyView;
import labyrinth.contracts.communication.dtos.PlayerDtoBase;
import labyrinth.contracts.communication.dtos.responses.EndGameResponseDto;

/**
 * ViewModel for @see GameEndView
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class GameEndViewModel extends ViewModelBase
{
	//Properties
	
	/**
	 * Navigation-Parameter for the EndGame-Response the server sent
	 */
	public static final String END_GAME_RESPONSE_PARAMETER = "END_GAME_RESPONSE_PARAMETER";
	
	/**
	 * Navigation-Parameter for the name of the player of the client
	 */
	public static final String CURRENT_PLAYER_PARAMETER = "CURRENT_PLAYER_PARAMETER";
	
	private String currentPlayer;
	
	private IntegerProperty player1ScoreProperty = new SimpleIntegerProperty();
	/**
	 * Corresponding getter
	 * @return player1ScoreProperty
	 */
	public IntegerProperty player1ScoreProperty() { return player1ScoreProperty; }
	
	private StringProperty player1NameProperty = new SimpleStringProperty();
	/**
	 * Corresponding getter
	 * @return player1NameProperty
	 */
	public StringProperty player1NameProperty() { return player1NameProperty; }
	
	private IntegerProperty player2ScoreProperty = new SimpleIntegerProperty();
	/**
	 * Corresponding getter
	 * @return player2ScoreProperty
	 */
	public IntegerProperty player2ScoreProperty() { return player2ScoreProperty; }
	
	private StringProperty player2NameProperty = new SimpleStringProperty();
	/**
	 * Corresponding getter
	 * @return player2NameProperty
	 */
	public StringProperty player2NameProperty() { return player2NameProperty; }
	
	private BooleanProperty player3VisibleProperty = new SimpleBooleanProperty();
	/**
	 * Corresponding getter
	 * @return player3VisibleProperty
	 */
	public BooleanProperty player3VisibleProperty() { return player3VisibleProperty; }
	
	private IntegerProperty player3ScoreProperty = new SimpleIntegerProperty();
	/**
	 * Corresponding getter
	 * @return player3ScoreProperty
	 */
	public IntegerProperty player3ScoreProperty() { return player3ScoreProperty; }
	
	private StringProperty player3NameProperty = new SimpleStringProperty();
	/**
	 * Corresponding getter
	 * @return player3NameProperty
	 */
	public StringProperty player3NameProperty() { return player3NameProperty; }
	
	private BooleanProperty player4VisibleProperty = new SimpleBooleanProperty();
	/**
	 * Corresponding getter
	 * @return player4VisibleProperty
	 */
	public BooleanProperty player4VisibleProperty() { return player4VisibleProperty; }
	
	private IntegerProperty player4ScoreProperty = new SimpleIntegerProperty();
	/**
	 * Corresponding getter
	 * @return player4ScoreProperty
	 */
	public IntegerProperty player4ScoreProperty() { return player4ScoreProperty; }
	
	private StringProperty player4NameProperty = new SimpleStringProperty();
	/**
	 * Corresponding getter
	 * @return player4NameProperty
	 */
	public StringProperty player4NameProperty() { return player4NameProperty; }
	
	private Command navigateToLobbyCommand = new DelegateCommand(() -> new Action() 
	{
		@Override
		protected void action() { navigateToLobby(); }
	});
	/**
	 * Corresponding getter
	 * @return navigateToLobbyCommand
	 */
	public Command getNavigateToLobbyCommand() { return navigateToLobbyCommand; } 
	
	//Methods
	
	/**
	 * {@inheritDoc}
	 * @throws IllegalArgumentException when a parameter is missing
	 */
	@Override
	public void handleNavigation(Map<String, Object> parameters) 
	{ 
		if(parameters == null || parameters.get(END_GAME_RESPONSE_PARAMETER) == null)
			throw new IllegalArgumentException(Errors.getGameEndViewModel_MissingParameter());	
		
		if(parameters.containsKey(CURRENT_PLAYER_PARAMETER))
			currentPlayer = String.class.cast(parameters.get(CURRENT_PLAYER_PARAMETER));
		
		//Initialize the binding properties
		EndGameResponseDto response = EndGameResponseDto.class.cast(parameters.get(END_GAME_RESPONSE_PARAMETER));
		initializeBindingProperties(response);
		
		//Commit the results of the game to the achievements
		commitGameResultsToAchievements();
	}
	
	private void navigateToLobby()
	{
    	//Navigate to the LobbyView
    	Map<String, Object> parameters = new HashMap<String, Object>();
    	parameters.put(LobbyViewModel.USER_PARAMETER, currentPlayer);
    	NavigationManager.navigate(LobbyView.class, parameters);
	}
	
	private void commitGameResultsToAchievements()
	{
		if(currentPlayer != null)
		{
			if(currentPlayer.equals(player1NameProperty.get())) //User is winner
			{
				ClientAchievementManager.addWonGame();
				ClientAchievementManager.addAquiredScore(player1ScoreProperty.get());
			}
			else //User is not the winner
				ClientAchievementManager.addPlayedGame();
			
			//Add score to score achievements
			if(currentPlayer.equals(player2NameProperty.get()))
				ClientAchievementManager.addAquiredScore(player2ScoreProperty.get());
			if(currentPlayer.equals(player3NameProperty.get()))
				ClientAchievementManager.addAquiredScore(player3ScoreProperty.get());
			if(currentPlayer.equals(player4NameProperty.get()))
				ClientAchievementManager.addAquiredScore(player4ScoreProperty.get());						
		}
	}
	
	private void initializeBindingProperties(EndGameResponseDto response)
	{
		List<PlayerDtoBase> players = response.getPlayers().stream().sorted(
			(player1, player2) -> Integer.compare(player2.getScore(), player1.getScore())).collect(Collectors.toList());
		player1NameProperty.set(players.get(0).getName());
		player1ScoreProperty.set(players.get(0).getScore());
		player2NameProperty.set(players.get(1).getName());
		player2ScoreProperty.set(players.get(1).getScore());
		
		if(players.size() > 2) //More than 2 players
		{
			player3VisibleProperty.set(true);
			player3NameProperty.set(players.get(2).getName());
			player3ScoreProperty.set(players.get(2).getScore());
			
			if(players.size() > 3) //More than 3 players
			{
				player4VisibleProperty.set(true);
				player4NameProperty.set(players.get(3).getName());
				player4ScoreProperty.set(players.get(3).getScore());
			}
		}
	}
}
