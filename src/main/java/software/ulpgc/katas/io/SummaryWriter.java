package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Platform;

import java.io.OutputStream;
import java.util.Map;

public interface SummaryWriter {
    void write(Map<Platform, Long> summary, OutputStream outputStream);
}
