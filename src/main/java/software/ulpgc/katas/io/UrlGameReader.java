package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Game;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class UrlGameReader implements GameReader {

    private final URL url;

    public UrlGameReader(URL url) {
        this.url = url;
    }

    @Override
    public List<Game> load() {
        try (InputStream is = url.openStream()) {
            return new FileGameReader(null).loadFrom(is);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
