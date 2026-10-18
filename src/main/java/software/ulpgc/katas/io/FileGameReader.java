package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Game;
import software.ulpgc.katas.model.Platform;

import java.io.*;
import java.util.List;

public class FileGameReader implements GameReader{

    private final File file;

    public FileGameReader(File file) {
        this.file = file;
    }

    @Override
    public List<Game> load() {
        try (InputStream is = new FileInputStream(file)) {
            return loadFrom(is);
        } catch (FileNotFoundException e) {
            System.out.println("Aviso: El archivo no existe o la ruta es inválida -> " + file.getPath());
            return List.of();
        } catch (IOException e) {
            System.out.println("Error de lectura al procesar el archivo -> " + e.getMessage());
            return List.of();
        }
    }

    public List<Game> loadFrom(InputStream is) {
        return new BufferedReader(new InputStreamReader(is))
                .lines().skip(1).map(this::toGame)
                .filter(game -> game != null)
                .toList();
    }

    private Game toGame(String line) {
        return toGame(line.split(","));
    }

    private Game toGame(String[] fields) {
        if (fields.length < 11) return null;

        try {
            return new Game(
                    fields[1].trim(),
                    platformOf(fields[2].trim()),
                    fields[4].trim(),
                    parseIntOrDefault(fields[3].trim(), 0),
                    parseDoubleOrDefault(fields[10].trim(), 0.0)
            );
        } catch (Exception e) {
            return null;
        }
    }

    private Platform platformOf(String field) {
        try {
            return Platform.valueOf(field);
        } catch (IllegalArgumentException e) {
            return Platform.Other;
        }
    }

    private int parseIntOrDefault(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private double parseDoubleOrDefault(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

}