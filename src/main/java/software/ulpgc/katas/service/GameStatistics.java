package software.ulpgc.katas.service;

import software.ulpgc.katas.model.Game;
import software.ulpgc.katas.model.Platform;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameStatistics {

    public static Map<Platform, Long> countByPlatform(List<Game> games) {
        return games.stream()
                .collect(Collectors.groupingBy(Game::platform, Collectors.counting()));
    }

}
