package software.ulpgc.katas;

import software.ulpgc.katas.io.*;
import software.ulpgc.katas.model.Game;
import software.ulpgc.katas.model.Platform;
import software.ulpgc.katas.service.GameStatistics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        SummaryWriter writer = new GameSummaryWriter();

        // -------------------------------------------------------------
        // ORIGEN 1: Fichero local (vgsales.csv)
        // -------------------------------------------------------------
        System.out.println("======== 1. Leyendo localmente (muestra vgsales.csv) ========");
        File localFile = new File("vgsales.csv");
        List<Game> gamesFromLocal = new FileGameReader(localFile).load();
        Map<Platform, Long> localSummary = GameStatistics.countByPlatform(gamesFromLocal);

        // DESTINO 1: Consola (System.out)
        System.out.println("\n--- Salida por Consola (System.out) ---");
        writer.write(localSummary, System.out);

        // DESTINO 2: Fichero en disco (FileOutputStream)
        try (OutputStream fileOutput = new FileOutputStream("summary.txt")) {
            writer.write(localSummary, fileOutput);
            System.out.println("Resumen exportado con éxito a summary.txt");
        } catch (IOException e) {
            System.err.println("Error al guardar el resumen en fichero: " + e.getMessage());
        }

        // -------------------------------------------------------------
        // ORIGEN 2: Stream desde URL pública
        // -------------------------------------------------------------
        System.out.println("\n=== 2. Leyendo desde URL pública (dataset completo) ===");
        URL remoteUrl = URI.create("https://raw.githubusercontent.com/molatosekgobela/Data-Science-Video-games-sales-dataset/main/vgsales.csv").toURL();

        GameReader urlReader = new UrlGameReader(remoteUrl);
        List<Game> gamesFromUrl = urlReader.load();

        System.out.println("Total de juegos descargados y parseados: " + gamesFromUrl.size());
        Map<Platform, Long> urlSummary = GameStatistics.countByPlatform(gamesFromUrl);
        writer.write(urlSummary, System.out);
    }
}