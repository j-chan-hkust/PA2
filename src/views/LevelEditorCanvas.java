package views;

import controllers.Renderer;
import controllers.SceneManager;
import io.Deserializer;
import io.GameProperties;
import io.Serializer;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import models.exceptions.InvalidMapException;
import models.map.cells.Cell;
import models.map.cells.FillableCell;
import models.map.cells.TerminationCell;
import models.map.cells.Wall;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import util.Coordinate;
import util.Direction;
import views.panes.LevelEditorPane;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Optional;

import static models.Config.TILE_SIZE;

public class LevelEditorCanvas extends Canvas {

    private static final String MSG_MISSING_SOURCE = "Source tile is missing!";
    private static final String MSG_MISSING_SINK = "Sink tile is missing!";
    private static final String MSG_BAD_DIMS = "Map size must be at least 2x2!";
    private static final String MSG_BAD_DELAY = "Delay must be a positive value!";
    private static final String MSG_SOURCE_TO_WALL = "Source tile is blocked by a wall!";
    private static final String MSG_SINK_TO_WALL = "Sink tile is blocked by a wall!";

    private GameProperties gameProp;

    @Nullable
    private TerminationCell sourceCell;
    @Nullable
    private TerminationCell sinkCell;

    public LevelEditorCanvas(int rows, int cols, int delay) {
        super();
        resetMap(rows, cols, delay);
    }

    /**
     * Changes the attributes of this canvas.
     *
     * @param rows  Number of rows.
     * @param cols  Number of columns.
     * @param delay Amount of delay.
     */
    public void changeAttributes(int rows, int cols, int delay) {
        resetMap(rows, cols, delay);
    }

    /**
     * Resets the map with the given attributes.
     *
     * @param rows  Number of rows.
     * @param cols  Number of columns.
     * @param delay Amount of delay.
     */
    private void resetMap(int rows, int cols, int delay) {
        // TODO done
        gameProp = new GameProperties(rows, cols);
        for(int i = 0; i<gameProp.cells.length; i++){
            for(int j = 0; j<gameProp.cells[i].length;j++){
                if(i==0||j==0||i==gameProp.cells.length-1||j==gameProp.cells[i].length-1){
                    gameProp.cells[i][j] = new Wall(new Coordinate(i,j));
                }else{
                    gameProp.cells[i][j] = new FillableCell(new Coordinate(i,j));
                }
            }
        }
        gameProp.delay = delay;
        sourceCell = null;
        sinkCell = null;
        renderCanvas();
    }

    /**
     * Renders the canvas.
     */
    private void renderCanvas() {
        Platform.runLater(() -> Renderer.renderMap(this, gameProp.cells));
    }

    /**
     * Sets a tile on the map.
     * <p>
     * Hint:
     * You may need to check/compute some attribute in order to create the new {@link Cell} object.
     *
     * @param sel Selected {@link CellSelection}.
     * @param x   X-coordinate relative to the canvas.
     * @param y   Y-coordinate relative to the canvas.
     */
    public void setTile(@NotNull CellSelection sel, double x, double y) {
        // TODO done
        int j = (int) Math.floor(x/TILE_SIZE);
        int i = (int) Math.floor(y/TILE_SIZE);

        switch(sel){
            case WALL:
                gameProp.cells[i][j] = new Wall(new Coordinate(i,j));
                if(sinkCell!=null) {
                    if (sinkCell.coord.equals(new Coordinate(i, j))) {
                        sinkCell = null;
                    }
                }
                if(sourceCell!=null) {
                    if (sourceCell.coord.equals(new Coordinate(i, j))) {
                        sourceCell = null;
                    }
                }
                break;
            case CELL:
                gameProp.cells[i][j] = new FillableCell(new Coordinate(i,j));
                if(sinkCell!=null) {
                if (sinkCell.coord.equals(new Coordinate(i, j))) {
                    sinkCell = null;
                }
            }
            if(sourceCell!=null) {
                if (sourceCell.coord.equals(new Coordinate(i, j))) {
                    sourceCell = null;
                }
            }
                break;
            case TERMINATION_CELL:
                if(i==0||j==0||i==gameProp.cells.length-1||j==gameProp.cells[i].length-1){//we are on the edge
                    Direction direction = null;
                    boolean set = false;

                    if(i==0){
                        direction = Direction.UP;
                        set = true;
                    }
                    if(j==0){
                        if(set)
                            break;
                        direction = Direction.LEFT;
                        set = true;
                    }
                    if(i==gameProp.cells.length-1){
                        if(set)
                            break;
                        direction = Direction.DOWN;
                        set=true;
                    }
                    if(j==gameProp.cells[i].length-1){
                        if (set)
                            break;
                        direction = Direction.RIGHT;
                        set=true;
                    }

                    sinkCell = new TerminationCell(new Coordinate(i,j),direction, TerminationCell.Type.SINK);
                    gameProp.cells[i][j] = sinkCell;
                }else{ //we a source cell!
                    sourceCell = new TerminationCell(new Coordinate(i,j), Direction.UP, TerminationCell.Type.SOURCE);
                    gameProp.cells[i][j] = sourceCell;
                }
                break;
        }
        renderCanvas();
    }

