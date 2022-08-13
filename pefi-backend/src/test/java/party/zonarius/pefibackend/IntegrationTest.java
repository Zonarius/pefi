package party.zonarius.pefibackend;


import au.com.origin.snapshots.junit5.SnapshotExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SpringBootTest(classes = PefiBackendApplication.class)
@ExtendWith({SnapshotExtension.class})
@Retention(RetentionPolicy.RUNTIME)
public @interface IntegrationTest {
}
