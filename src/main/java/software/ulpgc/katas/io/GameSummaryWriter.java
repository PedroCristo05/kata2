package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Platform;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

public class GameSummaryWriter implements SummaryWriter{
    @Override
    public void write(Map<Platform, Long> summary, OutputStream outputStream) {
        PrintWriter writer = new PrintWriter(outputStream, true);
        writer.println("--- RESUMEN POR PLATAFORMA ---");
        summary.forEach((platform, count) ->
                writer.printf("%-10s : %d juegos%n", platform, count)
        );
        writer.println("------------------------------");
    }
}
