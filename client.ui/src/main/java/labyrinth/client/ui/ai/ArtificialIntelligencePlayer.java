package labyrinth.client.ui.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import labyrinth.client.ui.ClientConnectionManager;
import labyrinth.contracts.communication.MessageType;
import labyrinth.contracts.communication.dtos.PlayerDto;
import labyrinth.contracts.communication.dtos.TileDto;
import labyrinth.contracts.communication.dtos.requests.TurnMovePlayerDto;
import labyrinth.contracts.communication.dtos.requests.TurnShiftTilesDto;
import labyrinth.contracts.communication.dtos.responses.GameStateResponseDto;
import labyrinth.contracts.communication.dtos.responses.SimpleSuccessResponseDto;
import labyrinth.contracts.communication.listeners.ActionListener;
import labyrinth.contracts.communication.listeners.ContinuousActionListener;
import labyrinth.contracts.entities.Coordinates;
import labyrinth.contracts.entities.game.BoardShiftingManager;
import labyrinth.contracts.entities.game.Player;
import labyrinth.contracts.entities.game.Tile;

/**
 * Class for an AI substitute for a player
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ArtificialIntelligencePlayer 
{
	//Properties
	
	private Coordinates nextMoveCoordinates;
	
	private Tile tileOutsideOfBoard;
	
	private Tile[][] board;
	
	private PlayerDto currentPlayer;
	
	private int boardSize;
	
	//Constructor
	
	/**
	 * ArtificialIntelligencePlayer constructor
	 * @param state The current game state
	 * @param currentPlayer the player of the client
	 * @param boardSize The size of the board
	 */
	public ArtificialIntelligencePlayer(GameStateResponseDto state, int boardSize, String currentPlayer, String activePlayer) 
	{ 
		this.boardSize = boardSize;
		updateState(state, currentPlayer); 
		
		ContinuousActionListener listener = new ContinuousActionListener(MessageType.CurrentPlayer, 
			content -> handleActivePlayerChanged(content));
		ClientConnectionManager.getGameServerClient().registerListener(listener);
		
		listener = new ContinuousActionListener(MessageType.GameState, content -> handleGameStateChanged(content));
		ClientConnectionManager.getGameServerClient().registerListener(listener);
		
		if(activePlayer.equals(currentPlayer))
			shiftTiles();
	}
	
	//Methods
	
	private void handleGameStateChanged(Object content)
	{
		GameStateResponseDto state = GameStateResponseDto.class.cast(content);
		updateState(state, currentPlayer.getName());
	}
	
	private void updateState(GameStateResponseDto state, String currentPlayerName)
	{
		//get current player
		Optional<PlayerDto> player1 = state.getPlayers().stream().filter(
				player -> player.getName().equals(currentPlayerName)).findFirst();
		if(player1.isPresent())
			currentPlayer = player1.get();
		
		//Update the board
		if(board == null)
			board = new Tile[boardSize][boardSize];
		for(TileDto tile : state.getBoard().getTiles())
		{
			if(tile.getX() == -1 && tile.getY() == -1)
				tileOutsideOfBoard = getTileFromDto(tile); 
			else
				board[tile.getX()][tile.getY()] = getTileFromDto(tile);
		}	
	}
	
	private void handleActivePlayerChanged(Object content)
	{
		String player = String.class.cast(content);
		if(player.equals(currentPlayer.getName()))
			shiftTiles();
	}
	
	private Tile getTileFromDto(TileDto dto)
	{
		Tile field = new Tile(dto.getType());
		field.setBonus(dto.getBoni());
		field.setPlayerBase(dto.getPlayerBase());
		field.setTreasure(dto.getTreasure());
		if(dto.getPlayer() != null)
			field.setPlayer(new Player(dto.getPlayer()));
		return field;
	}
	
	private void shiftTiles()
	{
		//Get target slot
		List<ShiftingResult> possibleSlots = getShiftingResults();
		ShiftingResult bestResult = possibleSlots.stream().sorted((slot1, slot2) -> Integer.compare(
			slot1.getDistance(), slot2.getDistance())).findFirst().get();
		
		//The coordinates of the field the ai is moving to next
		nextMoveCoordinates = bestResult.getBestCoordinatesAfterShift();
		
		//Listen for TurnShiftTiles-Response
		ActionListener listener = new ActionListener(MessageType.TurnShiftTiles, 
			content -> handleShiftTilesResponse(content));
		ClientConnectionManager.getGameServerClient().registerListener(listener);
		
		//Send request
		ClientConnectionManager.getGameServerClient().sendRequest(
			new TurnShiftTilesDto(bestResult.getSlot(), bestResult.getTileType()));
	}
	
	private void handleShiftTilesResponse(Object content)
	{
		SimpleSuccessResponseDto response = SimpleSuccessResponseDto.class.cast(content);
		if(response.getSuccess())
			movePlayer();
		else
			System.out.println("The AI was unable to shift the tiles");
	}
	
	private void movePlayer()
	{		
		//Listen for TurnMovePlayer-Response
		ActionListener listener = new ActionListener(MessageType.TurnMovePlayer, 
			content -> handleMovePlayerResponse(content));
		ClientConnectionManager.getGameServerClient().registerListener(listener);
		
		//Send request
		ClientConnectionManager.getGameServerClient().sendRequest(
			new TurnMovePlayerDto(nextMoveCoordinates.getX(), nextMoveCoordinates.getY()));
	}
	
	private void handleMovePlayerResponse(Object content)
	{
		SimpleSuccessResponseDto response = SimpleSuccessResponseDto.class.cast(content);
		if(!response.getSuccess())
			System.out.println("The AI was unable to move the player.");
	}
	
	private List<ShiftingResult> getShiftingResults()
	{
		List<Integer> possibleTileTypes =  getPossibleTileTypes(tileOutsideOfBoard.getType());
		List<ShiftingResult> results = new ArrayList<ShiftingResult>();	
		addShiftingUpResults(results, possibleTileTypes);
		addShiftingDownResults(results, possibleTileTypes);
		addShiftingLeftResults(results, possibleTileTypes);
		addShiftingRightResults(results, possibleTileTypes);		
		return results;
	}
	
	private void addShiftingUpResults(List<ShiftingResult> results, List<Integer> possibleTileTypes)
	{
		for(int i = 1;i < boardSize;i++)
		{
			if(i % 2 != 0)
			{
				for(int tileType : possibleTileTypes)
				{
					Tile[][] temporaryBoard = getBoardAfterShiftingUp(i, tileType);			
					ShiftingResult result = new ShiftingResult(3 * boardSize - i - 1, tileType, boardSize, temporaryBoard, 
						currentPlayer);
					results.add(result);
				}
			}
		}
	}
	
	private void addShiftingDownResults(List<ShiftingResult> results, List<Integer> possibleTileTypes)
	{
		for(int i = 1;i < boardSize;i++)
		{
			if(i % 2 != 0)
			{
				for(int tileType : possibleTileTypes)
				{
					Tile[][] temporaryBoard = getBoardAfterShiftingDown(i, tileType);		
					ShiftingResult result = new ShiftingResult(i, tileType, boardSize, temporaryBoard, currentPlayer);
					results.add(result);
				}
			}
		}
	}
	
	private void addShiftingLeftResults(List<ShiftingResult> results, List<Integer> possibleTileTypes)
	{
		for(int i = 1;i < boardSize;i++)
		{
			if(i % 2 != 0)
			{
				for(int tileType : possibleTileTypes)
				{
					Tile[][] temporaryBoard = getBoardAfterShiftingLeft(i, tileType);					
					ShiftingResult result = new ShiftingResult(boardSize + i, tileType, boardSize, temporaryBoard, 
						currentPlayer);
					results.add(result);
				}
			}
		}
	}
	
	private void addShiftingRightResults(List<ShiftingResult> results, List<Integer> possibleTileTypes)
	{
		for(int i = 1;i < boardSize;i++)
		{
			if(i % 2 != 0)
			{
				for(int tileType : possibleTileTypes)
				{
					Tile[][] temporaryBoard = getBoardAfterShiftingRight(i, tileType);	
					ShiftingResult result = new ShiftingResult(4 * boardSize - i - 1, tileType, boardSize, temporaryBoard, 
						currentPlayer);
					results.add(result);
				}
			}
		}
	}
	
	private Tile[][] getBoardAfterShiftingDown(int x, int tileType)
	{
		Tile[][] temporaryBoard = copyBoard();
		BoardShiftingManager.shiftTileDown(x, tileType, boardSize, temporaryBoard, tileOutsideOfBoard);		
		return temporaryBoard;
	}
	
	private Tile[][] getBoardAfterShiftingLeft(int y, int tileType)
	{
		Tile[][] temporaryBoard = copyBoard();
		BoardShiftingManager.shiftTileLeft(y, tileType, boardSize, temporaryBoard, tileOutsideOfBoard);		
		return temporaryBoard;
	}
	
	private Tile[][] getBoardAfterShiftingUp(int x, int tileType)
	{
		Tile[][] temporaryBoard = copyBoard();
		BoardShiftingManager.shiftTileUp(x, tileType, boardSize, temporaryBoard, tileOutsideOfBoard);	
		return temporaryBoard;
	}
	
	private Tile[][] getBoardAfterShiftingRight(int y, int tileType)
	{
		Tile[][] temporaryBoard = copyBoard();
		BoardShiftingManager.shiftTileLeft(y, tileType, boardSize, temporaryBoard, tileOutsideOfBoard);
		return temporaryBoard;
	}
	
	@SuppressWarnings("serial")
	private List<Integer> getPossibleTileTypes(int currentType)
	{
		if(currentType < 4)
			return new ArrayList<Integer>() {{ add(0); add(1); add(2); add(3); }};
		if(currentType < 8)
			return new ArrayList<Integer>() {{ add(4); add(5); add(6); add(7); }};
		return new ArrayList<Integer>() {{ add(8); add(9); }};
	}
	
	private Tile[][] copyBoard()
	{
		int i = 0; int j = 0;
		try
		{
			Tile[][] copy = new Tile[boardSize][boardSize];
			for(i = 0;i < boardSize;i++)
				for(j = 0;j < boardSize;j++)
				{
					copy[i][j] = new Tile(board[i][j].getType());
					copy[i][j].setBonus(board[i][j].getBonus());
					copy[i][j].setPlayer(board[i][j].getPlayer());
					copy[i][j].setPlayerBase(board[i][j].getPlayerBase());
					copy[i][j].setTreasure(board[i][j].getTreasure());
				}	
			return copy;
		}
		catch(Exception e)
		{
			System.out.println(i + "," + j);
			throw e;
		}
	}
}
