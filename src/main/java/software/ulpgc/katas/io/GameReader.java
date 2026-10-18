package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Game;

import java.util.List;

public interface GameReader {
    List<Game> load();
}
