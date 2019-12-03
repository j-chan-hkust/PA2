package controllers;

import models.exceptions.ResourceNotFoundException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Helper class for loading resources from the filesystem.
 */
public class ResourceLoader {

    /**
     * Path to the resources directory.
     */
    @NotNull
    private static final Path RES_PATH;

    static {
        // TODO: Initialize RES_PATH done?

        RES_PATH = Paths.get("resources/");

    }

    /**
     * Retrieves a resource file from the resource directory.
     *
     * @param relativePath Path to the resource file, relative to the root of the resource directory.
     * @return Absolute path to the resource file.
     * @throws ResourceNotFoundException If the file cannot be found under the resource directory.
     */
    @NotNull
    public static String getResource(@NotNull final String relativePath) {
        // TODO so fucking hacky lol
        Path path = RES_PATH.resolve(relativePath);
        File f = new File(path.toString());
        if(f.exists() && !f.isDirectory()) {
            System.out.println(path.toString());
            return "file:\\\\\\"+ path.toAbsolutePath().toString();
        }else
            throw new ResourceNotFoundException(path.toString());
//
//        try {var file = path.toFile();
//        }catch (ResourceNotFoundException e){
//            e.printStackTrace();
//        }

    }
}
