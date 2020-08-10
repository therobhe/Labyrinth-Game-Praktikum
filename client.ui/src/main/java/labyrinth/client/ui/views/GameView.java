package labyrinth.client.ui.views;

import java.net.URL;
import java.util.ResourceBundle;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import labyrinth.client.ui.App;
import labyrinth.client.ui.viewModels.GameViewModel;
import labyrinth.client.ui.viewModels.PlayerBindingModel;
import labyrinth.client.ui.viewModels.TileBindingModel;

/**
 * Code behind class of the view for the GameView
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class GameView extends ViewBase<GameViewModel>
{
	//Controls
	
	@FXML
	private HBox achievementHBox;
	@FXML
	private Label achievementLabel;
	
	@FXML
	private GridPane boardGrid;
	
	@FXML
	private Label actionTextLabel;
	
	@FXML
	private AnchorPane tileOutsideOfBoardAnchorPane;	
	@FXML
	private ImageView currentTreasureImageView;
	
	@FXML
	private VBox player1VBox;
	@FXML
	private VBox player2VBox;
	@FXML
	private VBox player3VBox;
	@FXML
	private VBox player4VBox;
	
	@FXML
	private Button useSwapButton;
	@FXML
	private Button useBeamButton;
	@FXML
	private Button useShiftTwiceButton;
	@FXML
	private Button useShiftSolidButton;
	@FXML
	private Button switchTileTypeClockwiseButton;
	@FXML
	private Button switchTileTypeCounterClockwiseButton;
	
	//Player1	
	@FXML
	private Circle player1ColorCircle;
	@FXML
	private Label player1PlayerNameLabel;	
	@FXML
	private Label player1ScoreLabel;
	@FXML
	private Label player1RemainingTreasuresLabel;
	@FXML
	private Label player1SwapLabel;
	@FXML
	private Label player1BeamLabel;
	@FXML
	private Label player1ShiftTwiceLabel;
	@FXML
	private Label player1ShiftSolidLabel;
	
	//Player2	
	@FXML
	private Circle player2ColorCircle;
	@FXML
	private Label player2PlayerNameLabel;	
	@FXML
	private Label player2ScoreLabel;	
	@FXML
	private Label player2RemainingTreasuresLabel;
	@FXML
	private Label player2SwapLabel;
	@FXML
	private Label player2BeamLabel;
	@FXML
	private Label player2ShiftTwiceLabel;
	@FXML
	private Label player2ShiftSolidLabel;
	
	//Player3
	@FXML
	private Circle player3ColorCircle;
	@FXML
	private Label player3PlayerNameLabel;	
	@FXML
	private Label player3ScoreLabel;	
	@FXML
	private Label player3RemainingTreasuresLabel;
	@FXML
	private Label player3SwapLabel;
	@FXML
	private Label player3BeamLabel;
	@FXML
	private Label player3ShiftTwiceLabel;
	@FXML
	private Label player3ShiftSolidLabel;
	
	//Player4
	@FXML
	private Circle player4ColorCircle;
	@FXML
	private Label player4PlayerNameLabel;	
	@FXML
	private Label player4ScoreLabel;	
	@FXML
	private Label player4RemainingTreasuresLabel;
	@FXML
	private Label player4SwapLabel;
	@FXML
	private Label player4BeamLabel;
	@FXML
	private Label player4ShiftTwiceLabel;
	@FXML
	private Label player4ShiftSolidLabel;
	
	//ViewModel
	
	@InjectViewModel
	private GameViewModel viewModel;
	
	//Properties
	
	private int tileOutsideOfBoardSize = 150;
	
	private TileView[][] tiles;
	
	private TileView tileOutsideOfBoard;
	
	//Methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{ 
		String style = "-fx-border-color: black;";
		actionTextLabel.textProperty().bind(viewModel.actionTextProperty());
		player1VBox.setStyle(style);
		player2VBox.setStyle(style);
		player3VBox.setStyle(style);
		player4VBox.setStyle(style);
		
		//Bindings for achievement notifications
		actionTextLabel.visibleProperty().bind(viewModel.achievementTextVisibleProperty().not());
		achievementHBox.visibleProperty().bind(viewModel.achievementTextVisibleProperty());
		achievementLabel.textProperty().bind(viewModel.achievementTextProperty());
		
		if(App.getScreenHeight() < 1000 || App.getScreenWidth() < 1800)
		{
			boardGrid.setPrefWidth(500);
			boardGrid.setPrefHeight(500);
			tileOutsideOfBoardAnchorPane.setPrefHeight(100);
			tileOutsideOfBoardAnchorPane.setPrefWidth(100);
			currentTreasureImageView.setFitWidth(100);
			currentTreasureImageView.setFitHeight(100);
			tileOutsideOfBoardSize = 100;
		}		
	}
	
	@FXML
	private void useSwap() { viewModel.getUseSwapBonusCommand().execute(); }
	
	@FXML
	private void useBeam() { viewModel.getUseBeamBonusCommand().execute(); }
	
	@FXML
	private void useShiftTwice() { viewModel.getUseShiftTwiceBonusCommand().execute(); }
	
	@FXML
	private void useShiftSolid() { viewModel.getUseShiftSolidBonusCommand().execute(); }
	
	@FXML
	private void switchTileTypeClockwise() { viewModel.getSwitchTileTypeClockwiseCommand().execute(); }
	
	@FXML
	private void switchTileTypeCounterClockwise() { viewModel.getSwitchTileTypeCounterClockwiseCommand().execute(); }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onLoaded()
	{
		//Initialize Bindings
		initializeBindingsForPlayer1();
		initializeBindingsForPlayer2();
		initializeBindingsForPlayer3();
		initializeBindingsForPlayer4();
		
		//Initialize the board
		initializeBoard();
		
		//Initialize the Tiles
		initializeTiles();
		
		//Initialize the Command bindings
		initializeCommandBindings();
	}
	
	private void initializeCommandBindings()
	{
		tileOutsideOfBoardAnchorPane.getChildren().add(tileOutsideOfBoard.getStackPane());
		currentTreasureImageView.imageProperty().bind(viewModel.getPlayer1().treasureImageProperty());
		switchTileTypeClockwiseButton.disableProperty()
			.bind(viewModel.getSwitchTileTypeClockwiseCommand().executableProperty().not());
		switchTileTypeCounterClockwiseButton.disableProperty()
			.bind(viewModel.getSwitchTileTypeCounterClockwiseCommand().executableProperty().not());
		useSwapButton.visibleProperty().set(!viewModel.getIsSpectator());
		useSwapButton.disableProperty().bind(viewModel.getUseSwapBonusCommand().executableProperty().not());
		useBeamButton.visibleProperty().set(!viewModel.getIsSpectator());
		useBeamButton.disableProperty().bind(viewModel.getUseBeamBonusCommand().executableProperty().not());
		useShiftTwiceButton.visibleProperty().set(!viewModel.getIsSpectator());
		useShiftTwiceButton.disableProperty().bind(viewModel.getUseShiftTwiceBonusCommand().executableProperty().not());
		useShiftSolidButton.visibleProperty().set(!viewModel.getIsSpectator());
		useShiftSolidButton.disableProperty().bind(viewModel.getUseShiftSolidBonusCommand().executableProperty().not());
	}
	
	private void initializeBoard()
	{
		for(int i = 0;i < viewModel.getBoardSize();i++)
		{
			boardGrid.getColumnConstraints().add(new ColumnConstraints());
			boardGrid.getRowConstraints().add(new RowConstraints());
		}
	}
	
	private void initializeTiles()
	{
		int boardSize = viewModel.getBoardSize();
		double tileSize = boardGrid.getPrefWidth() / boardSize;
		tiles = new TileView[boardSize][boardSize];
		for(TileBindingModel tile : viewModel.getTiles())
		{
			int x = tile.getCoordinates().getX();
			int y = tile.getCoordinates().getY();
			TileView tileView = new TileView(tile, tileSize, clickedTile -> onTileClick(clickedTile), x, y);
			tiles[x][y] = tileView;
			boardGrid.add(tileView.getStackPane(), x, y);
		}
		
		tileOutsideOfBoard = new TileView(viewModel.getTileOutsideOfBoard(), tileOutsideOfBoardSize, (clickedTile) -> {}, -1, -1);
	}
	
	private void onTileClick(TileView tile) { viewModel.doTileAction(tile.getX(), tile.getY()); }
	
	private void initializeBindingsForPlayer1()
	{
		PlayerBindingModel player = viewModel.getPlayer1();
		player1ColorCircle.setFill(Color.valueOf(player.getColor()));
		player1PlayerNameLabel.textProperty().bind(player.playerNameProperty());
		player1ScoreLabel.textProperty().bind(player.scoreProperty().asString());
		player1RemainingTreasuresLabel.textProperty().bind(player.remainingTreasuresProperty().asString());
		player1SwapLabel.textProperty().bind(player.swapProperty().asString());
		player1BeamLabel.textProperty().bind(player.beamProperty().asString());
		player1ShiftTwiceLabel.textProperty().bind(player.shiftTwiceProperty().asString());
		player1ShiftSolidLabel.textProperty().bind(player.shiftSolidProperty().asString());
	}
	
	private void initializeBindingsForPlayer2()
	{
		PlayerBindingModel player = viewModel.getPlayer2();
		player2ColorCircle.setFill(Color.valueOf(player.getColor()));
		player2PlayerNameLabel.textProperty().bind(player.playerNameProperty());
		player2ScoreLabel.textProperty().bind(player.scoreProperty().asString());
		player2RemainingTreasuresLabel.textProperty().bind(player.remainingTreasuresProperty().asString());
		player2SwapLabel.textProperty().bind(player.swapProperty().asString());
		player2BeamLabel.textProperty().bind(player.beamProperty().asString());
		player2ShiftTwiceLabel.textProperty().bind(player.shiftTwiceProperty().asString());
		player2ShiftSolidLabel.textProperty().bind(player.shiftSolidProperty().asString());
	}
	
	private void initializeBindingsForPlayer3()
	{
		PlayerBindingModel player = viewModel.getPlayer3();
		if(player != null)
		{
			player3ColorCircle.setFill(Color.valueOf(player.getColor()));
			player3PlayerNameLabel.textProperty().bind(player.playerNameProperty());
			player3ScoreLabel.textProperty().bind(player.scoreProperty().asString());
			player3RemainingTreasuresLabel.textProperty().bind(player.remainingTreasuresProperty().asString());
			player3SwapLabel.textProperty().bind(player.swapProperty().asString());
			player3BeamLabel.textProperty().bind(player.beamProperty().asString());
			player3ShiftTwiceLabel.textProperty().bind(player.shiftTwiceProperty().asString());
			player3ShiftSolidLabel.textProperty().bind(player.shiftSolidProperty().asString());
		}
	}
	
	private void initializeBindingsForPlayer4()
	{
		PlayerBindingModel player = viewModel.getPlayer4();
		if(player != null)
		{
			player4ColorCircle.setFill(Color.valueOf(player.getColor()));
			player4PlayerNameLabel.textProperty().bind(player.playerNameProperty());
			player4ScoreLabel.textProperty().bind(player.scoreProperty().asString());
			player4RemainingTreasuresLabel.textProperty().bind(player.remainingTreasuresProperty().asString());
			player4SwapLabel.textProperty().bind(player.swapProperty().asString());
			player4BeamLabel.textProperty().bind(player.beamProperty().asString());
			player4ShiftTwiceLabel.textProperty().bind(player.shiftTwiceProperty().asString());
			player4ShiftSolidLabel.textProperty().bind(player.shiftSolidProperty().asString());
		}
	}
}