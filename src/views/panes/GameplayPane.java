package views.panes;

import controllers.AudioManager;
import controllers.LevelManager;
import controllers.Renderer;
import controllers.SceneManager;
import io.Deserializer;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import models.FXGame;
import org.jetbrains.annotations.NotNull;
import views.BigButton;
import views.BigVBox;
import views.GameplayInfoPane;

import java.io.FileNotFoundException;
import java.util.Optional;

import static models.Config.TILE_SIZE;

/**
 * Pane for displaying the actual gameplay.
 */
public class GameplayPane extends GamePane {

    private HBox topBar = new HBox(20);
    private VBox canvasContainer = new BigVBox();
    private Canvas gameplayCanvas = new Canvas();
    private HBox bottomBar = new HBox(20);
    private Canvas queueCanvas = new Canvas();
    private Button quitToMenuButton = new BigButton("Quit to menu");

    private FXGame game;

    private final IntegerProperty ticksElapsed = new SimpleIntegerProperty();
    private GameplayInfoPane infoPane = null;

    public GameplayPane() {
        connectComponents();
        styleComponents();
        setCallbacks();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void connectComponents() {
        // TODO done
        //topBar.getChildren().add(infoPane); ignore for now
        bottomBar.getChildren().addAll(queueCanvas,quitToMenuButton);
        canvasContainer.getChildren().add(gameplayCanvas);
        this.setTop(topBar);
        this.setCenter(canvasContainer);
        this.setBottom(bottomBar);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void styleComponents() {
        // TODO
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void setCallbacks() {
        // TODO
        quitToMenuButton.setOnMouseClicked(mouseEvent -> doQuitToMenuAction());
        gameplayCanvas.setOnMouseClicked(mouseEvent -> onCanvasClicked(mouseEvent));
        this.setOnKeyPressed(keyEvent -> onKeyPressed(keyEvent));
    }

    /**
     * Handles events when somewhere on the {@link GameplayPane#gameplayCanvas} is clicked.
     *
     * @param event Event to handle.
     */
    private void onCanvasClicked(MouseEvent event) {
        // TODO
        int j = (int) Math.floor(event.getX()/TILE_SIZE);
        int i = (int) Math.floor(event.getY()/TILE_SIZE);

        game.placePipe(i,j);
        game.renderMap(gameplayCanvas);
        game.renderQueue(queueCanvas);

    }

    /**
     * Handles events when a key is pressed.
     *
     * @param event Event to handle.
     */
    private void onKeyPressed(KeyEvent event) {
        // TODO
        System.out.println(event.getText());
        switch (event.getText()){
            case "s":
                game.skipPipe();
                game.renderQueue(queueCanvas);
                break;
            case "u":
                game.undoStep();
                game.renderQueue(queueCanvas);
                game.renderMap(gameplayCanvas);
                break;

        }
    }

    /**
     * Creates a popup which tells the player they have completed the map.
     */
    private void createWinPopup() {
        // TODO
    }

    /**
     * Loads the next map in the series, or generate a new map if one is not available.
     */
    private void loadNextMap() {
        // TODO
    }

    /**
     * Creates a popup which tells the player they have lost the map.
     */
    private void createLosePopup() {
        // TODO
    }

    /**
     * Creates a popup which prompts the player whether they want to quit.
     */
    private void doQuitToMenuAction() {
        // TODO
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Quit current game?");
        alert.setContentText("you will quit this very fun game!");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get()==ButtonType.OK)
            doQuitToMenu();
        return;
    }

    /**
     * Go back to the Level Select scene.
     */
    private void doQuitToMenu() {
        // TODO
        endGame();
        SceneManager.getInstance().showPane(LevelSelectPane.class);
    }

    /**
     * Starts a new game with the given name.
     *
     * @param game New game to start.
     */
    void startGame(@NotNull FXGame game) {
        // TODO
        this.game = game;
        game.renderMap(gameplayCanvas);
        game.renderQueue(queueCanvas);
        infoPane = new GameplayInfoPane(LevelManager.getInstance().getCurrentLevelProperty(),
                ticksElapsed,
                game.getNumOfSteps(),
                game.getNumOfUndo());
        topBar.getChildren().clear();
        topBar.getChildren().add(infoPane);
        game.startCountdown();
    }

    /**
     * Cleans up the currently bound game.
     */
    private void endGame() {
        // TODO
    }
}
