package views.panes;

import controllers.LevelManager;
import controllers.SceneManager;
import io.Deserializer;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ListView;
import javafx.stage.DirectoryChooser;
import models.FXGame;
import views.BigButton;
import views.BigVBox;
import views.SideMenuVBox;

import java.io.File;
import java.io.FileNotFoundException;

public class LevelSelectPane extends GamePane {

    private SideMenuVBox leftContainer = new SideMenuVBox();
    private BigButton returnButton = new BigButton("Return");
    private BigButton playButton = new BigButton("Play");
    private BigButton playRandom = new BigButton("Generate Map and Play");
    private BigButton chooseMapDirButton = new BigButton("Choose map directory");
    private ListView<String> levelsListView = new ListView<>(LevelManager.getInstance().getLevelNames());
    private BigVBox centerContainer = new BigVBox();
    private Canvas levelPreview = new Canvas();

    public LevelSelectPane() {
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
        leftContainer.getChildren().addAll(returnButton,chooseMapDirButton,levelsListView,playButton,playRandom);
        centerContainer.getChildren().add(levelPreview);
        this.setLeft(leftContainer);
        this.setCenter(centerContainer); //maybe change?
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void styleComponents() {
        // TODO done?
        leftContainer.getStyleClass().add("side-menu");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void setCallbacks() {
        // TODO not done
        returnButton.setOnMouseClicked(mouseEvent -> SceneManager.getInstance().showPane(MainMenuPane.class));
        playRandom.setOnMouseClicked(mouseEvent -> startGame(true));
    }

    /**
     * Starts the game.
     *
     * <p>
     * This method should do everything that is required to initialize and start the game, including loading/generating
     * maps, switching scenes, etc.
     * </p>
     *
     * @param generateRandom Whether to use a generated map.
     */
    private void startGame(final boolean generateRandom) {
        // TODO done for the basic case
        if(generateRandom){
            FXGame newGame = new FXGame(); //make a default
            ((GameplayPane)SceneManager.getInstance().getPane(GameplayPane.class)).startGame(newGame);
            SceneManager.getInstance().showPane(GameplayPane.class);
        }
    }

    /**
     * Listener method that executes when a map on the list is selected.
     *
     * @param observable Observable value.
     * @param oldValue   Original value.
     * @param newValue   New value.
     */
    private void onMapSelected(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        // TODO
    }

    /**
     * Prompts the user for a map directory.
     *
     * <p>
     * Hint:
     * Use {@link DirectoryChooser} to display a folder selection prompt.
     * </p>
     */
    private void promptUserForMapDirectory() {
        // TODO
    }

    /**
     * Actually changes the current map directory.
     *
     * @param dir New directory to change to.
     */
    private void commitMapDirectoryChange(File dir) {
        // TODO
    }
}
