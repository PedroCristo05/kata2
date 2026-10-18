package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Game;
import software.ulpgc.katas.model.Platform;

import java.io.*;
import java.util.List;

public class FileGameReader implements GameReader {

    private final File file;

    public FileGameReader(File file) {
        this.file = file;
    }

    @Override
    public List<Game> load() {
        try (InputStream is = new FileInputStream(file)) {
            return loadFrom(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Game> loadFrom(InputStream is) {
        return new BufferedReader(new InputStreamReader(is))
                .lines()
                .skip(1)
                .map(this::toGame)
                .toList();
    }

    private Game toGame(String line) {
        return toGame(line.split(","));
    }

    private Game toGame(String[] fields) {
        return new Game(
                fields[1].trim(),
                platformOf(fields[2].trim()),
                fields[4].trim(),
                Integer.parseInt(fields[3].trim()),
                Double.parseDouble(fields[10].trim())
        );
    }

    private Platform platformOf(String field) {
        try {
            return Platform.valueOf(field);
        } catch (IllegalArgumentException e) {
            return Platform.Other;
        }
    }
}
