package views.panes;

import controllers.SceneManager;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import views.BigButton;
import views.BigVBox;

/**
 * Pane for displaying the main menu.
 */
public class MainMenuPane extends GamePane {

    @NotNull
    private final VBox container = new BigVBox();
    @NotNull
    private final Label title = new Label("Pipes");
    @NotNull
    private final Button levelSelectButton = new BigButton("Play Game");
    @NotNull
    private final Button levelEditorButton = new BigButton("Level Editor");
    @NotNull
    private final Button settingsButton = new BigButton("About / Settings");
    @NotNull
    private final Button quitButton = new BigButton("Quit");

    public MainMenuPane() {
        connectComponents();
        styleComponents();
        setCallbacks();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void connectComponents() {
        // TODO done i think?
        container.getChildren().addAll(title, levelSelectButton,levelEditorButton,settingsButton,quitButton);
        this.setCenter(container);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void styleComponents() {
        // Nothing to style here :)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void setCallbacks() {
        // TODO done!
        levelSelectButton.setOnMouseClicked(mouseEvent -> SceneManager.getInstance().showPane(GameplayPane.class));
        levelEditorButton.setOnMouseClicked(mouseEvent -> SceneManager.getInstance().showPane(LevelEditorPane.class));
        settingsButton.setOnMouseClicked(mouseEvent -> SceneManager.getInstance().showPane(SettingsPane.class));
        quitButton.setOnMouseClicked(mouseEvent -> System.exit(0));

    }
}
