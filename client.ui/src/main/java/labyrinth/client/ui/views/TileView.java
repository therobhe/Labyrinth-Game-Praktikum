package labyrinth.client.ui.views;

import java.util.function.Consumer;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import labyrinth.client.ui.viewModels.TileBindingModel;

/**
 * View for a single Tile in the GameView
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class TileView
{
	//Properties
	
	private int x;
	/**
	 * Corresponding getter
	 * @return Y-Coordinate of the tile
	 */
	public int getX() { return x; }
	
	private int y;
	/**
	 * Corresponding getter
	 * @return X-Coordinate of the tile
	 */
	public int getY() { return y; }
	
	private StackPane stackPane = new StackPane();
	/**
	 * Corresponding getter
	 * @return The StackPane which represents a stackPane 
	 */
	public StackPane getStackPane() { return stackPane; }
	
	//Constructor
	
	/**
	 * TileView Constructor
	 * @param model The model to bind to
	 * @param size The size of one tile
	 * @param clickAction The action to perform on click
	 * @param x X-Coordinate of the tile
	 * @param y Y-Coordinate of the tile
	 */
	public TileView(TileBindingModel model, double size, Consumer<TileView> clickAction, int x, int y)
	{
		this.x = x;
		this.y = y;
		initializeStackPane(size, clickAction);
		initializeChildElements(model, size);
	}
	
	private void initializeStackPane(double size, Consumer<TileView> clickAction)
	{
		stackPane.setStyle("-fx-background-color: white;");
		stackPane.setPrefHeight(size);
		stackPane.setPrefWidth(size);
		stackPane.setOnMouseEntered(e -> stackPane.setStyle("-fx-background-color: black;"));
		stackPane.setOnMouseExited(e -> stackPane.setStyle("-fx-background-color: white;"));
		stackPane.setOnMouseClicked(e -> clickAction.accept(this));
	}
	
	private void initializeChildElements(TileBindingModel model, double size)
	{		
		initializeImageViews(model, size);
		
		Circle playerCircle = new Circle();
		playerCircle.setRadius(size / 4);
		playerCircle.fillProperty().bind(model.playerColorProperty());
		playerCircle.visibleProperty().bind(model.playerCircleVisibleProperty());
		stackPane.getChildren().add(playerCircle);
		
		Circle playerBaseCircle = new Circle();
		playerBaseCircle.setRadius(size / 4);
		playerBaseCircle.fillProperty().bind(model.playerBaseColorProperty());
		playerBaseCircle.visibleProperty().bind(model.playerBaseCircleVisibleProperty());
		stackPane.getChildren().add(playerBaseCircle);
	}
	
	private void initializeImageViews(TileBindingModel model, double size)
	{
		ImageView tileImageView = new ImageView();
		tileImageView.setFitHeight(size - 1);
		tileImageView.setFitWidth(size - 1);
		tileImageView.imageProperty().bind(model.tileImageProperty());
		stackPane.getChildren().add(tileImageView);
		
		ImageView treasureImageView = new ImageView();
		treasureImageView.setFitHeight(size - 4);
		treasureImageView.setFitWidth(size - 4);
		treasureImageView.imageProperty().bind(model.treasureImageProperty());	
		stackPane.getChildren().add(treasureImageView);
		
		ImageView bonusImageView = new ImageView();
		bonusImageView.setFitHeight(size - 4);
		bonusImageView.setFitWidth(size - 4);
		bonusImageView.imageProperty().bind(model.bonusImageProperty());	
		stackPane.getChildren().add(bonusImageView);
	}
}
