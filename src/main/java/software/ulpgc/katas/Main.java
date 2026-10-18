package software.ulpgc.katas;

import software.ulpgc.katas.io.*;
import software.ulpgc.katas.model.Game;
import software.ulpgc.katas.model.Platform;
import software.ulpgc.katas.service.GameStatistics;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        SummaryWriter writer = new GameSummaryWriter();

        // -----------------------------------------------------------------
        // CASO A: Fichero local
        // -----------------------------------------------------------------
        // Puedes poner la ruta real a cualquier carpeta (ej. Descargas o Escritorio)
        File externalFile = new File("vgsales.csv");

        if (externalFile.exists()) {
            System.out.println("======== 1. Leyendo localmente (muestra pequeña) ========");
            List<Game> gamesFromLocal = new FileGameReader(externalFile).load();
            Map<Platform, Long> localSummary = GameStatistics.countByPlatform(gamesFromLocal);
            writer.write(localSummary, System.out);
        } else {
            System.out.println("Nota: " + externalFile.getPath() + " no existe aún, usando ruta del proyecto.");
            List<Game> gamesFromProject = new FileGameReader(new File("vgsales.csv")).load();
            writer.write(GameStatistics.countByPlatform(gamesFromProject), System.out);
        }

        // -----------------------------------------------------------------
        // CASO B: Lectura directa desde URL pública de Internet
        // -----------------------------------------------------------------
        System.out.println("\n=== 2. Leyendo desde URL pública (fichero original) ===");
        URL remoteUrl = URI.create("https://raw.githubusercontent.com/molatosekgobela/Data-Science-Video-games-sales-dataset/main/vgsales.csv").toURL();

        GameReader urlReader = new UrlGameReader(remoteUrl);
        List<Game> gamesFromUrl = urlReader.load();

        System.out.println("Total de juegos descargados y parseados desde la red: " + gamesFromUrl.size());
        Map<Platform, Long> urlSummary = GameStatistics.countByPlatform(gamesFromUrl);
        writer.write(urlSummary, System.out);
    }
}