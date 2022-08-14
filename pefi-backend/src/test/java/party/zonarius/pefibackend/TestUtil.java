package party.zonarius.pefibackend;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUtil {
    public static String getResource(String path) {
        try {
            return Files.readString(Paths.get(TestUtil.class.getResource(path).toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getScript(String name) {
        return getResource("/scripts/" + name);
    }
}
