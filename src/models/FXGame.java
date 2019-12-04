package models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.canvas.Canvas;
import models.map.Map;
import models.map.cells.Cell;
import models.map.cells.FillableCell;
import models.pipes.Pipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import textgame.game.Game;
import util.Coordinate;

import java.util.List;

/**
 * JavaFX version of {@link textgame.game.Game}.
 */
public class FXGame {

    /**
     * Default number of rows.
     */
    private static int defaultRows = 8;
    /**
     * Default number of columns.
     */
    private static int defaultCols = 8;

    @NotNull
    private final Map map;
    @NotNull
    private final PipeQueue pipeQueue;
    @NotNull
    private final FlowTimer flowTimer;
    @NotNull
    private final CellStack cellStack = new CellStack();

    private IntegerProperty numOfSteps = new SimpleIntegerProperty(0);

    /**
     * Sets the default number of rows for generated maps.
     *
     * @param rows New default number of rows.
     */
    public static void setDefaultRows(int rows) {
        defaultRows = rows;
    }

    /**
     * Sets the default number of column for generated maps.
     *
     * @param cols New default number of columns.
     */
    public static void setDefaultCols(int cols) {
        defaultCols = cols;
    }

    /**
     * @return Current default number of rows for generated maps.
     */
    public static int getDefaultRows() {
        return defaultRows;
    }

    /**
     * @return Current default number of columns for generated maps.
     */
    public static int getDefaultCols() {
        return defaultCols;
    }

    /**
     * Constructs an instance with default number of rows and columns.
     */
    public FXGame() {
        this(defaultRows, defaultCols);
    }

    /**
     * Constructs an instance with given number of rows and columns.
     *
     * @param rows Number of rows (excluding side walls)
     * @param cols Number of columns (excluding side walls)
     */
    private FXGame(int rows, int cols) {
        // TODO done
        map = new Map(rows,cols);
        pipeQueue = new PipeQueue();
        flowTimer = new FlowTimer();
    }

    /**
     * Constructs an instance with all given parameters.
     *
     * @param rows  Number of rows including side walls
     * @param cols  Number of columns including side walls
     * @param delay Delay in seconds before water starts flowing.
     * @param cells Initial map.
     * @param pipes Initial pipes, if provided.
     */
    public FXGame(int rows, int cols, int delay, @NotNull Cell[][] cells, @Nullable List<Pipe> pipes) {
        // TODO done
        map = new Map(rows, cols, cells);
        pipeQueue = new PipeQueue(pipes);
        flowTimer = new FlowTimer(delay);
    }

    /**
     * Adds a handler to be run when the water flows into an additional tile.
     *
     * @param handler {@link Runnable} to execute.
     */
    public void addOnFlowHandler(@NotNull Runnable handler) {
        flowTimer.registerFlowCallback(handler);
    }

    /**
     * Adds a handler to be run when a tick elapses.
     *
     * @param handler {@link Runnable} to execute.
     */
    public void addOnTickHandler(@NotNull Runnable handler) {
        flowTimer.registerTickCallback(handler);
    }

    /**
     * Starts the flow of water.
     */
    public void startCountdown() {
        flowTimer.start();
    }

    /**
     * Stops the flow of water.
     */
    public void stopCountdown() {
        flowTimer.stop();
    }

    public void restartCountdown(){flowTimer.resume();}

    /**
     * @param row Row index to place pipe
     * @param col Column index to place pipe
     * @see Game#placePipe(int, char)
     */
    public void placePipe(int row, int col) {
        // TODO done
        var coord = new Coordinate(row,col);
        var pipe = pipeQueue.peek();
        if(map.tryPlacePipe(coord,pipe)){
           pipeQueue.consume();
           cellStack.push(new FillableCell(coord,pipe));
           numOfSteps.setValue(numOfSteps.intValue()+1);
        }
    }

    /**
     * @see Game#skipPipe()
     */
    public void skipPipe() {
        // TODO done
        numOfSteps.setValue(numOfSteps.get()+1);
        pipeQueue.consume();
    }

    /**
     * @see Game#undoStep()
     */
    public void undoStep() {
        // TODO done!
        FillableCell undoCell = cellStack.pop();//this increments undocount
        if(undoCell != null){
            if(undoCell.getPipe().get().getFilled()){
                cellStack.push(undoCell);
                return;
            }
            pipeQueue.undo(undoCell.getPipe().get());
            map.undo(undoCell.coord);
            numOfSteps.setValue(numOfSteps.intValue()+1);
        }
    }

    /**
     * Renders the map onto a {@link Canvas}.
     *
     * @param canvas {@link Canvas} to render to.
     */
    public void renderMap(@NotNull Canvas canvas) {
        map.render(canvas);
    }

    /**
     * Renders the queue onto a {@link Canvas}.
     *
     * @param canvas {@link Canvas} to render to.
     */
    public void renderQueue(@NotNull Canvas canvas) {
        pipeQueue.render(canvas);
    }

    /**
     * @see Game#updateState()
     */
    public void updateState() {
        // TODO done
        if (flowTimer.distance()==0){
            map.fillBeginTile();
            map.fillTiles(flowTimer.distance());
        }else if(flowTimer.distance()>0){
            map.fillTiles(flowTimer.distance());
        }
    }

    /**
     * @see Game#updateState()
     */
    public boolean hasWon() {
        // TODO done
        return map.checkPath();
    }

    /**
     * @see Game#hasLost()
     */
    public boolean hasLost() {
        // TODO done
        if (flowTimer.distance()<=0){
            return false;
        }else{
            return map.hasLost();
        }
    }

    /**
     * Fills all reachable pipes in the map.
     */
    public void fillAllPipes() {
        map.fillAll();
    }



    public IntegerProperty getNumOfSteps() {
        return numOfSteps;
    }

    public IntegerProperty getNumOfUndo() {
        return cellStack.getUndoCountProperty();
    }
}
