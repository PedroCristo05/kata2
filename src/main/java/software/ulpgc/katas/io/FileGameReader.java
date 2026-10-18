package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Game;

import java.io.File;
import java.util.List;

public class FileGameReader implements GameReader{

    private final File file;

    public FileGameReader(File file) {
        this.file = file;
    }

    @Override
    public List<Game> load() {
        return List.of();
    }
}