    /**
     * Sets a tile on the map.
     * <p>
     * Hint:
     * You will need to make sure that there is only one source/sink cells in the map.
     *
     * @param cell The {@link Cell} object to set.
     */
    private void setTileByMapCoord(@NotNull Cell cell) {
        // TODO

    }

    /**
     * Toggles the rotation of the source tile clockwise.
     */
    public void toggleSourceTileRotation() {
        // TODO
        Optional validity = checkValidity();
        if(sourceCell!=null){//!validity.get().equals(MSG_MISSING_SOURCE)) {//it is valid
            switch (sourceCell.pointingTo){
                case UP:
                    sourceCell = new TerminationCell(sourceCell.coord, Direction.RIGHT, TerminationCell.Type.SOURCE);
                    gameProp.cells[sourceCell.coord.row][sourceCell.coord.col] = sourceCell;
                    break;
                case DOWN:
                    sourceCell = new TerminationCell(sourceCell.coord, Direction.LEFT, TerminationCell.Type.SOURCE);
                    gameProp.cells[sourceCell.coord.row][sourceCell.coord.col] = sourceCell;
                    break;
                case LEFT:
                    sourceCell = new TerminationCell(sourceCell.coord, Direction.UP, TerminationCell.Type.SOURCE);
                    gameProp.cells[sourceCell.coord.row][sourceCell.coord.col] = sourceCell;
                    break;
                case RIGHT:
                    sourceCell = new TerminationCell(sourceCell.coord, Direction.DOWN, TerminationCell.Type.SOURCE);
                    gameProp.cells[sourceCell.coord.row][sourceCell.coord.col] = sourceCell;
                    break;
            }
            renderCanvas();
        }
    }

    /**
     * Loads a map from a file.
     * <p>
     * Prompts the player if they want to discard the changes, displays the file chooser prompt, and loads the file.
     *
     * @return {@code true} if the file is loaded successfully.
     */
    public boolean loadFromFile() {
        // TODO
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Load map from file?");
        alert.setContentText("Are you ok with this?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get()==ButtonType.OK){
            File file = getTargetLoadFile();
            if(loadFromFile(file.toPath()))
                renderCanvas();
            return true;
        }else
            return false;
    }

