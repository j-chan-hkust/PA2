package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Singleton class responsible for loading and managing levels from the filesystem.
 */
public class LevelManager {

    /**
     * Singleton instance.
     */
    @NotNull
    private static final LevelManager INSTANCE = new LevelManager();

    /**
     * List of all loaded level names.
     */
    @NotNull
    private final ObservableList<String> levelNames = FXCollections.observableArrayList();
    /**
     * The JavaFX property housing the current level name.
     * <p>
     * The {@link String} backing the property may be {@code null} if no level is loaded from the filesystem.
     */
    @NotNull
    private final StringProperty curLevelNameProperty = new SimpleStringProperty();

    /**
     * Path to the map directory. Defaults to the current working directory.
     */
    @NotNull
    private Path mapDirectory = Paths.get("");

    private LevelManager() {
        setMapDirectory(mapDirectory);
    }

    /**
     * @return Singleton instance of this class.
     */
    @NotNull
    public static LevelManager getInstance() {
        return INSTANCE;
    }

    /**
     * Sets the current map directory, and loads all maps from the newly set directory.
     *
     * @param mapDirectory New map directory to load maps from.
     */
    public void setMapDirectory(@NotNull Path mapDirectory) {
        // TODO done
        this.mapDirectory = mapDirectory;
        loadLevelNamesFromDisk();
    }

    /**
     * Loads all level names from the currently set {@link LevelManager#mapDirectory}.
     *
     * <p>
     * Hint:
     * <ul>
     * <li>Use try-with-resources to automatically manage the lifetime of {@link Stream}.</li>
     * <li>Use {@link Files#walk(Path, int, FileVisitOption...)} to retrieve all files inside a given directory. Check
     * the documentation to see what to put for the second parameter!</li>
     * <li>Use {@link Stream} to filter, map and sort the loaded filenames.</li>
     * </ul>
     * </p>
     */
    private void loadLevelNamesFromDisk() {
        // TODO done?
        levelNames.clear();
        try(Stream<Path> walk = Files.walk(this.mapDirectory, Integer.MAX_VALUE, FileVisitOption.FOLLOW_LINKS)){
            //filter
            //map
            //sort
            List<String> list = walk.filter(Files::isRegularFile)
                    .map(path -> path.toString())
                    .map(path -> path.replace(this.mapDirectory.toString()+"\\",""))
                    .filter(path -> path.endsWith(".map"))
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
            levelNames.addAll(list);
        }catch (IOException e){
            e.printStackTrace();
            return;
        }
    }

    @NotNull
    public ObservableList<String> getLevelNames() {
        return levelNames;
    }

    /**
     * @return Full path to the currently selected level.
     */
    @NotNull
    public Path getCurrentLevelPath() {
        // TODO done
        Path path = Path.of(mapDirectory+"\\"+curLevelNameProperty.get()).toAbsolutePath();
        return path;
    }

    /**
     * Sets the currently selected level.
     *
     * @param levelName Name of the newly selected level, or {@code null} if a level is not loaded.
     * @throws IllegalArgumentException When the level name is blank.
     */
    public void setLevel(@Nullable String levelName) throws IllegalArgumentException{
        // TODO done
        if (levelName.isBlank()){
            throw new IllegalStateException();
        }else {
            curLevelNameProperty.set(levelName);
        }
    }

    /**
     * Retrieves and sets the next level.
     * <p>
     * If there is a "next map" in the directory, set the level to that map. Otherwise, set the level to {@code null}.
     * </p>
     * <p>
     * Hint:
     * <ul>
     * <li>Remember to check whether the string in {@code curLevelNameProperty} is empty or not.</li>
     * </ul>
     * </p>
     *
     * @return Name of the next map if present, {@code null} otherwise.
     */
    @Nullable
    public String getAndSetNextLevel() {
        // TODO done
        if(getCurrentLevelProperty().toString()==null){
            setLevel(null);
            return null; //no map has been set, so there is no next map!
        }

        if(INSTANCE.levelNames.contains(getCurrentLevelProperty().toString())){
            //if last element, and index too big
            int currIndex = INSTANCE.levelNames.indexOf(getCurrentLevelProperty().toString());
            if(currIndex==INSTANCE.levelNames.size()-1){
                setLevel(null);
                return null; //there are no more indices to search!
            }else{
                setLevel(INSTANCE.levelNames.get(currIndex+1));
                return INSTANCE.levelNames.get(currIndex+1);
            }
        } else {
            setLevel(null);
            return null;
        }
    }

    @NotNull
    public StringProperty getCurrentLevelProperty() {
        return curLevelNameProperty;
    }
}
