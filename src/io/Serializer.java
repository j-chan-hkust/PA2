package io;

import models.MapElement;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

/**
 * A serializer for converting {@link GameProperties} into a map file.
 */
public class Serializer {

    /**
     * Path to the map to serialize to.
     */
    @NotNull
    private Path path;

    public Serializer(@NotNull final Path path) {
        this.path = path;
    }

    /**
     * Serializes a {@link GameProperties} object and saves it into a file.
     *
     * @param prop {@link GameProperties} objeect to serialize and save.
     * @throws IOException if an I/O exception has occurred.
     */
    public void serializeGameProp(@NotNull final GameProperties prop) throws IOException {
        // TODO done
        try(PrintWriter pw = new PrintWriter(path.toFile())){
            pw.println(prop.rows);
            pw.println(prop.cols);
            pw.println(prop.delay);
            for (int i = 0; i<prop.rows;i++){
                for (int j = 0; j<prop.cols; j++){
                    pw.print(prop.cells[i][j].toSerializedRep());
                }
                pw.println();
            }
            for(var pipe:prop.pipes){
                pw.print(pipe.toSerializedRep());
            }
            pw.println();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