    /**
     * Prompts the user for the file to load.
     * <p>
     * Hint:
     * Use {@link FileChooser} and {@link FileChooser#setSelectedExtensionFilter(FileChooser.ExtensionFilter)}.
     *
     * @return {@link File} to load, or {@code null} if the operation is canceled.
     */
    @Nullable
    private File getTargetLoadFile() {
        // TODO
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Map File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Map Files", "*.map")
        );
        File file = fileChooser.showOpenDialog(null);
        return file;
    }

    /**
     * Loads the file from the given path and replaces the current {@link LevelEditorCanvas#gameProp}.
     * <p>
     * Hint:
     * You should handle any exceptions which arise from loading in this method.
     *
     * @param path Path to load the file from.
     * @return {@code true} if the file is loaded successfully, {@code false} otherwise.
     */
    private boolean loadFromFile(@NotNull Path path) {
        // TODO
        try{
            Deserializer d = new Deserializer(path);
            gameProp = d.parseGameFile();
            return true;
        }catch(FileNotFoundException e){e.printStackTrace();}

        return false;
    }

    /**
     * Checks the validity of the map, prompts the player for the target save directory, and saves the file.
     */
    public void saveToFile() {
        // TODO
        var validity = checkValidity();
        if(validity.isPresent()){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Map not valid!");
            errorAlert.setContentText(validity.get());
            errorAlert.showAndWait();
            return;
        }
        File file = getTargetSaveDirectory();

        if(file!=null){
            try{file.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
                exportToFile(file.toPath());
        }

    }

    /**
     * Prompts the user for the directory and filename to save as.
     * <p>
     * Hint:
     * Use {@link FileChooser} and {@link FileChooser#setSelectedExtensionFilter(FileChooser.ExtensionFilter)}.
     *
     * @return {@link File} to save to, or {@code null} if the operation is canceled.
     */
    @Nullable
    private File getTargetSaveDirectory() {
        // TODO done
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Map File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Map Files", "*.map")
        );
        File file = fileChooser.showSaveDialog(null);
        return file;
    }

    /**
     * Exports the current map to a file.
     * <p>
     * Hint:
     * You should handle any exceptions which arise from saving in this method.
     *
     * @param p Path to export to.
     */
    private void exportToFile(@NotNull Path p) {
        // TODO done
        var s = new Serializer(p);

        try{s.serializeGameProp(gameProp);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks whether the current map and its properties are valid.
     * <p>
     * Hint:
     * You should check for the following conditions:
     * <ul>
     * <li>Source cell is present</li>
     * <li>Sink cell is present</li>
     * <li>Minimum map size is 2x2</li>
     * <li>Flow delay is at least 1</li>
     * <li>Source/Sink tiles are not blocked by walls</li>
     * </ul>
     *
     * @return {@link Optional} containing the error message, or an empty {@link Optional} if the map is valid.
     */
    private Optional<String> checkValidity() {
        // TODO missing point to walls
        if(sourceCell==null){
            return Optional.of(MSG_MISSING_SOURCE);
        }
        if(sinkCell==null)
            return Optional.of(MSG_MISSING_SINK);
        if(gameProp.rows<2||gameProp.cols<2)
            return Optional.of(MSG_BAD_DIMS);
        if(gameProp.delay<1)
            return Optional.of(MSG_BAD_DELAY);
        if(gameProp.cells[sourceCell.pointingTo.getOffset().row+sourceCell.coord.row][sourceCell.pointingTo.getOffset().col+sourceCell.coord.col].getClass()==Wall.class){
            return Optional.of(MSG_SOURCE_TO_WALL);
        }
        if(gameProp.cells[sinkCell.pointingTo.getOpposite().getOffset().row+sinkCell.coord.row][sinkCell.pointingTo.getOpposite().getOffset().col+sinkCell.coord.col].getClass()==Wall.class){
            return Optional.of(MSG_SINK_TO_WALL);
        }

        return Optional.empty();

    }

    public int getNumOfRows() {
        return gameProp.rows;
    }

    public int getNumOfCols() {
        return gameProp.cols;
    }

    public int getAmountOfDelay() {
        return gameProp.delay;
    }

    public void setAmountOfDelay(int delay) {
        gameProp.delay = delay;
    }

    public enum CellSelection {
        WALL("Wall"),
        CELL("Cell"),
        TERMINATION_CELL("Source/Sink");

        private String text;

        CellSelection(@NotNull String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }
}
