package views.panes;

import controllers.LevelManager;
import controllers.Renderer;
import controllers.SceneManager;
import io.Deserializer;
import javafx.application.Platform;
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
import java.util.Optional;

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
        playButton.setOnMouseClicked(mouseEvent -> startGame(false));
        playRandom.setOnMouseClicked(mouseEvent -> startGame(true));
        chooseMapDirButton.setOnMouseClicked(mouseEvent -> promptUserForMapDirectory());
        levelsListView.setOnMouseClicked(mouseEvent ->
                onMapSelected(LevelManager.getInstance().getCurrentLevelProperty(),
                        "",
                        levelsListView.getSelectionModel().getSelectedItem()));
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
        }else{
            try{
                Deserializer d = new Deserializer(LevelManager.getInstance().getCurrentLevelPath());
                ((GameplayPane)SceneManager.getInstance().getPane(GameplayPane.class)).startGame(d.parseFXGame());
            } catch (FileNotFoundException e){
                e.printStackTrace();
            }
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
        // TODO done
        System.out.println(newValue); //dum.map
        if(observable.getValue()==null){//we haven't set currentlevel
            LevelManager.getInstance().setLevel(newValue);
            oldValue ="";
        }else{
            oldValue = observable.getValue();
        }

        if(oldValue==newValue){
            return; //they're the same, we dont have to do anything
        }else{
            LevelManager.getInstance().setLevel(newValue);
            try{
                Deserializer d = new Deserializer(LevelManager.getInstance().getCurrentLevelPath());
                Platform.runLater(() -> Renderer.renderMap(levelPreview, d.parseGameFile().cells));
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
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
        // TODO done
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory =
                directoryChooser.showDialog(null);

        if(selectedDirectory == null){
            return;
        }else{
            commitMapDirectoryChange(selectedDirectory);
        }
    }

    /**
     * Actually changes the current map directory.
     *
     * @param dir New directory to change to.
     */
    private void commitMapDirectoryChange(File dir) {
        // TODO done? theres some weird behavior, need to look into it!
        LevelManager.getInstance().setMapDirectory(dir.toPath());
        LevelManager.getInstance().getLevelNames().forEach(System.out::println);
        levelsListView.getItems().addAll(LevelManager.getInstance().getLevelNames());
    }
}
