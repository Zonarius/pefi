package party.zonarius.pefibackend;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public final class TestResources {
    public static final class CtrlA {
        public static String directExample() {
            return TestUtil.getResource("/sensitive-test-data/ctrla");
        }

        public static String partial() {
            return TestUtil.getResource("/sensitive-test-data/ctrla_partial");
        }

    }

    public static final class Csv {
        public static File exampleAsFile() {
            URL url = TestUtil.class.getResource("/sensitive-test-data/EASYBANK_Umsatzliste_20220715_1118.csv");
            try {
                return new File(url.toURI());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
