package views;

import controllers.ResourceLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 * Helper class for a {@link VBox} with "big-vbox" style applied.
 */
public class BigVBox extends VBox {

    /**
     * Creates an instance with spacing of 20.
     */
    public BigVBox() {
        // TODO
        this.setSpacing(20);
    }

    public BigVBox(double spacing) {
        super(spacing);
    }

    public BigVBox(Node... children) {
        super(children);
    }

    public BigVBox(double spacing, Node... children) {
        super(spacing, children);
    }

    {
        // TODO: Add "big-vbox" style done?
        this.getStyleClass().add("big-vbox");
    }
}
