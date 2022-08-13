package party.zonarius.pefibackend;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUtil {
    public static File getCsvExample() {
        URL url = TestUtil.class.getResource("/sensitive-test-data/EASYBANK_Umsatzliste_20220715_1118.csv");
        try {
            return new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

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
